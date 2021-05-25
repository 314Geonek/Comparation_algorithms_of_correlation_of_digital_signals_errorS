package com.example.errors_correlation.HorizontalListAdapter;

import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.errors_correlation.R;


public class HorizontalListViewHolder extends RecyclerView.ViewHolder {
    public Button listItemBtn;
    public HorizontalListViewHolder(View itemView) {
        super(itemView);
        this.listItemBtn = itemView.findViewById(R.id.HorizontalListItemBtn);
    }
}
