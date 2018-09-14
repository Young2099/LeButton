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
public class ChatMessageHolder extends BaseViewHolder {
    public TextView message;
    public TextView voice;
    public RecyclerView mWeatherRecycler;

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


    public ChatMessageHolder(ViewGroup parent, int resId) {
        super(parent, resId);
        message = getView(R.id.message);
        voice = getView(R.id.voice);
        mWeatherRecycler = getView(R.id.weather_recyclerview);
        rl_weather = getView(R.id.rl_weather);
        voice_layout = getView(R.id.voice_layout);
        message_layout = getView(R.id.message_layout);
        res_image = getView(R.id.res_image);
        res_name = getView(R.id.res_name);
        res_address_detail = getView(R.id.res_address_detail);
        res_telephone = getView(R.id.res_telephone);
        poetry_content = getView(R.id.poetry);
        poetry_dynasty = getView(R.id.poetry_dynasty);
        poetry_title = getView(R.id.poetry_title);
    }
}
