package com.mb.kibeti;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OutflowBudgetPopupAdapter extends RecyclerView.Adapter<OutflowBudgetPopupAdapter.ViewHolder> {

    List<String> items;
    OnItemClick listener;

    public interface OnItemClick {
        void onClicked(String text);
    }

    public OutflowBudgetPopupAdapter(List<String> items, OnItemClick listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.budget_popup_item_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String text = items.get(position);
        holder.title.setText(text);

        holder.itemView.setOnClickListener(v -> {
            listener.onClicked(text);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
//            icon  = itemView.findViewById(R.id.icon);
        }
    }
}
