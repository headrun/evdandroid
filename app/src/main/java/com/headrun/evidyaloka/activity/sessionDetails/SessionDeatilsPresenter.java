package com.headrun.evidyaloka.activity.sessionDetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.dto.ChangeSessionStatus;
import com.headrun.evidyaloka.dto.SessionDetailsResponse;
import com.headrun.evidyaloka.model.*;
import com.headrun.evidyaloka.model.SessionDetails;
import com.headrun.evidyaloka.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sujith on 6/3/17.
 */

public class SessionDeatilsPresenter {

    SessionDetailsView mSessionview;
    Context montext;

    public SessionDeatilsPresenter(Context montext, SessionDetailsView mSessionview) {
        this.mSessionview = mSessionview;
        this.montext = montext;
    }

    public void callSessionnData(String session_id) {
        if (session_id != null && !session_id.isEmpty())
            new CallSessionData(session_id);
    }

    public void submitSessionAlert(SessionDetails mDeatils, final List<String> attendance_present, final String session_id, final SessionDetails.Feedback topic, final String feedback) {

        AlertDialog.Builder builder = new AlertDialog.Builder(montext);

        LayoutInflater inflater = LayoutInflater.from(montext);
        View dialogView = inflater.inflate(R.layout.alert_session_submitdetails, null);

        builder.setView(dialogView);
        builder.setCancelable(false);

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        final ImageView img_cancel = (ImageView) dialogView.findViewById(R.id.cancel_dialog);
        TextView teacher_name = (TextView) dialogView.findViewById(R.id.teacher_name);
        TextView session_topic = (TextView) dialogView.findViewById(R.id.session_topic);
        TextView session_comment = (TextView) dialogView.findViewById(R.id.session_comment);
        TextView session_present = (TextView) dialogView.findViewById(R.id.session_present);
        TextView session_total = (TextView) dialogView.findViewById(R.id.session_total);
        TextView session_abscent = (TextView) dialogView.findViewById(R.id.session_abscent);
        Button completed_btn = (Button) dialogView.findViewById(R.id.completed_btn);
        Button cancelled_btn = (Button) dialogView.findViewById(R.id.cancelled_btn);
        final Spinner cancel_reason = (Spinner) dialogView.findViewById(R.id.cancel_reason);

        final LinearLayout session_attend_lay = (LinearLayout) dialogView.findViewById(R.id.session_attend_lay);
        final LinearLayout reason_lay = (LinearLayout) dialogView.findViewById(R.id.reason_lay);


        teacher_name.setText(mDeatils.teacher);
        session_comment.setText(feedback);
        String toic_title = topic != null ? (topic.title != null ? topic.title : "") : "";
        final String topic_id = topic != null ? (topic.id != null ? topic.id : "") : "";
        session_topic.setText(mDeatils.grade + " , " + mDeatils.subject + "\n" + toic_title);

        session_present.setText("Present\n" + attendance_present.size());
        session_total.setText("Total\n" + mDeatils.session_attandance.length);
        session_abscent.setText("Abscent\n" + (mDeatils.session_attandance.length - attendance_present.size()));

        final HashMap<String, String> reason_map = new LinkedHashMap<>();
        final List<String> reasons_list_values = new ArrayList<>();
        final List<String> reasons_list_keys = new ArrayList<>();
        if (mDeatils.cancel_reasons != null) {
            reason_map.putAll(mDeatils.cancel_reasons);

        } else {
            reason_map.put("Internet Down School", "Internet is Down in school");
            reason_map.put("Power Cut School", "Power Cut in school");
            reason_map.put("Unscheduled leave School", "Unscheduled Leave at school");
            reason_map.put("Internet down Teacher", "Internet Down at teachers side");
            reason_map.put("Power Cut Teacher", "Power cut at teacher side");
            reason_map.put("Last Minute Dropout Teacher", "Last Minute Dropout Teacher");
            reason_map.put("Communication Issue", "Communication Issue");
            reason_map.put("Teacher yet to be backfilled", "Teacher yet to be backfilled");
            reason_map.put("Others", "Others");
        }

        reasons_list_values.addAll(reason_map.values());
        reasons_list_keys.addAll(reason_map.keySet());

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(montext, android.R.layout.simple_spinner_dropdown_item, reasons_list_values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cancel_reason.setAdapter(adapter);

        final String[] sel_reason = new String[1];
        cancel_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sel_reason[0] = reasons_list_keys.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sel_reason[0] = reasons_list_keys.get(0);
            }
        });

        completed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SessionDataSubmit(session_id, topic_id, feedback, attendance_present, Constants.COMPLETED, "");

            }
        });

        cancelled_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (reason_lay.getVisibility() == View.VISIBLE) {
                    String reason = sel_reason[0];
                    if (reason == null && reason.isEmpty()) {
                        reason = reasons_list_keys.get(0);
                    }

                    new SessionDataSubmit(session_id, topic_id, feedback, attendance_present, Constants.CANCELLED, reason);
                    dialog.dismiss();
                } else {
                    reason_lay.setVisibility(View.VISIBLE);
                    session_attend_lay.setVisibility(View.GONE);
                }
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

    public void submitSessionDetails() {

    }

    public void getPresentedList(com.headrun.evidyaloka.model.SessionDetails.Attandance[] mAttendancelsit) {
        if (mAttendancelsit != null) {
            Constants.ATTENDANCE_LIST.clear();
            for (int i = 0; i < mAttendancelsit.length; i++) {
                SessionDetails.Attandance item = mAttendancelsit[i];
                boolean check = item.is_present.toLowerCase().equals("yes") ? true : false;
                if (check && item.id != null && !Constants.ATTENDANCE_LIST.contains(item.id))
                    Constants.ATTENDANCE_LIST.add(item.id);
            }
        }

    }

    public class CallSessionData implements ResponseListener<SessionDetailsResponse> {

        public String id;

        public CallSessionData(String id) {
            mSessionview.showProcessingBar();
            new EVDNetowrkServices().sessionDeatils(montext, this, id);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            mSessionview.hideProcessingBar();
            mSessionview.showNetworkError(error);
        }

        @Override
        public void onResponse(SessionDetailsResponse response) {
            mSessionview.hideProcessingBar();
            if (response != null) {

                if (response.status != null && response.status.equals("0")) {
                    mSessionview.hideNetworkError();
                    mSessionview.setSessionData(response.data);
                }
            }
        }
    }

    public class SessionDataSubmit implements ResponseListener<ChangeSessionStatus> {

        public SessionDataSubmit(String session_id, String topic_id, String comment, List<String> data, String status, String reason) {

            mSessionview.showProcessingBar();
            HashMap<String, String> params = new HashMap<>();

            params.put("topic_id", topic_id);
            params.put("session_id", session_id);
            params.put("status", status);
            if (status.contains(Constants.COMPLETED)) {
                params.put("comment", comment);
                params.put("attended_students", data.toString());
            } else {
                params.put("comment", "");
                params.put("attended_students", "");
                params.put("reason", reason);
            }
            new EVDNetowrkServices().sessionStatusChange(montext, this, params);

        }

        @Override
        public void onErrorResponse(VolleyError error) {

            mSessionview.hideProcessingBar();
            Toast.makeText(montext, "failed the update session", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onResponse(ChangeSessionStatus response) {

            mSessionview.hideProcessingBar();

            if (response != null) {

                if (response.status != null && response.status.toLowerCase().equals("success")) {
                    mSessionview.movetoSessionScreen();

                } else {
                    mSessionview.movetoSessionScreen();
                    Toast.makeText(montext, response.status != null ? response.status : "get an error updating the session details", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
