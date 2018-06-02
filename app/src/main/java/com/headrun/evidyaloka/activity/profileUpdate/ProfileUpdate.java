package com.headrun.evidyaloka.activity.profileUpdate;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.drawee.components.DraweeEventTracker;
import com.facebook.drawee.view.SimpleDraweeView;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.base.BaseActivity;
import com.headrun.evidyaloka.activity.base.HomeActivity;
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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sujith on 28/2/17.
 */

public class ProfileUpdate extends BaseActivity implements ProfileUpdateView, ResponseListener<ChangeSessionStatus> {

    public String TAG = ProfileUpdate.class.getSimpleName();
    //public TextInputEditText txt_first_name, txt_last_name, txt_age, txt_email, txt_alternate_email, txt_skype_id, txt_ph_no;
    public TextInputEditText txt_first_name, txt_last_name, txt_age, txt_email, txt_skype_id, txt_ph_no;
    //public TextInputEditText txt_referrer, txt_brief_intro;
    //TextView txt_role;
    TextView sel_txt_role;
    //EditText txt_gender, txt_profession, txt_preferred_medium, txt_reference_channel, txt_Country, txt_state, txt_city;
    EditText txt_gender, txt_profession, txt_preferred_medium, txt_reference_channel;
    FrameLayout profile_pic_lay;
    public Toolbar toolbar;
    public AppBarLayout appbarLayout;
    public CollapsingToolbarLayout collapsing_toolbar;
    public SimpleDraweeView user_pic;
    public ProgressBar progress_bar;
    public LinearLayout role_lay;
    Utils utils;
    Uri uri = null;
    eu.fiskur.chipcloud.ChipCloud txt_role;

    List<String> user_roles = new LinkedList<>();
    HashMap<String, String>[] map = null;
    LinkedHashMap<Integer, String> roles_list = new LinkedHashMap<>();

    ProfileUpdate_presenter mprofileUpdate_presenter;

