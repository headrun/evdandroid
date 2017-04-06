package com.headrun.evidyaloka.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.auth.LoginActivity;
import com.headrun.evidyaloka.activity.auth.LoginActivity;
import com.headrun.evidyaloka.activity.demands.DemandFragment;
import com.headrun.evidyaloka.activity.auth.ProfileFragment;
import com.headrun.evidyaloka.activity.profileUpdate.ProfileUpdate;
import com.headrun.evidyaloka.activity.self_evaluation.SelfEvaluationActivity;
import com.headrun.evidyaloka.activity.sessions.SessionsTabFragment;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.dto.FiltersDataResponse;
import com.headrun.evidyaloka.evdservices.ChangeSessionStatusService;
import com.headrun.evidyaloka.model.FiltersData;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends BaseActivity implements HomeView, ImageAdapter.NavigatePage, ViewPager.OnPageChangeListener, ResponseListener<FiltersDataResponse>, BottomNavigationView.OnNavigationItemSelectedListener {

    public String TAG = HomeActivity.class.getSimpleName();

    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;
    boolean doubleBackToExitPressedOnce = false;

    private BottomNavigationView bottom_nav;
    ViewPager view_pager;

    Menu NavMenu, home_menu;
    MenuItem act_volunteer;
    TextView act_vol_txt;

    private boolean redirect_to = false;
    private String redirect_type;
    private LinearLayout mDotsLayout;
    private ImageAdapter mAdapter;
    private int mDotsCount;
    static TextView mDotsText[];
    private int mPosition = 0;

    HomePresenter mHomePresenter;

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "start");
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        Log.i(TAG, "destory");

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mHomePresenter == null)
            mHomePresenter = new HomePresenter(this, this);
        mHomePresenter.setSession();

        this.doubleBackToExitPressedOnce = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "restart");
    }

    @Override
    public void enableSession() {

        if (NavMenu != null)
            NavMenu.findItem(R.id.action_sessions).setEnabled(true);


    }

    @Override
    public void disableSession() {
        if (NavMenu != null)
            NavMenu.findItem(R.id.action_sessions).setEnabled(false);
    }

    @Override
    public void setGalleryImages() {

        LinkedHashMap<String, Drawable> gallery_img = mHomePresenter.setGalleryImages();

        mDotsCount = gallery_img.size();
        if (mDotsCount > 0) {
            mAdapter = new ImageAdapter(getApplicationContext(), 1);

            mDotsText = new TextView[mDotsCount];

            mDotsLayout.removeAllViews();
            for (int i = 0; i < mDotsCount; i++) {
                mDotsText[i] = new TextView(getApplicationContext());
                mDotsText[i].setText(".");
                mDotsText[i].setTextSize(45);
                mDotsText[i].setTypeface(null, Typeface.BOLD);
                mDotsText[i].setTextColor(android.graphics.Color.GRAY);
                mDotsLayout.addView(mDotsText[i]);
            }

            mAdapter.setList(gallery_img);
            view_pager.setAdapter(mAdapter);
            mAdapter.setonclick(this);

            setSelectedDotColor(mPosition);
            Timer timer = new Timer();
            timer.schedule(new UpdateTimeTask(), 10000, 10000);
            showGallery();
        } else {
            hideGallery();
        }
    }

    @Override
    public void setDemands() {

    }

    @Override
    public void LoginCheck() {

    }

    @Override
    public void showGallery() {
        view_pager.setVisibility(View.VISIBLE);
        mDotsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideGallery() {
        view_pager.setVisibility(View.GONE);
        mDotsLayout.setVisibility(View.GONE);
    }

    @Override
    public void navigateToPage(String value) {
        mHomePresenter.navigateGallery(value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mHomePresenter = new HomePresenter(this, this);

        getintentData();
        initview();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        bottom_nav.setOnNavigationItemSelectedListener(this);

        Constants.ISNOTIFICATION = false;
        hideGallery();
        getFilerData();
        showFavoriteMoviesFragment();
    }

    private void initview() {
        bottom_nav = (BottomNavigationView) findViewById(R.id.bottom_nav);
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        mDotsLayout = (LinearLayout) findViewById(R.id.image_count);
        NavMenu = bottom_nav.getMenu();
        view_pager.addOnPageChangeListener(this);

    }

    private void getintentData() {

        Bundle data = getIntent().getExtras();
        if (data != null) {
            redirect_to = data.getBoolean(Constants.REDIRECT_TO);
            redirect_type = data.getString(Constants.TYPE);
        }
    }

    private void setSelectedDotColor(int position) {
        try {
            for (int i = 0; i < mDotsCount; i++) {
                mDotsText[i].setTextColor(getApplicationContext().getResources().getColor(
                        R.color.dark_grey));
            }
            mDotsText[position].setTextColor(getApplicationContext().getResources().getColor(
                    R.color.magenta));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFavoriteMoviesFragment() {

        if (redirect_to) {
            onNavigationItemSelected(bottom_nav.getMenu().getItem(1).setChecked(true));
        } else {
            onNavigationItemSelected(bottom_nav.getMenu().getItem(0).setChecked(true));
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.homeFragment, fragment)
                .commit();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onpause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            doubleBackToExitPressedOnce = false;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            System.exit(0);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(getBaseContext(),
                "Press once again to exit!", Toast.LENGTH_SHORT)
                .show();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_demands:
                mHomePresenter.setGallery();

                enableVolunteer();
                replaceFragment(new DemandFragment());
                break;
            case R.id.action_sessions:
                hideGallery();
                disableVolunteer();
                if (redirect_type != null && redirect_type.equals("session_page"))
                    replaceFragment(new SessionsTabFragment().newInstance("prefered"));
                else
                    replaceFragment(new SessionsTabFragment());
                break;
            case R.id.action_profile:
                hideGallery();
                disableVolunteer();
                if (utils.userSession.getIsLogin())
                    replaceFragment(new ProfileFragment());
                else
                    startActivity(new Intent(this, LoginActivity.class)
                            .putExtra(Constants.TYPE, true)
                            .putExtra(Constants.REDIRECT_TO, "home_page"));
                break;
        }

        setActivityTitle(item.getTitle().toString());

        return true;

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(FiltersDataResponse response) {

        Log.i(FiltersData.TAG, String.valueOf(response));
        if (response != null) {
            Constants.FILTERDATA = response;
            userSession.setLangFilter(Arrays.asList(response.languages).toString());
            userSession.setStateFilter(Arrays.asList(response.states).toString());
            userSession.setSessionTitles(Arrays.asList(response.sessions_filters).toString());
            userSession.setUserRoles(utils.userRolesMap(response.user_roles));
            userSession.getUserRoles();
        }
    }

    public void getFilerData() {

        if (Constants.FILTERDATA == null)
            new EVDNetowrkServices().getFiltersData(this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        home_menu = menu;
        try {
            act_volunteer = home_menu.findItem(R.id.action_volunteer);
            act_volunteer.setActionView(android.R.layout.simple_list_item_1);
            act_vol_txt = (TextView) act_volunteer.getActionView().findViewById(android.R.id.text1);
            act_vol_txt.setAllCaps(false);
            act_vol_txt.setText(R.string.volunteer);
            act_vol_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (utils.userSession.getIsLogin())
                        startActivity(new Intent(HomeActivity.this, ProfileUpdate.class));
                    else
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class)
                                .putExtra(Constants.TYPE, true)
                                .putExtra(Constants.REDIRECT_TO, Constants.PROFILE_TYPE));
                }
            });

            act_volunteer.setVisible(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_volunteer:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void enableVolunteer() {

        if (act_vol_txt != null) {
            act_vol_txt.setText(R.string.volunteer);
            act_vol_txt.setVisibility(View.VISIBLE);
        }
    }

    private void disableVolunteer() {

        if (act_vol_txt != null) {
            act_vol_txt.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mPosition = position;

        setSelectedDotColor(position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void navigateToPage(int position) {

        if (position == 0) {
            startActivity(new Intent(this, ProfileUpdate.class));
        } else if (position == 1) {
            startActivity(new Intent(this, SelfEvaluationActivity.class));
        } else if (position == 2) {
            //startActivity(new Intent(this, SelfEvaluationActivity.class));
        }

    }

    @Override
    public void inflateVideoPlayer(String videoKey) {

        int startTimeMillis = 0;
        boolean autoplay = true;
        boolean lightboxMode = false;

        Intent intent = YouTubeStandalonePlayer.createVideoIntent(
                this, "AIzaSyAI1tA5E-W6-uLfAn35VX5nwu-xOG9Tobc", videoKey, startTimeMillis, autoplay, lightboxMode);

        if (intent != null) {
            if (canResolveIntent(intent)) {
                startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
                startService(new Intent(getApplicationContext(), ChangeSessionStatusService.class)
                        .putExtra("request_type", "orientation"));

            } else {
                // Could not resolve the intent - must need to install or update the YouTube API service.
                YouTubeInitializationResult.SERVICE_MISSING
                        .getErrorDialog(this, REQ_RESOLVE_SERVICE_MISSING).show();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_START_STANDALONE_PLAYER && resultCode != Activity.RESULT_OK) {
            YouTubeInitializationResult errorReason =
                    YouTubeStandalonePlayer.getReturnedInitializationResult(data);
            if (errorReason.isUserRecoverableError()) {
                errorReason.getErrorDialog(this, 0).show();
            } else {
                String errorMessage = getResources().getString(R.string.player_error) + errorReason.toString();
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean canResolveIntent(Intent intent) {
        List<ResolveInfo> resolveInfo = this.getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }

    class UpdateTimeTask extends TimerTask {
        public void run() {
            view_pager.post(new Runnable() {
                public void run() {

                    if (view_pager.getCurrentItem() < mAdapter
                            .getCount() - 1) {
                        view_pager.setCurrentItem(
                                view_pager.getCurrentItem() + 1, true);

                    } else {
                        view_pager.setCurrentItem(0, true);

                    }
                }
            });
        }
    }
}
