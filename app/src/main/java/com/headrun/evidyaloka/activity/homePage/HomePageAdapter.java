package com.headrun.evidyaloka.activity.homePage;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.headrun.evidyaloka.widgets.FragmentStatePagerAdapter;

public class HomePageAdapter extends FragmentStatePagerAdapter {


    private FragmentManager fm;
    private String[] tabTitle ;

    public HomePageAdapter(Context mContext, FragmentManager fm, String[] tabTitle) {
        super(fm);
        this.fm = fm;
        this.tabTitle=tabTitle;

    }

    @Override
    public int getCount() {
        return tabTitle.length;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return AboutUsFragment.newInstance();
        }else if(position==1){
            return InfluencerFragment.newInstance();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = tabTitle[position];
        return title;
    }

}
