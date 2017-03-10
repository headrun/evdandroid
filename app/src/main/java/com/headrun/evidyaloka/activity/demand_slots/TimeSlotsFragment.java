package com.headrun.evidyaloka.activity.demand_slots;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.auth.LoginActivity;
import com.headrun.evidyaloka.activity.base.HomeActivity;
import com.headrun.evidyaloka.activity.demands.BaseEVDFragment;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.event.SlotConfirmEvent;
import com.headrun.evidyaloka.model.BlockReleaseDemand;
import com.headrun.evidyaloka.model.SchoolDetails;
import com.headrun.evidyaloka.utils.SimpleDividerItemDecoration;
import com.headrun.evidyaloka.utils.Utils;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by sujith on 23/1/17.
 */
public class TimeSlotsFragment extends BaseEVDFragment implements TimeSlotAdapter.Callbacks, ResponseListener<BlockReleaseDemand>, Response.ErrorListener {

    private String TAG = TimeSlotsFragment.class.getSimpleName();
    private static final String TAG_SORT = "state_sort";
    private static final String TAG_SLOT_POS = "slot_pos";
    private boolean RELEASE_SLOTS = false;
    private int course_slot_pos;
    private RecyclerView timeslot_list;
    private TimeSlotAdapter mTimeSlotAdapter;
    private DemandSlotActivity mDemandSlotActivity;
    private SchoolDetails mSchoolDeatils;
    private SlotConfirmEvent mSlotConfirmevent;
    private RelativeLayout no_result_lay;
    private TextView error_ext;
    private ImageView error_img;
    private Map<String, SchoolDetails.ClassTimings> slot_ids = new HashMap<>();
    public TimeSlotsFragment.menuConfirm menuConfirmevent;

    Utils utils;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public static TimeSlotsFragment newInstance(SchoolDetails mSchoolDeatils, int slot_pos) {

        Bundle args = new Bundle();
        args.putParcelable(SchoolDetails.TAG_SCHOOLDEATILS, mSchoolDeatils);
        args.putInt(TAG_SLOT_POS, slot_pos);

        TimeSlotsFragment fragment = new TimeSlotsFragment();
        fragment.setArguments(args);
        return fragment;

    }

    public TimeSlotsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeslot, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        utils = new Utils(getActivity());
        course_slot_pos = (Integer) getArguments().getInt(TAG_SLOT_POS);
        mSchoolDeatils = (SchoolDetails) getArguments().getParcelable(SchoolDetails.TAG_SCHOOLDEATILS);

        timeslot_list = (RecyclerView) view.findViewById(R.id.timeslot_list);
        no_result_lay = (RelativeLayout) view.findViewById(R.id.no_result_lay);
        error_ext = (TextView) view.findViewById(R.id.textView);
        error_img = (ImageView) view.findViewById(R.id.error_img);

        timeslot_list.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        timeslot_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mConfirmSlot,
                new IntentFilter("confirmSlot"));
