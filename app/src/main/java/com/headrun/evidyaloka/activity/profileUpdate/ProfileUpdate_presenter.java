package com.headrun.evidyaloka.activity.profileUpdate;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.model.LocData;
import com.headrun.evidyaloka.model.LoginResponse;
import com.headrun.evidyaloka.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sujith on 2/3/17.
 */

public class ProfileUpdate_presenter implements ResponseListener<LocData> {

    public String TAG = ProfileUpdate_presenter.class.getSimpleName();

    ProfileUpdateView mProfileUpdateView;
    Context mContext;
    String type;
    Utils utils;

    public ProfileUpdate_presenter(ProfileUpdateView mProfileUpdateView, Context mContext) {
        this.mProfileUpdateView = mProfileUpdateView;
        this.mContext = mContext;
        utils = new Utils(mContext);
        CallUserData();
        // getLocationData(Constants.COUNTRY, "");
    }

    public void CallUserData() {
        new GetUserData();
    }

    public void getDialog(String required_type) {

        List<String> list_data = new ArrayList<>();
        if (required_type.contains(Constants.GENDER)) {
            list_data.addAll(Arrays.asList(mContext.getResources().getStringArray(R.array.gender_list)));
            alertDialog(list_data, required_type);
        } else if (required_type.contains(Constants.PROFESSION)) {
            list_data.addAll(Arrays.asList(mContext.getResources().getStringArray(R.array.profession_list)));
            alertDialog(list_data, required_type);

        } else if (required_type.contains(Constants.MEDIUM)) {
            list_data.addAll(Arrays.asList(mContext.getResources().getStringArray(R.array.medium_list)));
            alertDialog(list_data, required_type);

        } else if (required_type.contains(Constants.CHANNEL)) {
            list_data.addAll(Arrays.asList(mContext.getResources().getStringArray(R.array.reference_channel)));
            alertDialog(list_data, required_type);

        } else if (required_type.contains(Constants.ROLE)) {

            list_data.addAll(utils.userSession.getUserRoles().values());

            alertDialog(list_data, required_type);

        }/* else if (required_type.contains(Constants.COUNTRY)) {
            if (Constants.COUNTRY_MAP.size() > 0)
                alertDialog(new ArrayList<String>(Constants.COUNTRY_MAP.values()), Constants.COUNTRY);
            else
                getLocationData(Constants.COUNTRY, "");

        } else if (required_type.contains(Constants.STATE)) {
            String country = mProfileUpdateView.getCountry();
            String county_id = getHasshKey(Constants.COUNTRY_MAP, country);
            if (county_id != null) {
                getLocationData(Constants.STATE, county_id);
            }

        } else if (required_type.contains(Constants.CITY)) {
            String state = mProfileUpdateView.getState();
            String state_id = getHasshKey(Constants.STATE_MAP, state);
            if (state_id != null) {
                getLocationData(Constants.CITY, state_id);
            }*/

    }

    public void getLocationData(String type, String id) {
        this.type = type;
        new EVDNetowrkServices().getLocationsData(mContext, this, type, id);
    }

    private void alertDialog(final List<String> mList, final String type) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialogView = inflater.inflate(R.layout.profile_update_dialog, null);

        builder.setView(dialogView);
        builder.setCancelable(true);
        final List<String> sel_roles_list = new ArrayList<>();

        TextView dialog_title = (TextView) dialogView.findViewById(R.id.dialog_title);
        final EditText auto_dilaog_list = (EditText) dialogView.findViewById(R.id.auto_dialog_list);
        final ListView dialog_list = (ListView) dialogView.findViewById(R.id.dialog_list);
        ArrayAdapter<String> itemsAdapter = null;

