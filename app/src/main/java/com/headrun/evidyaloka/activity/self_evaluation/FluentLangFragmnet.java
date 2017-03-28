package com.headrun.evidyaloka.activity.self_evaluation;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sujith on 20/3/17.
 */

public class FluentLangFragmnet extends Fragment implements View.OnClickListener {

    EditText edt_motivate;
    RadioGroup radio_fluent_lang, radio_provide_hours;
    TextView sel_other_lang;
    CardView other_lang_lay;
    Utils utils;

    public FluentLangFragmnet() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fluent_sel_lang, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        utils = new Utils(getActivity());
        initView();

    }

    private void initView() {

        edt_motivate = (EditText) getView().findViewById(R.id.edt_motivate);
        sel_other_lang = (TextView) getView().findViewById(R.id.sel_other_lang);
        radio_fluent_lang = (RadioGroup) getView().findViewById(R.id.radio_fluent_lang);
        radio_provide_hours = (RadioGroup) getView().findViewById(R.id.radio_provide_hours);
        other_lang_lay = (CardView) getView().findViewById(R.id.other_lang_lay);
        other_lang_lay.setOnClickListener(this);


        fillData();

        radio_fluent_lang.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {


                RadioButton button = (RadioButton) group.findViewById(checkedId);

                String data = button.getText().toString().trim();
                if (!data.isEmpty())
                    Constants.SELF_EVAL_DATA.put(Constants.NATIVE_LANG_FLUENT, data);

            }
        });

        radio_provide_hours.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {


                RadioButton button = (RadioButton) group.findViewById(checkedId);

                String data = button.getText().toString().trim();
                if (!data.isEmpty())
                    Constants.SELF_EVAL_DATA.put(Constants.PROVIDE_TIME, data);


            }
        });

        edt_motivate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Constants.SELF_EVAL_DATA.put(Constants.MOTIVATE_YOU, s.toString().trim());
            }
        });
    }

    private void fillData() {
        String fluent_lang = Constants.SELF_EVAL_DATA.get(Constants.NATIVE_LANG_FLUENT);
        if (!fluent_lang.isEmpty()) {
            if (fluent_lang.toLowerCase().contains("yes"))
                ((RadioButton) radio_fluent_lang.getChildAt(0)).setChecked(true);
            else
                ((RadioButton) radio_fluent_lang.getChildAt(1)).setChecked(true);
        }

        String provide_time = Constants.SELF_EVAL_DATA.get(Constants.PROVIDE_TIME);
        if (!provide_time.isEmpty()) {
            if (provide_time.toLowerCase().contains("yes"))
                ((RadioButton) radio_provide_hours.getChildAt(0)).setChecked(true);
            else
                ((RadioButton) radio_provide_hours.getChildAt(1)).setChecked(true);
        }

        String other_lang = Constants.SELF_EVAL_DATA.get(Constants.OTHER_LANG_FLUENT);
        if (!other_lang.isEmpty()) {
            sel_other_lang.setText(other_lang);
        }

        String motivate_youy = Constants.SELF_EVAL_DATA.get(Constants.MOTIVATE_YOU);
        if (!motivate_youy.isEmpty()) {
            edt_motivate.setText(motivate_youy);

        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.other_lang_lay:
                checkALertDialog();
                break;

        }
    }

    private void checkALertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.profile_update_dialog, null);

        builder.setView(dialogView);
        builder.setCancelable(true);

        TextView dialog_title = (TextView) dialogView.findViewById(R.id.dialog_title);
        final EditText auto_dilaog_list = (EditText) dialogView.findViewById(R.id.auto_dialog_list);
        final ListView dialog_list = (ListView) dialogView.findViewById(R.id.dialog_list);

        dialog_title.setText("select Language");
        auto_dilaog_list.setVisibility(View.GONE);

        String lang_filter = utils.userSession.getLangFilter().replaceAll("\\[|\\]|\\s", "");
        final List<String> langfilter_data = new ArrayList<>(Arrays.asList(lang_filter.split(",")));

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, langfilter_data);
        dialog_list.setAdapter(itemsAdapter);
        dialog_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        String sel_lang_list = Constants.SELF_EVAL_DATA.get(Constants.OTHER_LANG_FLUENT);
        if (!sel_lang_list.isEmpty()) {
            int pos = langfilter_data.indexOf(sel_lang_list);
            if (pos != -1) {
                dialog_list.setItemChecked(pos, true);
            }
        }

        final String[] sel_lang = new String[1];
        dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sel_lang[0] = langfilter_data.get(position).trim();


            }
        });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if (sel_lang[0] != null && !sel_lang[0].isEmpty()) {
                    sel_other_lang.setText(sel_lang[0]);
                    Constants.SELF_EVAL_DATA.put(Constants.OTHER_LANG_FLUENT, sel_lang[0]);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.show();
    }

}
