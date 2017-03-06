package com.headrun.evidyaloka.config;

import android.os.Parcelable;

import com.headrun.evidyaloka.dto.FiltersDataResponse;
import com.headrun.evidyaloka.event.SlotConfirmEvent;
import com.headrun.evidyaloka.model.Demand;
import com.headrun.evidyaloka.model.FiltersData;
import com.headrun.evidyaloka.model.SchoolDetails;
import com.headrun.evidyaloka.model.Sessions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sujith on 21/2/17.
 */

public class Constants {


    public static String TYPE = "redirect_type";
    public static String ID = "id";
    public static String REDIRECT_TO = "redirect_to";
    public static String SLOTS_TYPE = "slots";
    public static String HOME_TYPE = "home_page";
    public static String GENDER = "gender";
    public static Parcelable DEMAND_SLOTS_STATE;
    public static boolean PAGE_REFRESH = false;
    public static LinkedHashMap<String, Demand> LIST_DEMANDS = new LinkedHashMap<>();
    public static LinkedHashMap<String, List<Sessions>> LIST_SESSIONS = new LinkedHashMap<>();
    public static FiltersDataResponse FILTERDATA;
    public static SlotConfirmEvent SEL_SLOTDATA;

    public static LinkedHashMap<String, String> COUNTRY_MAP = new LinkedHashMap<>();
    public static LinkedHashMap<String, String> STATE_MAP = new LinkedHashMap<>();
    public static LinkedHashMap<String, String> CITY_MAP = new LinkedHashMap<>();


    public static String COUNTRY = "country";
    public static String STATE = "state";
    public static String CITY = "city";
    public static String PROFESSION = "profession";
    public static String MEDIUM = "medium";
    public static String CHANNEL = "channel";
    public static String ROLE = "role";
}
