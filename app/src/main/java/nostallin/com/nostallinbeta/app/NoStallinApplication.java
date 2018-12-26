package nostallin.com.nostallinbeta.app;

import android.app.Application;

public class NoStallinApplication extends Application {

    private static NoStallinApplication INSTANCE;
    public static NoStallinApplication get() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;
    }
}
