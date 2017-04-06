package com.headrun.evidyaloka.activity.SelectionDiscussion;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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

public class SelectionDiscussionActivity extends AppCompatActivity implements SelectionDiscussionView, BookedSlotAdapter.Callbacks {

    public String TAG = SelectionDiscussionActivity.class.getSimpleName();


    CompactCalendarView compactcalendar_view;
    SelectionDiscussionPresenter mSelectionDiscussionPresenter;
    CardView booked_slot_lay;
    Button release_btn;
    TextView data, mnth_title, booked_slot_text, available_slot_title;
    android.support.v7.widget.RecyclerView booked_slot_list;
    ImageView date_picker_left_arrow, date_picker_right_arrow;
    ChipCloud time_slots;
    BookedSlotAdapter mBookedSlotAdapter;
    ProgressBar progress_bar, cal_progress_bar;
    Utils utils;
    Date sel_date;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", /*Locale.getDefault()*/Locale.ENGLISH);
    LinkedList<String> slots_keys = new LinkedList<>();
    LinkedList<String> slots_values = new LinkedList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sel_discuss_activity);

        inItView();
        mSelectionDiscussionPresenter = new SelectionDiscussionPresenter(this, this);
        utils = new Utils(this);
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
                sel_date = dateClicked;
                time_slots.removeAllViews();
                String sel_date = dateFormat.format(dateClicked);
                String cur_date = dateFormat.format(new Date());

                if (sel_date.equals(cur_date) || dateClicked.after(new Date())) {

                    mSelectionDiscussionPresenter.setSlotsData(dateClicked, compactcalendar_view.getEvents(dateClicked));

                } else {
                    Toast.makeText(getApplicationContext(), "please choose the valid date", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                time_slots.removeAllViews();
                setMnthtitle(firstDayOfNewMonth);
            }
        });

        release_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectionDiscussionPresenter.releaseSlots();
            }
        });

        booked_slot_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewBookedSlots();
            }
        });


        setMnthtitle(new Date());
    }

    @Override
    public void onBoodeSlot(String slot_id) {
        mSelectionDiscussionPresenter.callReleaseTsd(sel_date, slot_id);
    }

    @Override
    public void setMnthtitle(Date date) {
        sel_date = date;
        String sel_date = dateFormat.format(date);
        mSelectionDiscussionPresenter.callSlotsData(date);
        mnth_title.setText(sel_date);
    }

    private void inItView() {
        compactcalendar_view = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        data = (TextView) findViewById(R.id.data);
        mnth_title = (TextView) findViewById(R.id.mnth_title);
        available_slot_title = (TextView) findViewById(R.id.available_slot_title);
        date_picker_left_arrow = (ImageView) findViewById(R.id.date_picker_left_arrow);
        date_picker_right_arrow = (ImageView) findViewById(R.id.date_picker_right_arrow);
        time_slots = (ChipCloud) findViewById(R.id.time_slots);
        booked_slot_lay = (CardView) findViewById(R.id.booked_slot_lay);
        release_btn = (Button) findViewById(R.id.release_btn);
        booked_slot_list = (RecyclerView) findViewById(R.id.booked_slot_list);
        booked_slot_text = (TextView) findViewById(R.id.booked_slot_text);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        cal_progress_bar = (ProgressBar) findViewById(R.id.cal_progress_bar);

        booked_slot_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        hideBookedslot();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Selection Discussion");

    }

    @Override
    public void hideProgressBar() {
        progress_bar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        progress_bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCalProgressBar() {

        cal_progress_bar.setVisibility(View.GONE);
    }

    @Override
    public void showCalProgressBar() {

        cal_progress_bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void setEventList(List<Event> event_list) {
        compactcalendar_view.removeAllEvents();
        compactcalendar_view.addEvents(event_list);

        mSelectionDiscussionPresenter.setSlotsData(sel_date, compactcalendar_view.getEvents(sel_date));
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
    public void setBookedSlot(LinkedHashMap<String, SelectDiscussionData.Booked_slots> slots) {

        ShowBookedSlot();

        mBookedSlotAdapter = new BookedSlotAdapter(this, slots);
        booked_slot_list.setAdapter(mBookedSlotAdapter);
        mBookedSlotAdapter.setCallbacks(this);
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

        mnth_title.setText(dateFormat.format(date));

        time_slots.removeAllViews();
        slots_keys.clear();
        slots_values.clear();
        slots_keys.addAll(new ArrayList<String>(list.keySet()));
        slots_values.addAll(new ArrayList<String>(list.values()));

        if (list.size() > 0) {
            available_slot_title.setText("Available Slots ");
            available_slot_title.setGravity(Gravity.LEFT);
            time_slots.setVisibility(View.VISIBLE);

            new ChipCloud.Configure()
                    .chipCloud(time_slots)
                    .labels(slots_values.toArray(new String[slots_values.size()]))
                    .mode(ChipCloud.Mode.SINGLE)
                    .chipListener(new ChipListener() {
                        @Override
                        public void chipSelected(int index) {
                            confirmDialog(index, date, slots_values.get(index), slots_keys.get(index));
                            Log.i(TAG, "sel val is " + slots_values.get(index) + "key is " + slots_keys.get(index));
                        }

                        @Override
                        public void chipDeselected(int index) {
                            Log.i(TAG, "sel val is " + slots_values.get(index) + "key is " + slots_keys.get(index));
                        }
                    }).build();

        } else if (list.size() == 0) {
            available_slot_title.setText("Slots are not available ");
            available_slot_title.setGravity(Gravity.CENTER);
            time_slots.setVisibility(View.GONE);
        }


    }

    private void confirmDialog(final int chip_index, Date date, String slot, final String slot_id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_tsd, null);

        builder.setView(dialogView);
        builder.setCancelable(false);

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        final ImageView img_cancel = (ImageView) dialogView.findViewById(R.id.cancel_dialog);
        TextView slot_time = (TextView) dialogView.findViewById(R.id.slot_time);
        TextView sel_role_title = (TextView) dialogView.findViewById(R.id.sel_role_title);

        RadioGroup check_lay = (RadioGroup) dialogView.findViewById(R.id.check_lay);
        Button confirm_btn = (Button) dialogView.findViewById(R.id.confirm_btn);

        final TextView txt_error = (TextView) dialogView.findViewById(R.id.txt_error);
        txt_error.setVisibility(View.GONE);

        slot_time.setText(new SimpleDateFormat("MMMdd,").format(date) + " " + slot);
        final List<String> roles_list = new ArrayList<>();

        roles_list.addAll(Constants.SELF_VAL_ONBOARD.get(Constants.TSD));


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER;

        if (roles_list.size() < 0) {
            check_lay.setVisibility(View.GONE);
            confirm_btn.setVisibility(View.GONE);
            sel_role_title.setVisibility(View.GONE);
            txt_error.setText("You have already booked slots for the preferred role. Release the booked slot to chose a slot.");
            txt_error.setVisibility(View.VISIBLE);
        } else {
            check_lay.setVisibility(View.VISIBLE);
            confirm_btn.setVisibility(View.VISIBLE);
            sel_role_title.setVisibility(View.VISIBLE);
            txt_error.setVisibility(View.GONE);
        }

        for (int i = 0; i < roles_list.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(roles_list.get(i));
            radioButton.setId(i + 1000);
            radioButton.setGravity(params.gravity);
            check_lay.addView(radioButton);
        }
        final String[] data = new String[1];
        check_lay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                RadioButton button = (RadioButton) group.findViewById(checkedId);

                data[0] = button.getText().toString().trim();


            }
        });

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (data[0] != null && !data[0].isEmpty()) {
                    if (!Constants.BOOKED_SLOTS_ROLES.contains(data[0])) {
                        Integer role_id = -1;
                        for (Map.Entry<Integer, String> entry : utils.getUserRolesList().entrySet()) {
                            if (entry.getValue().equals(data[0])) {
                                role_id = entry.getKey();
                                break;
                            }
                        }
                        Log.i(TAG, "role is" + role_id + "slot id is " + slot_id);
                        dialog.cancel();

                        if (role_id != -1)
                            mSelectionDiscussionPresenter.callBookTsd(sel_date, slot_id, role_id);

                    } else {
                        txt_error.setText("You have already booked slots for the preferred role. Release the booked slot to chose a slot.");
                        Log.i(TAG, "please check the role");
                        txt_error.setVisibility(View.VISIBLE);
                    }
                } else {
                    txt_error.setText("please choose the role");
                }
            }
        });

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_slots.chipDeselected(chip_index);
                dialog.cancel();

            }
        });

        dialog.show();

    }

    @Override
    public void viewBookedSlots() {
        if (booked_slot_list.getVisibility() == View.VISIBLE) {
            booked_slot_list.setVisibility(View.GONE);
            utils.setTextDrawable(booked_slot_text, 0, 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
        } else {
            booked_slot_list.setVisibility(View.VISIBLE);
            utils.setTextDrawable(booked_slot_text, 0, 0, R.drawable.up_arrow, 0);
        }
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