/*
        if (mSlotConfirmevent != null) {
            String demand_id = Constants.SEL_SLOTDATA.center_id;
            if (!demand_id.equals(mSchoolDeatils.school_id))
                Constants.SEL_SLOTDATA = null;

        }*/

        if (mSchoolDeatils.time_sllots != null && mSchoolDeatils.time_sllots.size() <= 0) {
            no_result_lay.setVisibility(View.VISIBLE);
            error_ext.setText(R.string.no_slots);
        } else {
            no_result_lay.setVisibility(View.GONE);
            mTimeSlotAdapter = new TimeSlotAdapter(mSchoolDeatils, course_slot_pos, getActivity());
            mTimeSlotAdapter.setCallbacks(this);
            timeslot_list.setAdapter(mTimeSlotAdapter);

        }
        try {

            menuConfirmevent = (TimeSlotsFragment.menuConfirm) getActivity();
            menuCOnfirmDisable();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setmenuConfirm(TimeSlotsFragment.menuConfirm menuConfirmevent) {
        this.menuConfirmevent = menuConfirmevent;
    }

    @Override
    public void onSlotSelect(int index, String deamnd_id, String slot_day, SchoolDetails.ClassTimings sel_slot) {

        //   mTimeSlotAdapter.DeSelectChip(index);

        if (mSlotConfirmevent == null) {
            String offerId = "";
            if (mSchoolDeatils != null && mSchoolDeatils.demands != null && mSchoolDeatils.demands.length >= course_slot_pos) {
                offerId = mSchoolDeatils.demands[course_slot_pos].id;
            }

            slot_ids.clear();
            slot_ids.put(slot_day, sel_slot);
            mSlotConfirmevent = new SlotConfirmEvent((String) mSchoolDeatils.school_id, offerId, slot_ids);

        } else {
            slot_ids.put(slot_day, sel_slot);
        }

        if (!utils.getCookieValue(getActivity(), "sessionid").isEmpty()) {
            if (checkSlotDetails()) {
                if (slot_ids.size() == 2) {
                    if (!isRelease()) {
                        menuConfirmEnable();
                        confirmDialog();
                    }
                } else if (slot_ids.size() > 2) {
                    alertdilaog(index);
                }
            } else {
                slotReleaseDialog();
            }
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class)
                    .putExtra(Constants.TYPE, true)
                    .putExtra(Constants.ID, mSchoolDeatils.school_id)
                    .putExtra(Constants.REDIRECT_TO, Constants.SLOTS_TYPE));
        }

    }

    private boolean checkSlotDetails() {
        if (mSchoolDeatils.slot_details != null && mSchoolDeatils.slot_details.slots_data != null && mSchoolDeatils.slot_details.slots_data.length > 0)
            return false;
        return true;
    }

    private void alertdilaog(final int index) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.evidya_slot_error);
        builder.setCancelable(false);

        builder.setMessage("You are trying to select more than 2 Slots");
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                //mTimeSlotAdapter.setChipMode();
                //mTimeSlotAdapter.DeSelectChip(index);
                menuCOnfirmDisable();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    /* @Subscribe(threadMode = ThreadMode.MAIN)
     public void onEvent(final SlotConfirmEvent event) {
         //Log.e("onEvent sel confirm", "->" + event.center_id + " offer id " + event.offer_id + " slots " + event.slot_ids.toString());
         confirmDialog();
     }
 */
    private void confirmDialog() {

        //
      /*  final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setTitle("Select Content Language");
*/

        try {
            final SlotConfirmEvent slots_data = mSlotConfirmevent;


            if (slots_data != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.teach_confirm_dialog, null);

                builder.setView(dialogView);

                //dialog.setContentView(R.layout.teach_confirm_dialog);
                //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                builder.setCancelable(false);

                ImageView img_cancel = (ImageView) dialogView.findViewById(R.id.cancel_dialog);
                TextView sel_slots = (TextView) dialogView.findViewById(R.id.sel_slots);
                TextView cener_name = (TextView) dialogView.findViewById(R.id.cener_name);
                Button confirm_btn = (Button) dialogView.findViewById(R.id.confirm_btn);

                final AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);

                cener_name.setText(mSchoolDeatils.school_name);

                StringBuffer sel_day = new StringBuffer();

                for (Map.Entry<String, SchoolDetails.ClassTimings> entry : slot_ids.entrySet()) {
                    sel_day.append(entry.getKey());
                    sel_day.append("\n");
                    sel_day.append(entry.getValue().start_date + "-" + entry.getValue().end_date);
                    sel_day.append("\n");
                }

                sel_slots.setText(sel_day.toString());

                final ResponseListener<BlockReleaseDemand> context = this;

                confirm_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RELEASE_SLOTS = false;
                        dialog.dismiss();
                        showProgressDialog();
                        new EVDNetowrkServices().blockDemand(getActivity(), context, slots_data);
                        //  Log.e("onEvent sel confirm", "->" + slots_data.center_id + " offer id " + slots_data.offer_id + " slots " + slots_data.slot_ids.toString());
                    }
                });

                img_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSlotDeSelect(int index, String demand_id, String Sel_day, SchoolDetails.ClassTimings sel_slot) {

        String offerId = demand_id;
        slot_ids.remove(Sel_day);

        /*if (mSlotConfirmevent != null) {
            int id = slot_ids.indexOf(slotId);
            if (id != -1) {
                slot_ids.remove(id);
            }
        }*/

        if (!utils.getCookieValue(getActivity(), "sessionid").isEmpty()) {
            if (checkSlotDetails()) {
                if (slot_ids.size() == 2) {
                    if (!isRelease()) {
                        menuCOnfirmDisable();
                    }
                }
            } else {
                slotReleaseDialog();
            }
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class)
                    .putExtra(Constants.TYPE, true)
                    .putExtra(Constants.ID, mSchoolDeatils.school_id)
                    .putExtra(Constants.REDIRECT_TO, Constants.SLOTS_TYPE));
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.i(TAG, "error");
        hideProgressDialog();
    }

    @Override
    public void onResponse(BlockReleaseDemand response) {

        hideProgressDialog();
        if (RELEASE_SLOTS) {
            if (response.message.toLowerCase().contains("success")) {
                mSchoolDeatils.slot_details = null;
                menuConfirmevent.menuChangeTitle(getActivity().getString(R.string.confirm));
            } else {
                new Utils(getActivity()).showAlertDialog(response.message);
            }
        } else {
            if (response.status == 0) {

                startActivity(new Intent(getActivity(), HomeActivity.class)
                        .putExtra(Constants.REDIRECT_TO, true)
                        .putExtra(Constants.TYPE, "session_page"));
            } else {
                new Utils(getActivity()).showAlertDialog(response.message);

            }
        }
    }

    interface menuConfirm {

        public void menuConfirmEnable();

        public void menuConfirmDesable();

        public void menuChangeTitle(String title);

    }

    private void menuConfirmEnable() {
        checkMenu();
    }

    private void menuCOnfirmDisable() {

        checkMenu();
    }

    private void checkMenu() {

        if (menuConfirmevent != null && slot_ids != null)
            if (slot_ids.size() == 2) {
                menuConfirmevent.menuConfirmEnable();
            } else if (slot_ids.size() > 2 || slot_ids.size() < 2) {
                menuConfirmevent.menuConfirmDesable();
            }
    }

    private void slotReleaseDialog() {

        SchoolDetails.SlotDeatils release_slot = mSchoolDeatils.slot_details;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.teach_confirm_dialog, null);

        builder.setView(dialogView);

        //dialog.setContentView(R.layout.teach_confirm_dialog);
        //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        builder.setCancelable(false);

        ImageView img_cancel = (ImageView) dialogView.findViewById(R.id.cancel_dialog);
        TextView sel_slots = (TextView) dialogView.findViewById(R.id.sel_slots);
        TextView cener_name = (TextView) dialogView.findViewById(R.id.cener_name);
        Button confirm_btn = (Button) dialogView.findViewById(R.id.confirm_btn);
        confirm_btn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.release_btn_color));

        confirm_btn.setText("Release slots");

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        cener_name.setText(release_slot.center_name);

        StringBuffer sel_day = new StringBuffer();

        for (SchoolDetails.SlotsData entry : release_slot.slots_data) {
            sel_day.append(entry.day);
            sel_day.append("\n");
            sel_day.append(new Utils().convertTime(entry.start_time) + "-" + new Utils().convertTime(entry.end_time));
            sel_day.append("\n");
        }

        sel_slots.setText(sel_day.toString());

        final ResponseListener<BlockReleaseDemand> context = this;
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                showProgressDialog();
                RELEASE_SLOTS = true;
                new EVDNetowrkServices().releaseDemand(getActivity(), context);
            }
        });

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                menuConfirmEnable();

            }
        });

        dialog.show();

    }

    private BroadcastReceiver mConfirmSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle data = intent.getExtras();
            if (data != null) {
                String type = data.getString("type");
               /* if (type.equals("release"))
                    slotReleaseDialog();
                else*/
                confirmDialog();
            }

        }
    };

    private boolean isRelease() {

        if (mSchoolDeatils.slot_details != null && mSchoolDeatils.slot_details.slots_data != null && mSchoolDeatils.slot_details.slots_data.length > 0)
            return true;

        return false;
    }

}


