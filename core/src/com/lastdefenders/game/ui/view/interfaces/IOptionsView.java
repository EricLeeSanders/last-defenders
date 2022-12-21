package com.lastdefenders.game.ui.view.interfaces;

import com.lastdefenders.sound.SoundAdjusterView;
import com.lastdefenders.store.PurchasableView;

/**
 * Interface for Options View. Used by the Options Presenter to communicate with
 * the view
 *
 * @author Eric
 */
public interface IOptionsView extends SoundAdjusterView, PurchasableView {

    void optionsState();

    void standByState();

    void setBtnShowRangesOn(boolean showRangesOn);

}
