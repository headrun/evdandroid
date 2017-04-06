package com.headrun.evidyaloka.activity.demands;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.headrun.evidyaloka.config.Config;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.demand_details.SchoolDetailsActivity;
import com.headrun.evidyaloka.activity.demand_slots.DemandSlotActivity;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.dto.DemandResponse;
import com.headrun.evidyaloka.event.SchoolSelectEvent;
import com.headrun.evidyaloka.event.TeachClickEvent;
import com.headrun.evidyaloka.model.Demand;
import com.headrun.evidyaloka.model.SchoolDetails;
import com.headrun.evidyaloka.utils.EndlessRecyclerView;
import com.headrun.evidyaloka.utils.GridSpacingItemDecoration;
import com.headrun.evidyaloka.utils.NetworkUtils;
import com.headrun.evidyaloka.utils.UserSession;
import com.headrun.evidyaloka.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sujith on 10/1/17.
 */

public class DemandFragment extends BaseEVDFragment implements ResponseListener<DemandResponse>, Response.ErrorListener, DemandAdapter.Callbacks, TabLayout.OnTabSelectedListener {

    private static final String TAG_SORT = "state_sort";

    private static final String TAG = DemandFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    //  private ViewPager image_pager;
    private TabLayout subtabLayout;
    private GridLayoutManager gridLayoutManager;
    private SwipeRefreshLayout demand_refresh_layout;

    private DemandAdapter mDemandAdapter;
    // private ImageSlideAdapter image_adapter;
    private List<Demand> mDemands = new ArrayList<>();

    private int currentPage = 0, totalPages = 0;

    private RelativeLayout no_result_lay;
    private TextView error_textView;
    private ImageView error_img;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDetach() {

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        super.onDetach();
    }

    public static DemandFragment newInstance() {
        Bundle args = new Bundle();
        //args.putSerializable(TAG_SORT, sort);

        DemandFragment fragment = new DemandFragment();
        //fragment.setArguments(args);
        return fragment;
    }

    public DemandFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        subtabLayout = (TabLayout) view.findViewById(R.id.subtabLayout);
        no_result_lay = (RelativeLayout) view.findViewById(R.id.no_result_lay);
        demand_refresh_layout = (SwipeRefreshLayout) view.findViewById(R.id.demand_refresh_layout);
        error_textView = (TextView) view.findViewById(R.id.textView);
        error_img = (ImageView) view.findViewById(R.id.error_img);

        userSession = new UserSession(getActivity());
        int columnCount = getResources().getInteger(R.integer.movies_columns);

        gridLayoutManager = new GridLayoutManager(getActivity(), columnCount);
        int spacing = Utils.dpToPx(1, getActivity()); // 10px
        boolean includeEdge = false;
        // recyclerView.addItemDecoration(new GridSpacingItemDecoration(columnCount, spacing, includeEdge));
        recyclerView.setLayoutManager(gridLayoutManager);

        subtabLayout.addOnTabSelectedListener(this);

