package so.chinaso.com.voicemodule.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import so.chinaso.com.voicemodule.R;
import so.chinaso.com.voicemodule.entity.RestaurantEntity;

/**
 * Created by yf on 2018/9/7.
 */
public class RestaurantAdapter extends RecyclerView.Adapter<ChatMessageHolder> {
    private List<RestaurantEntity> list;
    private Context mContext;

    public RestaurantAdapter(Context mContext, List<RestaurantEntity> list) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ChatMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatMessageHolder(parent, R.layout.item_restaurant);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageHolder holder, int position) {
        holder.res_name.setText(list.get(position).getName());
        holder.res_address_detail.setText(list.get(position).getAddress());
        String telephone = list.get(position).getPhone().split(";")[0];
        holder.res_telephone.setText(telephone);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        Glide.with(mContext).applyDefaultRequestOptions(requestOptions).load(list.get(position).getImg()).into(holder.res_image);
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}
