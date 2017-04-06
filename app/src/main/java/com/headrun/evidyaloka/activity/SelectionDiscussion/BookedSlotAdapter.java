package com.headrun.evidyaloka.activity.SelectionDiscussion;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.demands.DemandAdapter;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.model.Demand;
import com.headrun.evidyaloka.model.SelectDiscussionData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import static com.headrun.evidyaloka.model.SelectDiscussionData.*;

/**
 * Created by sujith on 31/3/17.
 */

public class BookedSlotAdapter extends RecyclerView.Adapter<BookedSlotAdapter.BookedSlotViewHolder> {

    public interface Callbacks {
        public void onBoodeSlot(String slot_id);
    }

    private String TAG = DemandAdapter.class.getSimpleName();
    private BookedSlotAdapter.Callbacks mCallbacks;
    private Context context;

    LinkedHashMap<String, SelectDiscussionData.Booked_slots> slots;
    LinkedList<SelectDiscussionData.Booked_slots> booked_lsit_values = new LinkedList<>();
    LinkedList<String> booked_lsit_keys = new LinkedList<>();

    public BookedSlotAdapter(Context context, LinkedHashMap<String, SelectDiscussionData.Booked_slots> slots) {
        this.context = context;
        this.slots = slots;
        booked_lsit_values.addAll(slots.values());
        booked_lsit_keys.addAll(slots.keySet());
    }


    @Override
    public BookedSlotAdapter.BookedSlotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = View.inflate(parent.getContext(), R.layout.booked_slot_adapter, null);
        return new BookedSlotAdapter.BookedSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookedSlotAdapter.BookedSlotViewHolder holder, final int position) {

        SelectDiscussionData.Booked_slots slot = booked_lsit_values.get(position);
        Constants.BOOKED_SLOTS_ROLES.add(slot.role);
        holder.role_txt.setText(slot.role);
        holder.slot_txt.setText(slot.booked_time);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallbacks != null) {
                    mCallbacks.onBoodeSlot(booked_lsit_keys.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return slots.size();
    }

    public void setCallbacks(BookedSlotAdapter.Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    public class BookedSlotViewHolder extends RecyclerView.ViewHolder {


        TextView role_txt, slot_txt;
        ImageView booked_close;

        public BookedSlotViewHolder(View itemView) {
            super(itemView);

            role_txt = (TextView) itemView.findViewById(R.id.role_txt);
            slot_txt = (TextView) itemView.findViewById(R.id.slot_txt);
            booked_close = (ImageView) itemView.findViewById(R.id.booked_close);

        }
    }

}
