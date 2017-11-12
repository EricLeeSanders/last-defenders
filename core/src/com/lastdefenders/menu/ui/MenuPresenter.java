package com.lastdefenders.menu.ui;

import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.LDAudio.LDSound;
import com.lastdefenders.util.Logger;

/**
 * Presenter class for the Main Menu
 *
 * @author Eric
 */
public class MenuPresenter {

    private ScreenChanger screenChanger;
    private IMenuView view;
    private LDAudio audio;

    public MenuPresenter(ScreenChanger screenChanger, LDAudio audio) {

        this.screenChanger = screenChanger;
        this.audio = audio;
    }

    public void setView(IMenuView view) {

        this.view = view;
        initView();
    }

    private void initView() {

        Logger.info("Menu Presenter: initializing view");
        view.setBtnMusicOn(audio.isMusicEnabled());
        view.setBtnSoundOn(audio.isSoundEnabled());
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
    }

    public void musicPressed() {

        Logger.info("Menu Presenter: music pressed");
        audio.playSound(LDSound.SMALL_CLICK);
        audio.changeMusicEnabled();
        view.setBtnMusicOn(audio.isMusicEnabled());
    }

    public void volumeChanged(float vol) {

        audio.setMasterVolume(vol);
    }

    public float getMasterVolume() {

        return audio.getMasterVolume();
    }

}
