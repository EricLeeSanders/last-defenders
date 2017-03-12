package com.foxholedefense.game.ui.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.foxholedefense.game.ui.presenter.DebugPresenter;
import com.foxholedefense.game.ui.view.interfaces.IDebugView;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

/**
 * Created by Eric on 3/12/2017.
 */

public class DebugView extends Group implements IDebugView {
    private DebugPresenter presenter;
    private Label framesLabel;
    private CheckBox btnShowTextureBoundaries, btnShowFPS;
    private TextButton btnResume;

    public DebugView(DebugPresenter presenter, Skin skin){
        this.presenter = presenter;
        this.setTransform(false);
        createControls(skin);
    }

    /**
     * Create controls
     */
    public void createControls(Skin skin) {

        Logger.info("Debug View: creating controls");

        Table container = new Table();
        container.setTransform(false);
        container.setBackground(skin.getDrawable("main-panel"));
        container.setSize(500,360);
        container.setPosition((Resources.VIRTUAL_WIDTH/2)-(container.getWidth()/2), (Resources.VIRTUAL_HEIGHT/2)-(container.getHeight()/2));
        //table.debug();
        this.addActor(container);

        Table mainTable = new Table();
        mainTable.setTransform(false);
        mainTable.setBackground(skin.getDrawable("hollow"));
        container.add(mainTable);

        Label lblTitle = new Label("DEBUG", skin);
        lblTitle.setPosition(container.getX() + (container.getWidth()/2) - (lblTitle.getWidth()/2)
                ,container.getY() + container.getHeight() - lblTitle.getHeight() );
        lblTitle.setAlignment(Align.center);
        lblTitle.setFontScale(0.7f);
        this.addActor(lblTitle);

        framesLabel = new Label("x", skin);
        framesLabel.setFontScale(0.35f);
        framesLabel.setColor(1f, 1f, 1f, 0.30f);
        framesLabel.setPosition(200, 320);


        btnResume = new TextButton("RESUME",skin);
        btnResume.getLabel().setFontScale(0.45f);
        btnResume.pack();
        btnResume.setPosition(112,20);
        addActor(btnResume);
        setBtnResumeListener();


        btnShowFPS = new CheckBox(" SHOW FPS", skin);
        btnShowFPS.getLabel().setFontScale(0.45f);
        btnShowFPS.getImageCell().width(32).height(32);
        btnShowFPS.getImage().setScaling(Scaling.stretch);
        setBtnShowFPSListener(btnShowFPS);

        btnShowTextureBoundaries = new CheckBox(" SHOW BOUNDARIES", skin);
        btnShowTextureBoundaries.getLabel().setFontScale(0.45f);
        btnShowTextureBoundaries.getImageCell().width(32).height(32);
        btnShowTextureBoundaries.getImage().setScaling(Scaling.stretch);
        setBtnShowTextureBoundariesListener(btnShowTextureBoundaries);


        //mainTable.add(btnResume).width(128).height(41).spaceBottom(10);
        mainTable.add(btnShowFPS).colspan(2).left().spaceLeft(15).spaceBottom(10);

        mainTable.row();

        //mainTable.add(btnNewGame).width(128).height(41).spaceBottom(10);
        mainTable.add(btnShowTextureBoundaries).colspan(2).left().spaceLeft(15).spaceBottom(10);

        Logger.info("Debug View: controls created");
    }

    @Override
    public void act (float delta) {
        framesLabel.setText("FPS: " + Integer.valueOf(Gdx.graphics.getFramesPerSecond()).toString());
    }

    private void setBtnShowFPSListener(Button button){
        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                presenter.showFPSListener();
            }
        });
    }

    private void setBtnShowTextureBoundariesListener(Button button){
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
    public void showFPS(boolean show){

        if(show){
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
