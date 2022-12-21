package com.lastdefenders.menu.ui;

import com.lastdefenders.menu.ui.view.interfaces.IMenuOptionsView;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.store.StoreManager;
import com.lastdefenders.store.StoreManager.PurchasableItem;
import com.lastdefenders.store.StoreManagerObserver;
import com.lastdefenders.ui.presenter.GooglePlayServicesPresenter;
import com.lastdefenders.sound.LDAudio;
import com.lastdefenders.sound.LDAudio.LDSound;
import com.lastdefenders.util.Logger;
import com.lastdefenders.menu.ui.view.interfaces.IMenuView;

/**
 * Presenter class for the Main Menu
 *
 * @author Eric
 */
public class MenuPresenter implements StoreManagerObserver {

    private ScreenChanger screenChanger;
    private IMenuView view;
    private IMenuOptionsView menuOptionsView;
    private LDAudio audio;
    private GooglePlayServicesPresenter gpsPresenter;
    private boolean optionsActive;
    private StoreManager storeManager;

    public MenuPresenter(ScreenChanger screenChanger, LDAudio audio, GooglePlayServicesPresenter gpsPresenter,
        StoreManager storeManager) {
        this.screenChanger = screenChanger;
        this.audio = audio;
        this.gpsPresenter = gpsPresenter;
        this.storeManager = storeManager;

        storeManager.addObserver(this);
    }

    public void setView(IMenuView view, IMenuOptionsView menuOptionsView) {

        this.view = view;
        this.menuOptionsView = menuOptionsView;
        initView();
    }

    private void initView() {

        Logger.info("Menu Presenter: initializing view");
        view.setBtnMusicOn(audio.isMusicEnabled());
        view.setBtnSoundOn(audio.isSoundEnabled());
        menuOptionsView.setBtnMusicOn(audio.isMusicEnabled());
        menuOptionsView.setBtnSoundOn(audio.isSoundEnabled());
        menuOptionsView.setVisible(false);
    }

    public void playGame() {

        Logger.info("Menu Presenter: play game");
        audio.playSound(LDSound.LARGE_CLICK);
        screenChanger.changeToLevelSelect();

    }

    public void soundPressed() {

        Logger.info("Menu Presenter: sound pressed");
        audio.playSound(LDSound.SMALL_CLICK);
        audio.changeSoundEnabled();
        view.setBtnSoundOn(audio.isSoundEnabled());
        menuOptionsView.setBtnSoundOn(audio.isSoundEnabled());
    }

    public void musicPressed() {

        Logger.info("Menu Presenter: music pressed");
        audio.playSound(LDSound.SMALL_CLICK);
        audio.changeMusicEnabled();
        if(audio.isMusicEnabled()){
            audio.playMenuMusic();
        }
        view.setBtnMusicOn(audio.isMusicEnabled());
        menuOptionsView.setBtnMusicOn(audio.isMusicEnabled());
    }

    public void playServicesPressed() {
        Logger.info("Menu Presenter: play services pressed");
        if(gpsPresenter == null){
            return;
        }
        audio.playSound(LDSound.SMALL_CLICK);
        gpsPresenter.showGPSView();
    }

    public void menuOptions() {
        Logger.info("Menu Presenter: menu options");
        audio.playSound(LDSound.SMALL_CLICK);
        menuOptionsView.setVisible(true);
        optionsActive = true;
    }

    public boolean isGooglePlayServicesAvailable(){
        return gpsPresenter != null;
    }

    /**
     * Close options window
     */
    public void closeMenuOptions() {

        Logger.info("Options Presenter: close options");
        audio.playSound(LDSound.SMALL_CLICK);
        audio.saveMasterVolume();
        menuOptionsView.setVisible(false);
        optionsActive = false;

    }

    public void volumeChanged(float vol) {

        audio.setMasterVolume(vol);
    }

    public float getMasterVolume() {

        return audio.getMasterVolume();
    }

    public boolean isOptionsActive(){
        return optionsActive;
    }

    public boolean isAdsRemovalPurchasable(){
        return storeManager.isAdsRemovalPurchasable();
    }

    public void removeAds(){
        if(!isAdsRemovalPurchasable()){
            throw new IllegalStateException("Ad removal is not purchasable");
        }
        storeManager.purchaseItem(PurchasableItem.NO_ADS);
    }

    /**
     * Handles a back/escape request. Returns true if the event was handled.
     * @return - boolean
     */
    public boolean handleBack(){

        boolean handled = false;
        if(isOptionsActive()){
            closeMenuOptions();
            handled = true;
        } else if(gpsPresenter.isActive()){
            gpsPresenter.close();
            handled = true;
        }
        return handled;
    }

    @Override
    public void handlePurchase(PurchasableItem purchasableItem) {
        if(purchasableItem.equals(PurchasableItem.NO_ADS)){
            view.adRemovalPurchased(true);
        }
    }

    @Override
    public void purchaseManagerInstalled(boolean installed) {
        view.setPurchaseManagerInstalled(installed);
    }
}
