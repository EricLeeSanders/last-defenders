package com.lastdefenders.android;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.lastdefenders.ads.AdController;
import com.lastdefenders.util.Logger;

/**
 * Created by Eric on 8/5/2018.
 */

public class GoogleAdsControllerImpl implements AdController {

    private AndroidLauncher androidLauncher;
    private InterstitialAd interstitialAd;

    public void initialize(AndroidLauncher androidLauncher){

        this.androidLauncher = androidLauncher;

        MobileAds.initialize(androidLauncher, BuildConfig.ADMOB_APP_ID);

        interstitialAd = new InterstitialAd(androidLauncher);
        interstitialAd.setAdUnitId(BuildConfig.ADMOB_AD_UNIT_ID);
        interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
    }
    @Override
    public void showInterstitialAd() {
        androidLauncher.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (interstitialAd.isLoaded()) {
                    Logger.info("GoogleAdsControllerImpl showInterstitialAd: Showing ad.");
                    interstitialAd.show();
                } else {
                    Logger.info("GoogleAdsControllerImpl showInterstitialAd: Not loaded.");
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                }
            }
        });
    }
}
