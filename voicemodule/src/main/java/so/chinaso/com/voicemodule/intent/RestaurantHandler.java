package so.chinaso.com.voicemodule.intent;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import so.chinaso.com.voicemodule.entity.RestaurantEntity;

/**
 * Created by yf on 2018/9/13.
 */
public class RestaurantHandler extends IntentHandler<RestaurantEntity> {
    private List<RestaurantEntity> getRes(byte[] result) {
        List<RestaurantEntity> list = new ArrayList<>();
        RestaurantEntity restaurantEntity;
        JsonObject jsonObject1 = new JsonParser().parse(new String(result)).getAsJsonObject();
        JsonArray data = jsonObject1.getAsJsonArray("result");
        for (int i = 0; i < data.size(); i++) {
            Gson objectMapper = new Gson();
            restaurantEntity = objectMapper.fromJson(data.get(i).getAsJsonObject(), RestaurantEntity.class);
            list.add(restaurantEntity);
        }
        return list;
    }

    @Override
    public List<RestaurantEntity> getFormatContent(byte[] result) {
        return getRes(result);
    }
}
