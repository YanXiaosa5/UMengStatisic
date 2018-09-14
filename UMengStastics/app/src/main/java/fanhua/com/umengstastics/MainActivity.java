package fanhua.com.umengstastics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;


//下载库:   https://github.com/lingochamp/FileDownloader   下载列表

//https://www.cnblogs.com/zsychanpin/p/6708161.html  监听自身应用被卸载


//android调用https://blog.csdn.net/wangliu1102/article/details/78919742 的步骤
//https://blog.csdn.net/chuhongcai/article/details/52558049  步骤
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.tv_channel)).setText(UmengApplication.CHANEEL_ID);
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
