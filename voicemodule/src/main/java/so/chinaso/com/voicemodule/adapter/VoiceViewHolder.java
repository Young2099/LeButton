package so.chinaso.com.voicemodule.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daasuu.bl.BubbleLayout;

import so.chinaso.com.voicemodule.R;
import so.chinaso.com.voicemodule.adapter.BaseViewHolder;

/**
 * Created by yf on 2018/9/7.
 */
public class VoiceViewHolder extends BaseViewHolder {
    public TextView message;
    public TextView voice;
    public TextView weather;
    public RelativeLayout today_weather;
    public RecyclerView mWeatherRecycler;
    public TextView time;
    public TextView city;
    public TextView temp;
    public TextView airQuality;
    public TextView wind;
    public TextView weather_detail;
    public TextView tempRange;
    public ImageView weather_icon;
    public RelativeLayout rl_weather;
    public BubbleLayout voice_layout;
    public BubbleLayout message_layout;
    public ImageView res_image;
    public TextView res_name;
    public TextView res_address_detail;
    public TextView res_telephone;
    public TextView poetry_content;
    public TextView poetry_author;
    public TextView poetry_dynasty;
    public TextView poetry_title;


    public VoiceViewHolder(ViewGroup parent, int resId) {
        super(parent, resId);
        message = getView(R.id.message);
        voice = getView(R.id.voice);
        weather = getView(R.id.weather);
        today_weather = getView(R.id.today_weather);
        mWeatherRecycler = getView(R.id.weather_recyclerview);
        time = getView(R.id.time);
        city = getView(R.id.city);
        temp = getView(R.id.temp);
        airQuality = getView(R.id.air);
        wind = getView(R.id.wind);
        weather_detail = getView(R.id.weather_detail);
        tempRange = getView(R.id.tem_range);
        weather_icon = getView(R.id.weather_icon);
        rl_weather = getView(R.id.rl_weather);
        voice_layout = getView(R.id.voice_layout);
        message_layout = getView(R.id.message_layout);
        res_image = getView(R.id.res_image);
        res_name = getView(R.id.res_name);
        res_address_detail = getView(R.id.res_address_detail);
        res_telephone = getView(R.id.res_telephone);
        poetry_content = getView(R.id.poetry);
        poetry_author = getView(R.id.poetry_author);
        poetry_dynasty = getView(R.id.poetry_dynasty);
        poetry_title = getView(R.id.poetry_title);
    }
}
