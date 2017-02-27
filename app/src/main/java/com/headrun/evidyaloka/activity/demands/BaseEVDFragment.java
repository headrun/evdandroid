package com.headrun.evidyaloka.activity.demands;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.headrun.evidyaloka.activity.base.HomeActivity;
import com.headrun.evidyaloka.utils.NetworkUtils;
import com.headrun.evidyaloka.utils.UserSession;

/**
 * Created by HP-HP on 01-04-2016.
 */
public class BaseEVDFragment extends Fragment {

    MaterialDialog materialDialog;
    UserSession userSession;


    public void showProgressDialog() {
        materialDialog = new MaterialDialog.Builder(getActivity())
                .content("Please Wait")
                .cancelable(false)
                .progress(true, 0)
                .show();
    }


    public void hideProgressDialog() {
        if (materialDialog != null)
            materialDialog.dismiss();
    }

    public boolean isInternetAvailable() {
        return NetworkUtils.isNetworkConnected(getActivity());
    }

    public void showSnackBar(String value) {
        ((HomeActivity) getActivity()).showSnackBar(value);
    }

    public void showSnackBar(int value) {
        ((HomeActivity) getActivity()).showSnackBar(value);
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return ((HomeActivity) getActivity()).getCoordinatorLayout();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (materialDialog != null) {
            materialDialog.dismiss();
        }
    }


}
