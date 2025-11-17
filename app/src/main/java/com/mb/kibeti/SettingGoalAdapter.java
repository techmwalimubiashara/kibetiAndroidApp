package com.mb.kibeti;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SettingGoalAdapter extends ArrayAdapter<SettingGoalRowItem> {

        Context context;

        public SettingGoalAdapter(Context context, int resourceId, List<SettingGoalRowItem> items) {
            super(context, resourceId, items);
            this.context = context;
        }

        private class ViewHolder {
            TextView tvGoal, tvAmount, tvDate, tvDaily, tvMonthly, tvZeta;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            com.mb.kibeti.SettingGoalAdapter.ViewHolder holder = null;
            SettingGoalRowItem rowItem = getItem(position);

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_setting_goals, null);
                holder = new com.mb.kibeti.SettingGoalAdapter.ViewHolder();

                holder.tvGoal = convertView.findViewById(R.id.tvGoal);
                holder.tvAmount = convertView.findViewById(R.id.tvAmount);
                holder.tvDate = convertView.findViewById(R.id.tvDate);
                holder.tvDaily = convertView.findViewById(R.id.tvDaily);
                holder.tvMonthly = convertView.findViewById(R.id.tvMonthly);
//                holder.tvQ = convertView.findViewById(R.id.tvMonthly);
//                holder.tvZeta = convertView.findViewById(R.id.tvZeta);
                convertView.setTag(holder);
            } else
                holder = (com.mb.kibeti.SettingGoalAdapter.ViewHolder) convertView.getTag();
            holder.tvGoal.setText(rowItem.getStrGoal());
            holder.tvAmount.setText(rowItem.getStrAmount());
            holder.tvDate.setText(rowItem.getStrDate());
            holder.tvDaily.setText(rowItem.getStrDaily());
            holder.tvMonthly.setText(rowItem.getStrMonthly());
//            holder.tvZeta.setText(rowItem.getStrZeta());
            return convertView;
        }
}
