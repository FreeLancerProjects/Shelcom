package com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_home.fragment_orders;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.shelcom.R;
import com.appzone.shelcom.adapters.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Orders extends Fragment {

    private TabLayout tab;
    private ViewPager pager;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private ViewPagerAdapter adapter;
    private HomeActivity activity;

    public static Fragment_Orders newInstance()
    {
        return new Fragment_Orders();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        tab = view.findViewById(R.id.tab);
        pager = view.findViewById(R.id.pager);
        tab.setupWithViewPager(pager);
        pager.setOffscreenPageLimit(3);

        fragmentList.add(Fragment_New_Orders.newInstance());
        fragmentList.add(Fragment_Current_Orders.newInstance());
        fragmentList.add(Fragment_Previous_Orders.newInstance());

        titleList.add(getString(R.string.new_order));
        titleList.add(getString(R.string.current_order));
        titleList.add(getString(R.string.previous_order));

        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.AddFragments(fragmentList);
        adapter.AddTitles(titleList);
        pager.setAdapter(adapter);

    }

    public void RefreshFragments()
    {
        Fragment_New_Orders fragment_new_orders = (Fragment_New_Orders) adapter.getItem(0);
        fragment_new_orders.getOrders();
        Fragment_Current_Orders fragment_current_orders = (Fragment_Current_Orders) adapter.getItem(1);
        fragment_current_orders.getOrders();
        Fragment_Previous_Orders fragment_previous_orders = (Fragment_Previous_Orders) adapter.getItem(2);
        fragment_previous_orders.getOrders();
    }

    public void RefreshTime()
    {
        Fragment_New_Orders fragment_new_orders = (Fragment_New_Orders) adapter.getItem(0);
        fragment_new_orders.NotifyAdapterChangeTime();
        Fragment_Current_Orders fragment_current_orders = (Fragment_Current_Orders) adapter.getItem(1);
        fragment_current_orders.NotifyAdapterChangeTime();
        Fragment_Previous_Orders fragment_previous_orders = (Fragment_Previous_Orders) adapter.getItem(2);
        fragment_previous_orders.NotifyAdapterChangeTime();
    }
    public void NavigateToFragmentNew()
    {
        pager.setCurrentItem(0);
    }
    public void NavigateToFragmentCurrent()
    {
        pager.setCurrentItem(1);
    }
    public void NavigateToFragmentPrevious()
    {
        pager.setCurrentItem(2);
    }
}
