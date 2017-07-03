package com.foxholedefense.menu.ui;

import com.foxholedefense.screen.ScreenChanger;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;
import com.foxholedefense.util.Logger;

/**
 * Presenter class for the Main Menu
 *
 * @author Eric
 */
public class MenuPresenter {

    private ScreenChanger screenChanger;
    private IMenuView view;
    private FHDAudio audio;

    public MenuPresenter(ScreenChanger screenChanger, FHDAudio audio) {

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
        audio.playSound(FHDSound.LARGE_CLICK);
        screenChanger.changeToLevelSelect();

    }

    public void soundPressed() {

        Logger.info("Menu Presenter: sound pressed");
        audio.playSound(FHDSound.SMALL_CLICK);
        audio.changeSoundEnabled();
        view.setBtnSoundOn(audio.isSoundEnabled());
    }

    public void musicPressed() {

        Logger.info("Menu Presenter: music pressed");
        audio.playSound(FHDSound.SMALL_CLICK);
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
