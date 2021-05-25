package com.example.errors_correlation.HorizontalListAdapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.errors_correlation.MainActivity;
import com.example.errors_correlation.R;

import java.util.List;


import static android.graphics.Color.parseColor;
import static com.example.errors_correlation.R.*;
import static com.example.errors_correlation.R.color.*;


public class  ListItemAdapter extends RecyclerView.Adapter<HorizontalListViewHolder> {
        List<itemList> horizontalList;

        public ListItemAdapter(List horizontalList, MainActivity mainActivity) {
        this.horizontalList = horizontalList;
        }

        @NonNull
        @Override
        public HorizontalListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(layout.list_item, viewGroup, false);
                return new HorizontalListViewHolder(itemView);
                }

    @Override
    public void onBindViewHolder(HorizontalListViewHolder holder, int position) {
        itemList data = horizontalList.get(position);
        holder.listItemBtn.setText(data.bit.toString());
        holder.listItemBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                int number = holder.listItemBtn.getText().equals("1") ? 0 : 1;
                holder.listItemBtn.setText(String.valueOf(number));
                horizontalList.set(position, new itemList((byte) number));
                holder.listItemBtn.setTextColor( holder.listItemBtn.getCurrentTextColor() == parseColor("#ffaeea00")? parseColor("#ffff1744") : parseColor("#ffaeea00"));
            }
        });
    }
        @Override
        public int getItemCount() {
                return horizontalList.size();
        }

}
