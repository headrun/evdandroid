package com.headrun.evidyaloka.activity.sessions;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.VolleyError;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.demands.BaseEVDFragment;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.dto.SessionResponse;
import com.headrun.evidyaloka.evdservices.ChangeSessionStatusService;
import com.headrun.evidyaloka.model.Sessions;
import com.headrun.evidyaloka.utils.EndlessRecyclerView;
import com.headrun.evidyaloka.utils.GridSpacingItemDecoration;
import com.headrun.evidyaloka.utils.NetworkUtils;
import com.headrun.evidyaloka.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by sujith on 10/2/17.
 */

public class SessionTypeFrament extends BaseEVDFragment implements ResponseListener<SessionResponse>, SessionsAdapter.Callbacks {

    private String TAG = SessionTypeFrament.class.getSimpleName();
    private static String TAG_SESSION_TYPE = "session_type";

    private RecyclerView session_recycler;
    private GridLayoutManager gridLayoutManager;
    private SessionsAdapter mSessionAdaper;
    private List<Sessions> mUserSessionList;
    private String mSessionType;
    private RelativeLayout no_result_lay;
    private SwipeRefreshLayout session_refresh_layout;
    private TextView error_textView;
    private ImageView error_img;
    private int starting_page = 0, next_page_flag = -1;
    private boolean first_cal = false;
    private Utils utils;

    public static SessionTypeFrament newInstance(String sesstion_type) {
        Bundle args = new Bundle();
        args.putString(TAG_SESSION_TYPE, sesstion_type);
        SessionTypeFrament fragment = new SessionTypeFrament();
        fragment.setArguments(args);
        return fragment;
    }

    public SessionTypeFrament() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.session_type_fragment, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {

        super.onDetach();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        utils = new Utils(getActivity());
        getSessionType();

        initViews(view);

        if (Constants.LIST_SESSIONS.get(mSessionType).size() <= 0) {
            getData(starting_page);
        }
    }

    private void getData(int current_page) {
        if (next_page_flag != 0 && mSessionType != null) {
            if (current_page == 0) {
                if (session_refresh_layout.isRefreshing() == false)
                    showProgressDialog();
            }
            new EVDNetowrkServices().getUpComingSessions(getActivity(), this, mSessionType.toLowerCase(), current_page);
        }
    }

