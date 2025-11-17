package com.mb.kibeti;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CashflowViewPagerAdapter extends FragmentStateAdapter {

    public CashflowViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new InflowFragment();
            case 1:
                return new OutflowFragment();
            case 2:
                return new CashflowSnapshotFragment();
            default:
                return new InflowFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
