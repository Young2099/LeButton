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

    public StoryHandler(PlayerViewModel mPlayer, byte[] rawMessage) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(new String(rawMessage));
            List<AIUIPlayer.SongInfo> songList = new ArrayList<>();
            JSONArray list = jsonObject.optJSONArray("result");
            if (list != null) {
                for (int index = 0; index < list.length(); index++) {
                    JSONObject audio = list.optJSONObject(index);
                    String audioPath = audio.optString("playUrl");
                    String songName = audio.optString("name");
                    String author = audio.optString("author");
                    songList.add(new AIUIPlayer.SongInfo(author, songName, audioPath));
                    if (songList.size() != 0) {

                        mPlayer.playList(songList);
//                        if (isNeedShowControlTip()) {
//                            result.answer = result.answer + NEWLINE_NO_HTML + NEWLINE_NO_HTML + CONTROL_TIP;
//                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //减少播放控制类指令提示次数
    public static boolean isNeedShowControlTip() {
        return controlTipReqCount++ % 5 == 0;
    }
}
