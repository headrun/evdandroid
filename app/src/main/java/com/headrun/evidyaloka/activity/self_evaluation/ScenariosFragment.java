package com.headrun.evidyaloka.activity.self_evaluation;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.demands.DemandFragment;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.model.SchoolDetails;

/**
 * Created by sujith on 20/3/17.
 */

public class ScenariosFragment extends Fragment {

    TextView txt_scenarios;
    RadioGroup radio_scenarios;
    EditText approach_edit;
    String disaply_type = "disaply_type";
    int disaply_frag = -1;

    public ScenariosFragment() {
    }

    public static ScenariosFragment newInstance(int displayType) {
        Bundle args = new Bundle();
        args.putInt("disaply_type", displayType);
        ScenariosFragment fragment = new ScenariosFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.teach_scenarios, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getArgs();
        initView();
        displayField(disaply_frag);
    }

    private void initView() {

        txt_scenarios = (TextView) getView().findViewById(R.id.txt_scenarios);
        radio_scenarios = (RadioGroup) getView().findViewById(R.id.radio_scenarios);
        approach_edit = (EditText) getView().findViewById(R.id.approach_edit);

        radio_scenarios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                RadioButton button = (RadioButton) group.findViewById(checkedId);

                String data = button.getText().toString().trim();
                setValue(disaply_frag, data);

            }
        });

        approach_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setValue(disaply_frag, s.toString().trim());
            }
        });

    }

    public void getArgs() {

        disaply_frag = (int) getArguments().getInt(disaply_type);
    }

    private void displayField(int displayfrag) {

        if (displayfrag == Constants.SE_2) {
            txt_scenarios.setText(getString(R.string.scenario_one));
            String[] options = getResources().getStringArray(R.array.scenario_one_opt);
            setRadioButton(options);
            goneEditTaext();
            setRaddioButtonselection(radio_scenarios, options, Constants.SELF_EVAL_DATA.get(Constants.SCENARIO_TWO));
        } else if (displayfrag == Constants.SE_3) {
            txt_scenarios.setText(getString(R.string.scenario_two));
            String[] options = getResources().getStringArray(R.array.scenario_two_opt);
            setRadioButton(options);
            goneEditTaext();
            setRaddioButtonselection(radio_scenarios, options, Constants.SELF_EVAL_DATA.get(Constants.SCENARIO_THREE));
        } else if (displayfrag == Constants.SE_4) {
            txt_scenarios.setText(getString(R.string.scenario_three));
            String[] options = getResources().getStringArray(R.array.scenario_three_opt);
            setRadioButton(options);
            goneEditTaext();
            setRaddioButtonselection(radio_scenarios, options, Constants.SELF_EVAL_DATA.get(Constants.SCENARIO_FOUR));
        } else if (displayfrag == Constants.SE_5) {
            txt_scenarios.setText(getString(R.string.scenario_four));
            String[] options = getResources().getStringArray(R.array.scenario_four_opt);
            setRadioButton(options);
            goneEditTaext();
            setRaddioButtonselection(radio_scenarios, options, Constants.SELF_EVAL_DATA.get(Constants.SCENARIO_FIVE));
        } else if (displayfrag == Constants.SE_6) {
            txt_scenarios.setText(getString(R.string.scenario_five));
            visibleEditText();
            String val = Constants.SELF_EVAL_DATA.get(Constants.SCENARIO_SIX);
            if (!val.isEmpty()) {
                approach_edit.setText(val);
            }
        }
    }

    private void setRaddioButtonselection(RadioGroup rgroup, String[] values, String sel_val) {

        if (values != null && sel_val != null && !sel_val.isEmpty()) {
            for (int val = 0; val < values.length; val++) {
                if (values[val].toLowerCase().equals(sel_val.toLowerCase())) {
                    ((RadioButton) rgroup.getChildAt(val)).setChecked(true);
                    break;
                }
            }
        }
    }

    private void setRadioButton(String[] opt) {

        for (int i = 0; i < opt.length; i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(opt[i]);
            radioButton.setId(i + 1000);
            radio_scenarios.addView(radioButton);
        }
    }

    private void visibleEditText() {
        approach_edit.setVisibility(View.VISIBLE);
    }

    private void goneEditTaext() {
        approach_edit.setVisibility(View.GONE);
    }

    private void setValue(int check_vlaue, String value) {

        if (check_vlaue != -1) {
            if (check_vlaue == Constants.SE_2) {
                Constants.SELF_EVAL_DATA.put(Constants.SCENARIO_TWO, value);
            } else if (check_vlaue == Constants.SE_3) {
                Constants.SELF_EVAL_DATA.put(Constants.SCENARIO_THREE, value);
            } else if (check_vlaue == Constants.SE_4) {
                Constants.SELF_EVAL_DATA.put(Constants.SCENARIO_FOUR, value);
            } else if (check_vlaue == Constants.SE_5) {
                Constants.SELF_EVAL_DATA.put(Constants.SCENARIO_FIVE, value);
            } else if (check_vlaue == Constants.SE_6) {
                Constants.SELF_EVAL_DATA.put(Constants.SCENARIO_SIX, value);
            }

        }
    }

}
