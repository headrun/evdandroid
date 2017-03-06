package com.headrun.evidyaloka.activity.demands;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.model.Demand;
import com.headrun.evidyaloka.utils.ImageLoadingUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by sujith on 10/1/17.
 */

public class DemandAdapter extends RecyclerView.Adapter<DemandAdapter.DemandViewHolder> {

    public interface Callbacks {
        public void onSchoolClick(Demand demand);

        public void onTeachClick(Demand demand);
    }

    private String TAG = DemandAdapter.class.getSimpleName();
    private DemandAdapter.Callbacks mCallbacks;
    private Context context;
    private List<Demand> mFeedList;
    private LinkedHashMap<String, Demand> mFeedDemands = new LinkedHashMap<>();

    public DemandAdapter(List<Demand> feedList, Context context) {
        this.mFeedList = feedList;
        this.context = context;
    }

    public DemandAdapter(LinkedHashMap<String, Demand> mFeedDemands, Context context) {
        this.mFeedDemands = mFeedDemands;
        this.context = context;
    }

    @Override
    public DemandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = View.inflate(parent.getContext(), R.layout.item_school, null);
        //view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new DemandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DemandViewHolder holder, int position) {

        final Demand mDemand = getMapDeamnd(position);

        //Log.i("TAG", mDemand.title);

        try {
            // final DemandViewHolder demandViewHolder = (DemandViewHolder) holder;
            if (mDemand.image != null)
                ImageLoadingUtils.load(holder.schoolImage, mDemand.image);

            // Log.i(TAG, mDemand.title + " image is " + mDemand.image);
            holder.schoolTextView.setText(mDemand.title);
            holder.schooldemand.setText(Arrays.toString(mDemand.tags.subjects).replaceAll("\\[|\\]", ""));
            holder.teachers_teaching.setText(mDemand.running_courses + " " + context.getString(R.string.teacher_teaching));
            holder.more_need.setText(mDemand.pending_courses + " " + context.getString(R.string.teacher_need));


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mCallbacks != null) {
                        mCallbacks.onSchoolClick(mDemand);
                    }
                }
            });


            holder.sel_session_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mCallbacks != null) {
                        mCallbacks.onTeachClick(mDemand);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (mFeedDemands != null ? mFeedDemands.size() : 0);
    }

    public void setCallbacks(DemandAdapter.Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    public class DemandViewHolder extends RecyclerView.ViewHolder {

        public TextView schoolTextView, schooldemand, teachers_teaching, more_need;
        public SimpleDraweeView schoolImage;
        public Button sel_session_btn;
        // public ImageButton mFavoriteButton;

        public DemandViewHolder(View itemView) {
            super(itemView);
            schoolTextView = (TextView) itemView.findViewById(R.id.schoolTextView);
            schooldemand = (TextView) itemView.findViewById(R.id.schooldemand);
            teachers_teaching = (TextView) itemView.findViewById(R.id.teachers_teaching);
            more_need = (TextView) itemView.findViewById(R.id.teachers_needed);
            schoolImage = (SimpleDraweeView) itemView.findViewById(R.id.schoolImage);
            sel_session_btn = (Button) itemView.findViewById(R.id.sel_session_btn);
            // mFavoriteButton = (ImageButton) itemView.findViewById(R.id.movie_item_btn_favorite);
        }
    }

    public Demand getMapDeamnd(int pos) {

        return (Demand) mFeedDemands.values().toArray()[pos];

    }

}

