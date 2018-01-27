package com.lastdefenders.game.ui.presenter;

import com.badlogic.gdx.Preferences;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.game.ui.state.GameUIStateObserver;
import com.lastdefenders.game.ui.view.interfaces.IOptionsView;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.LDAudio.LDSound;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * Presenter for the Options View
 *
 * @author Eric
 */
public class OptionsPresenter implements GameUIStateObserver {

    private GameUIStateManager uiStateManager;
    private ScreenChanger screenChanger;
    private IOptionsView view;
    private LDAudio audio;
    private Resources resources;

    public OptionsPresenter(GameUIStateManager uiStateManager, ScreenChanger screenChanger,
        Resources resources, LDAudio audio) {

        this.uiStateManager = uiStateManager;
        uiStateManager.attach(this);
        this.screenChanger = screenChanger;
        this.audio = audio;
        this.resources = resources;
    }

    /**
     * Set the options view
     */
    public void setView(IOptionsView view) {

        this.view = view;
        initView();
    }

    private void initView() {

        Logger.info("Options Presenter: initializing view");
        stateChange(uiStateManager.getState());
        view.setBtnMusicOn(audio.isMusicEnabled());
        view.setBtnSoundOn(audio.isSoundEnabled());
        view.setBtnShowRangesOn(isShowRangesEnabled());
    }

    /**
     * Close options window
     */
    public void closeOptions() {

        Logger.info("Options Presenter: close options");
        audio.playSound(LDSound.SMALL_CLICK);
        audio.saveMasterVolume();
        uiStateManager.setStateReturn();
    }

    /**
     * Go to main menu
     */
    public void mainMenu() {

        if (canChangeToMainMenu()) {
            Logger.info("Options Presenter: main menu");
            audio.playSound(LDSound.SMALL_CLICK);
            audio.saveMasterVolume();
            screenChanger.changeToMenu();
        }
    }

    /**
     * Start a new game
     */
    public void newGame() {

        if (canChangeToNewGame()) {
            Logger.info("Options Presenter: new game");
            audio.playSound(LDSound.SMALL_CLICK);
            audio.saveMasterVolume();
            screenChanger.changeToLevelSelect();
        }
    }

    public void debug() {

        if (canChangeToDebug()) {
            Logger.info("Options Presenter: debug");
            audio.playSound(LDSound.SMALL_CLICK);
            audio.saveMasterVolume();
            uiStateManager.setState(GameUIState.DEBUG);
        }
    }

    private boolean isShowRangesEnabled() {

        Preferences prefs = resources.getUserPreferences().getPreferences();
        return prefs.getBoolean("showRanges", false);
    }

    public void showRangesPressed() {

        Logger.info("Options Presenter: show ranges pressed");
        audio.playSound(LDSound.SMALL_CLICK);
        Preferences prefs = resources.getUserPreferences().getPreferences();
        boolean isShowRangesEnabled = isShowRangesEnabled();
        prefs.putBoolean("showRanges", !isShowRangesEnabled);
        prefs.flush();
        view.setBtnShowRangesOn(!isShowRangesEnabled);
    }

    public void soundPressed() {

        Logger.info("Options Presenter: sound pressed");
        audio.playSound(LDSound.SMALL_CLICK);
        audio.changeSoundEnabled();
        view.setBtnSoundOn(audio.isSoundEnabled());
    }


    public void musicPressed() {

        Logger.info("Options Presenter: music pressed");
        audio.playSound(LDSound.SMALL_CLICK);
        audio.changeMusicEnabled();
        view.setBtnMusicOn(audio.isMusicEnabled());
    }

    public void speedChanged(float val) {

        resources.setGameSpeed(val);
    }

    public float getGameSpeed() {

        return resources.getGameSpeed();
    }

    public void volumeChanged(float vol) {

        audio.setMasterVolume(vol);
    }

    public float getMasterVolume() {

        return audio.getMasterVolume();
    }

    private boolean canChangeToMainMenu() {

        return uiStateManager.getState().equals(GameUIState.OPTIONS);
    }

    private boolean canChangeToNewGame() {

        return uiStateManager.getState().equals(GameUIState.OPTIONS);
    }

    private boolean canChangeToDebug() {

        return uiStateManager.getState().equals(GameUIState.OPTIONS);
    }

    @Override
    public void stateChange(GameUIState state) {

        switch (state) {
            case OPTIONS:
                view.optionsState();
                break;
            default:
                view.standByState();
                break;
        }

    }

}
