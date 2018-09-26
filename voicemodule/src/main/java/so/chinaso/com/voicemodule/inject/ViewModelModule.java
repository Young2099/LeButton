package so.chinaso.com.voicemodule.inject;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import so.chinaso.com.voicemodule.ViewModelFactory;
import so.chinaso.com.voicemodule.chat.ChatViewModel;
import so.chinaso.com.voicemodule.chat.PlayerViewModel;

/**
 * Created by yf on 2018/9/12.
 */
@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel.class)
    abstract ViewModel buildChatViewModel(ChatViewModel messagesViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel.class)
    abstract ViewModel buildPlayerViewModel(PlayerViewModel playerViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}