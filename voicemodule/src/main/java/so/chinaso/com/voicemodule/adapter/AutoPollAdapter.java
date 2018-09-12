package so.chinaso.com.voicemodule.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import so.chinaso.com.voicemodule.ItemClickItem;
import so.chinaso.com.voicemodule.R;
import so.chinaso.com.voicemodule.VoiceActivity;

/**
 * created by yf on 2018/8/15.
 */
public class AutoPollAdapter extends RecyclerView.Adapter<AutoPollAdapter.BaseViewHolder> {
    private final List<String> mData;

    public AutoPollAdapter(List<String> list) {
        this.mData = list;
    }

    private ItemClickItem clickItem;

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_auto_poll, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        String data = mData.get(position % mData.size());
        holder.tv.setText(data);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickItem != null) {
                    clickItem.clickListener(mData.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setClickListener(ItemClickItem clickListener) {
        clickItem = clickListener;
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public BaseViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_content);
        }
    }

}