    private void initViews(View view) {
        session_recycler = (RecyclerView) view.findViewById(R.id.sessions_recycler);
        no_result_lay = (RelativeLayout) view.findViewById(R.id.no_result_lay);
        error_textView = (TextView) view.findViewById(R.id.textView);
        error_img = (ImageView) view.findViewById(R.id.error_img);
        session_refresh_layout = (SwipeRefreshLayout) view.findViewById(R.id.session_refresh_layout);

        int columnCount = getResources().getInteger(R.integer.movies_columns);
        gridLayoutManager = new GridLayoutManager(getActivity(), columnCount);
        int spacing = Utils.dpToPx(1, getActivity()); // 10px
        boolean includeEdge = false;
        //session_recycler.addItemDecoration(new GridSpacingItemDecoration(columnCount, spacing, includeEdge));
        session_recycler.setLayoutManager(gridLayoutManager);

        if (!Constants.LIST_SESSIONS.containsKey(mSessionType)) {
            Constants.LIST_SESSIONS.put(mSessionType, new ArrayList<Sessions>());
        }

        mSessionAdaper = new SessionsAdapter(Constants.LIST_SESSIONS.get(mSessionType), mSessionType, getActivity());
        mSessionAdaper.setCallbacks(this);
        session_recycler.setAdapter(mSessionAdaper);

        session_recycler.addOnScrollListener(new EndlessRecyclerView(gridLayoutManager) {
            @Override
            public void onLoadMore(int next_page) {

                if (next_page_flag != 0)
                    getData(current_page);


            }
        });


        session_refresh_layout.setColorSchemeColors(
                Color.parseColor("#ff0000"),
                Color.parseColor("#00ff00"),
                Color.parseColor("#0000ff"),
                Color.parseColor("#f234ab"));

        session_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                session_refresh_layout.setRefreshing(true);

                if (NetworkUtils.isNetworkConnected(getActivity())) {
                    next_page_flag = -1;
                    EndlessRecyclerView.current_page = 0;
                    getData(starting_page);
                }

            }
        });

    }

    private void getSessionType() {
        Bundle data = getArguments();
        if (data != null) {
            mSessionType = data.getString(TAG_SESSION_TYPE);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        if (session_refresh_layout.isRefreshing())
            session_refresh_layout.setRefreshing(false);
        else
            hideProgressDialog();
        /*SessionResponse data = new Gson().fromJson(SampleResponseData.Sessions_data, SessionResponse.class);
        processionData(data);*/

        if (error instanceof NetworkError) {
            if (Constants.LIST_SESSIONS.get(mSessionType).size() == 0)
                seterror_display(R.drawable.connection_error, "");
        }
    }

    private void processionData(SessionResponse resposne_data) {


        if (resposne_data != null) {
            next_page_flag = resposne_data.next_page;
            //total_page = resposne_data.total;
            if (resposne_data.results != null) {
                for (Map.Entry<String, List<Sessions>> entry : resposne_data.results.entrySet()) {
                    if (Constants.LIST_SESSIONS.containsKey(entry.getKey())) {
                        Constants.LIST_SESSIONS.get(mSessionType).addAll(resposne_data.results.get(entry.getKey()));
                    }
                }

                mSessionAdaper.notifyDataSetChanged();

            }
        }

        noresult();
    }

    private void noresult() {

        try {
            if (Constants.LIST_SESSIONS.get(mSessionType) != null)
                if (Constants.LIST_SESSIONS.get(mSessionType).size() > 0)
                    no_result_lay.setVisibility(View.GONE);
                else
                    no_result_lay.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(SessionResponse response) {


        if (session_refresh_layout.isRefreshing()) {
            session_refresh_layout.setRefreshing(false);
            Constants.LIST_SESSIONS.get(mSessionType).clear();
        } else {
            hideProgressDialog();
        }

        if (response.status == 0) {
            processionData(response);
        }

    }

    @Override
    public void onSessionClick(int pos, Sessions mSession) {


    }

    @Override
    public void onStatusClick(int pos, Sessions mSession) {

        if (mSessionType.toLowerCase().contains("scheduled"))
            statusChangeDialog(pos, mSession);
        else if (mSessionType.toLowerCase().contains("teaching"))
            sessioChangeAlert(pos, mSession);
    }

    private void sessioChangeAlert(final int pos, final Sessions mSession) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Are you Complete the class ?");

        alertDialogBuilder.setPositiveButton("Completed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                callStauChangeService(pos, mSession, "completed");

            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void callStauChangeService(int pos, Sessions mSession, String Staus) {
        if (new NetworkUtils().isNetworkConnected(getActivity())) {

            utils.userSession.setChangeSessionStatu(mSession.session_id, Staus);
            mSession.changed_status = Staus;
            mSessionAdaper.updateListItem(pos, mSession);
            getActivity().startService(new Intent(getActivity(), ChangeSessionStatusService.class).
                    putExtra("request_type", "session_status"));

        }
    }

    private void seterror_display(int img, String error) {

        if (error.length() > 0)
            error_textView.setText(error);
        else
            error_textView.setText("");

        error_img.setImageResource(img);

        no_result_lay.setVisibility(View.VISIBLE);
    }

    private void statusChangeDialog(final int pos, final Sessions mSession) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.session_scheduled_dialog, null);

        builder.setView(dialogView);
        builder.setCancelable(false);

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);


        TextView school_name = (TextView) dialogView.findViewById(R.id.school_name);
        TextView subject_txt = (TextView) dialogView.findViewById(R.id.subject_txt);
        TextView topic_txt = (TextView) dialogView.findViewById(R.id.topic_txt);
        TextView date_txt = (TextView) dialogView.findViewById(R.id.date_txt);
        Button started_btn = (Button) dialogView.findViewById(R.id.started_btn);
        Button completed_btn = (Button) dialogView.findViewById(R.id.completed_btn);
        Button cancelled_btn = (Button) dialogView.findViewById(R.id.cancelled_btn);
        ImageView cancel_dialog = (ImageView) dialogView.findViewById(R.id.cancel_dialog);


        if (mSession.center != null)
            school_name.setText(mSession.center);

        if (mSession.subject != null)
            subject_txt.setText(mSession.subject);

        if (mSession.topic != null)
            topic_txt.setText(mSession.topic);
/*
        try {
            if (mSession.date != null && !mSession.date.isEmpty()) {
                SimpleDateFormat session_date = new SimpleDateFormat("dd/MM/yyyy");
                Date date = session_date.parse(mSession.date);
                String date_item = new SimpleDateFormat("MMM dd yyy").format(date.getTime());

                date_txt.setText(date_item + " , " + mSession.time);
            }
        } catch (Exception e) {
            date_txt.setText("");
        }*/

        started_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callStauChangeService(pos, mSession, "teaching");
                dialog.dismiss();
            }
        });

        completed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callStauChangeService(pos, mSession, "completed");
                dialog.dismiss();
            }
        });

        cancelled_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callStauChangeService(pos, mSession, "cancelled");
                dialog.dismiss();
            }
        });

        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

}
