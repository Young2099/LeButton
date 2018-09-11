package so.chinaso.com.voicemodule.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import so.chinaso.com.voicemodule.R;
import so.chinaso.com.voicemodule.entity.WeatherEntity;

/**
 * Created by yf on 2018/8/31.
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ItemViewHolder> {
    private List<WeatherEntity> list;

    public WeatherAdapter(List<WeatherEntity> lis) {
        list = lis;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather, null);
        return new WeatherAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.week.setText(list.get(position).getWeek());
        holder.temp_detail.setText(list.get(position).getTempRange());
        holder.weather_detail.setText(list.get(position).getWeather());
        if (position == 0) {
            holder.toady_show.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView week;
        TextView toady_show;
        TextView weather_detail;
        TextView temp_detail;

        public ItemViewHolder(View itemView) {
            super(itemView);
            week = itemView.findViewById(R.id.week);
            toady_show = itemView.findViewById(R.id.toady_show);
            weather_detail = itemView.findViewById(R.id.weather_detail);
            temp_detail = itemView.findViewById(R.id.temp_detail);

        }
    }
}
