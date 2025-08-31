package com.lastdefenders.android;

import androidx.annotation.NonNull;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.lastdefenders.ads.AdController;
import com.lastdefenders.game.BuildConfig;
import com.lastdefenders.util.Logger;

/**
 * Created by Eric on 8/5/2018.
 */

public class GoogleAdsControllerImpl implements AdController {

    private AndroidLauncher androidLauncher;
    private InterstitialAd mInterstitialAd;

    public void initialize(AndroidLauncher androidLauncher) {

        this.androidLauncher = androidLauncher;
        Logger.info("GoogleAdsControllerImpl - Initializing");
        MobileAds.initialize(androidLauncher, initializationStatus -> {
            Logger.info("GoogleAdsControllerImpl - Initialization Complete");
            loadInterstitialAd();
        });
    }

    private void loadInterstitialAd(){

        Logger.info("GoogleAdsControllerImpl - Loading Ad");

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(androidLauncher, BuildConfig.ADMOB_AD_UNIT_ID, adRequest, new InterstitialAdLoadCallback() {

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                mInterstitialAd = interstitialAd;

                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        mInterstitialAd = null;
                        Logger.info("The ad was dismissed");
                        loadInterstitialAd();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        mInterstitialAd = null;
                        Logger.error("The ad failed to show: " + adError.getMessage());
                        loadInterstitialAd();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        Logger.info("The ad was shown");
                    }
                });

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Logger.error("Ad failed to load: " + loadAdError);
                Logger.error("Ad failed to load: " + loadAdError.getResponseInfo());
                mInterstitialAd = null;
            }
        });
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
                }
            }
        });
    }

    @Override
    public boolean adsEnabled() {

        return true;
    }

    @Override
    public boolean adReady() {

        return mInterstitialAd != null;
    }



}
