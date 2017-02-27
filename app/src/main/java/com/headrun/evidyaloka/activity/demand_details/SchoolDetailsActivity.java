package com.headrun.evidyaloka.activity.demand_details;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.demand_slots.DemandSlotActivity;
import com.headrun.evidyaloka.activity.base.HomeActivity;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.dto.SchoolDetailResponse;
import com.headrun.evidyaloka.model.Demand;
import com.headrun.evidyaloka.model.SchoolDetails;
import com.headrun.evidyaloka.utils.ImageLoadingUtils;
import com.headrun.evidyaloka.utils.NetworkUtils;
import com.headrun.evidyaloka.widgets.AppBarStateChangeListener;

import java.util.Arrays;

/**
 * Created by sujith on 18/1/17.
 */

public class SchoolDetailsActivity extends AppCompatActivity implements ResponseListener<SchoolDetailResponse> {

    public String TAG = SchoolDetailsActivity.class.getSimpleName();
    private Demand mdemands;
    private SchoolDetails mSchoolDetails;
    private SimpleDraweeView mHeaderImage, posterImage;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private TextView school_title, school_demand, txt_over_view;
    private RecyclerView current_tech_list, class_assistent_list, center_admin_list, school_news_list;

    private Button teach_btn;
    private ViewPager mViewPager;

    private SchoolNewsAdapter mSchoolNewsAdapter;
    private TabLayout tabLayout;
    private boolean isFavoriteChanged = false, is_network = false;

    private TeacharsPhotoAdapter teacher_adapter, admin_adapter, assistant_adapter;


    @Override
    protected void onStart() {
        super.onStart();
        /*if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }*/
    }

    @Override
    protected void onDestroy() {
        /*if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }*/
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_school_details);

        Log.i(TAG, "school details activity");
        getIntentData();

        initViews();

        inflateData();

    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle data = intent.getExtras();
            mdemands = (Demand) intent.getExtras().getParcelable(Demand.TAG_DEMAND);
        }
    }

    private void initViews() {
        appBarLayout = (AppBarLayout) findViewById(R.id.appbarLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        //  mFavoriteButton = (FloatingActionButton) findViewById(R.id.favButton);
        mHeaderImage = (SimpleDraweeView) findViewById(R.id.headerImage);
        posterImage = (SimpleDraweeView) findViewById(R.id.posterImage);
        school_title = (TextView) findViewById(R.id.school_title);
        school_demand = (TextView) findViewById(R.id.school_demand);
        txt_over_view = (TextView) findViewById(R.id.txt_over_view);
        current_tech_list = (RecyclerView) findViewById(R.id.current_tech_list);
        class_assistent_list = (RecyclerView) findViewById(R.id.class_assistent_list);
        center_admin_list = (RecyclerView) findViewById(R.id.center_admin_list);
        school_news_list = (RecyclerView) findViewById(R.id.school_news_list);
        teach_btn = (Button) findViewById(R.id.teach_btn);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        current_tech_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        class_assistent_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        center_admin_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        school_news_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    private void inflateData() {

        try {
            appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                @Override
                public void onExpanded(AppBarLayout appBarLayout) {

                    getSupportActionBar().setTitle(mdemands.title);

                }

                @Override
                public void onCollapsed(AppBarLayout appBarLayout) {


                    getSupportActionBar().setTitle(mdemands.title);

                }

                @Override
                public void onIdle(AppBarLayout appBarLayout) {

                }
            });

            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

            ImageLoadingUtils.load(mHeaderImage, mdemands.image);
            ImageLoadingUtils.load(posterImage, mdemands.image);
            school_title.setText(mdemands.title);
            txt_over_view.setText(mdemands.description);
            school_demand.setText(Arrays.toString(mdemands.tags.subjects).replaceAll("\\[|\\]", ""));

            teach_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(SchoolDetailsActivity.this, DemandSlotActivity.class).
                            putExtra(Demand.TAG_DEMAND, mdemands).
                            putExtra(SchoolDetails.TAG_SCHOOLDEATILS, mSchoolDetails).
                            putExtra(Constants.TYPE, is_network));

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (NetworkUtils.isNetworkConnected(getApplicationContext())) {
            getFilerData();
        } else {
            if (mdemands != null || mdemands.Scholle_deatils != null) {
                this.mSchoolDetails = mdemands.Scholle_deatils;
                notifyData(mSchoolDetails);
                is_network = true;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                //onClickShare();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeActivity.class));
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
        new EVDNetowrkServices().getDemandDetails(this, this, mdemands.id);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(SchoolDetailResponse response) {

        //  Log.i(TAG, "response is " + response);

        this.mSchoolDetails = response.data;
        if (mSchoolDetails != null) {
            mdemands.Scholle_deatils = mSchoolDetails;
            Constants.LIST_DEMANDS.put(mdemands.id, mdemands);
            notifyData(mSchoolDetails);
        }
    }

    private void notifyData(SchoolDetails mSchoolDetails) {

        txt_over_view.setText(mSchoolDetails.school_desc);
        teacher_adapter = new TeacharsPhotoAdapter(this, mSchoolDetails.current_teachers);
        admin_adapter = new TeacharsPhotoAdapter(this, mSchoolDetails.school_admin);
        assistant_adapter = new TeacharsPhotoAdapter(this, mSchoolDetails.class_assistant);
        mSchoolNewsAdapter = new SchoolNewsAdapter(this, mSchoolDetails.class_detals);

        current_tech_list.setAdapter(teacher_adapter);
        class_assistent_list.setAdapter(assistant_adapter);
        center_admin_list.setAdapter(admin_adapter);
        school_news_list.setAdapter(mSchoolNewsAdapter);
    }
}
