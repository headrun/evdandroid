package com.headrun.evidyaloka.activity.base;

/**
 * Created by sujith on 22/3/17.
 */

public interface HomeView {

    public void enableSession();

    public void disableSession();

    public void setGalleryImages();

    public void setDemands();

    public void LoginCheck();

    public void showGallery();

    public void hideGallery();

    public void inflateVideoPlayer(String videoKey);
}
