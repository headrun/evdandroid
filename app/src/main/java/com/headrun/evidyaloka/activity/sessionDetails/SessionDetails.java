package com.headrun.evidyaloka.activity.sessionDetails;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.base.BaseActivity;
import com.headrun.evidyaloka.activity.base.HomeActivity;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.utils.ImageLoadingUtils;
import com.headrun.evidyaloka.utils.Utils;

import java.util.Date;

/**
 * Created by sujith on 4/3/17.
 */

public class SessionDetails extends AppCompatActivity implements SessionDetailsView, AdapterView.OnItemSelectedListener {

    public String TAG = SessionDetails.class.getSimpleName();

    TextView session_teacher, session_topic, session_time, session_status, session_mnth, session_date;
    ExpandableListView attendance_list;
    // Toolbar toolbar;
    TextView session_grade;
    Spinner topic_covered;
    EditText session_feedback;
    String sessin_id;
    SimpleDraweeView center_image;
    SessionDeatilsPresenter mSessionPresenter;
    // FeedBackAdapter mFeedBackAdapter;
    com.headrun.evidyaloka.model.SessionDetails mSessionDeatils;
    NestedScrollView session_lay;
    Utils utils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sessiondeatils_activity);

        mSessionPresenter = new SessionDeatilsPresenter(this, this);
        mSessionDeatils = new com.headrun.evidyaloka.model.SessionDetails();
        utils = new Utils(this);
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

        // toolbar = (Toolbar) findViewById(R.id.toolbar);
        // session_grade = (TextView) findViewById(R.id.session_grade);
        session_teacher = (TextView) findViewById(R.id.session_teacher);
        session_topic = (TextView) findViewById(R.id.session_topic);
        session_time = (TextView) findViewById(R.id.session_time);
        session_mnth = (TextView) findViewById(R.id.session_mnth);
        session_date = (TextView) findViewById(R.id.session_date);
        session_status = (TextView) findViewById(R.id.session_status);
        session_feedback = (EditText) findViewById(R.id.session_feedback);
        attendance_list = (ExpandableListView) findViewById(R.id.attendance_list);
        topic_covered = (Spinner) findViewById(R.id.topic_covered);
        center_image = (SimpleDraweeView) findViewById(R.id.session_center_img);
        session_lay = (NestedScrollView) findViewById(R.id.session_lay);
        topic_covered.setOnItemSelectedListener(this);
        session_lay.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setSessionData(final com.headrun.evidyaloka.model.SessionDetails mSessionDeatils) {

        session_lay.setVisibility(View.VISIBLE);
        if (mSessionDeatils != null) {

            this.mSessionDeatils = mSessionDeatils;
            getSupportActionBar().setTitle(mSessionDeatils.center);
            // session_grade.setText(mSessionDeatils.grade != null ? mSessionDeatils.grade : "");
            session_teacher.setText(mSessionDeatils.teacher != null ? mSessionDeatils.teacher : "");
            session_topic.setText(mSessionDeatils.subject != null ? mSessionDeatils.subject : "");
            session_time.setText(mSessionDeatils.start_time != null ? mSessionDeatils.start_time : "");


            if (mSessionDeatils.center_image != null && !mSessionDeatils.center_image.isEmpty())
                ImageLoadingUtils.load(center_image, mSessionDeatils.center_image);

            if (!TextUtils.isEmpty(mSessionDeatils.date)) {

                try {
                    //java.text.SimpleDateFormat date_formate = new java.text.SimpleDateFormat("dd th MMM yyyy");
                    // Date date = date_formate.parse(mSessionDeatils.date);
                    String date[] = mSessionDeatils.date.split(" ");
                    if (date.length >= 3) {
                        session_mnth.setText(date[1]);
                        session_date.setText(date[0]);

                        //String mnth = new java.text.SimpleDateFormat("MMM").format(date.getTime());
                        //String datee = new java.text.SimpleDateFormat("dd").format(date.getTime());
                    }

                } catch (Exception e) {
                    session_mnth.setText("");
                    session_date.setText("");
                }
            }

            session_status.setText(mSessionDeatils.session_status != null ? mSessionDeatils.session_status : "");
            session_feedback.setText(mSessionDeatils.comments != null ? mSessionDeatils.comments : "");
            if (mSessionDeatils.session_status != null) {
                session_status.setTextColor(utils.set_statusColor(mSessionDeatils.session_status));
                if (mSessionDeatils.session_status.toLowerCase().equals("scheduled")) {
                    session_feedback.setFocusable(true);
                    //session_feedback.setEnabled(false);
                }
            }

            ArrayAdapter<com.headrun.evidyaloka.model.SessionDetails.Feedback> adapter =
                    new ArrayAdapter<com.headrun.evidyaloka.model.SessionDetails.Feedback>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, mSessionDeatils.feedback_details);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            topic_covered.setAdapter(adapter);

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Log.i(TAG, "sel title is " + mSessionDeatils.feedback_details[position].title);

        String id_ = mSessionDeatils.feedback_details[position].id;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.profile_update, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

           /* case android.R.id.action_skip:
                startActivity(new Intent(this, HomeActivity.class));*/
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Constants.ISNOTIFICATION) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }
}


