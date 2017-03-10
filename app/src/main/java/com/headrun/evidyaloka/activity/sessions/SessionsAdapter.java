package com.headrun.evidyaloka.activity.sessions;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.model.Sessions;
import com.headrun.evidyaloka.utils.ImageLoadingUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.fiskur.chipcloud.ChipCloud;

import static com.headrun.evidyaloka.R.id.session_time;

/**
 * Created by sujith on 10/1/17.
 */

public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.SessionViewHolder> {

    public interface Callbacks {
        public void onSessionClick(int pos, Sessions mSession);

        public void onStatusClick(int pos, Sessions mSession);
    }

    private String TAG = SessionsAdapter.class.getSimpleName();
    private SessionsAdapter.Callbacks mCallbacks;
    private Context context;
    private List<Sessions> mFeedList;
    private String Session_type;

    public SessionsAdapter(List<Sessions> feedList, String Session_type, Context context) {
        this.mFeedList = feedList;
        this.context = context;
        this.Session_type = Session_type;
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = View.inflate(parent.getContext(), R.layout.item_session, null);
        //view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SessionViewHolder holder, final int position) {

        final Sessions mSession = mFeedList.get(position);


        try {
            if (mSession.center_image != null && !mSession.center_image.isEmpty())
                ImageLoadingUtils.load(holder.session_center_img, mSession.center_image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.session_center.setText(mSession.center);
        holder.session_subject.setText(mSession.subject);
        holder.session_topic.setText(mSession.topic);

        String session_type = "";
        if (mSession.changed_status != null && !mSession.changed_status.isEmpty())
            session_type = mSession.changed_status;
        else
            session_type = Session_type;

        holder.session_status.setText(session_type);
        holder.session_status.setTextColor(set_statusColor(session_type.toLowerCase()));

        if (mSession.date_start != null && !mSession.date_start.isEmpty()) {

            try {

                SimpleDateFormat session_date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date date = session_date.parse(mSession.date_start);
                Date end_date = session_date.parse(mSession.date_end);
                String mnth = new SimpleDateFormat("MMM").format(date.getTime());
                String parse_date = new SimpleDateFormat("dd").format(date.getTime());
                String time = new SimpleDateFormat("hh:mm a").format(date.getTime()) + " - " +
                        new SimpleDateFormat("hh:mm a").format(end_date.getTime());

                String[] sess_time = new String[]{time};
                holder.session_mnth.setText(mnth);
                holder.session_date.setText(parse_date);
                //holder.session_time.setText(time);

                /*new ChipCloud.Configure()
                        .chipCloud(holder.session_time)
                        .labels(sess_time);*/


                holder.session_time.removeAllViews();
                holder.session_time.addChip(time);

            } catch (Exception e) {
                holder.time_lay.setVisibility(View.GONE);
                e.printStackTrace();
            }

        } else {
            holder.time_lay.setVisibility(View.GONE);
        }

        if (Session_type.toLowerCase().contains("prefered")) {

            StringBuffer session_times = new StringBuffer();
            if (mSession.start_time != null)
                session_times.append(mSession.start_time);
            if (mSession.end_time != null)
                session_times.append(" - " + mSession.end_time);

            holder.session_time.removeAllViews();
            holder.session_time.addChip(session_times.toString());
            holder.session_topic.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCallbacks != null && !Session_type.toLowerCase().contains("prefered")) {
                    mCallbacks.onSessionClick(position, mSession);
                }
            }
        });

        if (Session_type.toLowerCase().contains("scheduled") || Session_type.toLowerCase().contains("teaching")) {

            // holder.session_status.setVisibility(View.GONE);
            holder.update_status_btn.setVisibility(View.VISIBLE);
            // holder.update_status_btn.setText(mSession.status);
            holder.update_status_btn.setBackgroundColor(set_statusColor(Session_type));
            holder.update_status_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mCallbacks != null) {
                        mCallbacks.onStatusClick(position, mSession);
                    }
                }
            });
        } else {
            holder.update_status_btn.setVisibility(View.GONE);
        }

    }

    private int set_statusColor(String status) {

        if (status.contains("scheduled"))
            return ContextCompat.getColor(context, R.color.teach_now);
        else if (status.contains("started") || status.contains("completed"))
            return ContextCompat.getColor(context, R.color.button_color);
        else if (status.contains("cancelled"))
            return ContextCompat.getColor(context, R.color.cancelled);

        return ContextCompat.getColor(context, R.color.button_color);

    }

    @Override
    public int getItemCount() {
        return (mFeedList != null ? mFeedList.size() : 0);
    }

    public void setCallbacks(SessionsAdapter.Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    public class SessionViewHolder extends RecyclerView.ViewHolder {

        public TextView session_subject, session_topic, session_center, session_mnth, session_date, session_status;
        public SimpleDraweeView session_center_img;
        public Button update_status_btn;
        public LinearLayout time_lay;
        public eu.fiskur.chipcloud.ChipCloud session_time;

        public SessionViewHolder(View itemView) {
            super(itemView);
            //session_time = (TextView) itemView.findViewById(R.id.session_time);
            session_mnth = (TextView) itemView.findViewById(R.id.session_mnth);
            session_date = (TextView) itemView.findViewById(R.id.session_date);
            session_subject = (TextView) itemView.findViewById(R.id.session_subject);
            session_topic = (TextView) itemView.findViewById(R.id.session_topic);
            session_center = (TextView) itemView.findViewById(R.id.session_center);
            session_center_img = (SimpleDraweeView) itemView.findViewById(R.id.session_center_img);
            session_status = (TextView) itemView.findViewById(R.id.session_status);
            update_status_btn = (Button) itemView.findViewById(R.id.update_status);
            time_lay = (LinearLayout) itemView.findViewById(R.id.time_lay);
            session_time = (eu.fiskur.chipcloud.ChipCloud) itemView.findViewById(R.id.session_time);

        }
    }

    public void updateListItem(int pos, Sessions mSession) {
        notifyItemChanged(pos, mSession);
    }
}

