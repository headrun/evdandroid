package com.headrun.evidyaloka.activity.SelectionDiscussion;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.base.HomePresenter;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.model.Book_Relase_Tsd;
import com.headrun.evidyaloka.model.LoginResponse;
import com.headrun.evidyaloka.model.SelectDiscussionData;
import com.headrun.evidyaloka.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

    LinkedHashMap<String, SelectDiscussionData.Booked_slots> current_bookedSlots = new LinkedHashMap<>();

    public SelectionDiscussionPresenter(SelectionDiscussionView mSelectionDiscussionView, Context mContenxt) {
        this.mSelectionDiscussionView = mSelectionDiscussionView;
        this.mContenxt = mContenxt;
    }


    public void callSlotsData(Date date) {
        sel_date = date;
        Log.i(TAG, "sel date is " + date_formate.format(sel_date));
        new CallSlots(new SimpleDateFormat("MM").format(date), new SimpleDateFormat("yyyy").format(date));
    }

    public void setSlotsData(Date dateClicked, List<Event> events) {

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
        mSelectionDiscussionView.displaySoltsData(dateClicked, slots_data);
    }

    public void releaseSlots() {
        if (current_bookedSlots.keySet().size() > 0) {
            Log.i(TAG, "slots are " + current_bookedSlots.keySet().toString());
            //new CallSlots();
        } else {
            Log.i(TAG, "slots are " + current_bookedSlots.keySet().toString());
        }
    }

    public void callReleaseTsd(Date date, String slot_id) {
        mSelectionDiscussionView.showProgressBar();
        new Book_Relase_TsdCall(date, slot_id);
    }

    public void callBookTsd(Date date, String slot_id, Integer role_id) {
        mSelectionDiscussionView.showProgressBar();
        new Book_Relase_TsdCall(date, slot_id, role_id);
    }

    public class CallSlots implements ResponseListener<SelectDiscussionData> {
        HashMap<String, String> params = new HashMap<>();

        CallSlots(String mnth, String year) {
            mSelectionDiscussionView.showCalProgressBar();
            new EVDNetowrkServices().get_avalilable_tsd_slots(mContenxt, this, mnth, year);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            mSelectionDiscussionView.hideCalProgressBar();
            Toast.makeText(mContenxt, "seems get error", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onResponse(SelectDiscussionData response) {
            mSelectionDiscussionView.hideCalProgressBar();
            new UserData(mContenxt, response);


        }
    }

    public void processTsdData(SelectDiscussionData response) {
        if (response.status == 0) {
            if (response.data != null) {
                //data = response.slots_data;
                SelfEvens(response.data);
            }

        } else {
            Toast.makeText(mContenxt, response.message, Toast.LENGTH_LONG).show();
        }
    }

    public class Book_Relase_TsdCall implements ResponseListener<Book_Relase_Tsd> {
        HashMap<String, String> params = new HashMap<>();
        Date sel_date;

        Book_Relase_TsdCall(Date date, String slot_id) {
            params.clear();
            sel_date = date;
            params.put("type", "release");
            params.put("slot_id", slot_id);
            new EVDNetowrkServices().book_Releasee_tsd_slots(mContenxt, this, params);
        }

        Book_Relase_TsdCall(Date date, String slot_id, int role_id) {
            params.clear();
            sel_date = date;
            params.put("type", "book");
            params.put("slot_id", slot_id);
            params.put("role_id", String.valueOf(role_id));

            new EVDNetowrkServices().book_Releasee_tsd_slots(mContenxt, this, params);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            mSelectionDiscussionView.hideProgressBar();
            Toast.makeText(mContenxt, "Booking is not confirmed", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onResponse(Book_Relase_Tsd response) {
            mSelectionDiscussionView.hideProgressBar();

            if (response.status == 0) {
                callSlotsData(sel_date);
                Toast.makeText(mContenxt, "TSD Booked successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContenxt, response.message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void SelfEvens(SelectDiscussionData.Selection_Slot_Data data) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(sel_date);

        SLOTS_DATA.clear();
        current_bookedSlots.clear();
        Constants.BOOKED_SLOTS_ROLES.clear();

        if (data.booked_slots != null) {
            current_bookedSlots.putAll(data.booked_slots);
            if (current_bookedSlots.size() > 0)
                mSelectionDiscussionView.setBookedSlot(current_bookedSlots);
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

    public class UserData implements ResponseListener<LoginResponse> {

        SelectDiscussionData mTsdResponse;

        public UserData(Context mContext, SelectDiscussionData mTsdResponse) {
            this.mTsdResponse = mTsdResponse;
            mSelectionDiscussionView.showCalProgressBar();
            new EVDNetowrkServices().getUserData(mContext, this);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            mSelectionDiscussionView.hideCalProgressBar();
        }

        @Override
        public void onResponse(LoginResponse response) {

            mSelectionDiscussionView.hideCalProgressBar();
            if (response.status == 0) {
                new Utils(mContenxt).userSession.setUserData(response);
                new HomePresenter(mContenxt).setGalleryImages();
                processTsdData(mTsdResponse);
            }
        }
    }

}
