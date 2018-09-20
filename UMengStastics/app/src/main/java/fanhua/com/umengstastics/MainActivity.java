package fanhua.com.umengstastics;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import fanhua.com.umengstastics.dowload.DowloadActivity;


//下载库:   https://github.com/lingochamp/FileDownloader   下载列表

//https://www.cnblogs.com/zsychanpin/p/6708161.html  监听自身应用被卸载


//android调用https://blog.csdn.net/wangliu1102/article/details/78919742 的步骤
//https://blog.csdn.net/chuhongcai/article/details/52558049  步骤

//https://blog.csdn.net/wangliu1102/article/details/78919742
//https://www.jianshu.com/p/e7c2c63fa70e  jni使用步骤
//https://www.cnblogs.com/tt2015-sz/p/6148723.html   jni使用步骤ndk-build（  Android ndk开发与调试）

//https://blog.csdn.net/allen315410/article/details/42521251   Android NDK开发(八)——应用监听自身卸载，弹出用户反馈调查

//https://blog.csdn.net/zhangmingbao2016/article/details/73551468               Android源码分析实战之JNI so库加载System.loadLibrary流程分析


///http://www.cnblogs.com/dzblog/p/4062288.html   android命令获取包名的几种方式

/**
 * 总结一下流程：

 编写静态方法（用java声明）–>编译生成class文件—>编译生成h文件—->编写C文件（用C/C++实现）

 —->配置NDK—->配置so库—->在Activity调用（Java调用C/C++）。
 */

/**
 * 使用时步骤:将so库复制到项目的jniLibs目录下,需要注意的是要将提供的native接口所在的类也要复制到相同的目录下(这里是fanhua.com.umengstastics.LoadJni),复制以后,LoadJni的路径要保持不变,
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        //隐去电池等图标和一切修饰部分（状态栏部分）
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, UmengApplication.CHANEEL_ID+"渠道ID", Toast.LENGTH_SHORT).show();

        PushAgent.getInstance(this).onAppStart();
        ((TextView) findViewById(R.id.tv_channel)).setText(LoadJni.listenUnInstall());
        ((TextView) findViewById(R.id.tv_channel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,DowloadActivity.class));
            }
        });

        //点击通知(后续行为-->自定义行为有内容时才会调用)
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                System.out.println("接收消息"+new Gson().toJson(msg));
                /**
                 * {"a":{"nameValuePairs":{"display_type":"notification","extra":{"nameValuePairs":{"key":"main"}},"msg_id":"uuynd2s153724897629610","body":{"nameValuePairs":{"after_open":"go_custom","play_lights":"false","ticker":"fdgdfgd","play_vibrate":"false","custom":"可怜九月初三夜","text":"dfgdfgdf","title":"fdgdfgd","play_sound":"true"}},"random_min":0}},"activity":"","after_open":"go_custom","alias":"","bar_image":"","builder_id":0,"clickOrDismiss":true,"custom":"可怜九月初三夜","display_type":"notification","expand_image":"","extra":{"key":"main"},"icon":"","img":"","isAction":false,"largeIcon":"","message_id":"f__-mhTSIjWrRVwu\u0026\u0026uuynd2s153724897629610\u0026\u0026V4YV22awBi8DAHu6HFer7Pvn\u0026\u002601\u0026\u0026","msg_id":"uuynd2s153724897629610","play_lights":false,"play_sound":true,"play_vibrate":false,"pulledWho":"","pulled_package":"","pulled_service":"","random_min":0,"recall":"","screen_on":false,"sound":"","text":"dfgdfgdf","ticker":"fdgdfgd","title":"fdgdfgd","url":""}
                 */
            }
        };
        UmengApplication.mPushAgent.setNotificationClickHandler(notificationClickHandler);

        //自定义通知栏
        UmengMessageHandler messageHandler = new UmengMessageHandler() {

            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                //处理自定义消息
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // 对于自定义消息，PushSDK默认只统计送达。若开发者需要统计点击和忽略，则需手动调用统计方法。
                        boolean isClickOrDismissed = true;
                        if(isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
                        }
                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public Notification getNotification(Context context, UMessage msg) {//处理普通通知

                Notification.Builder builder = new Notification.Builder(context);
                RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
                        R.layout.self_notification_layout);
                myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                myNotificationView.setTextViewText(R.id.notification_text, msg.text);

                myNotificationView.setImageViewBitmap(R.id.notification_large_icon,
                        getLargeIcon(context, msg));
                myNotificationView.setImageViewResource(R.id.notification_small_icon,
                        getSmallIconId(context, msg));

                builder.setContent(myNotificationView)
                        .setSmallIcon(getSmallIconId(context, msg))
                        .setTicker(msg.ticker)
                        .setAutoCancel(true);
                return builder.getNotification();

//                switch (msg.builder_id) {
//                    case 1:
//                        Notification.Builder builder = new Notification.Builder(context);
//                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
//                                R.layout.self_notification_layout);
//                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
//                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
//
//                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon,
//                                getLargeIcon(context, msg));
//                        myNotificationView.setImageViewResource(R.id.notification_small_icon,
//                                getSmallIconId(context, msg));
//
//                        builder.setContent(myNotificationView)
//                                .setSmallIcon(getSmallIconId(context, msg))
//                                .setTicker(msg.ticker)
//                                .setAutoCancel(true);
//                        return builder.getNotification();
//                    default:
//                        //默认为0，若填写的builder_id并不存在，也使用默认。
//                        return super.getNotification(context, msg);
//                }
            }
        };
        UmengApplication.mPushAgent.setMessageHandler(messageHandler);

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
