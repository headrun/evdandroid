package com.headrun.evidyaloka.activity.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.config.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class ImageAdapter extends PagerAdapter {


    interface NavigatePage {
        public void navigateToPage(int position);

        public void navigateToPage(String value);

    }

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private LinkedList<Drawable> mResources = new LinkedList<>();
    private LinkedList<String> mResources_set = new LinkedList<>();
    private LinkedHashMap<String, Drawable> mGallery_img;
    ImageAdapter.NavigatePage mNavigatePage;
    int type;
    int img = 1, str = 2;

    public ImageAdapter(Context context, int type) {
        mContext = context;
        this.type = type;
    }

    @Override
    public int getCount() {
        return mResources.size();
    }

    public void setonclick(ImageAdapter.NavigatePage mNavigatePage) {
        this.mNavigatePage = mNavigatePage;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = mLayoutInflater.inflate(
                R.layout.adapter_images, container, false);


        final ImageView imageView = (ImageView) itemView
                .findViewById(R.id.adapter_image_view);

        final TextView textView = (TextView) itemView
                .findViewById(R.id.adapter_text);

        if (type == img) {
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            imageView.setImageDrawable(mResources.get(position));

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mNavigatePage != null) {
                        mNavigatePage.navigateToPage(mResources_set.get(position));
                    }
                }
            });

        } else {
            textView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            textView.setText("" + mResources_set.get(position));

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNavigatePage.navigateToPage(position);
                }
            });
        }

        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    public void setList(LinkedHashMap<String, Drawable> images) {
        mGallery_img = images;
        setOrderGallery(mGallery_img);
    }

    public void setList(LinkedList<String> values) {
        mResources_set = values;

    }

    private void setOrderGallery(LinkedHashMap<String, Drawable> images) {


        if (images.containsKey(Constants.PROFILE_UPDATE)) {

            mResources.add(images.get(Constants.PROFILE_UPDATE));
            mResources_set.add(Constants.PROFILE_UPDATE);
        }
        if (images.containsKey(Constants.ORIENTAION)) {
            mResources.add(images.get(Constants.ORIENTAION));
            mResources_set.add(Constants.ORIENTAION);
        }
        if (images.containsKey(Constants.SE)) {
            mResources.add(images.get(Constants.SE));
            mResources_set.add(Constants.SE);
        }
        if (images.containsKey(Constants.TSD)) {
            mResources.add(images.get(Constants.TSD));
            mResources_set.add(Constants.TSD);
        }

    }
}
