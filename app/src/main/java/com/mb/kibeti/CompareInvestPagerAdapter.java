package com.mb.kibeti;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CompareInvestPagerAdapter extends FragmentStateAdapter {

    public CompareInvestPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new BanksFragment("get_banks.php");
            case 1:
                return new BanksFragment("get_mmf.php");
            case 2:
                return new BanksFragment("get_saccos.php");
            default:
                return new BanksFragment("get_banks.php");
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

}
