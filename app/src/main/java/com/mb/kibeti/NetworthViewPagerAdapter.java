package com.mb.kibeti;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class NetworthViewPagerAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> headers = new ArrayList<>();



    public NetworthViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager,lifecycle);
        initData();
    }
    public NetworthViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        initData();
    }
    private void initData(){

        addData(new IncomeFragment(),"Income");
        addData(new ExpensesFragment(),"Expenses");
        addData(new SpendingReport(),"Report");
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    private void addData(Fragment fragment, String header){
        fragments.add(fragment);
        headers.add(header);
    }
    public String getHeader(int position){ return headers.get(position);}

}
