package com.lastdefenders.menu.ui;

import com.lastdefenders.menu.ui.view.interfaces.IMenuOptionsView;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.LDAudio.LDSound;
import com.lastdefenders.util.Logger;
import com.lastdefenders.menu.ui.view.interfaces.IMenuView;

/**
 * Presenter class for the Main Menu
 *
 * @author Eric
 */
public class MenuPresenter {

    private ScreenChanger screenChanger;
    private IMenuView view;
    private IMenuOptionsView menuOptionsView;
    private LDAudio audio;

    public MenuPresenter(ScreenChanger screenChanger, LDAudio audio) {

        this.screenChanger = screenChanger;
        this.audio = audio;
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
        view.setBtnMusicOn(audio.isMusicEnabled());
        menuOptionsView.setBtnMusicOn(audio.isMusicEnabled());
    }

    public void menuOptions() {
        Logger.info("Menu Presenter: menu options");
        audio.playSound(LDSound.SMALL_CLICK);
        menuOptionsView.setVisible(true);
    }

    /**
     * Close options window
     */
    public void closeMenuOptions() {

        Logger.info("Options Presenter: close options");
        audio.playSound(LDSound.SMALL_CLICK);
        audio.saveMasterVolume();
        menuOptionsView.setVisible(false);

    }

    public void volumeChanged(float vol) {

        audio.setMasterVolume(vol);
    }

    public float getMasterVolume() {

        return audio.getMasterVolume();
    }

}
