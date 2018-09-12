package so.chinaso.com.voicemodule.app;

import android.app.Application;

import so.chinaso.com.voicemodule.inject.DaggerAppComponent;

/**
 * Created by yf on 2018/9/12.
 */
public class VoiceModuleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.builder().build();
    }
}
