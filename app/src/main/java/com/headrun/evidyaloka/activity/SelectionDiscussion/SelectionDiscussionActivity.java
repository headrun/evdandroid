package com.headrun.evidyaloka.activity.SelectionDiscussion;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.event.SlotConfirmEvent;
import com.headrun.evidyaloka.model.BlockReleaseDemand;
import com.headrun.evidyaloka.model.SchoolDetails;
import com.headrun.evidyaloka.model.SelectDiscussionData;
import com.headrun.evidyaloka.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import eu.fiskur.chipcloud.ChipCloud;
import eu.fiskur.chipcloud.ChipListener;

/**
 * Created by sujith on 28/3/17.
 */

public class SelectionDiscussionActivity extends AppCompatActivity implements SelectionDiscussionView {

    public String TAG = SelectionDiscussionActivity.class.getSimpleName();

    CompactCalendarView compactcalendar_view;
    SelectionDiscussionPresenter mSelectionDiscussionPresenter;
    CardView booked_slot_lay;
    Button release_btn;
    TextView data, mnth_title;
    ImageView date_picker_left_arrow, date_picker_right_arrow;
    ChipCloud time_slots;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", /*Locale.getDefault()*/Locale.ENGLISH);
    LinkedList<String> slots_keys = new LinkedList<>();
    LinkedList<String> slots_values = new LinkedList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sel_discuss_activity);

        inItView();
        mSelectionDiscussionPresenter = new SelectionDiscussionPresenter(this, this);

        compactcalendar_view.setLocale(TimeZone.getDefault(), Locale.ENGLISH);
        compactcalendar_view.setShouldDrawDaysHeader(true);

        date_picker_left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactcalendar_view.showPreviousMonth();
            }
        });

        date_picker_right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactcalendar_view.showNextMonth();
            }
        });

        compactcalendar_view.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                String sel_date = dateFormat.format(dateClicked);
                String cur_date = dateFormat.format(new Date());

                if (sel_date.equals(cur_date) || dateClicked.after(new Date())) {

                    mSelectionDiscussionPresenter.setSlotsData(compactcalendar_view.getEvents(dateClicked));

                } else {
                    Toast.makeText(getApplicationContext(), "please choose the valid date", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                setMnthtitle(firstDayOfNewMonth);
            }
        });

        release_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectionDiscussionPresenter.releaseSlots();
            }
        });


        setMnthtitle(new Date());
    }

    @Override
    public void setMnthtitle(Date date) {
        String sel_date = dateFormat.format(date);
        int size = compactcalendar_view.getEventsForMonth(date).size();
        if (size <= 0) {
            mSelectionDiscussionPresenter.callSlotsData(date);
        }
        mnth_title.setText(sel_date);
    }

    private void inItView() {
        compactcalendar_view = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        data = (TextView) findViewById(R.id.data);
        mnth_title = (TextView) findViewById(R.id.mnth_title);
        date_picker_left_arrow = (ImageView) findViewById(R.id.date_picker_left_arrow);
        date_picker_right_arrow = (ImageView) findViewById(R.id.date_picker_right_arrow);
        time_slots = (ChipCloud) findViewById(R.id.time_slots);
        booked_slot_lay = (CardView) findViewById(R.id.booked_slot_lay);
        release_btn = (Button) findViewById(R.id.release_btn);
        hideBookedslot();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Selection Discussion");

    }

    @Override
    public void setEventList(List<Event> event_list) {
        compactcalendar_view.addEvents(event_list);

    }

    @Override
    public void displaySoltsData(LinkedList<SelectDiscussionData.Selection_Slot_Data> list) {
        /*slots_data.clear();
        slots_data.addAll(list);
        for (SelectDiscussionData.Selection_Slot_Data item : slots_data) {
            time_slots.addChip(item.slot);
        }*/
    }

    @Override
    public void setBookedSlot(String slot) {
        data.setText(slot);
        ShowBookedSlot();
    }

    @Override
    public void hideBookedslot() {
        booked_slot_lay.setVisibility(View.GONE);
    }

    @Override
    public void ShowBookedSlot() {
        booked_slot_lay.setVisibility(View.VISIBLE);
    }

    @Override
    public void displaySoltsData(final Date date, LinkedHashMap<String, String> list) {
        time_slots.removeAllViews();
        slots_keys.clear();
        slots_values.clear();
        slots_keys.addAll(new ArrayList<String>(list.keySet()));
        slots_values.addAll(new ArrayList<String>(list.values()));

        new ChipCloud.Configure()
                .chipCloud(time_slots)
                .labels(slots_values.toArray(new String[slots_values.size()]))
                .mode(ChipCloud.Mode.SINGLE)
                .chipListener(new ChipListener() {
                    @Override
                    public void chipSelected(int index) {
                        confirmDialog(date, slots_values.get(index));
                        Log.i(TAG, "sel val is " + slots_values.get(index) + "key is " + slots_keys.get(index));
                    }

                    @Override
                    public void chipDeselected(int index) {
                        Log.i(TAG, "sel val is " + slots_values.get(index) + "key is " + slots_keys.get(index));
                    }
                }).build();

    }

    private void confirmDialog(Date date, String slot) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_tsd, null);

        builder.setView(dialogView);
        builder.setCancelable(false);

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        ImageView img_cancel = (ImageView) dialogView.findViewById(R.id.cancel_dialog);
        TextView slot_time = (TextView) dialogView.findViewById(R.id.slot_time);
        // Spinner user_roles = (Spinner) dialogView.findViewById(R.id.user_roles);
        LinearLayout check_lay = (LinearLayout) dialogView.findViewById(R.id.check_lay);
        Button confirm_btn = (Button) dialogView.findViewById(R.id.confirm_btn);

        slot_time.setText(new SimpleDateFormat("MMMdd,").format(date) + " " + slot);
        final List<String> roles_list = new ArrayList<>();
        roles_list.addAll(Constants.SELF_VAL_ONBOARD.get(Constants.TSD));
        final List<String> sel_roles = new ArrayList<>();

        for (int i = 0; i < roles_list.size(); i++) {
            final CheckBox c = new CheckBox(this);
            c.setText(roles_list.get(i));
            c.setId(1000 + i);
            c.setChecked(true);
            sel_roles.add(roles_list.get(i));
            c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String sel_text = buttonView.getText().toString();
                    if (isChecked) {
                        if (!sel_roles.contains(sel_text))
                            sel_roles.add(sel_text);
                    } else {
                        sel_roles.remove(sel_text);
                    }
                }
            });
            check_lay.addView(c);
        }


        /*ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roles_list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        user_roles.setAdapter(dataAdapter);

        user_roles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.i(TAG, "sel value is " + roles_list.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();

            }
        });

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


        dialog.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
