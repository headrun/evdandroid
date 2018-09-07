package com.headrun.evidyaloka.activity.homePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.demands.BaseEVDFragment;

public class InfluencerFragment extends BaseEVDFragment implements View.OnClickListener {

    private Button enroll;

    public static InfluencerFragment newInstance() {
        return new InfluencerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.influencer_fragment, container, false);
        enroll = view.findViewById(R.id.enroll);
        enroll.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.enroll:
                startActivity(new Intent(getActivity(), InfluencerEnrollmentActivity.class));
                break;
            default:
        }
    }
}
