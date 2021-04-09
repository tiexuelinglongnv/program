package com.zll.program.ui.home;


import com.zll.program.ui.top.TopFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles;
    private int[] mChannelTypes;

    public HomeFragmentPagerAdapter(FragmentManager fm, String[] titles, int[] channelTypes) {
        super(fm);
        this.mTitles = titles;
        this.mChannelTypes = channelTypes;
    }

    @Override
    public int getCount() {
        return null == mTitles ? 0 : mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return TopFragment.newInstance(mChannelTypes[position], position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
