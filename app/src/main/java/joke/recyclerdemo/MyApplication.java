package joke.recyclerdemo;

import android.app.Application;

/**
 * Created by SHF on 2016/12/20.
 */

public class MyApplication extends Application {
    public static int themeID = R.style.AppTheme;
    private static MyApplication myApplication;

    public static MyApplication getApplication() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }
}
