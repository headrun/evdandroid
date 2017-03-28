package com.headrun.evidyaloka.activity.self_evaluation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.base.HomeActivity;
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

    SelfEvaluationPresenter mSelfEvaluationPresenter;
    Menu menu;
    MenuItem menuNextItem, menuSubmitItem;
    List<String> role_list = new ArrayList<>();
    ProgressBar progress_bar;
    String[] role_type;
    boolean Teacher_Admin = false, Content_Dev = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_evaluation_layout);

        getData();
        initView();

        Constants.SE_CURRENT_STATUS = 0;
        mSelfEvaluationPresenter = new SelfEvaluationPresenter(this, this);

        if (role_list.contains("Teacher") || role_list.contains("Center Admin")) {
            Teacher_Admin = true;
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

        } else {
            Content_Dev = true;
            replaceFragment(new SeContentDeveloper());
        }
    }

    private void getData() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            if (data.containsKey("role_type")) {
                role_list = data.getStringArrayList("role_type");
            }
        }
    }

    private void initView() {
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        getSupportActionBar().setTitle(getResources().getString(R.string.self_evaluation));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        menuSubmitItem = menu.findItem(R.id.action_submit);
        checkmenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_next:
                Constants.SE_CURRENT_STATUS++;
                checkmenu();
                changeFragment(Constants.SE_CURRENT_STATUS);
                break;
            case R.id.action_submit:
                if (Teacher_Admin)
                    checkSubmit();
                else
                    checkContent_dev_submit();
                break;
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
            mSelfEvaluationPresenter.sEServerCall(Constants.SELF_EVAL_DATA);
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
            mSelfEvaluationPresenter.contentDevSelf_eval(Constants.SELF_EVAL_DATA);
        } else {
            Toast.makeText(this, "Please fill the missing fields", Toast.LENGTH_LONG).show();
        }

    }

}
