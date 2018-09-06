package so.chinaso.com.voicemodule.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import so.chinaso.com.voicemodule.R;

/**
 * created by yf on 2018/8/15.
 */
public class AutoPollAdapter extends RecyclerView.Adapter<AutoPollAdapter.BaseViewHolder> {
    private final List<String> mData;

    public AutoPollAdapter(List<String> list) {
        this.mData = list;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_auto_poll, parent, false);
        BaseViewHolder holder = new BaseViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        String data = mData.get(position % mData.size());
        holder.tv.setText(data);
    }

    @Override
    public int getItemCount() {
        return mData== null ? 0:mData.size();
    }

    class BaseViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        public BaseViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_content);
        }
    }
}