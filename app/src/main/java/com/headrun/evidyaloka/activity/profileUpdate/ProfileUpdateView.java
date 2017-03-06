package com.headrun.evidyaloka.activity.profileUpdate;

import java.util.List;

/**
 * Created by sujith on 1/3/17.
 */

public interface ProfileUpdateView {

    public void setFieldsData();

    public void requiredDialog(String required_type);

    public void setGender(String gender);

    public void setProfession(String profession);

    public void setReferenceChannel(String channel);

    public void setPreferredMedium(String medium);

    public void setCounty(String county);

    public void setCity(String city);

    public void setState(String state);

    public void setRole(List<String> roles);

    public String getCountry();

    public String getState();

    public String getCity();

    public String getFirst_name();

    public String getLast_name();

    public String get_gender();

    public String get_age();

    public String get_email();

    public String get_sec_email();

    public String get_skype_id();

    public String get_ph_no();

    public String get_Role();

    public String get_preofession();

    public String get_preferred_medium();

    public String get_reference_channel();

    public String get_referrer();

    public String get_brief_intro();

    public void showError(String msg);

    public void submitUserPofile();
}
