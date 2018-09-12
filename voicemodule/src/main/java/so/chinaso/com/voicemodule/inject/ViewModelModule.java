package so.chinaso.com.voicemodule.inject;

import android.arch.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import so.chinaso.com.voicemodule.voice.ChatViewModel;

/**
 * Created by yf on 2018/9/12.
 */
@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel.class)
    abstract ViewModel buildChatViewModel(ChatViewModel messagesViewModel);
}