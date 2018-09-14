package fanhua.com.umengstastics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * 监听其他应用被删除,通过广播并不能检测本身应用被卸载
 * 检测本身应用被卸载可能需要通过JNI来实现
 */
public class AppsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //判断收到的是什么广播
        String action = intent.getAction();
        //获取安装更新卸载的是什么应用
        Uri uri = intent.getData();
        if(Intent.ACTION_PACKAGE_ADDED.equals(action)){
            Toast.makeText(context, uri + "被安装了", Toast.LENGTH_LONG).show();
            System.out.println("安装的uri:"+uri);
        }
        else if(Intent.ACTION_PACKAGE_REMOVED.equals(action)){
            Toast.makeText(context, uri + "被删除了", Toast.LENGTH_LONG).show();
            System.out.println("删除的uri:"+uri);
        }
        else if(Intent.ACTION_PACKAGE_REPLACED.equals(action)){
            Toast.makeText(context, uri + "被更新了", Toast.LENGTH_LONG).show();
            System.out.println("更新的uri:"+uri);
        }
        else{

        }

    }

}
