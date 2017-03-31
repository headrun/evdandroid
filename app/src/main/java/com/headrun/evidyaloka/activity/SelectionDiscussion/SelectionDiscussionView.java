package com.headrun.evidyaloka.activity.SelectionDiscussion;

import android.provider.CalendarContract;

import com.github.sundeepk.compactcalendarview.domain.Event;
import com.headrun.evidyaloka.model.SelectDiscussionData;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sujith on 28/3/17.
 */

public interface SelectionDiscussionView {


    public void setMnthtitle(Date date);

    public void setEventList(List<Event> event_list);

    public void displaySoltsData(LinkedList<SelectDiscussionData.Selection_Slot_Data> list);

    public void displaySoltsData(Date date, LinkedHashMap<String, String> list);

    public void setBookedSlot(String slot);

    public void hideBookedslot();

    public void ShowBookedSlot();


}