        for (int i = 0; i < subtabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = subtabLayout.getTabAt(i);
            RelativeLayout relativeLayout = (RelativeLayout)
                    LayoutInflater.from(getActivity()).inflate(R.layout.tablayout_divider, subtabLayout, false);

            TextView tabTextView = (TextView) relativeLayout.findViewById(R.id.tab_title);
            View tab_divider = (View) relativeLayout.findViewById(R.id.tab_divider);

            if (i == 0)
                tab_divider.setVisibility(View.GONE);

            if (tab.getText().toString().contains("All Languages")) {
                tabTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lang, 0, 0, 0);
                String text_title = userSession.getSelLangFilter().toString().replaceAll("\\[|\\]", "");
                if (!text_title.isEmpty())
                    tabTextView.setText(text_title);
                else
                    tabTextView.setText(tab.getText());
            } else {
                tabTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.loc, 0, 0, 0);
                String text_title = userSession.getSelStateFilter().toString().replaceAll("\\[|\\]", "");
                if (!text_title.isEmpty())
                    tabTextView.setText(text_title);
                else
                    tabTextView.setText(tab.getText());
            }

            tab.setCustomView(relativeLayout);

        }

        recyclerView.addOnScrollListener(new EndlessRecyclerView(gridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {


            }
        });

        mDemandAdapter = new DemandAdapter(Constants.LIST_DEMANDS, getActivity());
        mDemandAdapter.setCallbacks(this);
        recyclerView.setAdapter(mDemandAdapter);


        if (Constants.LIST_DEMANDS.size() == 0 || Constants.PAGE_REFRESH) {
            Constants.PAGE_REFRESH = false;
            getDemandData(currentPage);
        } else {
            getState();
        }

        demand_refresh_layout.setColorSchemeColors(
                Color.parseColor("#ff0000"),
                Color.parseColor("#00ff00"),
                Color.parseColor("#0000ff"),
                Color.parseColor("#f234ab"));

        demand_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                demand_refresh_layout.setRefreshing(true);

                if (NetworkUtils.isNetworkConnected(getActivity())) {
                    getDemandData(currentPage);

                } else {
                    setSnackBar();
                }

            }
        });

    }

    public void getDemandData(final int currentPage) {
        if (isInternetAvailable()) {

            Map<String, String> params = new HashMap<>();
            String sel_lang = new UserSession(getActivity()).getSelLangFilter();
            String sel_states = new UserSession(getActivity()).getSelStateFilter();
            String sel_lang_data = sel_lang.substring(1, sel_lang.length() - 1).replaceAll("\\[|\\]", "");
            String sel_state_data = sel_states.substring(1, sel_states.length() - 1).replaceAll("\\[|\\]", "");

            if (sel_lang_data.length() > 0) {
                String lang_items = sel_lang_data.trim();
                params.put("sel_langs", lang_items);

            }
            if (sel_state_data.length() > 0) {
                String states_items = sel_state_data.trim();
                params.put("sel_states", states_items);
            }

            Constants.DEMAND_SLOTS_STATE = null;
            new EVDNetowrkServices().getDeamnds(getActivity(), this, params);
            if (demand_refresh_layout.isRefreshing() == false)
                showProgressDialog();
        } else {
            setSnackBar();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("error", "->" + error);

        if (demand_refresh_layout.isRefreshing())
            demand_refresh_layout.setRefreshing(false);
        else
            hideProgressDialog();


        if (error instanceof NetworkError) {
            seterror_display(R.drawable.connection_error, getResources().getString(R.string.no_demands));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final TeachClickEvent event) {
        // Log.e("onEvent", "->" + event.mdemand);
        setState();
        startActivity(new Intent(getActivity(), DemandSlotActivity.class).
                putExtra(Demand.TAG_DEMAND, event.mdemand).
                putExtra(SchoolDetails.TAG_SCHOOLDEATILS, event.mdemand.Scholle_deatils).
                putExtra(Constants.TYPE, true));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final SchoolSelectEvent event) {
        //Log.e("onEvent", "->" + event.getSchoolSelecedEvent());
        setState();
        Demand demand = event.getSchoolSelecedEvent();

        if (demand != null) {
            Intent intent = new Intent(getActivity(), SchoolDetailsActivity.class);
            intent.putExtra(Demand.TAG_DEMAND, demand);
            startActivity(intent);
        }
    }

    @Override
    public void onResponse(DemandResponse response) {

        if (demand_refresh_layout.isRefreshing()) {
            demand_refresh_layout.setRefreshing(false);
        }
        Constants.LIST_DEMANDS.clear();
        if (response != null && response.data != null) {

            List<Demand> demands = response.data;

            if (demands.size() > 0) {
                ListtoHaspMap(demands);
            } else {
                seterror_display(R.drawable.no_results, getResources().getString(R.string.no_demands));
            }
        } else {
            Log.i(TAG, "response is null");
            seterror_display(R.drawable.no_results, getResources().getString(R.string.no_demands));
        }
        mDemandAdapter.notifyDataSetChanged();
        hideProgressDialog();
    }

    private void ListtoHaspMap(List<Demand> demands) {

        if (demands.size() > 0) {
            for (Demand demand : demands) {
                Constants.LIST_DEMANDS.put(demand.id, demand);
            }
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        selecredTab(tab);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        selecredTab(tab);
    }

    public void selecredTab(TabLayout.Tab tab) {

        if (tab.getPosition() == 0) {
            Intent intent = new Intent(getActivity(), ListItemsactivity.class);
            intent.putExtra("data", "language");
            startActivity(intent);
        } else if (tab.getPosition() == 1) {
            Intent intent = new Intent(getActivity(), ListItemsactivity.class);
            intent.putExtra("data", "subject");
            startActivity(intent);
        }
    }

    @Override
    public void onSchoolClick(Demand demand) {
        EventBus.getDefault().post(new SchoolSelectEvent(demand));
    }

    @Override
    public void onTeachClick(Demand demand) {
        EventBus.getDefault().post(new TeachClickEvent(demand));
    }

    private void seterror_display(int img, String error) {

        if (error.length() > 0)
            error_textView.setText(error);
        else
            error_textView.setText("");

        error_img.setImageResource(img);

        no_result_lay.setVisibility(View.VISIBLE);
    }

    private void getState() {
        if (Constants.DEMAND_SLOTS_STATE != null)
            recyclerView.getLayoutManager().onRestoreInstanceState(Constants.DEMAND_SLOTS_STATE);
    }

    private void setState() {
        Constants.DEMAND_SLOTS_STATE = recyclerView.getLayoutManager().onSaveInstanceState();
    }

    private void setSnackBar() {

        Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        /*Snackbar snackbar = Snackbar
                .make(getCoordinatorLayout(), R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getDemandData(currentPage);
                    }
                });

        snackbar.setActionTextColor(Color.RED);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();*/
    }
}

