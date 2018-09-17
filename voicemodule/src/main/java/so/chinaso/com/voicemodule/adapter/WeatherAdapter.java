package so.chinaso.com.voicemodule.adapter;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import so.chinaso.com.voicemodule.R;
import so.chinaso.com.voicemodule.entity.RawMessage;
import so.chinaso.com.voicemodule.entity.WeatherEntity;

/**
 * Created by yf on 2018/8/31.
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ItemViewHolder> {
    private static final int HEADER = 1;
    private static final int WEAT_NORMAL = 2;
    private List<WeatherEntity> list;
    private Context context;

    public WeatherAdapter(Context mContext, List<WeatherEntity> lis) {
        list = lis;
        context = mContext;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder viewHolder = null;
        switch (viewType) {
            case HEADER:
                View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_weather, null);
                if (headerView.getLayoutParams() == null) {
                    Log.e("TAG", "onCreateViewHolder: ");
                    headerView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, Dp2Px(160)));
                }
                viewHolder = new WeatherAdapter.ItemViewHolder(headerView);
                break;
            case WEAT_NORMAL:
                viewHolder = new WeatherAdapter.ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather, null));

                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case WEAT_NORMAL:
                WeatherEntity weatherEntity = list.get(position-1);
                holder.week.setText(weatherEntity.getWeek());
                holder.temp_detail.setText(weatherEntity.getTempRange());
                holder.weather_detail.setText(weatherEntity.getWeather());
                if (position == 0) {
                    holder.toady_show.setVisibility(View.VISIBLE);
                }
                break;
            case HEADER:
                /**
                 * 0 晴天
                 * 1 云
                 * 2 阴
                 * 4 雷阵雨
                 * 7 小雨
                 * 8 中雨
                 */
                if (list != null && list.size() != 0) {
                    WeatherEntity weatherEntity1 = list.get(0);
                    Log.e("TAG", "ocdcdcd: "+weatherEntity1.toString() );
                    holder.city.setText(weatherEntity1.getCity());
                    holder.weather_detail.setText(weatherEntity1.getWeather());
                    holder.airQuality.setText(weatherEntity1.getAir());
                    holder.tempRange.setText(weatherEntity1.getTempRange());
                    holder.temp.setText(weatherEntity1.getTemp());
                    holder.wind.setText(weatherEntity1.getWind());
                    Glide.with(context).load(weatherEntity1.getImg()).into(holder.weather_icon);
                    String date = weatherEntity1.getDate();
                    String year = date.split("-")[0];
                    String month = date.split("-")[1];
                    String day = date.split("-")[2];
                    String dateC = year + "年" + month + "月" + day + "日";
                    holder.time.setText(dateC+" "+weatherEntity1.getWeek());
                    Log.e("TAG", "onBindViewHolder: "+list.get(0).getWeatherType() );
                    if ("0".equals(weatherEntity1.getWeatherType())) {
                        holder.weather_rl.setBackgroundResource(R.drawable.sunny);
                    }else if ("1".equals(weatherEntity1.getWeatherType())){
                        holder.weather_rl.setBackgroundResource(R.drawable.cloud);
                    }else if ("2".equals(weatherEntity1.getWeatherType())){
                        holder.weather_rl.setBackgroundResource(R.drawable.yin);
                    }else if ("4".equals(weatherEntity1.getWeatherType())){
                        holder.weather_rl.setBackgroundResource(R.drawable.rain);
                    }
                }
                break;
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size() + 1;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView week;
        TextView toady_show;
        TextView weather_detail;
        TextView temp_detail;

        TextView time;
        TextView city;
        TextView temp;
        TextView airQuality;
        TextView wind;
        TextView tempRange;
        RelativeLayout weather_rl;
        ImageView weather_icon;

        public ItemViewHolder(View itemView) {
            super(itemView);
            week = itemView.findViewById(R.id.week);
            toady_show = itemView.findViewById(R.id.toady_show);
            weather_detail = itemView.findViewById(R.id.weather_detail);
            temp_detail = itemView.findViewById(R.id.temp_detail);
            time = itemView.findViewById(R.id.time);
            city = itemView.findViewById(R.id.city);
            temp = itemView.findViewById(R.id.temp);
            airQuality = itemView.findViewById(R.id.air);
            wind = itemView.findViewById(R.id.wind);
            weather_detail = itemView.findViewById(R.id.weather_detail);
            tempRange = itemView.findViewById(R.id.tem_range);
            weather_icon = itemView.findViewById(R.id.weather_icon);
            weather_rl = itemView.findViewById(R.id.today_weather);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (position == 0) {
            type = HEADER;
        } else {
            type = WEAT_NORMAL;
        }
        return type;
    }

    public int Dp2Px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
