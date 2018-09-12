package so.chinaso.com.voicemodule.inject;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Room;
import android.content.Context;

import org.json.JSONObject;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import so.chinaso.com.voicemodule.db.MessageDB;
import so.chinaso.com.voicemodule.db.MessageDao;
import so.chinaso.com.voicemodule.voice.AIUIRepository;

/**
 * Created by yf on 2018/9/12.
 */
@Module(includes = {ViewModelModule.class})
public class AppModule {
    @Provides
    @Singleton
    public Context providerContext(Application application) {
        return application;
    }
    @Provides
    @Singleton
    public MessageDB provideMessageDB(Application application) {
        return Room.inMemoryDatabaseBuilder(application, MessageDB.class).build();
    }


    @Provides
    @Singleton
    public AIUIRepository provideCenterRepository(Application application
            ,  MessageDao dao) {
//        SpeechCreateIfExists(application, config);
        return new AIUIRepository(application);
    }


    @Provides
    @Singleton
    public MessageDao provideMessageDao(MessageDB db) {
        return db.messageDao();
    }
}
