package com.mb.kibeti;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;

public class CashFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    View view;
    CashflowViewPagerAdapter cashflowViewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_cash, container, false);
//        return inflater.inflate(R.layout.fragment_cash, container, false);

        tabLayout = view.findViewById(R.id.tablayout);
        viewPager2 = view.findViewById(R.id.view_pager);
        cashflowViewPagerAdapter = new CashflowViewPagerAdapter(getActivity());

//        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
//                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
//            @Override
//            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//            }        }
//        );

        viewPager2.setAdapter(cashflowViewPagerAdapter);

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

        viewPager2.registerOnPageChangeCallback(
                new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        return view;
    }
}