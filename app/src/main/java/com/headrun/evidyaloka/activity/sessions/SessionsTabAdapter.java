package com.headrun.evidyaloka.activity.sessions;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.widgets.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sujith on 10/2/17.
 */
public class SessionsTabAdapter extends FragmentStatePagerAdapter {


    private FragmentManager fm;
    private List<String> tabTitle = new ArrayList<>();

    public SessionsTabAdapter(Context mContext, FragmentManager fm, List<String> tabTitle) {
        super(fm);
        this.fm = fm;
        this.tabTitle.addAll(tabTitle);

    }

    @Override
    public int getCount() {
        return tabTitle.size();
    }

    @Override
    public Fragment getItem(int position) {
        return SessionTypeFrament.newInstance(tabTitle.get(position).toString());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = tabTitle.get(position);
        return title.substring(0, 1).toUpperCase() + title.substring(1).toLowerCase();
    }

}
