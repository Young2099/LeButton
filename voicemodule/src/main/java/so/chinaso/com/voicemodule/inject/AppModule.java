package so.chinaso.com.voicemodule.inject;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import so.chinaso.com.voicemodule.chat.AIUIRepository;
import so.chinaso.com.voicemodule.db.MessageDB;
import so.chinaso.com.voicemodule.db.MessageDao;
import so.chinaso.com.voicemodule.intent.player.AIUIPlayer;

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
    public AIUIRepository provideCenterRepository(Application application,
             MessageDao dao, AIUIPlayer player) {
//        SpeechCreateIfExists(application, config);
        return new AIUIRepository(application, dao, player);
    }



    @Provides
    @Singleton
    public AIUIPlayer providePlayer(Application application){
        // 1. Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        SimpleExoPlayer player =
                ExoPlayerFactory.newSimpleInstance(application, trackSelector);

        return new AIUIPlayer(application, player);
    }

    @Provides
    @Singleton
    public MessageDao provideMessageDao(MessageDB db) {
        return db.messageDao();
    }
}
