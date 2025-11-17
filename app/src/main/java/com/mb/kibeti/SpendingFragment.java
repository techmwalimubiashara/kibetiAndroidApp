package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;
import static com.mb.kibeti.LoginActivity.SMSREAD;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
//import com.google.android.material.tabs.TabLayoutMediator;
//import com.mb.treasurechest.databinding.ActivityTablayoutWithIconsBinding;

public class SpendingFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    View view;
    NetworthViewPagerAdapter networthViewPagerAdapter;

    BudgetTracker budgetTracker = new BudgetTracker();
    boolean smsIsRead = false;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_spending, container, false);

        sharedPreferences = getContext().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        smsIsRead = sharedPreferences.getBoolean(SMSREAD, false);

        tabLayout = view.findViewById(R.id.tablayout);
        viewPager2 = view.findViewById(R.id.view_pager);
//        viewPager2.setUserInputEnabled(false);
        networthViewPagerAdapter = new NetworthViewPagerAdapter(getActivity());

        viewPager2.setAdapter(networthViewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_SMS)
//                    != PackageManager.PERMISSION_GRANTED ||
//                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECEIVE_SMS)
//                            != PackageManager.PERMISSION_GRANTED) {
////                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, PERMISSION_REQUEST_CODE);
//                budgetTracker.showPermissionExplanationDialog();
//            } else {
//                if (!smsIsRead) {
//                    if (budgetTracker.readSms() > 0) {
//                        Intent intent = new Intent(getContext(), Read5SMS.class);
//                        startActivity(intent);
////                        SharedPreferences.Editor editor = sharedPreferences.edit();
////                        editor.putBoolean(SMSREAD, true);
////                        editor.apply();
//                    }
//                }
//            }
//        } else {
//            if (!smsIsRead) {
//                budgetTracker.readSms();
//            }
//
//        }

        return view;
    }
}