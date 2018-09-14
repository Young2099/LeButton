package so.chinaso.com.voicemodule.intent;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import so.chinaso.com.voicemodule.entity.RawMessage;
import so.chinaso.com.voicemodule.intent.player.AIUIPlayer;
import so.chinaso.com.voicemodule.voice.PlayerViewModel;

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
