package com.headrun.evidyaloka.activity.demand_slots;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.headrun.evidyaloka.model.SchoolDetails;
import com.headrun.evidyaloka.widgets.FragmentStatePagerAdapter;

/**
 * Created by sujith on 23/1/17.
 */
public class CourseAdapter extends FragmentStatePagerAdapter {

    final int PAGE_COUNT = 2;
    private FragmentManager fm;
    private SchoolDetails mSchooldetails;

    public CourseAdapter(FragmentManager fm, SchoolDetails mSchooldetails) {
        super(fm);
        this.fm = fm;
        this.mSchooldetails = mSchooldetails;
    }

    @Override
    public Fragment getItem(int position) {
        return TimeSlotsFragment.newInstance(mSchooldetails, position);
    }

    @Override
    public int getCount() {
        return mSchooldetails.demands.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mSchooldetails.demands[position].grade + "\n" + mSchooldetails.demands[position].subject;
    }
}
