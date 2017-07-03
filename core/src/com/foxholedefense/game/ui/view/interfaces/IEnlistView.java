package com.foxholedefense.game.ui.view.interfaces;

/**
 * Interface for Enlist View. Used by the Enlist Presenter to communicate with
 * the view
 *
 * @author Eric
 */
public interface IEnlistView {
    void enlistingState();

    void placingTowerState();

    void standByState();

    void showBtnPlace();

    void showBtnRotate();
}
