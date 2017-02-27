package com.headrun.evidyaloka.event;

import com.headrun.evidyaloka.model.Demand;

/**
 * Created by sujith on 18/1/17.
 */

public class SchoolSelectEvent {

    public final Demand mdemand;

    public Demand getSchoolSelecedEvent() {
        return mdemand;
    }

    public SchoolSelectEvent(Demand mdemand) {
        this.mdemand = mdemand;
    }


}
