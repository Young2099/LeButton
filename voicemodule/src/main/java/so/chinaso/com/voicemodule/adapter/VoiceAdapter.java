package so.chinaso.com.voicemodule.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.daasuu.bl.BubbleLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import so.chinaso.com.voicemodule.R;
import so.chinaso.com.voicemodule.entity.RawMessage;
import so.chinaso.com.voicemodule.entity.WeatherEntity;

/**
 * Created by yf on 2018/8/27.
 */
public class VoiceAdapter extends RecyclerView.Adapter<VoiceAdapter.BaseViewHolder> {
    private List<RawMessage> list;
    private Context mContext;
    private List<WeatherEntity> weatherEntities;

    public VoiceAdapter(List<RawMessage> list, Context context) {
        this.list = list;
        mContext = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voice, parent, false);
        return new BaseViewHolder(view);
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        if (position == 0) {
            holder.voice_layout.setVisibility(View.GONE);
            holder.message.setText(list.get(0).getMessage());
        }else {
            holder.voice_layout.setVisibility(View.VISIBLE);
            holder.voice.setText(list.get(position).getVoice());
        }
        if(list.get(position) == null){
            return;
        }
        if (list.get(position).getIntent() != null) {
            switch (list.get(position).getIntent()) {
                case "weather":
                    Log.e("TAG", "onBindViewHolder: " + list.toString());
                    setWeather(list, position, holder);
                    break;
                case "telephone":
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_DIAL);
//                    intent.setData(Uri.parse("tel:" + message));
//                    startActivity(intent);
                    break;

                default:
                    holder.today_weather.setVisibility(View.GONE);
                    holder.mWeatherRecycler.setVisibility(View.GONE);
                    holder.message.setVisibility(View.VISIBLE);
                    holder.message.setText(list.get(position).getMessage());
                    break;
            }

        }

    }


    private void setWeather(List<RawMessage> list, int position, BaseViewHolder holder) {
        if ("weather".equals(list.get(position).getIntent())) {
            List<WeatherEntity> weatherEntities = getData(list.get(position).getJsonObject());
            holder.today_weather.setVisibility(View.VISIBLE);
            holder.mWeatherRecycler.setVisibility(View.VISIBLE);
            holder.mWeatherRecycler.setLayoutManager(new LinearLayoutManager(mContext));
            holder.mWeatherRecycler.setNestedScrollingEnabled(false);
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
//                GlideApp.with(mContext).load(weatherEntities.get(0).getImg()).into(holder.weather_icon);
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
    public int getItemCount() {
        return list.size();
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView voice;
        TextView weather;
        RelativeLayout today_weather;
        RecyclerView mWeatherRecycler;
        TextView time;
        TextView city;
        TextView temp;
        TextView airQuality;
        TextView wind;
        TextView weather_detail;
        TextView tempRange;
        ImageView weather_icon;
        RelativeLayout rl_weather;
        BubbleLayout voice_layout;
        BubbleLayout message_layout;

        public BaseViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            voice = itemView.findViewById(R.id.voice);
            weather = itemView.findViewById(R.id.weather);
            today_weather = itemView.findViewById(R.id.today_weather);
            mWeatherRecycler = itemView.findViewById(R.id.weather_recyclerview);
            time = itemView.findViewById(R.id.time);
            city = itemView.findViewById(R.id.city);
            temp = itemView.findViewById(R.id.temp);
            airQuality = itemView.findViewById(R.id.air);
            wind = itemView.findViewById(R.id.wind);
            weather_detail = itemView.findViewById(R.id.weather_detail);
            tempRange = itemView.findViewById(R.id.tem_range);
            weather_icon = itemView.findViewById(R.id.weather_icon);
            rl_weather = itemView.findViewById(R.id.rl_weather);
            voice_layout = itemView.findViewById(R.id.voice_layout);
            message_layout = itemView.findViewById(R.id.message_layout);
        }
    }

    /**
     * add one data
     *
     * @param data
     */
    public void add(RawMessage data) {
        Log.e("TAG", "add: "+data );
        list.add(data);
        int index = list.indexOf(data);
        notifyItemChanged(index);
    }

}
