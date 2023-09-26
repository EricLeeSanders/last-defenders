package com.lastdefenders.game.ui.presenter;

import com.lastdefenders.ads.AdControllerHelper;
import com.lastdefenders.game.model.level.state.LevelStateManager.LevelState;
import com.lastdefenders.game.model.level.state.LevelStateObserver;
import com.lastdefenders.game.ui.view.AdView;
import com.lastdefenders.game.ui.view.interfaces.IAdView;
import com.lastdefenders.sound.LDSound;
import com.lastdefenders.sound.SoundPlayer;
import com.lastdefenders.store.StoreManager;
import com.lastdefenders.store.StoreManager.PurchasableItem;
import com.lastdefenders.store.StoreManagerObserver;

public class AdPresenter implements LevelStateObserver, StoreManagerObserver {

    private AdControllerHelper adControllerHelper;
    private StoreManager storeManager;
    private SoundPlayer soundPlayer;
    private IAdView adView;

    public AdPresenter(AdControllerHelper adControllerHelper, StoreManager storeManager, SoundPlayer soundPlayer){
        this.adControllerHelper = adControllerHelper;
        this.storeManager = storeManager;
        this.soundPlayer = soundPlayer;
        storeManager.addObserver(this);
    }

    @Override
    public void stateChange(LevelState state) {

        if (state == LevelState.WAVE_COMPLETED) {
            if(storeManager.isAdsRemovalPurchasable()) {
                adControllerHelper.incrementEventTriggered();
                if (adControllerHelper.readyToShowAd()) {
                    adView.showPreAd();
                }
            }
        }
    }

    public void removeAds(){
        if(!storeManager.isAdsRemovalPurchasable()){
            throw new IllegalStateException("Ad removal is not purchasable");
        }
        soundPlayer.play(LDSound.Type.SMALL_CLICK);
        storeManager.purchaseItem(PurchasableItem.NO_ADS);
    }

    public void setView(IAdView adView){
        this.adView = adView;
    }

    public void showAd(){
        soundPlayer.play(LDSound.Type.SMALL_CLICK);
        adControllerHelper.showAd();
        adView.close();
    }

    @Override
    public void handlePurchase(PurchasableItem purchasableItem) {
        if(purchasableItem.equals(PurchasableItem.NO_ADS)){
            adView.close();
        }
    }

    @Override
    public void purchaseManagerInstalled(boolean installed) {

    }
}
