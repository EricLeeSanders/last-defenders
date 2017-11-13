package com.lastdefenders.game.ui.view.interfaces;

/**
 * Interface for Options View. Used by the Options Presenter to communicate with
 * the view
 *
 * @author Eric
 */
public interface IOptionsView {

    void optionsState();

    void standByState();

    void setBtnShowRangesOn(boolean showRangesOn);

    void setBtnSoundOn(boolean soundOn);

    void setBtnMusicOn(boolean musicOn);
}
