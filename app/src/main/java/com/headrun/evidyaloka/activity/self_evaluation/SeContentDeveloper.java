package com.headrun.evidyaloka.activity.self_evaluation;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sujith on 24/3/17.
 */

public class SeContentDeveloper extends Fragment implements RadioGroup.OnCheckedChangeListener {

    RadioGroup radio_content_se_2, radio_content_se_3, radio_content_se_4, radio_content_se_5, radio_content_se_6, radio_content_se_7;
    TextView lang_txt;
    CardView lang_lay;
    final List<String> sel_roles_list = new ArrayList<String>();

    public SeContentDeveloper() {

    }

    public static SeContentDeveloper newInstance(int displayType) {
        Bundle args = new Bundle();
        args.putInt("disaply_type", displayType);
        SeContentDeveloper fragment = new SeContentDeveloper();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.self_eval_content_dev, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inItView();
    }

    private void inItView() {

        lang_lay = (CardView) getView().findViewById(R.id.lang_lay);
        lang_txt = (TextView) getView().findViewById(R.id.lang_txt);
        radio_content_se_2 = (RadioGroup) getView().findViewById(R.id.radio_content_se_2);
        radio_content_se_3 = (RadioGroup) getView().findViewById(R.id.radio_content_se_3);
        radio_content_se_4 = (RadioGroup) getView().findViewById(R.id.radio_content_se_4);
        radio_content_se_5 = (RadioGroup) getView().findViewById(R.id.radio_content_se_5);
        radio_content_se_6 = (RadioGroup) getView().findViewById(R.id.radio_content_se_6);
        radio_content_se_7 = (RadioGroup) getView().findViewById(R.id.radio_content_se_7);

        setRadioButton(getResources().getStringArray(R.array.yes_no), radio_content_se_2);
        setRadioButton(getResources().getStringArray(R.array.content_sub), radio_content_se_3);
        setRadioButton(getResources().getStringArray(R.array.content_search_internet), radio_content_se_4);
        setRadioButton(getResources().getStringArray(R.array.content_comfortable), radio_content_se_5);
        setRadioButton(getResources().getStringArray(R.array.content_comfortable), radio_content_se_6);
        setRadioButton(getResources().getStringArray(R.array.content_contribute_hours), radio_content_se_7);

        radio_content_se_2.setOnCheckedChangeListener(this);
        radio_content_se_3.setOnCheckedChangeListener(this);
        radio_content_se_4.setOnCheckedChangeListener(this);
        radio_content_se_5.setOnCheckedChangeListener(this);
        radio_content_se_6.setOnCheckedChangeListener(this);
        radio_content_se_7.setOnCheckedChangeListener(this);

        Constants.SELF_EVAL_DATA.put(getResources().getString(R.string.content_se_1), "");
        Constants.SELF_EVAL_DATA.put(getResources().getString(R.string.content_se_2), "");
        Constants.SELF_EVAL_DATA.put(getResources().getString(R.string.content_se_3), "");
        Constants.SELF_EVAL_DATA.put(getResources().getString(R.string.content_se_4), "");
        Constants.SELF_EVAL_DATA.put(getResources().getString(R.string.content_se_5), "");
        Constants.SELF_EVAL_DATA.put(getResources().getString(R.string.content_se_6), "");
        Constants.SELF_EVAL_DATA.put(getResources().getString(R.string.content_se_7), "");

        lang_lay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    checkALertDialog();

                }

                return false;
            }
        });
    }

    private void setRadioButton(String[] opt, RadioGroup rgroup) {

        for (int i = 0; i < opt.length; i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(opt[i]);
            radioButton.setId(i + 1000);
            rgroup.addView(radioButton);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

        RadioButton button = (RadioButton) group.findViewById(checkedId);

        String data = button.getText().toString().trim();

        switch (group.getId()) {
            case R.id.radio_content_se_2:
                Constants.SELF_EVAL_DATA.put(getResources().getString(R.string.content_se_2), data);
                break;
            case R.id.radio_content_se_3:
                Constants.SELF_EVAL_DATA.put(getResources().getString(R.string.content_se_3), data);
                break;
            case R.id.radio_content_se_4:
                Constants.SELF_EVAL_DATA.put(getResources().getString(R.string.content_se_4), data);
                break;
            case R.id.radio_content_se_5:
                Constants.SELF_EVAL_DATA.put(getResources().getString(R.string.content_se_5), data);
                break;
            case R.id.radio_content_se_6:
                Constants.SELF_EVAL_DATA.put(getResources().getString(R.string.content_se_6), data);
                break;
            case R.id.radio_content_se_7:
                Constants.SELF_EVAL_DATA.put(getResources().getString(R.string.content_se_7), data);
                break;

        }
    }

    private void checkALertDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.profile_update_dialog, null);

        builder.setView(dialogView);
        builder.setCancelable(true);

        TextView dialog_title = (TextView) dialogView.findViewById(R.id.dialog_title);
        final EditText auto_dilaog_list = (EditText) dialogView.findViewById(R.id.auto_dialog_list);
        final ListView dialog_list = (ListView) dialogView.findViewById(R.id.dialog_list);

        dialog_title.setText("select Language");
        auto_dilaog_list.setVisibility(View.GONE);


        final List<String> langfilter_data = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.medium_list)));

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, langfilter_data);
        dialog_list.setAdapter(itemsAdapter);
        dialog_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        if (Constants.SELF_EVAL_DATA.containsKey(getString(R.string.content_se_1))) {

            String[] sel_lang_list = Constants.SELF_EVAL_DATA.get(getString(R.string.content_se_1)).split(",");

            for (String item : sel_lang_list) {
                int pos = langfilter_data.indexOf(item.trim());
                if (pos != -1) {
                    dialog_list.setItemChecked(pos, true);
                }
            }
        }

        final String[] sel_lang = new String[1];
        dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                SparseBooleanArray clickedItemPositions = dialog_list.getCheckedItemPositions();
                sel_roles_list.clear();
                for (int index = 0; index < clickedItemPositions.size(); index++) {
                    boolean checked = clickedItemPositions.valueAt(index);

                    if (checked) {
                        int key = clickedItemPositions.keyAt(index);
                        if (key != -1) {
                            String item = (String) dialog_list.getItemAtPosition(key);
                            sel_roles_list.add(item);
                        }
                    }


                }
            }
        });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                String sel_lang = sel_roles_list.toString().replaceAll("\\[|\\]|\\s", "");
                lang_txt.setText(sel_lang);
                Constants.SELF_EVAL_DATA.put(getResources().getString(R.string.content_se_1), sel_lang);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()

        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.show();
    }


}
