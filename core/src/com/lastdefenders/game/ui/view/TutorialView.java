package com.lastdefenders.game.ui.view;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.utils.Scaling;
import com.lastdefenders.game.ui.presenter.TutorialPresenter;
import com.lastdefenders.game.ui.view.interfaces.ITutorialView;

/**
 * Created by Eric on 5/4/2018.
 */

public class TutorialView extends Group implements ITutorialView, InputProcessor {

    private TextureAtlas tutorialAtlas;
    private TutorialPresenter presenter;

    public TutorialView(TutorialPresenter presenter, TextureAtlas tutorialAtlas){
        this.presenter = presenter;
        this.tutorialAtlas = tutorialAtlas;
    }

    @Override
    public void showTutorialScreen(ImageButton button, String tutorialScreenName){

        Image screen = new Image(tutorialAtlas.findRegion(tutorialScreenName));
        screen.setSize(getStage().getViewport().getWorldWidth(), getStage().getViewport().getWorldHeight());
        screen.setPosition(0,0);
        addActor(screen);

        ImageButton btnCopy = copyButton(button);
        addActor(btnCopy);
    }

    private ImageButton copyButton(ImageButton original){

        ImageButtonStyle style = new ImageButtonStyle();
        style.imageUp = original.getStyle().imageUp;
        style.up = original.getStyle().up;

        ImageButton copy = new ImageButton(style);

        copy.setSize(original.getWidth(), original.getHeight());
        copy.getImageCell().size(original.getImageCell().getPrefWidth(), original.getImageCell().getPrefHeight());
        copy.getImage().setScaling(Scaling.stretch);
        copy.setPosition(original.getX(), original.getY());
        copy.setDisabled(true);

        return copy;
    }

    @Override
    public void removeTutorialScreens(){
        this.clearChildren();
    }

    @Override
    public boolean keyDown(int keycode) {

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        presenter.showNextTip();
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {

        return false;
    }

    @Override
    public boolean scrolled(int amount) {

        return false;
    }
}