        if (!type.contains(Constants.ROLE)) {
            if (mList != null)
                itemsAdapter =
                        new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mList);
            else
                itemsAdapter =
                        new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, new ArrayList<String>());
        } else if (mList != null) {
            itemsAdapter =
                    new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_multiple_choice, mList);
        }

        if (!type.contains(Constants.COUNTRY) && !type.contains(Constants.STATE) && !type.contains(Constants.CITY))
            auto_dilaog_list.setVisibility(View.GONE);
        else
            auto_dilaog_list.setVisibility(View.VISIBLE);

        dialog_list.setAdapter(itemsAdapter);
        dialog_title.setText(type);

        int user_role_size = 0;
        try {
            user_role_size = utils.userSession.getUserData().data.roles.length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (type.contains(Constants.ROLE)) {

            if (user_role_size > 1)
                dialog_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            else
                dialog_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (sel_roles_list.size() >= 0) {
                        mProfileUpdateView.updateRoles(sel_roles_list);
                        mProfileUpdateView.setRole(sel_roles_list);

                    }
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
        }

        List<String> data = mProfileUpdateView.get_Role();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                String item = data.get(i).trim();
                if (mList.indexOf(item) != -1)
                    dialog_list.setItemChecked(mList.indexOf(item), true);
            }
        }

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (Constants.ROLE.contains(type)) {
                    SparseBooleanArray clickedItemPositions = dialog_list.getCheckedItemPositions();
                    sel_roles_list.clear();
                    for (int index = 0; index < clickedItemPositions.size(); index++) {
                        boolean checked = clickedItemPositions.valueAt(index);

                        if (checked) {
                            int key = clickedItemPositions.keyAt(index);
                            if (key != -1) {
                                String item = (String) dialog_list.getItemAtPosition(key);
                                sel_roles_list.add(item);
                            }
                        }
                    }
                } else {
                    dialog.dismiss();
                    Log.i(TAG, "sel item is " + parent.getAdapter().getItem(position));
                    String sel_val = (String) parent.getAdapter().getItem(position);
                    if (sel_val != null)
                        if (Constants.GENDER.contains(type))
                            mProfileUpdateView.setGender(sel_val);
                        else if (Constants.PROFESSION.contains(type))
                            mProfileUpdateView.setProfession(sel_val);
                        else if (Constants.MEDIUM.contains(type))
                            mProfileUpdateView.setPreferredMedium(sel_val);
                        else if (Constants.CHANNEL.contains(type))
                            mProfileUpdateView.setReferenceChannel(sel_val);
                        else if (Constants.COUNTRY.contains(type))
                            mProfileUpdateView.setCounty(sel_val);
                        else if (Constants.STATE.contains(type))
                            mProfileUpdateView.setState(sel_val);
                        else if (Constants.CITY.contains(type))
                            mProfileUpdateView.setCity(sel_val);
                }
            }
        });

        /*auto_dilaog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                dialog.dismiss();
                Log.i(TAG, "sel item is " + mList.get(position));
                String sel_val = mList.get(position);
                if (sel_val != null)
                    if (Constants.COUNTRY.contains(type))
                        mProfileUpdateView.setCounty(sel_val);
                    else if (Constants.STATE.contains(type))
                        mProfileUpdateView.setState(sel_val);
                    else if (Constants.CITY.contains(type))
                        mProfileUpdateView.setCity(sel_val);

            }
        });
*/
        final ArrayAdapter<String> finalItemsAdapter = itemsAdapter;
        auto_dilaog_list.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                finalItemsAdapter.getFilter().filter(cs.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        dialog.show();

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(LocData response) {

        if (response.loc_data != null) {

            if (Constants.COUNTRY.contains(type)) {
                Constants.COUNTRY_MAP.clear();
                Constants.COUNTRY_MAP = response.loc_data;
                alertDialog(new ArrayList<String>(Constants.COUNTRY_MAP.values()), type);
            } else if (Constants.STATE.contains(type)) {
                Constants.STATE_MAP.clear();
                Constants.STATE_MAP = response.loc_data;
                alertDialog(new ArrayList<String>(Constants.STATE_MAP.values()), type);
            } else if (Constants.CITY.contains(type)) {
                Constants.CITY_MAP.clear();
                Constants.CITY_MAP = response.loc_data;
                alertDialog(new ArrayList<String>(Constants.CITY_MAP.values()), type);
            }
        }
    }

    public String getHasshKey(HashMap<String, String> map, String value) {

        for (Map.Entry m : map.entrySet())
            if (m.getValue().toString().toLowerCase().equalsIgnoreCase(value.toLowerCase()))
                return m.getKey().toString();

        return null;
    }

    public void validatecheck() {
        boolean check = false;

        String fname = mProfileUpdateView.getFirst_name();
        String lname = mProfileUpdateView.getLast_name();
        String gender = mProfileUpdateView.get_gender();
        String age = mProfileUpdateView.get_age();
        String email = mProfileUpdateView.get_email();
        String sec_email = mProfileUpdateView.get_sec_email();
        String skype_id = mProfileUpdateView.get_skype_id();
        String ph_no = mProfileUpdateView.get_ph_no();
        String profession = mProfileUpdateView.get_preofession();
        String preferred_medium = mProfileUpdateView.get_preferred_medium();
        String referrence_channel = mProfileUpdateView.get_reference_channel();
        String referrer = mProfileUpdateView.get_referrer();
        String country = mProfileUpdateView.getCountry();
        String state = mProfileUpdateView.getState();
        String city = mProfileUpdateView.getCity();
        String brieff_intro = mProfileUpdateView.get_brief_intro();
        List<String> role = mProfileUpdateView.get_Role();

        /*if (fname.isEmpty() || gender.isEmpty() || age.isEmpty() || email.isEmpty() || preferred_medium.isEmpty())
            mProfileUpdateView.showError(mContext.getResources().getString(R.string.missing_fields));
        else if ((!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()))
            mProfileUpdateView.showError(mContext.getResources().getString(R.string.email_error));
        else
            mProfileUpdateView.submitUserPofile();*/


        if (fname.isEmpty())
            mProfileUpdateView.showError(Constants.EDIT_FNAME, mContext.getResources().getString(R.string.missing_fields));
        else if (gender.isEmpty())
            mProfileUpdateView.showError(Constants.EDIT_GENDER, mContext.getResources().getString(R.string.missing_fields));
        else if (email.isEmpty())
            mProfileUpdateView.showError(Constants.EDIT_EMAIL, mContext.getResources().getString(R.string.missing_fields));
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            mProfileUpdateView.showError(Constants.EDIT_EMAIL, mContext.getResources().getString(R.string.missing_fields));
        else if (preferred_medium.isEmpty())
            mProfileUpdateView.showError(Constants.EDIT_MEDIUM, mContext.getResources().getString(R.string.missing_fields));
        else if (role.size() <= 0)
            mProfileUpdateView.showError(Constants.EDIT_ROLE, mContext.getResources().getString(R.string.missing_fields));
        else
            mProfileUpdateView.submitUserPofile();

    }

    public class GetUserData implements ResponseListener<LoginResponse> {

        public GetUserData() {
            mProfileUpdateView.showProgressBar();
            new EVDNetowrkServices().getUserData(mContext, this);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            mProfileUpdateView.hideProgressBar();
            Toast.makeText(mContext, "Seems to be an issue getting the user data", Toast.LENGTH_LONG).show();
            mProfileUpdateView.setFieldsData();
        }

        @Override
        public void onResponse(LoginResponse response) {
            mProfileUpdateView.hideProgressBar();
            if (response != null) {
                Log.i(TAG, "login sucess");
                if (response.status == 0) {
                    utils.userSession.setUserData(response);
                    mProfileUpdateView.setFieldsData();
                } else {
                    utils.showAlertDialog(response.message);
                }
            }
        }
    }
}
