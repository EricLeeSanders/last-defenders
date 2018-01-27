package com.lastdefenders.game.ui.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.lastdefenders.game.ui.presenter.DebugPresenter;
import com.lastdefenders.game.ui.view.interfaces.IDebugView;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * Created by Eric on 3/12/2017.
 */

public class DebugView extends Group implements IDebugView {

    private DebugPresenter presenter;
    private Label framesLabel;
    private CheckBox btnShowTextureBoundaries, btnShowFPS;
    private TextButton btnResume;
    private Resources resources;

    public DebugView(DebugPresenter presenter, Resources resources) {

        this.presenter = presenter;
        this.setTransform(false);
        this.resources = resources;

    }

    public void init(){
        createControls();
    }

    /**
     * Create controls
     */
    private void createControls() {

        Logger.info("Debug View: creating controls");

        Skin skin = resources.getSkin();

        Table container = new Table();
        container.setTransform(false);
        container.setBackground(skin.getDrawable("main-panel"));
        container.setSize(500, 360);
        container.setPosition(getStage().getViewport().getWorldWidth() / 2, getStage().getViewport().getWorldHeight() / 2, Align.center);
        //table.debug();
        this.addActor(container);

        Table mainTable = new Table();
        mainTable.setTransform(false);
        mainTable.setBackground(skin.getDrawable("hollow"));
        container.add(mainTable);

        Label lblTitle = new Label("DEBUG", skin);
        lblTitle.setFontScale(0.7f * resources.getFontScale());
        lblTitle.setAlignment(Align.center);
        lblTitle.setHeight(60);
        float x = container.getX(Align.center);
        float y = container.getY(Align.top) - (lblTitle.getHeight()/2);
        lblTitle.setPosition(x, y, Align.center);
        this.addActor(lblTitle);

        framesLabel = new Label("x", skin);
        framesLabel.setFontScale(0.35f * resources.getFontScale());
        framesLabel.setColor(1f, 1f, 1f, 0.30f);
        framesLabel.setPosition(200, 320);

        btnResume = new TextButton("RESUME", skin);
        btnResume.getLabel().setFontScale(0.45f * resources.getFontScale());
        btnResume.getLabel().setAlignment(Align.center);
        btnResume.setSize(150, 45);
        btnResume.setPosition((getStage().getViewport().getWorldWidth() / 2) - btnResume.getWidth() - 50, 20);
        addActor(btnResume);
        setBtnResumeListener();

        btnShowFPS = new CheckBox(" SHOW FPS", skin);
        btnShowFPS.getLabel().setFontScale(0.45f * resources.getFontScale());
        btnShowFPS.getImageCell().width(32).height(32);
        btnShowFPS.getImage().setScaling(Scaling.stretch);
        setBtnShowFPSListener(btnShowFPS);

        btnShowTextureBoundaries = new CheckBox(" SHOW BOUNDARIES", skin);
        btnShowTextureBoundaries.getLabel().setFontScale(0.45f * resources.getFontScale());
        btnShowTextureBoundaries.getImageCell().width(32).height(32);
        btnShowTextureBoundaries.getImage().setScaling(Scaling.stretch);
        setBtnShowTextureBoundariesListener(btnShowTextureBoundaries);

        TextButton btnCrash = new TextButton("TEST CRASH", skin);
        btnCrash.getLabel().setFontScale(0.45f * resources.getFontScale());
        btnCrash.pack();
        btnCrash.setSize(150, 45);
        setBtnCrashListener(btnCrash);

        mainTable.add(btnShowFPS).left().spaceLeft(15).spaceBottom(10);

        mainTable.row();

        mainTable.add(btnShowTextureBoundaries).left().spaceLeft(15).spaceBottom(10);

        mainTable.row();

        mainTable.add(btnCrash).left().spaceLeft(15).spaceBottom(10).size(150,45);

        Logger.info("Debug View: controls created");
    }

    @Override
    public void act(float delta) {

        framesLabel
            .setText("FPS: " + Integer.valueOf(Gdx.graphics.getFramesPerSecond()).toString());
    }

    private void setBtnCrashListener(Button button) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.crash();
            }
        });

    }

    private void setBtnShowFPSListener(Button button) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.showFPSPressed();
            }
        });
    }

    private void setBtnShowTextureBoundariesListener(Button button) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.showTextureBoundariesPressed();
            }
        });
    }

    private void setBtnResumeListener() {

        btnResume.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.resumeGame();
            }
        });

    }

    @Override
    public void showFPS(boolean show) {

        if (show) {
            this.getParent().addActor(framesLabel);
        } else {
            this.getParent().removeActor(framesLabel);
        }
    }

    @Override
    public void setFPSChecked(boolean isChecked) {

        btnShowFPS.setChecked(isChecked);
    }

    @Override
    public void setTextureBoundariesChecked(boolean isChecked) {

        btnShowTextureBoundaries.setChecked(isChecked);
    }

    @Override
    public void debugState() {

        this.setVisible(true);
    }

    @Override
    public void standByState() {

        this.setVisible(false);
    }
}
