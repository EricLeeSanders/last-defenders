package com.lastdefenders.game.ui.view;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.lastdefenders.game.ui.presenter.TutorialPresenter;
import com.lastdefenders.game.ui.view.interfaces.ITutorialView;
import com.lastdefenders.util.Resources;

/**
 * Created by Eric on 5/6/2018.
 */

public class TutorialView extends Group implements ITutorialView {
    private TutorialPresenter presenter;
    private TextureAtlas tutorialAtlas;

    public TutorialView(TutorialPresenter presenter, TextureAtlas tutorialAtlas){
        this.presenter = presenter;
        this.tutorialAtlas = tutorialAtlas;
        setTransform(false);
    }

    @Override
    public void placeFirstSoldier() {
        Image screen = new Image(tutorialAtlas.findRegion("enlist-button"));
        screen.setSize(getStage().getViewport().getScreenWidth(), getStage().getViewport().getScreenHeight());
        screen.setPosition(0,0);
        addActor(screen);
    }
}
