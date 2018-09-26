package so.chinaso.com.voicemodule.intent;

import so.chinaso.com.voicemodule.chat.PlayerViewModel;

/**
 * Created by yf on 2018/9/13.
 */
public class StoryHandler {
    private static int controlTipReqCount = 0;

    public StoryHandler(PlayerViewModel mPlayer, byte[] data) {

    }

    //减少播放控制类指令提示次数
    public static boolean isNeedShowControlTip() {
        return controlTipReqCount++ % 5 == 0;
    }
}
