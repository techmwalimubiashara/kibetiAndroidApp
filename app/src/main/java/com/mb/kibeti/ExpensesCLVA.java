package com.mb.kibeti;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ExpensesCLVA extends ArrayAdapter<FeedbackRowItem> {
    Context context;

    public ExpensesCLVA(Context context, int resourceId, List<FeedbackRowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    private class ViewHolder {
        TextView tvAlpha, tvBeta, tvGamma;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        FeedbackRowItem rowItem = getItem(position);
         
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_expenses, null);
            holder = new ViewHolder();

            holder.tvAlpha = convertView.findViewById(R.id.tvAlpha);
            holder.tvBeta = convertView.findViewById(R.id.tvBeta);
            holder.tvGamma = convertView.findViewById(R.id.tvGamma);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.tvAlpha.setText(rowItem.getStrAlpha());
        holder.tvBeta.setText(rowItem.getStrBeta());
        holder.tvGamma.setText(rowItem.getStrGamma());
        return convertView;
    }
}