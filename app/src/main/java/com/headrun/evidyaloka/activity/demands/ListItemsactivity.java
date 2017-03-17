package com.headrun.evidyaloka.activity.demands;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.base.BaseActivity;
import com.headrun.evidyaloka.activity.base.HomeActivity;
import com.headrun.evidyaloka.config.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListItemsactivity extends BaseActivity implements View.OnClickListener {

    public String TAG = ListItemsactivity.class.getSimpleName();
    ListView list_view_list;
    com.facebook.drawee.view.SimpleDraweeView image_view;
    Button apply;
    ArrayAdapter<String> adapter;
    List<String> list_items = new ArrayList<>();
    List<String> sel_items = new ArrayList<>();
    SparseBooleanArray sparseBooleanArray;
    boolean change_filer;
    String sel_data = "language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_itemsactivity);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Log.i(TAG, "sample lsitview");
        change_filer = false;
        list_view_list = (ListView) findViewById(R.id.list_view);
        image_view = (com.facebook.drawee.view.SimpleDraweeView) findViewById(R.id.image_view);
        apply = (Button) findViewById(R.id.apply);

        // image_view.setImageURI(SampleResponseData.image);
        apply.setOnClickListener(this);

        Bundle args = getIntent().getExtras();
        if (args != null) {
            sel_data = args.getString("data");

        }
        changeImage(sel_data);
        setListviewData(sel_data);


        list_view_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SparseBooleanArray clickedItemPositions = list_view_list.getCheckedItemPositions();

                change_filer = true;
                sel_items.clear();
                for (int index = 0; index < clickedItemPositions.size(); index++) {
                    // Get the checked status of the current item
                    boolean checked = clickedItemPositions.valueAt(index);

                    if (checked) {
                        // If the current item is checked
                        int key = clickedItemPositions.keyAt(index);
                        String item = (String) list_view_list.getItemAtPosition(key);
                        sel_items.add(item.trim());
                        Log.i(TAG, item);
                    }
                }
            }

        });

    }

    private void changeImage(String sel_data) {

        if (!sel_data.contains("language")) {
            image_view.getLayoutParams().height = 0;

        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.apply) {
            if (change_filer)
                sel_filter();
            Constants.PAGE_REFRESH = true;
            startActivity(new Intent(ListItemsactivity.this, HomeActivity.class));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {


        }
        return super.onOptionsItemSelected(item);
    }

    public void sel_filter() {
        if (sel_data.contains("language")) {
            userSession.setSelLangFilter(sel_items.toString());

        } else {
            userSession.setSelStateFilter(sel_items.toString());

        }
    }

    public void setListviewData(String data) {
        if (data.contains("language")) {

            getSupportActionBar().setTitle(R.string.filter_lang);
            String lang = userSession.getLangFilter();

            //Log.i(TAG, "language filter size" + items.size() + "val are " + items.toString());
            //list_items = Arrays.asList(getResources().getStringArray(R.array.languages));
            list_items = Arrays.asList(lang.substring(1, lang.length() - 1).replaceAll("\\[|\\]|\\s", "").split(","));
            adapter = new ArrayAdapter<String>(ListItemsactivity.this,
                    android.R.layout.simple_list_item_multiple_choice, list_items);
            list_view_list.setAdapter(adapter);

            String sel_lang = userSession.getSelLangFilter();
            if (sel_lang.length() > 0) {
                List<String> items = Arrays.asList(userSession.getSelLangFilter().substring(1, userSession.getSelLangFilter().length() - 1).replaceAll("\\[|\\]|\\s", "").split(","));
                checkSelectedItems(items);
            }

        } else {
            getSupportActionBar().setTitle(R.string.states_filter);

            String states = userSession.getStateFilter();
            //Log.i(TAG, "states filter " + states);
            list_items = Arrays.asList(states.substring(1, states.length() - 1).replaceAll("\\[|\\]|\\s", "").split(","));
            adapter = new ArrayAdapter<String>(ListItemsactivity.this,
                    android.R.layout.simple_list_item_multiple_choice, list_items);
            list_view_list.setAdapter(adapter);

            String sel_states = userSession.getSelStateFilter();
            if (sel_states.length() > 0) {
                List<String> items = Arrays.asList(sel_states.substring(1, sel_states.length() - 1).replaceAll("\\[|\\]|\\s", "").split(","));
                checkSelectedItems(items);
            }
        }
    }

    private void checkSelectedItems(List<String> items) {

        for (String sel_item : items) {
            int pos = list_items.indexOf(sel_item);
            if (pos != -1)
                list_view_list.setItemChecked(pos, true);
            else
                Log.i(TAG, "list item not exist");
        }
    }
}


