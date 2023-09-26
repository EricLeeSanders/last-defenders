package com.lastdefenders.ads;

import com.lastdefenders.util.Logger;
import com.lastdefenders.util.UserPreferences;

/**
 * Created by Eric on 8/6/2018.
 */

public class AdControllerHelper {

    // Show the ad after every five occurrences
    private static final int DEFAULT_NUM_OF_EVENT_OCCURRENCES_REQUIRED = 15;

    private AdController adController;
    private UserPreferences userPreferences;
    private int numOfEventOccurrencesRequired;
    private int eventTriggeredCounter = 0;

    public AdControllerHelper(AdController adController, UserPreferences userPreferences){
        this(adController, userPreferences, DEFAULT_NUM_OF_EVENT_OCCURRENCES_REQUIRED);
    }

    public AdControllerHelper(AdController adController, UserPreferences userPreferences, int numOfEventOccurrencesRequired){
        this.adController = adController;
        this.userPreferences = userPreferences;
        this.numOfEventOccurrencesRequired = numOfEventOccurrencesRequired;
    }

    /**
     * Increments the number of times an event has been triggered.
     * When the number of given occurrences required to show the ad is reached, the ad is shown.
     */
    public void incrementEventTriggered(){

        Logger.info("AdControllerHelper: incrementing the number of times event triggered");
        eventTriggeredCounter++;
    }

    public boolean readyToShowAd(){
        return adsEnabled() && adController.adReady() &&
            (eventTriggeredCounter % numOfEventOccurrencesRequired == 0);
    }

    public void showAd(){
        if(adsEnabled()){
            Logger.info("AdControllerHelper: showing ad");
            adController.showInterstitialAd();
        }
    }

    public boolean adsEnabled(){
        return adController.adsEnabled() && !userPreferences.getAdRemovalPurchased();
    }

}
