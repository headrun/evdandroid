package com.headrun.evidyaloka.activity.profileUpdate;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.base.BaseActivity;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.dto.ChangeSessionStatus;
import com.headrun.evidyaloka.evdservices.UploadImageService;
import com.headrun.evidyaloka.model.LoginResponse;
import com.headrun.evidyaloka.utils.ImageLoadingUtils;
import com.headrun.evidyaloka.utils.NetworkUtils;
import com.headrun.evidyaloka.utils.Utils;
import com.headrun.evidyaloka.widgets.AppBarStateChangeListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sujith on 28/2/17.
 */

public class ProfileUpdate extends BaseActivity implements ProfileUpdateView, ResponseListener<ChangeSessionStatus> {

    public String TAG = ProfileUpdate.class.getSimpleName();
    public TextInputEditText txt_first_name, txt_last_name, txt_age, txt_email, txt_alternate_email, txt_skype_id, txt_ph_no;
    public TextInputEditText txt_referrer, txt_brief_intro;
    TextView txt_role;
    EditText txt_gender, txt_profession, txt_preferred_medium, txt_reference_channel, txt_Country, txt_state, txt_city;
    public Toolbar toolbar;
    public AppBarLayout appbarLayout;
    public CollapsingToolbarLayout collapsing_toolbar;
    public SimpleDraweeView user_pic;
    public LinearLayout role_lay;
    Utils utils;
    Uri uri = null;

