package com.lanfeng.young.lebutton;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yf on 2018/7/16.
 */
public class AnAdapter extends RecyclerView.Adapter<AnAdapter.ItemViewHolder> {
    private Context mContext;
    private List<String> list;

    public AnAdapter(MainActivity mainActivity, List<String> textList) {
        mContext = mainActivity;
        list = textList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_answer_layout, null);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.answer_text.setText(list.get(i));
        itemViewHolder.answer_choice.setLetterOrNumber("A");
        itemViewHolder.answer_choice.setBgColor(Color.BLACK);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        private LeImage answer_choice;
        private TextView answer_text;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            answer_choice = itemView.findViewById(R.id.answer_choice);
            answer_text = itemView.findViewById(R.id.answer_text);
        }
    }
}
