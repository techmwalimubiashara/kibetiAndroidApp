package com.mb.kibeti;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PopupSearchAdapter extends RecyclerView.Adapter<PopupSearchAdapter.ViewHolder> {
    private List<String> itemList;
    private List<String> filteredList;

    private OnItemClickListener onItemClickListener;
    public PopupSearchAdapter(List<String> itemList) {
        this.itemList = itemList;
        this.filteredList = new ArrayList<>(itemList);
    }


    public PopupSearchAdapter(List<String> itemList, OnItemClickListener listener) {
        this.filteredList = new ArrayList<>(itemList);
        this.itemList = itemList;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = filteredList.get(position);
        holder.itemText.setText(item);

        // Handle item click event
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(item, position));
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }
    public interface OnItemClickListener {
        void onItemClick(String item, int position);
    }

    // Search filter function
    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(itemList);
        } else {
            for (String item : itemList) {
                if (item.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemText;
        public ViewHolder(View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.itemText);
        }
    }
}
