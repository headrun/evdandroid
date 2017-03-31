package com.headrun.evidyaloka.activity.demand_slots;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.base.BaseActivity;
import com.headrun.evidyaloka.activity.base.HomeActivity;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.dto.SchoolDetailResponse;
import com.headrun.evidyaloka.model.Demand;
import com.headrun.evidyaloka.model.SchoolDetails;

/**
 * Created by sujith on 18/1/17.
 */

public class DemandSlotActivity extends BaseActivity implements ResponseListener<SchoolDetailResponse>, Response.ErrorListener, TabLayout.OnTabSelectedListener, TimeSlotsFragment.menuConfirm {

    public String TAG = DemandSlotActivity.class.getSimpleName();
    private SchoolDetails mSchoolDetails;
    private Demand demand;
    private TabLayout list_courses;
    private ViewPager time_slots_pager;
    public CourseAdapter mCourseAdapter;

    private TimeSlotsFragment mTimeSlotsFragment;
    Menu menu_confirm;
    MenuItem confirm_item;
    private boolean redirect_type = false;


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demand_slots);

        getIntentData();
        initViews();

        if (mSchoolDetails == null || redirect_type)
            getFilerData();
        else
            setupTabLayout();

    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle data = intent.getExtras();
            demand = (Demand) intent.getExtras().getParcelable(Demand.TAG_DEMAND);
            mSchoolDetails = (SchoolDetails) intent.getExtras().getParcelable(SchoolDetails.TAG_SCHOOLDEATILS);
            redirect_type = intent.getExtras().getBoolean(Constants.TYPE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {

        list_courses = (TabLayout) findViewById(R.id.list_courses);
        time_slots_pager = (ViewPager) findViewById(R.id.time_slots_pager);


    }

    private void setupTabLayout() {
        setActivityTitle("" + demand.title);

        mCourseAdapter = new CourseAdapter(getSupportFragmentManager(), mSchoolDetails);
        time_slots_pager.setAdapter(mCourseAdapter);
        list_courses.setupWithViewPager(time_slots_pager);

        mTimeSlotsFragment = new TimeSlotsFragment();
        mTimeSlotsFragment.setmenuConfirm(this);

        list_courses.addOnTabSelectedListener(this);

        try {

            confirm_item = menu_confirm.findItem(R.id.action_confirm);

           /* if (isRelease()) {
                confirm_item.setTitle(this.getResources().getString(R.string.release));
                menuConfirmEnable();
            }*/

            if (mSchoolDetails.demands != null && mSchoolDetails.demands.length <= 0)
                setErrormessage(R.drawable.no_results, getResources().getString(R.string.no_slots));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_slot_confirm, menu);
        menu_confirm = menu;
        try {
            confirm_item = menu_confirm.findItem(R.id.action_confirm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm:

                Intent intent = new Intent("confirmSlot");
                /*if (isRelease())
                    intent.putExtra("type", "release");
                else*/
                intent.putExtra("type", "confirm");

                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

                break;
            case android.R.id.home:
                if (redirect_type) {
                    startActivity(new Intent(this, HomeActivity.class));
                } else {
                    onBackPressed();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

       /* if (movies != null)
            savedInstanceState.putParcelable(Movies.TAG_MOVIES, movies);
       */
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
           /* if (savedInstanceState.containsKey(Movies.TAG_MOVIES)) {
                movies = (Movies) savedInstanceState.getParcelable(Movies.TAG_MOVIES);
            }*/
        }

        super.onRestoreInstanceState(savedInstanceState);
    }

    public void getFilerData() {
        new EVDNetowrkServices().getDemandDetails(this, this, demand.id);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        volleyerror(error, 0, "");
    }

    @Override
    public void onResponse(SchoolDetailResponse response) {

        Log.i(TAG, "response is " + response);

        mSchoolDetails = response.data;
        demand.Scholle_deatils = mSchoolDetails;

        if (Constants.LIST_DEMANDS.containsKey(demand.id))
            Constants.LIST_DEMANDS.put(demand.id, demand);

        setupTabLayout();

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        time_slots_pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        time_slots_pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        time_slots_pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void menuConfirmEnable() {
        if (isRelease() == false && confirm_item != null)
            confirm_item.setVisible(true);
    }

    @Override
    public void menuConfirmDesable() {
        if (!isRelease() && confirm_item != null)
            confirm_item.setVisible(false);
    }

    @Override
    public void menuChangeTitle(String title) {
        confirm_item.setTitle(title);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (redirect_type) {
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            onBackPressed();
        }
        return true;
    }

    private boolean isRelease() {
        if (mSchoolDetails.slot_details != null && mSchoolDetails.slot_details.slots_data != null && mSchoolDetails.slot_details.slots_data.length > 0)
            return true;

        return false;
    }

}
