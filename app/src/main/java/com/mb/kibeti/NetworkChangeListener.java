package com.mb.kibeti;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Common.isConnectedToInternet(context)) {
            Toast.makeText(context, "No internet connection...", Toast.LENGTH_LONG).show();

        }
//            new AlertDialog.Builder(context)
//
//                    .setMessage("Internet connection is required to proceed...")
//                    .setCancelable(true)
//                    .setPositiveButton("Check settings",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
//                                    Toast.makeText(context, "Kindly relaunch this App after reconfiguring the internet settings...", Toast.LENGTH_LONG).show();

//                                }
//                            }).create().show();
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.check_internet_dialog, null);
//            builder.setView(layout_dialog);
//            AppCompatButton btnRetry = layout_dialog.findViewById(R.id.btnRetry);
//            AlertDialog dialog = builder.create();
//            dialog.show();
//            dialog.setCancelable(false);
//            dialog.getWindow().setGravity(Gravity.CENTER);
//            btnRetry.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//                    onReceive(context, intent);
//                }
//            });
//        }
    }
    public boolean isNetwork(Context context){
        return Common.isConnectedToInternet(context);
    }
}
