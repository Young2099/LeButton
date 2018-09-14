package so.chinaso.com.voicemodule.intent.player;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import so.chinaso.com.voicemodule.entity.PoetryEntity;
import so.chinaso.com.voicemodule.intent.IntentHandler;

/**
 * Created by yf on 2018/9/14.
 */
public class PoetryHandler extends IntentHandler {
    @Override
    public List getFormatContent(byte[] data) {
        return getData(data);
    }

    private List getData(byte[] data) {
        List<PoetryEntity> poetryEntities = new ArrayList<>();
        PoetryEntity poetryEntity;
        JsonObject object = new JsonParser().parse(new String(data)).getAsJsonObject();
        JsonArray result = object.getAsJsonArray("result");
        for (int i = 0; i < result.size(); i++) {
            Gson objectMapper = new Gson();
            poetryEntity = objectMapper.fromJson(result.get(i).getAsJsonObject(), PoetryEntity.class);
            poetryEntities.add(poetryEntity);
        }
        return poetryEntities;
    }

}
