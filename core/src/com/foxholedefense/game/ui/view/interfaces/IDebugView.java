package com.foxholedefense.game.ui.view.interfaces;

/**
 * Created by Eric on 3/12/2017.
 */

public interface IDebugView {
    public void debugState();
    public void standByState();
    public void showFPS(boolean show);
    public void setFPSChecked(boolean isChecked);
    public void setTextureBoundariesChecked(boolean isChecked);
}
