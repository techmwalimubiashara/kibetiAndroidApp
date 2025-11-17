package com.mb.kibeti;

import android.content.Context;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class Frequency {
    PopupMenu popup;
    MenuInflater inflater;

    Context context;

    public Frequency(Context context) {
        this.context = context;
    }

    public void showPopup(View v, TextView tv) {
        popup = new PopupMenu(context, v);
        inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.frequency, popup.getMenu());
//        super.onCreateOptionsMenu(popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                tv.setTextColor(ContextCompat.getColor(context, R.color.primary_orange));
                int id = item.getItemId();

                if (id == R.id.daily) {
                    tv.setText("Daily");
                    return true;
                } else if (id == R.id.weekly) {
                    tv.setText("Weekly");
                    return true;
                } else if (id == R.id.monthly) {
                    tv.setText("Monthly");
                    return true;
                } else if (id == R.id.quarterly) {
                    tv.setText("Quarterly");
                    return true;
                } else if (id == R.id.semi_annually) {
                    tv.setText("Semi annually");
                    return true;
                } else if (id == R.id.annually) {
                    tv.setText("Annually");
                    return true;
                }

                return false;
            }

        });

        popup.show();
    }
}
