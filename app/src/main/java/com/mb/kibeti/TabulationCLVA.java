package com.mb.kibeti;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TabulationCLVA extends ArrayAdapter<FeedbackRowItem> {
    Context context;
 
    public TabulationCLVA(Context context, int resourceId, List<FeedbackRowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    private class ViewHolder {
        TextView tvAlpha, tvBeta, tvGamma, tvDelta, tvEpsilon, tvZeta;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        FeedbackRowItem rowItem = getItem(position);
         
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_tabulation, null);
            holder = new ViewHolder();

            holder.tvAlpha = convertView.findViewById(R.id.tvAlpha);
            holder.tvBeta = convertView.findViewById(R.id.tvBeta);
            holder.tvGamma = convertView.findViewById(R.id.tvGamma);
            holder.tvDelta = convertView.findViewById(R.id.tvDelta);
            holder.tvEpsilon = convertView.findViewById(R.id.tvEpsilon);
            holder.tvZeta = convertView.findViewById(R.id.tvZeta);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.tvAlpha.setText(rowItem.getStrAlpha());
        holder.tvBeta.setText(rowItem.getStrBeta());
        holder.tvGamma.setText(rowItem.getStrGamma());
        holder.tvDelta.setText(rowItem.getStrDelta());
        holder.tvEpsilon.setText(rowItem.getStrEpsilon());
        holder.tvZeta.setText(rowItem.getStrZeta());
        return convertView;
    }
}