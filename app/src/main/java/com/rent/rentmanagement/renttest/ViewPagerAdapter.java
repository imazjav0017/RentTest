package com.rent.rentmanagement.renttest;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imazjav0017 on 01-03-2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList=new ArrayList();
    private final List<String> fragmentTitlesList=new ArrayList();
    Context context;

    public ViewPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        return  fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentTitlesList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitlesList.get(position);
    }
    public void addFragment(Fragment fragment,String title)
    {
        fragmentList.add(fragment);
        fragmentTitlesList.add(title);
    }
}
