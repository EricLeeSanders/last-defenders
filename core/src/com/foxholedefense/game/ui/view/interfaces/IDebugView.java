package com.foxholedefense.game.ui.view.interfaces;

/**
 * Created by Eric on 3/12/2017.
 */

public interface IDebugView {

    void debugState();

    void standByState();

    void showFPS(boolean show);

    void setFPSChecked(boolean isChecked);

    void setTextureBoundariesChecked(boolean isChecked);
}
