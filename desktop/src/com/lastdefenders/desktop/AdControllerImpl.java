package com.lastdefenders.desktop;

import com.lastdefenders.ads.AdController;

/**
 * Created by Eric on 8/6/2018.
 */

public class AdControllerImpl implements AdController {

    @Override
    public void showInterstitialAd() {
        // Do nothing, ads are not enabled on desktop.
    }

    @Override
    public boolean adsEnabled() {

        return false;
    }

    @Override
    public boolean adReady() {

        return false;
    }

}
