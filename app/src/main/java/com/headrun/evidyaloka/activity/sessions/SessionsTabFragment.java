package com.headrun.evidyaloka.activity.sessions;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.demands.BaseEVDFragment;
import com.headrun.evidyaloka.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sujith on 10/2/17.
 */
public class SessionsTabFragment extends BaseEVDFragment implements TabLayout.OnTabSelectedListener {

    private static final String TAG_SESSION_TYPE = "session_type";
    private TabLayout tabLayout;
    private ViewPager SessionViewPager;
    private SessionsTabAdapter mSessionAdapter;
    private String arg_session_type;
    private List<String> tabTitle = new ArrayList<>();
    private Utils utils;

    public static SessionsTabFragment newInstance(String sesstion_type) {
        Bundle args = new Bundle();
        args.putString(TAG_SESSION_TYPE, sesstion_type);
        SessionsTabFragment fragment = new SessionsTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SessionsTabFragment newInstance() {
        SessionsTabFragment fragment = new SessionsTabFragment();
        return fragment;
    }

    public SessionsTabFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sessoins_fragment, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }*/
    }

    @Override
    public void onDetach() {

        /*if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }*/

        super.onDetach();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        utils = new Utils(getActivity());
        tabTitle = utils.userSession.getSessionTitles();
        getarguments();
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        SessionViewPager = (ViewPager) view.findViewById(R.id.session_pager);

        mSessionAdapter = new SessionsTabAdapter(getActivity(), getChildFragmentManager(), tabTitle);
        SessionViewPager.setAdapter(mSessionAdapter);
        tabLayout.setupWithViewPager(SessionViewPager);

        /*for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            RelativeLayout relativeLayout = (RelativeLayout)
                    LayoutInflater.from(getActivity()).inflate(R.layout.tablayout_divider, tabLayout, false);

            TextView tabTextView = (TextView) relativeLayout.findViewById(R.id.tab_title);
            View tab_divider = (View) relativeLayout.findViewById(R.id.tab_divider);

            if (i == 0)
                tab_divider.setVisibility(View.GONE);

            tabTextView.setText(tab.getText());

            tab.setCustomView(relativeLayout);

        }*/

        if (arg_session_type != null && !arg_session_type.isEmpty()) {
            int tab_pos = tabTitle.indexOf(arg_session_type);
            if (tab_pos != -1)
                SessionViewPager.setCurrentItem(tab_pos, true);
            else
                SessionViewPager.setCurrentItem(0, true);

        }

    }

    private void getarguments() {
        Bundle data = getArguments();
        if (data != null) {
            arg_session_type = data.getString(TAG_SESSION_TYPE);
        }

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