    private static final int PICK_IMAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);

        utils = new Utils(this);
        initviews();
        mprofileUpdate_presenter = new ProfileUpdate_presenter(this, this);
    }

    private void initviews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appbarLayout = (AppBarLayout) findViewById(R.id.appbarLayout);
        user_pic = (SimpleDraweeView) findViewById(R.id.user_pic);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        txt_first_name = (TextInputEditText) findViewById(R.id.txt_first_name);
        txt_last_name = (TextInputEditText) findViewById(R.id.txt_last_name);
        txt_gender = (EditText) findViewById(R.id.txt_gender);
        txt_age = (TextInputEditText) findViewById(R.id.txt_age);
        txt_email = (TextInputEditText) findViewById(R.id.txt_email);

        txt_skype_id = (TextInputEditText) findViewById(R.id.txt_skype_id);
        txt_ph_no = (TextInputEditText) findViewById(R.id.txt_ph_no);
        txt_profession = (EditText) findViewById(R.id.txt_profession);
        txt_preferred_medium = (EditText) findViewById(R.id.txt_preferred_medium);
        txt_reference_channel = (EditText) findViewById(R.id.txt_reference_channel);

        role_lay = (LinearLayout) findViewById(R.id.role_lay);
        txt_role = (eu.fiskur.chipcloud.ChipCloud) findViewById(R.id.txt_role);
        sel_txt_role = (TextView) findViewById(R.id.sel_txt_role);
        profile_pic_lay = (FrameLayout) findViewById(R.id.profile_pic_lay);
        sel_txt_role.setVisibility(View.GONE);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_first_name.requestFocus();

        appbarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onExpanded(AppBarLayout appBarLayout) {
                collapsing_toolbar.setTitle(" ");

            }

            @Override
            public void onCollapsed(AppBarLayout appBarLayout) {
                collapsing_toolbar.setTitle("");
                setActivityTitle("Edit Profile");
            }

            @Override
            public void onIdle(AppBarLayout appBarLayout) {

            }
        });


        user_pic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int permissionCheck = ContextCompat.checkSelfPermission(ProfileUpdate.this, Manifest.permission.READ_EXTERNAL_STORAGE);

                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ProfileUpdate.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
                    } else {
                        readDataExternal();
                    }
                }
                return false;
            }
        });

        txt_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(ProfileUpdate.this, "get gender 1234", Toast.LENGTH_SHORT).show();
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

        role_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requiredDialog(Constants.ROLE);
            }
        });

        txt_role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requiredDialog(Constants.ROLE);
            }
        });

    }

    private void readDataExternal() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
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
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    readDataExternal();
                } else {
                    Toast.makeText(this, "unable to access the external files", Toast.LENGTH_LONG).show();
                }
                break;

            default:
                break;
        }
    }

    private void showImage(Uri uri) {
        user_pic.setImageURI(uri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_update, menu);

        MenuItem item = menu.findItem(R.id.profile_save);
        MenuItemCompat.setActionView(item, R.layout.menu_item_background);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
        TextView tv = (TextView) notifCount.findViewById(R.id.txt_menu_item);
        tv.setText("Update");

        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprofileUpdate_presenter.validatecheck();

            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
/*
            case R.id.profile_save:

                break;
*/
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

    @Override
    public void showProgressBar() {
        progress_bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progress_bar.setVisibility(View.GONE);
    }

    @Override
    public void setFieldsData() {

        try {
            LoginResponse.userData user_data = utils.userSession.getUserData().data;
            roles_list = utils.getUserRolesList();

            if (user_data.picture != null && !user_data.picture.isEmpty())
                ImageLoadingUtils.load(user_pic, user_data.picture);

            txt_first_name.setText(user_data.first_name);
            txt_last_name.setText(user_data.last_name);
            txt_gender.setText(user_data.gender);
            txt_age.setText(user_data.age);

            txt_email.setText(user_data.email);
            txt_skype_id.setText(user_data.skype_id);
            txt_ph_no.setText(user_data.phone);

            txt_profession.setText(user_data.profession);
            txt_preferred_medium.setText(user_data.pref_medium);
            txt_reference_channel.setText(user_data.reference_channel);

            txt_gender.setFocusable(false);
            txt_profession.setFocusable(false);
            txt_preferred_medium.setFocusable(false);
            txt_reference_channel.setFocusable(false);

            if (user_data.roles != null) {
                user_roles.clear();
                if (user_data.roles.length > 0) {
                    user_roles.addAll(roles_list.values());
                    sel_txt_role.setVisibility(View.GONE);
                } else {
                    sel_txt_role.setVisibility(View.VISIBLE);
                }
                setRole(user_roles);

            } else {
                sel_txt_role.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        //  txt_referrer.requestFocus();
    }

    @Override
    public void setCounty(String county) {

        Constants.STATE_MAP.clear();
        Constants.CITY_MAP.clear();

        /*txt_Country.setText(county);
        txt_city.setText("");
        txt_state.setText("");
        txt_state.requestFocus();*/
    }

    @Override
    public void setCity(String city) {
        //  txt_city.setText(city);
        //  txt_brief_intro.requestFocus();
    }

    @Override
    public void setState(String state) {
        /*txt_state.setText(state);
        txt_city.requestFocus();*/
    }

    @Override
    public void setRole(List<String> roles) {
        if (roles.size() >= 0) {
            txt_role.removeAllViews();
            txt_role.addChips(roles.toArray(new String[roles.size()]));
            if (roles.size() == 0)
                sel_txt_role.setVisibility(View.VISIBLE);
            else
                sel_txt_role.setVisibility(View.GONE);

        }
    }

    @Override
    public void updateRoles(List<String> roles) {
        if (roles.size() >= 0) {
            user_roles.clear();
            user_roles.addAll(roles);

        }
    }

    @Override
    public String getCountry() {
        return "";//txt_Country.getText().toString().trim();
    }

    @Override
    public String getState() {
        return "";//txt_state.getText().toString().trim();
    }

    @Override
    public String getCity() {
        return "";//txt_city.getText().toString().trim();
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
        return "";//txt_alternate_email.getText().toString().trim();
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
    public List<String> get_Role() {

        return user_roles != null ? user_roles : new LinkedList<String>();
    }

    @Override
    public String get_reference_channel() {
        return txt_reference_channel.getText().toString().trim();
    }

    @Override
    public String get_referrer() {
        return "";// txt_referrer.getText().toString().trim();
    }

    @Override
    public String get_brief_intro() {
        return "";// txt_brief_intro.getText().toString().trim();
    }

    @Override
    public void showError(int val, String msg) {

        if (Constants.EDIT_FNAME == val)
            txt_first_name.setError(msg);
        else if (Constants.EDIT_GENDER == val)
            txt_gender.setError(msg);
        else if (Constants.EDIT_EMAIL == val)
            txt_email.setError(msg);
        else if (Constants.EDIT_MEDIUM == val)
            txt_preferred_medium.setError(msg);
        else if (Constants.EDIT_ROLE == val) {
            sel_txt_role.setError(msg);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                sel_txt_role.setTextColor(getResources().getColor(R.color.error_color, this.getTheme()));
            } else {
                sel_txt_role.setTextColor(getResources().getColor(R.color.error_color));
            }
            sel_txt_role.requestFocus();
            sel_txt_role.setVisibility(View.VISIBLE);
        }
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
            //params.put("alt_email", "");
            params.put("skype_id", get_skype_id());
            params.put("age", get_age());
            params.put("phone", get_ph_no());
            params.put("gender", get_gender());
            params.put("profession", get_preofession());
            params.put("prefered_medium", get_preferred_medium());
            // params.put("reference_id", "");
            params.put("reference_channel", get_reference_channel());

            /* params.put("country", "");
            params.put("state", "");
            params.put("city", "");
            params.put("short_notes", "");
           */

            HashMap<String, String> vals = new HashMap<>();
            vals.putAll(utils.reverseMap(utils.userSession.getUserRoles()));

            StringBuffer role_ids = new StringBuffer();
            int role_count = get_Role().size();
            map = null;
            map = new HashMap[role_count];
            int count = 0, map_count = 0;

            if (role_count > 0)
                for (String entry : get_Role()) {
                    count++;
                    if (vals.containsKey(entry)) {
                        HashMap<String, String> val = new HashMap<>();
                        val.put(vals.get(entry), entry);
                        map[map_count] = val;
                        map_count++;
                        role_ids.append(vals.get(entry));
                        if (role_count != count)
                            role_ids.append(";");
                    }
                }

            params.put("roles", role_ids.toString());

            new EVDNetowrkServices().saveProfileData(this, this, params);
            if (uri != null && !uri.toString().isEmpty())
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

        if (response != null)
            if (response.status.equals("0")) {
                Toast.makeText(this, "profile update Sucessfully", Toast.LENGTH_SHORT).show();
                mprofileUpdate_presenter.CallUserData();
                startActivity(new Intent(this,HomeActivity.class));
                finish();

            } else {
                String message = "profile update faild";
                if (!response.message.isEmpty())
                    message = response.message;
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }


    }

}
