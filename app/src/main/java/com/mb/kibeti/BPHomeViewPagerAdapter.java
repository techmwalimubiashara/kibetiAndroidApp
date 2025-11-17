package com.mb.kibeti;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class BPHomeViewPagerAdapter extends FragmentStateAdapter {

    PipeLines Genetics = new PipeLines();
    String strBusinessNamePh = Genetics.Earth;

    String strBusinessName = " ";
    public BPHomeViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String businessName) {
        super(fragmentActivity);

        strBusinessName = businessName;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {


        Bundle args=new Bundle();
        args.putString(strBusinessNamePh, strBusinessName);



        switch (position){
            case 0:
                    MyClientsFragment clientsFragment = new MyClientsFragment();
                    clientsFragment.setArguments(args);
                    return clientsFragment;
            case 1:
                    MyPerformanceFragment performanceFragment = new MyPerformanceFragment();
                    performanceFragment.setArguments(args);
                    return performanceFragment;
            case 2:
                    MyExpensesFragment expensesFragment = new MyExpensesFragment();
                    expensesFragment.setArguments(args);
                    return expensesFragment;
            default:
                    MyClientsFragment clientsFragmentd = new MyClientsFragment();
                    clientsFragmentd.setArguments(args);
                    return clientsFragmentd;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
