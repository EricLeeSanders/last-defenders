package com.lastdefenders.android;

import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget ;

/**
 * Created by Eric on 6/27/2018.
 */

public class LoadingView {

    private RelativeLayout layout;
    private ImageView loadingView;
    private ImageView bgView;
    private boolean loading;

    public LoadingView(RelativeLayout layout, AndroidLauncher androidLauncher){
        this.layout = layout;
        createLoadingView(androidLauncher);
    }

    private void createLoadingView(AndroidLauncher androidLauncher){

        bgView = new ImageView(androidLauncher);
        bgView.setImageResource(R.drawable.loading_bg);
        bgView.setScaleType(ScaleType.CENTER_CROP);

        RelativeLayout.LayoutParams bgParams = new RelativeLayout.LayoutParams(
            LayoutParams.FILL_PARENT,
            LayoutParams.FILL_PARENT);
        bgParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        bgView.setLayoutParams(bgParams);

        loadingView = new ImageView(androidLauncher);
        loadingView.setImageResource(R.drawable.loading);
        DrawableImageViewTarget  imageViewTarget = new DrawableImageViewTarget (loadingView);
        Glide.with(androidLauncher).load(R.drawable.loading).into(imageViewTarget);

        RelativeLayout.LayoutParams loadingViewParams = new RelativeLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT);
        loadingViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        loadingView.setLayoutParams(loadingViewParams);

    }

    public void showLoadingView(){
        if(!loading) {
            loading = true;
            layout.addView(bgView);
            layout.addView(loadingView);
        }
    }

    public void hideLoadingView(){
        if(loading) {
            loading = false;
            layout.removeView(bgView);
            layout.removeView(loadingView);
        }
    }
}
