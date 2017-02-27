package com.headrun.evidyaloka.activity.demand_details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.model.SchoolDetails;

/**
 * Created by sujith on 19/1/17.
 */

public class SchoolNewsAdapter extends RecyclerView.Adapter<SchoolNewsAdapter.ViewHolder> {

    public String TAG = SchoolNewsAdapter.class.getSimpleName();
    private Context mcontext;
    private SchoolDetails.ClassDetails[] jarray;

    public SchoolNewsAdapter(Context mcontext, SchoolDetails.ClassDetails[] jarray) {
        this.mcontext = mcontext;
        this.jarray = jarray;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_school_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //holder.txt_scholl_news.setText("Name " + jarray[position].name + "\nDate " + jarray[position].class_date);
        holder.txt_scholl_news.setText(jarray[position].name + " has taken the class on " + jarray[position].class_date);
    }

    @Override
    public int getItemCount() {
        return jarray.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_scholl_news;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_scholl_news = (TextView) itemView.findViewById(R.id.txt_scholl_news);

        }
    }
}
