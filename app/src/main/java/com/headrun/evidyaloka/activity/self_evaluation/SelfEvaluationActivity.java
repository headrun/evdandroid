package com.headrun.evidyaloka.activity.self_evaluation;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.SelectionDiscussion.SelectionDiscussionActivity;
import com.headrun.evidyaloka.activity.base.HomeActivity;
import com.headrun.evidyaloka.activity.base.HomePresenter;
import com.headrun.evidyaloka.config.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by sujith on 20/3/17.
 */

public class SelfEvaluationActivity extends AppCompatActivity implements SelfEvaluationView {

    String TAG = SelectionDiscussionActivity.class.getSimpleName();
    SelfEvaluationPresenter mSelfEvaluationPresenter;
    Menu menu;
    MenuItem menuNextItem, menuSubmitItem;
    Spinner roles_list_spinner;
    Toolbar toolbar;
    List<String> role_list = new ArrayList<>();
    String sel_role;
    ProgressBar progress_bar;
    String[] role_type;
    boolean Teacher_Admin = false, Content_Dev = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_evaluation_layout);

        mSelfEvaluationPresenter = new SelfEvaluationPresenter(this, this);

        getData();
        initView();

        roles_list_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Change the selected item's text color
                TextView tv = ((TextView) view);
                sel_role = role_list.get(position).trim();
                tv.setText(role_list.get(position).trim());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tv.setTextColor(getResources().getColor(R.color.white, getTheme()));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.white));
                }

                Log.i(TAG, "sel role is " + role_list.get(position));
                setContent(role_list.get(position).trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    private void setContent(String role_sel) {
        if (role_sel.contains("Teacher") || role_sel.contains("Center Admin")) {
            Constants.SE_CURRENT_STATUS = 0;
            Teacher_Admin = true;
            Content_Dev = false;
            Constants.SELF_EVAL_DATA.put(Constants.NATIVE_LANG_FLUENT, "");
            Constants.SELF_EVAL_DATA.put(Constants.OTHER_LANG_FLUENT, "");
            Constants.SELF_EVAL_DATA.put(Constants.PROVIDE_TIME, "");
            Constants.SELF_EVAL_DATA.put(Constants.MOTIVATE_YOU, "");
            Constants.SELF_EVAL_DATA.put(Constants.SCENARIO_TWO, "");
            Constants.SELF_EVAL_DATA.put(Constants.SCENARIO_THREE, "");
            Constants.SELF_EVAL_DATA.put(Constants.SCENARIO_FOUR, "");
            Constants.SELF_EVAL_DATA.put(Constants.SCENARIO_FIVE, "");
            Constants.SELF_EVAL_DATA.put(Constants.SCENARIO_SIX, "");
            Constants.SE_CURRENT_STATUS++;
            replaceFragment(new FluentLangFragmnet());
            checkmenu();

        } else {
            Constants.SE_CURRENT_STATUS = 0;
            Content_Dev = true;
            Teacher_Admin = false;
            replaceFragment(new SeContentDeveloper());
            checkmenu();
        }
    }

    private void getData() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            if (data.containsKey("role_type")) {
                sel_role = data.getString("role_type");
            }
        }
    }

    private void initView() {
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        roles_list_spinner = (Spinner) findViewById(R.id.roles_list);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setSpinner();
    }

    private void setSpinner() {
        new HomePresenter(this).setGalleryImages();

        role_list.addAll(Constants.SELF_VAL_ONBOARD.get(Constants.SE));

        if (role_list != null) {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, role_list);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            roles_list_spinner.setAdapter(dataAdapter);

            if (roles_list_spinner.getAdapter().getCount() > 0) {
                int val = -1;
                if (role_list.size() > 0) {
                    val = role_list.indexOf(sel_role);
                } else {
                    val = 0;
                }
                roles_list_spinner.setSelection(val);
            }
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag_container, fragment)
                .addToBackStack(String.valueOf(Constants.SE_CURRENT_STATUS))
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.self_evaluation_menu, menu);
        menuNextItem = menu.findItem(R.id.action_next);


        MenuItemCompat.setActionView(menuNextItem, R.layout.menu_item_background);
        RelativeLayout menuNextItem_lay = (RelativeLayout) MenuItemCompat.getActionView(menuNextItem);
        TextView tv_next = (TextView) menuNextItem_lay.findViewById(R.id.txt_menu_item);
        tv_next.setText("Next");

        menuSubmitItem = menu.findItem(R.id.action_submit);


        MenuItemCompat.setActionView(menuSubmitItem, R.layout.menu_item_background);
        RelativeLayout menuSubmitItem_lay = (RelativeLayout) MenuItemCompat.getActionView(menuSubmitItem);
        TextView tv = (TextView) menuSubmitItem_lay.findViewById(R.id.txt_menu_item);
        tv.setText("Submit");

        menuNextItem.setVisible(false);
        menuSubmitItem.setVisible(false);


        menuNextItem_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.SE_CURRENT_STATUS++;
                checkmenu();
                changeFragment(Constants.SE_CURRENT_STATUS);

            }
        });

        menuSubmitItem_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Teacher_Admin)
                    checkSubmit();
                else
                    checkContent_dev_submit();

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void changeFragment(int dispaly_type) {

        Fragment fragment = new ScenariosFragment().newInstance(dispaly_type);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag_container, fragment)
                .addToBackStack(String.valueOf(dispaly_type))
                .commit();

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
    public void onBackPressed() {
        super.onBackPressed();
        Constants.SE_CURRENT_STATUS--;

        if (Constants.SE_CURRENT_STATUS > 0) {
            if (Constants.SE_CURRENT_STATUS == 1) {
                replaceFragment(new FluentLangFragmnet());
            } else {
                // getFragmentManager().popBackStack();
                changeFragment(Constants.SE_CURRENT_STATUS);
            }
            checkmenu();
        } else {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    private void showMenuNext() {
        menuSubmitItem.setVisible(false);
        menuNextItem.setVisible(true);
    }

    private void showMenuSubmit() {
        menuSubmitItem.setVisible(true);
        menuNextItem.setVisible(false);
    }

    private void checkmenu() {
        if ((Constants.SE_CURRENT_STATUS < Constants.SE_6) && Teacher_Admin)
            showMenuNext();
        else
            showMenuSubmit();
    }

    private boolean checkfluentDetails() {

        boolean value = true;
        try {
            if (Constants.SELF_EVAL_DATA.get(Constants.NATIVE_LANG_FLUENT).isEmpty())
                value = false;
            if (Constants.SELF_EVAL_DATA.get(Constants.OTHER_LANG_FLUENT).isEmpty())
                value = false;
            if (Constants.SELF_EVAL_DATA.get(Constants.PROVIDE_TIME).isEmpty())
                value = false;
            if (Constants.SELF_EVAL_DATA.get(Constants.MOTIVATE_YOU).isEmpty())
                value = false;
        } catch (Exception e) {
            value = false;
        }
        return value;
    }

    private boolean checkScenarios() {
        boolean value = true;

        try {
            if (Constants.SELF_EVAL_DATA.get(Constants.SCENARIO_FIVE).isEmpty())
                value = false;
            if (Constants.SELF_EVAL_DATA.get(Constants.SCENARIO_SIX).isEmpty())
                value = false;
        } catch (Exception e) {
            value = false;
        }

        return value;
    }


    private void checkSubmit() {

        if (!checkfluentDetails()) {
            Constants.SE_CURRENT_STATUS = 0;
            replaceFragment(new FluentLangFragmnet());
            checkmenu();
        } else if (!checkScenarios()) {
            Constants.SE_CURRENT_STATUS = 5;
            changeFragment(Constants.SE_CURRENT_STATUS);
            checkmenu();
        } else {
            mSelfEvaluationPresenter.sEServerCall(Constants.SELF_EVAL_DATA, sel_role);
        }
    }

    private void checkContent_dev_submit() {
        boolean value_check = true;
        if (Constants.SELF_EVAL_DATA.size() > 0) {
            for (Map.Entry<String, String> entry : Constants.SELF_EVAL_DATA.entrySet()) {
                if (entry.getValue().isEmpty()) {
                    value_check = false;
                    break;
                }
            }
        }
        if (value_check) {
            mSelfEvaluationPresenter.contentDevSelf_eval(Constants.SELF_EVAL_DATA, sel_role);
        } else {
            Toast.makeText(this, "Please fill the missing fields", Toast.LENGTH_LONG).show();
        }

    }

}
