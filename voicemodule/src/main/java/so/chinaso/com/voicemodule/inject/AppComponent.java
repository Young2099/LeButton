package so.chinaso.com.voicemodule.inject;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import so.chinaso.com.voicemodule.app.VoiceModuleApplication;

/**
 * Created by yf on 2018/9/12.
 */
@Singleton
@Component(modules = {AndroidInjectionModule.class, ActivityModule.class, AppModule.class})
public interface AppComponent {
    void inject(VoiceModuleApplication application);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

}
