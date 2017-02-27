package com.headrun.evidyaloka.activity.demand_details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.model.SchoolDetails;
import com.headrun.evidyaloka.utils.ImageLoadingUtils;

/**
 * Created by sujith on 19/1/17.
 */

public class TeacharsPhotoAdapter extends RecyclerView.Adapter<TeacharsPhotoAdapter.ViewHolder> {

    public String TAG = TeacharsPhotoAdapter.class.getSimpleName();
    private Context mcontext;
    private SchoolDetails.Profile[] jarray;

    public TeacharsPhotoAdapter(Context mcontext, SchoolDetails.Profile[] jarray) {
        this.mcontext = mcontext;
        this.jarray = jarray;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mcontext = parent.getContext();
        View view = View.inflate(parent.getContext(), R.layout.teacher_photo_lay, null);
        return new TeacharsPhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (jarray[position].image != null && !jarray[position].image.isEmpty())
            ImageLoadingUtils.load(holder.image_view, jarray[position].image);
        Log.i(TAG, jarray[position].name + "img is " + jarray[position].image);
        holder.txt_name.setText(jarray[position].name);

    }

    @Override
    public int getItemCount() {
        return jarray.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView image_view;
        TextView txt_name;

        public ViewHolder(View itemView) {
            super(itemView);

            image_view = (SimpleDraweeView) itemView.findViewById(R.id.img);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
        }
    }
}