    ProfileUpdate_presenter mprofileUpdate_presenter;

    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);

        utils = new Utils(this);
        mprofileUpdate_presenter = new ProfileUpdate_presenter(this, this);

        initviews();
        setFieldsData();

    }

    private void initviews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appbarLayout = (AppBarLayout) findViewById(R.id.appbarLayout);
        user_pic = (SimpleDraweeView) findViewById(R.id.user_pic);

        txt_first_name = (TextInputEditText) findViewById(R.id.txt_first_name);
        txt_last_name = (TextInputEditText) findViewById(R.id.txt_last_name);
        txt_gender = (EditText) findViewById(R.id.txt_gender);
        txt_age = (TextInputEditText) findViewById(R.id.txt_age);
        txt_email = (TextInputEditText) findViewById(R.id.txt_email);
        txt_alternate_email = (TextInputEditText) findViewById(R.id.txt_alternate_email);
        txt_skype_id = (TextInputEditText) findViewById(R.id.txt_skype_id);
        txt_ph_no = (TextInputEditText) findViewById(R.id.txt_ph_no);
        txt_profession = (EditText) findViewById(R.id.txt_profession);
        txt_preferred_medium = (EditText) findViewById(R.id.txt_preferred_medium);
        txt_reference_channel = (EditText) findViewById(R.id.txt_reference_channel);
        txt_referrer = (TextInputEditText) findViewById(R.id.txt_referrer);
        txt_Country = (EditText) findViewById(R.id.txt_Country);
        txt_state = (EditText) findViewById(R.id.txt_state);
        txt_city = (EditText) findViewById(R.id.txt_city);
        txt_brief_intro = (TextInputEditText) findViewById(R.id.txt_brief_intro);
        role_lay = (LinearLayout) findViewById(R.id.role_lay);
        txt_role = (TextView) findViewById(R.id.txt_role);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_first_name.requestFocus();
        txt_first_name.setSelection(txt_first_name.getText().length());

        appbarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onExpanded(AppBarLayout appBarLayout) {
                collapsing_toolbar.setTitle(" ");
            }

            @Override
            public void onCollapsed(AppBarLayout appBarLayout) {
                collapsing_toolbar.setTitle("Edit Profile");
            }

            @Override
            public void onIdle(AppBarLayout appBarLayout) {

            }
        });

        user_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

            }
        });

        txt_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ProfileUpdate.this, "get gender 1234", Toast.LENGTH_SHORT).show();
                requiredDialog(Constants.GENDER);
            }
        });

        txt_profession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requiredDialog(Constants.PROFESSION);
            }
        });

        txt_preferred_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requiredDialog(Constants.MEDIUM);
            }
        });

        txt_reference_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requiredDialog(Constants.CHANNEL);
            }
        });

        txt_Country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requiredDialog(Constants.COUNTRY);
            }
        });

        txt_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getCountry().isEmpty())
                    requiredDialog(Constants.STATE);
                else
                    txt_state.setError(getResources().getString(R.string.selectCountry));
            }
        });

        txt_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getCountry().isEmpty() && !getState().isEmpty())
                    requiredDialog(Constants.CITY);
                else
                    txt_city.setError(getResources().getString(R.string.cityError));
            }
        });

        role_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requiredDialog(Constants.ROLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                uri = data.getData();
                Log.i(TAG, "Uri: " + uri.toString());
                showImage(uri);

            }
        } else {

        }
    }

    private void showImage(Uri uri) {
        user_pic.setImageURI(uri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.profile_save:
                mprofileUpdate_presenter.validatecheck();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setFieldsData() {

        LoginResponse.userData user_data = utils.userSession.getUserData().data;

        if (user_data.picture != null && !user_data.picture.isEmpty())
            ImageLoadingUtils.load(user_pic, user_data.picture);

        txt_first_name.setText(user_data.first_name);
        txt_last_name.setText(user_data.last_name);
        txt_gender.setText(user_data.gender);
        txt_age.setText(user_data.age);

        txt_email.setText(user_data.email);
        txt_alternate_email.setText(user_data.secondary_email);
        txt_skype_id.setText(user_data.skype_id);
        txt_ph_no.setText(user_data.phone);

        txt_profession.setText(user_data.profession);
        txt_preferred_medium.setText(user_data.pref_medium);
        txt_reference_channel.setText(user_data.reference_channel);
        txt_referrer.setText(user_data.referer);

        txt_Country.setText(user_data.country);
        txt_state.setText(user_data.state);
        txt_city.setText(user_data.city);
        txt_brief_intro.setText(user_data.short_notes);

        txt_gender.setFocusable(false);
        txt_profession.setFocusable(false);
        txt_preferred_medium.setFocusable(false);
        txt_reference_channel.setFocusable(false);
        txt_Country.setFocusable(false);
        txt_state.setFocusable(false);
        txt_city.setFocusable(false);

        if (user_data.roles != null) {
            List<String> data = Arrays.asList(user_data.roles);
            setRole(data);
        }
    }

    @Override
    public void requiredDialog(String required_type) {
        mprofileUpdate_presenter.getDialog(required_type);

    }

    @Override
    public void setGender(String gender) {
        txt_gender.setText(gender);
        txt_age.requestFocus();
    }

    @Override
    public void setPreferredMedium(String medium) {
        txt_preferred_medium.setText(medium);
        txt_reference_channel.requestFocus();
    }

    @Override
    public void setProfession(String profession) {
        txt_profession.setText(profession);
        txt_preferred_medium.requestFocus();
    }

    @Override
    public void setReferenceChannel(String channel) {
        txt_reference_channel.setText(channel);
        txt_referrer.requestFocus();
    }

    @Override
    public void setCounty(String county) {
        txt_Country.setText(county);
        Constants.STATE_MAP.clear();
        Constants.CITY_MAP.clear();
        txt_city.setText("");
        txt_state.setText("");
        txt_state.requestFocus();
    }

    @Override
    public void setCity(String city) {
        txt_city.setText(city);
        txt_brief_intro.requestFocus();
    }

    @Override
    public void setState(String state) {
        txt_state.setText(state);
        txt_city.requestFocus();
    }

    @Override
    public void setRole(List<String> roles) {
        if (roles != null) {
            txt_role.setText(roles.toString().replaceAll("\\[|\\]|\\s", ""));
        }
    }

    @Override
    public String getCountry() {
        return txt_Country.getText().toString().trim();
    }

    @Override
    public String getState() {
        return txt_state.getText().toString().trim();
    }

    @Override
    public String getCity() {
        return txt_city.getText().toString().trim();
    }

    @Override
    public String getFirst_name() {
        return txt_first_name.getText().toString().trim();
    }

    @Override
    public String getLast_name() {
        return txt_last_name.getText().toString().trim();
    }

    @Override
    public String get_gender() {
        return txt_gender.getText().toString().trim();
    }

    @Override
    public String get_age() {
        return txt_age.getText().toString().trim();
    }

    @Override
    public String get_email() {
        return txt_email.getText().toString().trim();
    }

    @Override
    public String get_sec_email() {
        return txt_alternate_email.getText().toString().trim();
    }

    @Override
    public String get_skype_id() {
        return txt_skype_id.getText().toString().trim();
    }

    @Override
    public String get_ph_no() {
        return txt_ph_no.getText().toString().trim();
    }

    @Override
    public String get_preofession() {
        return txt_profession.getText().toString().trim();
    }

    @Override
    public String get_preferred_medium() {
        return txt_preferred_medium.getText().toString().trim();
    }

    @Override
    public String get_reference_channel() {
        return txt_reference_channel.getText().toString().trim();
    }

    @Override
    public String get_referrer() {
        return txt_referrer.getText().toString().trim();
    }

    @Override
    public String get_brief_intro() {
        return txt_brief_intro.getText().toString().trim();
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void submitUserPofile() {

        if (NetworkUtils.isNetworkConnected(this)) {
            showProgressDialog();

            HashMap<String, String> params = new HashMap<>();
            params.put("first_name", getFirst_name());
            params.put("last_name", getLast_name());
            params.put("email", get_email());
            params.put("alt_email", get_sec_email());
            params.put("skype_id", get_skype_id());
            params.put("age", get_age());
            params.put("phone", get_ph_no());
            params.put("gender", get_gender());
            params.put("profession", get_preofession());
            params.put("prefered_medium", get_preferred_medium());
            params.put("reference_id", get_referrer());
            params.put("reference_channel", get_reference_channel());
            params.put("country", getCountry());
            params.put("state", getState());
            params.put("city", getCity());
            params.put("short_notes", get_brief_intro());
            params.put("roles", get_brief_intro());

            new EVDNetowrkServices().saveProfileData(this, this, params);
            startService(new Intent(getApplicationContext(), UploadImageService.class)
                    .putExtra("uri", uri.toString()));
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        hideProgressDialog();
    }

    @Override
    public void onResponse(ChangeSessionStatus response) {
        hideProgressDialog();

    }
}
