package fanhua.com.umengstastics;

public class LoadJni {

    static {
        System.loadLibrary("uninstall");
    }

    public static native String listenUnInstall();
}
