package com.lastdefenders.android;

import android.support.annotation.NonNull;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.lastdefenders.ads.AdController;
import com.lastdefenders.util.Logger;

/**
 * Created by Eric on 8/5/2018.
 */

public class GoogleAdsControllerImpl implements AdController {

    private AndroidLauncher androidLauncher;
    private InterstitialAd mInterstitialAd;

    public void initialize(AndroidLauncher androidLauncher){

        this.androidLauncher = androidLauncher;

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(androidLauncher,BuildConfig.ADMOB_APP_ID, adRequest, new InterstitialAdLoadCallback() {

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Logger.error(loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });

//        MobileAds.initialize(androidLauncher, BuildConfig.ADMOB_APP_ID);
//
//        interstitialAd = new InterstitialAd(androidLauncher);
//        interstitialAd.setAdUnitId(BuildConfig.ADMOB_AD_UNIT_ID);
//        interstitialAd.loadAd(new AdRequest.Builder().build());
//
//        interstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                // Load the next interstitial.
//                interstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//
//        });
    }
    @Override
    public void showInterstitialAd() {
        androidLauncher.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mInterstitialAd != null) {
                    Logger.info("GoogleAdsControllerImpl showInterstitialAd: Showing ad.");
                    mInterstitialAd.show(androidLauncher);
                } else {
                    Logger.info("GoogleAdsControllerImpl showInterstitialAd: Not loaded.");
                    Logger.error("Ad not loaded");
                    //interstitialAd.loadAd(new AdRequest.Builder().build());
                }
            }
        });
    }
}
