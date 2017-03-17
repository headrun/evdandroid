package com.headrun.evidyaloka.activity.demand_slots;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.model.Demand;
import com.headrun.evidyaloka.model.SchoolDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.fiskur.chipcloud.ChipCloud;
import eu.fiskur.chipcloud.ChipListener;

/**
 * Created by sujith on 23/1/17.
 */
public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.SlotViewHolder> {

    public interface Callbacks {
        public void onSlotSelect(int chip_pos, String demand_id, String sel_day, SchoolDetails.ClassTimings sel_slot);

        public void onSlotDeSelect(int chip_pos, String demand_id, String sel_day, SchoolDetails.ClassTimings sel_slot);

    }

    private String TAG = TimeSlotAdapter.class.getSimpleName();
    private TimeSlotAdapter.Callbacks mCallbacks;
    private Context context;
    private List<Demand> mFeedList;
    private Map<String, SchoolDetails.ClassTimings[]> mschool_timings;
    private Map<String, String> slot_Ids = new HashMap<>();
    private String demand_id;
    private SchoolDetails mSchooldetails;
    private SchoolDetails.SlotDeatils mSlotDeatils;
    private int course_slot_pos;

    public TimeSlotAdapter(Map<String, SchoolDetails.ClassTimings[]> mschool_timings, String demand_id, Context context) {
        this.mschool_timings = mschool_timings;
        this.context = context;
        this.demand_id = demand_id;

    }

    public TimeSlotAdapter(SchoolDetails mSchooldetails, int course_slot_pos, Context context) {
        this.mSchooldetails = mSchooldetails;
        this.mschool_timings = mSchooldetails.time_sllots;
        this.context = context;
        this.demand_id = mSchooldetails.school_id;
        this.mSlotDeatils = mSchooldetails.slot_details;
        this.course_slot_pos = course_slot_pos;
    }

    public void setCallbacks(TimeSlotAdapter.Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    @Override
    public TimeSlotAdapter.SlotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = View.inflate(parent.getContext(), R.layout.inflate_timeslot_adapter, null);
        //view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new TimeSlotAdapter.SlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TimeSlotAdapter.SlotViewHolder holder, int position) {

        final Object key = (mschool_timings.keySet().toArray())[position];

        final String keys = (String) key;
        final SchoolDetails.ClassTimings[] timings = mschool_timings.get(keys);
        // boolean sel_center = sameCenter();
        holder.slot_day.setText(keys);
        final List<String> slot_lables = new ArrayList<>();

        for (SchoolDetails.ClassTimings slot : timings) {
            // Log.i(TAG, "slot id is " + slot.class_id + " timings are  " + slot.start_date + " - " + slot.end_date);
            slot_lables.add(slot.start_date + " - " + slot.end_date);
            //holder.time_slots.addChip();
        }

        new ChipCloud.Configure()
                .chipCloud(holder.time_slots)
                .labels(slot_lables.toArray(new String[slot_lables.size()]))
                .mode(ChipCloud.Mode.SINGLE)
                .chipListener(new ChipListener() {
                    @Override
                    public void chipSelected(int index) {

                        // Log.i(TAG, "demand_id " + demand_id + " id is " + timings[index].class_id + " sel pos is " + slot_lables.get(index));

                        if (mCallbacks != null) {
                            mCallbacks.onSlotSelect(index, demand_id, keys, timings[index]);
                        }
                    }

                    @Override
                    public void chipDeselected(int index) {
                        // Log.i(TAG, "de sel pos is " + slot_lables.get(index));
                        if (mCallbacks != null) {
                            mCallbacks.onSlotDeSelect(index, demand_id, keys, timings[index]);
                        }
                    }
                }).build();

        // holder.time_slots.setSelectedChip();

    }

    @Override
    public int getItemCount() {
        return (mschool_timings != null ? mschool_timings.size() : 0);
    }

    public static class SlotViewHolder extends RecyclerView.ViewHolder {

        static eu.fiskur.chipcloud.ChipCloud time_slots;
        TextView slot_day;

        public SlotViewHolder(View itemView) {
            super(itemView);
            slot_day = (TextView) itemView.findViewById(R.id.slot_day);
            time_slots = (eu.fiskur.chipcloud.ChipCloud) itemView.findViewById(R.id.time_slots);

        }
    }

    public void DeSelectChip(int pos) {

        if (TimeSlotAdapter.SlotViewHolder.time_slots.isSelected(pos)) {
            //Log.i(TAG, "chip is  sel");
            TimeSlotAdapter.SlotViewHolder.time_slots.chipDeselected(pos);

        } else {
            TimeSlotAdapter.SlotViewHolder.time_slots.setSelectedChip(pos);
            if (TimeSlotAdapter.SlotViewHolder.time_slots.isSelected(pos))
                TimeSlotAdapter.SlotViewHolder.time_slots.chipDeselected(pos);
            //Log.i(TAG, "chip is not sel");
        }

    }

    public void SelectChip(int pos) {
        TimeSlotAdapter.SlotViewHolder.time_slots.chipSelected(pos);
    }

    public void setChipMode() {
        TimeSlotAdapter.SlotViewHolder.time_slots.setMode(ChipCloud.Mode.NONE);
    }

    private boolean sameCenter() {

        if (mSlotDeatils.center_id != null) {
            if (mSchooldetails.school_id == mSlotDeatils.center_id)
                return true;
        }

        return false;
    }

    private boolean sameCourse() {
        if (mSlotDeatils.center_id != null) {
            if (course_slot_pos == Integer.valueOf(mSlotDeatils.center_id))
                return true;
        }

        return false;
    }

}


