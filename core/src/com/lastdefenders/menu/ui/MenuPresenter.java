package com.lastdefenders.menu.ui;

import com.lastdefenders.menu.ui.view.interfaces.IMenuOptionsView;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.sound.AudioManager;
import com.lastdefenders.sound.AudioPlayer;
import com.lastdefenders.sound.LDMusic;
import com.lastdefenders.sound.LDSound;
import com.lastdefenders.store.StoreManager;
import com.lastdefenders.store.StoreManager.PurchasableItem;
import com.lastdefenders.store.StoreManagerObserver;
import com.lastdefenders.ui.presenter.GooglePlayServicesPresenter;

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
    private AudioManager audio;
    private GooglePlayServicesPresenter gpsPresenter;
    private boolean optionsActive;
    private StoreManager storeManager;

    public MenuPresenter(ScreenChanger screenChanger, AudioManager audio, GooglePlayServicesPresenter gpsPresenter,
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
        view.setBtnMusicOn(audio.getMusicPlayer().isEnabled());
        view.setBtnSoundOn(audio.getSoundPlayer().isEnabled());
        menuOptionsView.setBtnMusicOn(audio.getMusicPlayer().isEnabled());
        menuOptionsView.setBtnSoundOn(audio.getSoundPlayer().isEnabled());
        menuOptionsView.setVisible(false);
    }

    public void playGame() {

        Logger.info("Menu Presenter: play game");
        audio.getSoundPlayer().play(LDSound.Type.LARGE_CLICK);
        screenChanger.changeToLevelSelect();

    }

    public void soundPressed() {

        Logger.info("Menu Presenter: sound pressed");
        audio.getSoundPlayer().play(LDSound.Type.SMALL_CLICK);
        audio.getSoundPlayer().toggleEnabled();
        view.setBtnSoundOn(audio.getSoundPlayer().isEnabled());
        menuOptionsView.setBtnSoundOn(audio.getSoundPlayer().isEnabled());
    }

    public void musicPressed() {

        Logger.info("Menu Presenter: music pressed");
        audio.getSoundPlayer().play(LDSound.Type.SMALL_CLICK);
        audio.getMusicPlayer().toggleEnabled();
        if(audio.getMusicPlayer().isEnabled()){
            audio.getMusicPlayer().play(LDMusic.Type.MENU);
        }
        view.setBtnMusicOn(audio.getMusicPlayer().isEnabled());
        menuOptionsView.setBtnMusicOn(audio.getMusicPlayer().isEnabled());
    }

    public void playServicesPressed() {
        Logger.info("Menu Presenter: play services pressed");
        if(gpsPresenter == null){
            return;
        }
        audio.getSoundPlayer().play(LDSound.Type.SMALL_CLICK);
        gpsPresenter.showGPSView();
    }

    public void menuOptions() {
        Logger.info("Menu Presenter: menu options");
        audio.getSoundPlayer().play(LDSound.Type.SMALL_CLICK);
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
        audio.getSoundPlayer().play(LDSound.Type.SMALL_CLICK);
        audio.saveMasterVolume();
        menuOptionsView.setVisible(false);
        optionsActive = false;

    }

    public void volumeChanged(float vol) {

        audio.setVolume(vol);
    }

    public float getMasterVolume() {

        return audio.getVolume();
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
        audio.getSoundPlayer().play(LDSound.Type.SMALL_CLICK);
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
