package com.headrun.evidyaloka.activity.sessionDetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.config.Constants;

/**
 * Created by sujith on 15/3/17.
 */

class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceHolder> {


    interface AttendanceCallBack {

        public void attendaceCheck(String id);

        public void attendaceUnCheck(String id);
    }

    private String TAG = AttendanceAdapter.class.getSimpleName();
    private com.headrun.evidyaloka.activity.demands.DemandAdapter.Callbacks mCallbacks;
    private Context context;
    private com.headrun.evidyaloka.model.SessionDetails.Attandance[] mAttandance;
    AttendanceAdapter.AttendanceCallBack mAttendanceCallBack;
    private String status;


    public AttendanceAdapter(com.headrun.evidyaloka.model.SessionDetails.Attandance[] mAttandance, Context context, String status) {
        this.mAttandance = mAttandance;
        this.context = context;
        this.status = status;
    }

    @Override
    public AttendanceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = View.inflate(parent.getContext(), R.layout.attendance_adapter, null);
        return new AttendanceHolder(view);
    }

    @Override
    public void onBindViewHolder(final AttendanceHolder holder, final int position) {

        final com.headrun.evidyaloka.model.SessionDetails.Attandance attendance_item = mAttandance[position];

        if (attendance_item != null) {

            holder.check1.setText(attendance_item.name);
            boolean check = attendance_item.is_present.toLowerCase().equals("yes") ? true : false;

            holder.check1.setChecked(check);
            holder.check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        holder.check1.setChecked(true);
                        if (mAttendanceCallBack != null)
                            mAttendanceCallBack.attendaceCheck(attendance_item.id);

                    } else {
                        holder.check1.setChecked(false);
                        if (mAttendanceCallBack != null)
                            mAttendanceCallBack.attendaceUnCheck(attendance_item.id);
                    }
                }
            });

            if (!status.toLowerCase().equals("scheduled")) {
                holder.check1.setEnabled(false);
            }

        }

    }

    @Override
    public int getItemCount() {
        int size = mAttandance != null ? mAttandance.length : 0;
        return size;
    }

    public void setAttendanceCallbacks(AttendanceAdapter.AttendanceCallBack mAttendanceCallBack) {
        this.mAttendanceCallBack = mAttendanceCallBack;
    }

    class AttendanceHolder extends RecyclerView.ViewHolder {


        public CheckBox check1;

        public AttendanceHolder(View itemView) {
            super(itemView);

            check1 = (CheckBox) itemView.findViewById(R.id.check1);

        }
    }
}
