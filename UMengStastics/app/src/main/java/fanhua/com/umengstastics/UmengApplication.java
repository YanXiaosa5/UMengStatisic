package fanhua.com.umengstastics;

import android.app.Application;

import com.meituan.android.walle.ChannelInfo;
import com.meituan.android.walle.WalleChannelReader;
import com.umeng.commonsdk.UMConfigure;

public class UmengApplication extends Application {

    /**
     * 渠道id
     */
    public static String CHANEEL_ID;

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
        UMConfigure.init(this, "5b979a3b8f4a9d11c40000f5", CHANEEL_ID, UMConfigure.DEVICE_TYPE_PHONE, null);
    }
}
