package so.chinaso.com.voicemodule.adapter;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import so.chinaso.com.voicemodule.R;
import so.chinaso.com.voicemodule.db.MessageDB;
import so.chinaso.com.voicemodule.entity.PoetryEntity;
import so.chinaso.com.voicemodule.entity.RawMessage;
import so.chinaso.com.voicemodule.entity.RestaurantEntity;
import so.chinaso.com.voicemodule.entity.WeatherEntity;

/**
 * Created by yf on 2018/8/27.
 */
public class VoiceAdapter extends RecyclerView.Adapter<VoiceViewHolder> {
    private static final int WEATHER = 1;
    private static final int NORMAL = 2;
    private static final int RESTAURANT = 3;
    private static final int POETRY = 4;
    private static final int WEB = 5;
    private static final int SEARCH = 6;
    private static final int LAUNCH_APP = 7;
    private List<RawMessage> list;
    private Context mContext;
    private List<WeatherEntity> weatherEntities;

    public VoiceAdapter(Context context) {
        mContext = context;
    }

    @Override
    public VoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        VoiceViewHolder weatherViewHolder = null;
        switch (viewType) {
            case NORMAL:
            case WEB:
            case SEARCH:
            case LAUNCH_APP:
                weatherViewHolder = new VoiceViewHolder(parent, R.layout.item_voice);
                break;
            case WEATHER:
            case RESTAURANT:
                weatherViewHolder = new VoiceViewHolder(parent, R.layout.item_total_weather);
                break;
            case POETRY:
                weatherViewHolder = new VoiceViewHolder(parent, R.layout.item_poetry);
                break;

        }
        return weatherViewHolder;
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(VoiceViewHolder holder, final int position) {
        if (list.get(position) == null) {
            return;
        }
        switch (getItemViewType(position)) {
            case WEATHER:
                setWeather(list.get(position), holder);
                holder.voice.setText(list.get(position).getVoice());
                holder.message.setText(list.get(position).getMessage());
                break;
            case NORMAL:
                if (position == 0) {
                    holder.voice_layout.setVisibility(View.GONE);
                } else {
                    holder.voice_layout.setVisibility(View.VISIBLE);
                    holder.voice.setText(list.get(position).getVoice());
                }
                holder.message.setText(list.get(position).getMessage());
                break;
            case RESTAURANT:
                holder.voice.setText(list.get(position).getVoice());
                holder.message.setText(list.get(position).getMessage());
                showRestaurant(list.get(position), holder);
                break;
            case POETRY:
                holder.voice.setText(list.get(position).getVoice());
                setPoetry(list.get(position), holder);
                break;
            case WEB:
                setShow(holder, list.get(position));
                if (list.get(position).isLaunch()) {
                    Class clazz = null;
                    try {
                        clazz = Class.forName("com.chinaso.domino.activity.WebUrlActivity");
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    startWeb(list.get(position),clazz);
                }
                break;
            case SEARCH:
                setShow(holder, list.get(position));
                if (list.get(position).isLaunch()) {
                    Class webdetailClazz = null;
                    try {
                        webdetailClazz = Class.forName("com.chinaso.domino.activity.WebDetailActivity");
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    startWeb(list.get(position),webdetailClazz);
                }
                break;
            case LAUNCH_APP:
                setShow(holder, list.get(position));
                if (list.get(position).isLaunch()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            bindMsg(list.get(position).getMessage());
                        }
                    }).start();
                    updateMessage(list.get(position));
                }
                break;
        }

////                    Intent intent = new Intent();
////                    intent.setAction(Intent.ACTION_DIAL);
////                    intent.setData(Uri.parse("tel:" + message));
////                    startActivity(intent);
//                    break;


    }

    private void startWeb(RawMessage rawMessage, Class clazz) {
        assert clazz != null;
        Intent intent = new Intent(mContext, clazz);
        intent.putExtra("search_words", rawMessage.getMessage());
        mContext.startActivity(intent);
        updateMessage(rawMessage);
    }

    private void setShow(VoiceViewHolder holder, RawMessage rawMessage) {
        holder.message_layout.setVisibility(View.GONE);
        holder.voice.setText(rawMessage.getVoice());
        holder.voice_layout.setVisibility(View.VISIBLE);
    }

    @SuppressLint("CheckResult")
    private void updateMessage(final RawMessage rawMessage) {
        rawMessage.setLaunch(false);
        Completable
                .complete()
                .observeOn(Schedulers.io())
                .subscribe(new Action() {
                    @Override
                    public void run() {
                        Log.e("TAG", "run: " + rawMessage);
                        MessageDB.getInstance(mContext).messageDao().updateMessage(rawMessage);
                    }
                });
    }

    private void bindMsg(String _appName) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> mAllApps = mContext.getPackageManager().queryIntentActivities(mainIntent, 0);
        String pkg = "";
        String cls = "";
        for (ResolveInfo element : mAllApps) {
            pkg = element.activityInfo.packageName;
            cls = element.activityInfo.name;
            String appName = element.loadLabel(mContext.getPackageManager()).toString();
            if (appName.equals(_appName)) {
                ComponentName componet = new ComponentName(pkg, cls);
                Intent i = new Intent();
                i.setComponent(componet);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            }
        }
    }

    private void setPoetry(RawMessage rawMessage, VoiceViewHolder holder) {
//        String answer = rawMessage.getMessage().replaceAll("\\[[a-zA-Z0-9]{2}\\]", "");
        List<PoetryEntity> poetryEntities = new ArrayList<>();
        PoetryEntity poetryEntity;
        JsonObject object = new JsonParser().parse(new String(rawMessage.getMsgData())).getAsJsonObject();
        JsonArray data = object.getAsJsonArray("result");
        for (int i = 0; i < data.size(); i++) {
            Gson objectMapper = new Gson();
            poetryEntity = objectMapper.fromJson(data.get(i).getAsJsonObject(), PoetryEntity.class);
            poetryEntities.add(poetryEntity);
        }
        holder.poetry_content.setText(poetryEntities.get(0).getShowContent());
        holder.poetry_author.setText(poetryEntities.get(0).getAuthor());
        holder.poetry_dynasty.setText(poetryEntities.get(0).getDynasty());
        holder.poetry_title.setText(poetryEntities.get(0).getTitle());
    }


    private void showRestaurant(RawMessage rawMessage, VoiceViewHolder holder) {
        holder.today_weather.setVisibility(View.GONE);
        holder.mWeatherRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        holder.mWeatherRecycler.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        RestaurantAdapter adapter = new RestaurantAdapter(mContext, getRes(rawMessage.getMsgData()));
        holder.mWeatherRecycler.setAdapter(adapter);
    }

    private List<RestaurantEntity> getRes(byte[] jsonObject) {
        List<RestaurantEntity> list = new ArrayList<>();
        RestaurantEntity restaurantEntity;
        JsonObject jsonObject1 = new JsonParser().parse(new String(jsonObject)).getAsJsonObject();
        JsonArray data = jsonObject1.getAsJsonArray("result");
        for (int i = 0; i < data.size(); i++) {
            Gson objectMapper = new Gson();
            restaurantEntity = objectMapper.fromJson(data.get(i).getAsJsonObject(), RestaurantEntity.class);
            list.add(restaurantEntity);
        }

        return list;
    }


    private void setWeather(RawMessage rawMessage,VoiceViewHolder holder) {
        if ("weather".equals(rawMessage.getIntent())) {
            JsonObject object = new JsonParser().parse(new String(rawMessage.getMsgData())).getAsJsonObject();
            List<WeatherEntity> weatherEntities = getData(object);
            holder.mWeatherRecycler.setLayoutManager(new LinearLayoutManager(mContext));
            WeatherAdapter adapter = new WeatherAdapter(weatherEntities);
            holder.mWeatherRecycler.setAdapter(adapter);
            /**
             * 0 晴天
             * 1 云
             * 2 阴
             * 4 雷阵雨
             * 7 小雨
             * 8 中雨
             */
            if (weatherEntities != null && weatherEntities.size() != 0) {
                holder.city.setText(weatherEntities.get(0).getCity());
                holder.weather_detail.setText(weatherEntities.get(0).getWeather());
                holder.airQuality.setText(weatherEntities.get(0).getAir());
                holder.tempRange.setText(weatherEntities.get(0).getTempRange());
                holder.temp.setText(weatherEntities.get(0).getTemp());
                holder.wind.setText(weatherEntities.get(0).getWind());
                Glide.with(mContext).load(weatherEntities.get(0).getImg()).into(holder.weather_icon);
                String date = weatherEntities.get(0).getDate();
                String year = date.split("-")[0];
                String month = date.split("-")[1];
                String day = date.split("-")[2];
                String dateC = year + "年" + month + "月" + day + "日";
                holder.time.setText(dateC);
                if ("0".equals(weatherEntities.get(0).getWeatherType())) {
//                        holder.today_weather.setBackgroundResource(R.mipmap.qing);
                }
            }
        }
    }


    private List<WeatherEntity> getData(JsonObject jsonObject) {
        weatherEntities = new ArrayList<>();
        WeatherEntity weatherEntity;
        JsonArray data = jsonObject.getAsJsonArray("result");
        for (int i = 0; i < data.size(); i++) {
            Gson objectMapper = new Gson();
            weatherEntity = objectMapper.fromJson(data.get(i).getAsJsonObject(), WeatherEntity.class);
            weatherEntities.add(weatherEntity);
        }
        if (weatherEntities == null) {
            return null;
        }
        return weatherEntities;
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        switch (list.get(position).getIntent()) {
            case "weather":
                type = WEATHER;
                break;
            case "restaurantSearch":
                type = RESTAURANT;
                break;
            case "poetry":
                type = POETRY;
                break;
            case "GUOSOU.open_web":
                type = WEB;
                break;
            case "GUOSOU.chinaso_search":
                type = SEARCH;
                break;
            case "app":
                type = LAUNCH_APP;
                break;
            default:
                type = NORMAL;
                break;
        }
        return type;
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setList(List<RawMessage> list) {
        this.list = list;
    }
}
