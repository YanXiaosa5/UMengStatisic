package fanhua.com.umengstastics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;


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
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.tv_channel)).setText(LoadJni.listenUnInstall());
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
