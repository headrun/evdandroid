package com.headrun.evidyaloka.event;

import com.headrun.evidyaloka.model.Demand;
import com.headrun.evidyaloka.model.SchoolDetails;

/**
 * Created by sujith on 24/1/17.
 */

public class TeachClickEvent {

    public final Demand mdemand;
    public final SchoolDetails mSchoolDeatils;

    public TeachClickEvent(Demand mdemand, SchoolDetails mSchoolDeatils) {
        this.mdemand = mdemand;
        this.mSchoolDeatils = mSchoolDeatils;
    }

    public TeachClickEvent(Demand mdemand) {
        this.mdemand = mdemand;
        mSchoolDeatils = null;
    }

    public TeachClickEvent(SchoolDetails mSchoolDeatils) {
        this.mSchoolDeatils = mSchoolDeatils;
        mdemand = null;
    }

    public Demand getMdemand() {
        return mdemand;
    }

    public SchoolDetails getmSchoolDeatils() {
        return mSchoolDeatils;
    }
}
