package com.headrun.evidyaloka.event;

import com.headrun.evidyaloka.model.SchoolDetails;

import java.util.Map;

/**
 * Created by sujith on 25/1/17.
 */
public class SlotConfirmEvent {

    public final String center_id;
    public final String offer_id;
    public final Map<String, SchoolDetails.ClassTimings> slot_ids;

    public SlotConfirmEvent(String center_id, String offer_id, Map<String, SchoolDetails.ClassTimings> slot_ids) {
        this.center_id = center_id;
        this.offer_id = offer_id;
        this.slot_ids = slot_ids;
    }
}
