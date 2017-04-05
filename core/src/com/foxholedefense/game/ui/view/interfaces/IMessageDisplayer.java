package com.foxholedefense.game.ui.view.interfaces;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Eric on 3/17/2017.
 */

public interface IMessageDisplayer {
    void displayMessage(String message);
    void displayMessage(String message, Color color);
    void displayMessage(String message, float fontScale);
    void displayMessage(String message, float fontScale, Color color);
}
