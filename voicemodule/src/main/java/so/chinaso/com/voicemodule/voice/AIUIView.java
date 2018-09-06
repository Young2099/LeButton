package so.chinaso.com.voicemodule.voice;


import java.util.List;

import so.chinaso.com.voicemodule.entity.RawMessage;

/**
 * Created by yf on 2018/8/9.
 */
public interface AIUIView {
    void showInitMessage(RawMessage rawMessage);

    void showVolume(int arg2);

    void showErrorMessage(String error);


    void setVadBegin(boolean b);

    void showHotWord(List<String> list);

    void showVoice(RawMessage rawMessage);
}
