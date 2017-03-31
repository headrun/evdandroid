package com.headrun.evidyaloka.activity.SelectionDiscussion;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.model.SelectDiscussionData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.headrun.evidyaloka.dto.FiltersDataResponse.TAG;

/**
 * Created by sujith on 28/3/17.
 */

public class SelectionDiscussionPresenter {

    SelectionDiscussionView mSelectionDiscussionView;
    Context mContenxt;
    LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedList<SelectDiscussionData.Selection_Slot_Data>[]>>> data;

    List<Event> SLOTS_DATA = new ArrayList<>();
    SimpleDateFormat date_formate = new SimpleDateFormat("dd-MM-yyyy");

    Date sel_date;

    LinkedHashMap<String, String> current_bookedSlots = new LinkedHashMap<>();

    public SelectionDiscussionPresenter(SelectionDiscussionView mSelectionDiscussionView, Context mContenxt) {
        this.mSelectionDiscussionView = mSelectionDiscussionView;
        this.mContenxt = mContenxt;
    }

    public void setEvents(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedList<SelectDiscussionData.Selection_Slot_Data>[]>>> data) {


        for (Map.Entry<String, LinkedHashMap<String, LinkedHashMap<String, LinkedList<SelectDiscussionData.Selection_Slot_Data>[]>>> entry : data.entrySet()) {

            String year = entry.getKey();
            LinkedHashMap<String, LinkedHashMap<String, LinkedList<SelectDiscussionData.Selection_Slot_Data>[]>> mnth_data = entry.getValue();
            for (Map.Entry<String, LinkedHashMap<String, LinkedList<SelectDiscussionData.Selection_Slot_Data>[]>> get_mnth_data : mnth_data.entrySet()) {
                String month = get_mnth_data.getKey();
                LinkedHashMap<String, LinkedList<SelectDiscussionData.Selection_Slot_Data>[]> date_wise_slots = get_mnth_data.getValue();
                for (Map.Entry<String, LinkedList<SelectDiscussionData.Selection_Slot_Data>[]> slots_data : date_wise_slots.entrySet()) {

                    String date = slots_data.getKey();
                    LinkedList<SelectDiscussionData.Selection_Slot_Data>[] slots = slots_data.getValue();
                    String event_date = date + "-" + month + "-" + year;
                    try {
                        Date d = date_formate.parse(event_date);
                        Calendar c = Calendar.getInstance();
                        c.setTime(d);
                        SLOTS_DATA.add(new Event(R.color.colorGrey50, c.getTimeInMillis(), slots));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        mSelectionDiscussionView.setEventList(SLOTS_DATA);
    }

    public void callSlotsData(Date date) {
        sel_date = date;
        Log.i(TAG, "sel date is " + date_formate.format(sel_date));
        new CallSlots(new SimpleDateFormat("MM").format(date), new SimpleDateFormat("yyyy").format(date));
    }

    public void setSlotsData(List<Event> events) {

        //LinkedList<SelectDiscussionData.Selection_Slot_Data> slots_data = new LinkedList<>();
        LinkedHashMap<String, String> slots_data = new LinkedHashMap<>();
        for (Event event : events) {
            slots_data.putAll((LinkedHashMap) event.getData());

            Log.i(TAG, "slots data size is " + slots_data.size());
            /*
            LinkedList<SelectDiscussionData.Selection_Slot_Data>[] slots = (LinkedList<SelectDiscussionData.Selection_Slot_Data>[]) event.getData();
            for (LinkedList<SelectDiscussionData.Selection_Slot_Data> data : slots) {
                slots_data.addAll(data);
            }
        }*/

        }
        mSelectionDiscussionView.displaySoltsData(sel_date, slots_data);
    }

    public void releaseSlots() {
        if (current_bookedSlots.keySet().size() > 0) {
            Log.i(TAG, "slots are " + current_bookedSlots.keySet().toString());
            //new CallSlots();
        } else {
            Log.i(TAG, "slots are " + current_bookedSlots.keySet().toString());
        }
    }

    public class CallSlots implements ResponseListener<SelectDiscussionData> {
        HashMap<String, String> params = new HashMap<>();

        CallSlots(String mnth, String year) {
            new EVDNetowrkServices().get_avalilable_tsd_slots(mContenxt, this, mnth, year);
        }

        CallSlots(String role) {
            params.clear();
            params.put("type", "release");
            params.put("role", "teacher");
            new EVDNetowrkServices().book_Releasee_tsd_slots(mContenxt, this, params);
        }

        CallSlots(String type, String mnth, String day, String role, String slot) {

            params.put("type", "release");
            params.put("month", "teacher");
            params.put("day", "teacher");
            params.put("role", "teacher");
            params.put("slot", "teacher");

            //new EVDNetowrkServices().book_Releasee_tsd_slots(mContenxt, this, release_slots, release_slots);
        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }

        @Override
        public void onResponse(SelectDiscussionData response) {

            if (response.status == 0) {
                if (response.data != null) {
                    //data = response.slots_data;
                    SelfEvens(response.data);
                }

            } else {

            }

        }
    }

    public void SelfEvens(SelectDiscussionData.Selection_Slot_Data data) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(sel_date);
        SLOTS_DATA.clear();
        if (data.current_slot != null) {
            current_bookedSlots.putAll(data.current_slot);
            if (current_bookedSlots.size() > 0)
                mSelectionDiscussionView.setBookedSlot(new ArrayList<String>(current_bookedSlots.values()).toString().replaceAll("\\[|\\]|\\s", ""));
            else
                mSelectionDiscussionView.hideBookedslot();
        }

        if (data.available_slots != null) {

            for (Map.Entry<String, LinkedHashMap<String, String>> entry : data.available_slots.entrySet()) {

                String event_date = entry.getKey() + "-" + new SimpleDateFormat("MM").format(sel_date) + "-" + new SimpleDateFormat("yyyy").format(sel_date);
                try {
                    Date d = date_formate.parse(event_date);
                    SLOTS_DATA.add(new Event(R.color.tabindication, d.getTime(), entry.getValue()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }
        if (SLOTS_DATA.size() > 0) {
            mSelectionDiscussionView.setEventList(SLOTS_DATA);
        }
    }

}
