package com.lastdefenders.game.ui.view.interfaces;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

/**
 * Created by Eric on 5/4/2018.
 */

public interface ITutorialView {
    void showTutorialScreen(ImageButton button, String tutorialScreenName);
    void removeTutorialScreens();

}
