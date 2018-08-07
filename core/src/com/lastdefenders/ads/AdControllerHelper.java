package com.lastdefenders.ads;

import com.lastdefenders.util.Logger;

/**
 * Created by Eric on 8/6/2018.
 */

public class AdControllerHelper {

    // Show the ad after every five occurrences
    private static final int DEFAULT_NUM_OF_EVENT_OCCURRENCES_REQUIRED = 5;

    private AdController adController;
    private int numOfEventOccurrencesRequired;
    private int eventTriggeredCounter = 0;

    public AdControllerHelper(AdController adController){
        this(adController, DEFAULT_NUM_OF_EVENT_OCCURRENCES_REQUIRED);
    }

    public AdControllerHelper(AdController adController, int numOfEventOccurrencesRequired){
        this.adController = adController;
        this.numOfEventOccurrencesRequired = numOfEventOccurrencesRequired;
    }

    /**
     * Increments the number of times an event has been triggered.
     * When the number of given occurrences required to show the ad is reached, the ad is shown.
     */
    public void incrementEventTriggered(){

        Logger.info("AdControllerHelper: incrementing the number of times event triggered");
        eventTriggeredCounter++;
        if(eventTriggeredCounter % numOfEventOccurrencesRequired == 0){
            showAd();
        }
    }

    private void showAd(){
        Logger.info("AdControllerHelper: showing ad");
        adController.showInterstitialAd();
    }

}
