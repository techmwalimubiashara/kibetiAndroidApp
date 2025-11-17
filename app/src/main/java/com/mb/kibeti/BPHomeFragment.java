package com.mb.kibeti;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class BPHomeFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    View view;
    BPHomeViewPagerAdapter homeViewPagerAdapter;
    PipeLines Genetics = new PipeLines();
    String strBusinessNamePh = Genetics.Earth;
    String strUsernamePh = Genetics.Mercury;
    String strEmailAddressPh = Genetics.Venus;
    String strBusinessName = "",strUsername="", strEmailAddress="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        strBusinessName = ((AppCompatActivity)getActivity()).getSupportActionBar().getTitle().toString();

        view = inflater.inflate(R.layout.fragment_bp_home, container, false);
//        return inflater.inflate(R.layout.fragment_cash, container, false);

//        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
//        strEmailAddress = sharedPreferences.getString(EMAIL, "");
//        strUsername = sharedPreferences.getString(USERNAME, "");

        Bundle bundle = this.getArguments();
        strBusinessName = bundle.getString(strBusinessNamePh);
        strEmailAddress = bundle.getString(strEmailAddressPh);
        strUsername = bundle.getString(strUsernamePh);


        tabLayout = view.findViewById(R.id.tablayout);
        viewPager2 = view.findViewById(R.id.view_pager);
        homeViewPagerAdapter = new BPHomeViewPagerAdapter(getActivity(),strBusinessName);

//        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
//                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
//            @Override
//            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//            }        }
//        );

        viewPager2.setAdapter(homeViewPagerAdapter);

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

        return view;
    }
}