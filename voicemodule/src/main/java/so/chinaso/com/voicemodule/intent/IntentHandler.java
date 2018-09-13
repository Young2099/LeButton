package so.chinaso.com.voicemodule.intent;

import java.util.List;

/**
 * Created by yf on 2018/9/13.
 */
public abstract class IntentHandler<T> {

    public IntentHandler() {
    }

    public abstract List<T> getFormatContent(byte[] data);
}
