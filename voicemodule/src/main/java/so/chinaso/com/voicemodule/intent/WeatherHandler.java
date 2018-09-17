package so.chinaso.com.voicemodule.intent;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import so.chinaso.com.voicemodule.entity.WeatherEntity;

/**
 * Created by yf on 2018/9/13.
 */
public class WeatherHandler extends IntentHandler<WeatherEntity> {

    @Override
    public List<WeatherEntity> getFormatContent(byte[] data) {
        return getData(data);
    }

    private List<WeatherEntity> getData(byte[] msgData) {
        List<WeatherEntity> weatherEntities = new ArrayList<>();
        JsonObject jsonObject = new JsonParser().parse(new String(msgData)).getAsJsonObject();
        WeatherEntity weatherEntity;
        JsonArray data = jsonObject.getAsJsonArray("result");
        for (int i = 0; i < data.size(); i++) {
            Gson objectMapper = new Gson();
            weatherEntity = objectMapper.fromJson(data.get(i).getAsJsonObject(), WeatherEntity.class);
            weatherEntities.add(weatherEntity);
        }
        Log.e("TAG", "getData: "+weatherEntities.get(0).toString());
        return weatherEntities;
    }

}
