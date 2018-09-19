package fanhua.com.umengstastics;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;
import com.meituan.android.walle.ChannelInfo;
import com.meituan.android.walle.WalleChannelReader;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

public class UmengApplication extends Application {

    /**
     * 渠道id
     */
    public static String CHANEEL_ID;
    public static PushAgent mPushAgent;

    @Override
    public void onCreate() {
        super.onCreate();

        final ChannelInfo channelInfo = WalleChannelReader.getChannelInfo(this);

        if (channelInfo != null) {
            CHANEEL_ID =  channelInfo.getChannel();
        }else {
            if (BuildConfig.DEBUG) {
                CHANEEL_ID = "dev";
            } else {
                CHANEEL_ID = "default";//改为default，因为有的三方渠道，会更改channelInfo
            }
        }
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this, "5b979a3b8f4a9d11c40000f5", CHANEEL_ID, UMConfigure.DEVICE_TYPE_PHONE, "67b79016585e0aad7a2590f3fee9f761");

        //umeng推送配置
        mPushAgent = PushAgent.getInstance(this);

        //应用在前台时是否显示通知  此方法请在mPushAgent.register方法之前调用。
//        mPushAgent.setNotificaitonOnForeground(true);

        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                //AsjwG0TBnMe0Q1iJM3Om-bKmZ0Q_JgxVI6bR_RGML52v
                System.out.println("设备token:"+deviceToken);
            }
            @Override
            public void onFailure(String s, String s1) {
                System.out.println("umeng‘推送"+s+"==="+s1);
            }
        });

        FileDownloader.init(this);
    }
}
