package com.example.errors_correlation.HorizontalListAdapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.errors_correlation.MainActivity;
import com.example.errors_correlation.R;

import java.util.List;
import java.util.Random;

public class  ListItemAdapter extends RecyclerView.Adapter<HorizontalListViewHolder> {
        List<itemList> horizontalList;

        public ListItemAdapter(List horizontalList, MainActivity mainActivity) {
        this.horizontalList = horizontalList;
        }

        @NonNull
        @Override
        public HorizontalListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);
                return new HorizontalListViewHolder(itemView);
                }

    @Override
    public void onBindViewHolder(HorizontalListViewHolder holder, int position) {
        itemList data = horizontalList.get(position);
        holder.listItemBtn.setText(data.bit.toString());
        holder.listItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = holder.listItemBtn.getText().equals("1") ? 0 : 1;
                holder.listItemBtn.setText(String.valueOf(number));
                horizontalList.set(position, new itemList((byte) number));
            }
        });
    }
        @Override
        public int getItemCount() {
                return horizontalList.size();
        }

}
