package com.headrun.evidyaloka.activity.sessionDetails;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.base.BaseActivity;
import com.headrun.evidyaloka.activity.base.HomeActivity;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.utils.ImageLoadingUtils;
import com.headrun.evidyaloka.utils.Utils;
import com.headrun.evidyaloka.widgets.AppBarStateChangeListener;

/**
 * Created by sujith on 4/3/17.
 */

public class SessionDetails extends BaseActivity implements SessionDetailsView, AdapterView.OnItemSelectedListener, AttendanceAdapter.AttendanceCallBack {

    public String TAG = SessionDetails.class.getSimpleName();

    TextView session_teacher, session_topic, attendance_count, session_status, text_topic_title, text_attdencae_title, session_date;

    RecyclerView attendance_list;
    Toolbar toolbar;
    CardView topic_view, topic_content_view, attendance_view, attendance_content;
    public AppBarLayout appbarLayout;
    public CollapsingToolbarLayout collapsing_toolbar;

    Spinner topic_covered;
    EditText session_feedback;
    String sessin_id;
    SimpleDraweeView center_image;
    SessionDeatilsPresenter mSessionPresenter;

    com.headrun.evidyaloka.model.SessionDetails mSessionDeatils;

    Utils utils;

    com.headrun.evidyaloka.model.SessionDetails.Attandance[] mAttandance_list;
    Menu sessionsMenu;
    MenuItem sessionsMenuItem;
    AttendanceAdapter mAttendanceAdapter;
    com.headrun.evidyaloka.model.SessionDetails.Feedback sel_topic;
    boolean alert_message = false;
    String set_session_status;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_details_activity);

        mSessionPresenter = new SessionDeatilsPresenter(this, this);
        mSessionDeatils = new com.headrun.evidyaloka.model.SessionDetails();
        utils = new Utils(this);
        Constants.ATTENDANCE_LIST.clear();
        getBundelData();
        initView();

        mSessionPresenter.callSessionnData(sessin_id);

    }

    private void getBundelData() {

        Bundle data = getIntent().getExtras();
        if (data != null)
            sessin_id = data.getString("session_id");

    }

    public void initView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appbarLayout = (AppBarLayout) findViewById(R.id.appbarLayout);
        // session_grade = (TextView) findViewById(R.id.session_grade);
        session_teacher = (TextView) findViewById(R.id.session_teacher);
        attendance_count = (TextView) findViewById(R.id.attendance_count);
        text_topic_title = (TextView) findViewById(R.id.text_topic_title);
        text_attdencae_title = (TextView) findViewById(R.id.text_attdencae_title);
        session_topic = (TextView) findViewById(R.id.session_topic);
        //  session_time = (TextView) findViewById(R.id.session_time);
        //  session_mnth = (TextView) findViewById(R.id.session_mnth);
        session_date = (TextView) findViewById(R.id.session_date);
        session_status = (TextView) findViewById(R.id.session_status);
        session_feedback = (EditText) findViewById(R.id.session_feedback);
        attendance_list = (RecyclerView) findViewById(R.id.attendance_list);
        topic_covered = (Spinner) findViewById(R.id.topic_covered);
        center_image = (SimpleDraweeView) findViewById(R.id.session_center_img);
        topic_view = (CardView) findViewById(R.id.topic_view);
        topic_content_view = (CardView) findViewById(R.id.topic_content_view);
        attendance_view = (CardView) findViewById(R.id.attendance_view);
        attendance_content = (CardView) findViewById(R.id.attendance_content);
        //   session_centerimg = (SimpleDraweeView) findViewById(R.id.session_center_img);
        // session_lay = (NestedScrollView) findViewById(R.id.session_lay);
        topic_covered.setOnItemSelectedListener(this);
        //session_lay.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsing_toolbar.setExpandedTitleTextAppearance(R.style.ExpandAppBar);
        collapsing_toolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsing_toolbar.setTitle("Evd session");


        attendance_list.setLayoutManager(new LinearLayoutManager(this));

        topic_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    if (topic_content_view.getVisibility() == View.GONE) {
                        enableTopic();
                    } else {
                        disableTopic();
                    }
                }
                return false;
            }
        });


        attendance_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (attendance_content.getVisibility() == View.GONE) {
                        enableAttendance();

                    } else {
                        disableAttendance();

                    }

                }
                return false;
            }
        });

        session_feedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0)
                    alert_message = true;
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }

    @Override
    public void setSessionData(final com.headrun.evidyaloka.model.SessionDetails mSessionDeatils) {


        if (mSessionDeatils != null) {

            this.mSessionDeatils = mSessionDeatils;
            getSupportActionBar().setTitle(mSessionDeatils.center != null ? mSessionDeatils.center : "");

            session_teacher.setText(mSessionDeatils.teacher != null ? mSessionDeatils.teacher : "");

            String grade = mSessionDeatils.grade != null ? mSessionDeatils.grade : "";
            String subject = mSessionDeatils.subject != null ? mSessionDeatils.subject : "";
            session_topic.setText(grade + " , " + subject);

            String time = mSessionDeatils.start_time != null ? mSessionDeatils.start_time : "";
            String date = mSessionDeatils.date != null ? mSessionDeatils.date : "";
            session_date.setText(date + " , " + time);

            if (mSessionDeatils.center_image != null && !mSessionDeatils.center_image.isEmpty())
                ImageLoadingUtils.load(center_image, mSessionDeatils.center_image);

            session_status.setText(mSessionDeatils.session_status != null ? mSessionDeatils.session_status : "");
            session_feedback.setText(mSessionDeatils.comments != null ? mSessionDeatils.comments : "");

            set_session_status = mSessionDeatils.session_status != null ? mSessionDeatils.session_status : "";
            session_status.setTextColor(utils.set_statusColor(set_session_status));

            if (!set_session_status.toLowerCase().equals("scheduled")) {
                session_feedback.setFocusable(false);
                topic_covered.setEnabled(false);
                if (sessionsMenuItem != null)
                    sessionsMenuItem.setVisible(false);
            } else {
                if (sessionsMenuItem != null)
                    sessionsMenuItem.setVisible(true);
            }

            ArrayAdapter<com.headrun.evidyaloka.model.SessionDetails.Feedback> adapter =
                    new ArrayAdapter<com.headrun.evidyaloka.model.SessionDetails.Feedback>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, mSessionDeatils.feedback_details);

            topic_covered.setAdapter(adapter);
            getspinnerSelIndex();

            appbarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                @Override
                public void onExpanded(AppBarLayout appBarLayout) {

                    collapsing_toolbar.setTitle(mSessionDeatils.center != null ? mSessionDeatils.center : "");
                }

                @Override
                public void onCollapsed(AppBarLayout appBarLayout) {

                    collapsing_toolbar.setTitle(mSessionDeatils.center != null ? mSessionDeatils.center : "");
                }

                @Override
                public void onIdle(AppBarLayout appBarLayout) {

                }
            });


            mAttandance_list = mSessionDeatils.session_attandance;
            mSessionPresenter.getPresentedList(mAttandance_list);
            updateSessionDetialsCount(Constants.ATTENDANCE_LIST.size());

            mAttendanceAdapter = new AttendanceAdapter(mAttandance_list, this, mSessionDeatils.session_status);
            attendance_list.setAdapter(mAttendanceAdapter);
            mAttendanceAdapter.setAttendanceCallbacks(this);
            attendance_list.setNestedScrollingEnabled(false);

        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        Log.i(TAG, "sel title is " + mSessionDeatils.feedback_details[position].title + " id " + id);
        sel_topic = mSessionDeatils.feedback_details[position];
        alert_message = true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.session_details_menu, menu);
        sessionsMenu = menu;

        sessionsMenuItem = menu.findItem(R.id.session_done);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.session_done:
                String feedback = session_feedback.getText().toString() != null ? session_feedback.getText().toString().trim() : "";
                mSessionPresenter.submitSessionAlert(mSessionDeatils, Constants.ATTENDANCE_LIST, sessin_id, sel_topic, feedback);
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (alert_message && set_session_status.toLowerCase().equals("scheduled"))
            CaustionAlert();
        else
            finish();
    }

    @Override
    public void attendaceCheck(String id) {
        alert_message = true;
        if (!Constants.ATTENDANCE_LIST.contains(id)) {
            Constants.ATTENDANCE_LIST.add(id);
            updateSessionDetialsCount(Constants.ATTENDANCE_LIST.size());
        }

    }

    @Override
    public void attendaceUnCheck(String id) {
        alert_message = true;
        if (Constants.ATTENDANCE_LIST.contains(id)) {
            Constants.ATTENDANCE_LIST.remove(id);
            updateSessionDetialsCount(Constants.ATTENDANCE_LIST.size());
        }
    }

    @Override
    public void updateSessionDetialsCount(int count) {

        if (mAttandance_list != null) {
            int total = mAttandance_list.length;

            SpannableStringBuilder builder = new SpannableStringBuilder();

            SpannableString str1 = new SpannableString(" Total - " + total + "  ");
            str1.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.teach_now)), 0, str1.length(), 0);
            builder.append(str1);

            SpannableString str2 = new SpannableString(" Present - " + count + "  ");
            str2.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.completed)), 0, str2.length(), 0);
            builder.append(str2);

            SpannableString str3 = new SpannableString(" Abscent - " + (total - count));
            str3.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.cancelled)), 0, str3.length(), 0);
            builder.append(str3);

            attendance_count.setText(builder, TextView.BufferType.SPANNABLE);
        }
    }

    public void CaustionAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String error_msg = "Are you sure you want to cancel the Session";

        builder.setTitle(R.string.app_name);
        builder.setMessage(error_msg);
        builder.setCancelable(false);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    public void getspinnerSelIndex() {

        String topic_id = "";
        if (mSessionDeatils.topics != null) {
            if (mSessionDeatils.topics.length >= 1) {
                com.headrun.evidyaloka.model.SessionDetails.Topic topic = mSessionDeatils.topics[0];
                topic_id = topic.id;
                if (mSessionDeatils.feedback_details != null) {
                    for (int i = 0; i < mSessionDeatils.feedback_details.length; i++) {
                        com.headrun.evidyaloka.model.SessionDetails.Feedback item = mSessionDeatils.feedback_details[i];

                        if (item.id.equals(topic_id)) {
                            topic_covered.setSelection(i);
                            sel_topic = item;
                        }
                    }
                }
            }
        }
    }

    public void enableTopic() {
        disableAttendance();
        topic_content_view.setVisibility(View.VISIBLE);
        attendance_content.setVisibility(View.GONE);
        utils.setTextDrawable(text_topic_title, 0, 0, R.drawable.up_arrow, 0);
    }

    public void enableAttendance() {
        disableTopic();
        attendance_content.setVisibility(View.VISIBLE);
        utils.setTextDrawable(text_attdencae_title, 0, 0, R.drawable.up_arrow, 0);

    }

    public void disableTopic() {
        topic_content_view.setVisibility(View.GONE);
        utils.setTextDrawable(text_topic_title, 0, 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
    }

    public void disableAttendance() {
        attendance_content.setVisibility(View.GONE);
        utils.setTextDrawable(text_attdencae_title, 0, 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
    }

    @Override
    public void movetoSessionScreen() {

        Log.i(TAG, "finish the request");
        finish();
    }

    @Override
    public void showProcessingBar() {
        showProgressDialog();
    }

    @Override
    public void hideProcessingBar() {
        hideProgressDialog();
    }
}


