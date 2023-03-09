package com.lastdefenders.ui.view.impl;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.lastdefenders.ui.presenter.GooglePlayServicesPresenter;
import com.lastdefenders.ui.view.GooglePlayServicesView;
import com.lastdefenders.util.Resources;

/**
 * Created by Eric on 6/28/2018.
 */

public class GooglePlayServicesViewImpl extends Group implements GooglePlayServicesView {

    private GooglePlayServicesPresenter presenter;
    private Resources resources;
    private TextButton btnAchievements, btnLeaderboards;

    public GooglePlayServicesViewImpl(GooglePlayServicesPresenter presenter, Resources resources) {

        this.presenter = presenter;
        this.resources = resources;
        this.setTransform(false);
    }

    @Override
    public void init(){
        createControls();
        setVisible(false);
    }


    private void createControls(){
        Skin skin = resources.getSkin();

        Table container = new Table();
        container.setTransform(false);
        container.setBackground(skin.getDrawable("main-panel"));
        container.setSize(500, 360);
        container.setPosition(getStage().getViewport().getWorldWidth() / 2, getStage().getViewport().getWorldHeight() / 2, Align.center );
        addActor(container);

        Table mainTable = new Table();
        mainTable.setTransform(false);
        container.add(mainTable);


        Label lblTitle = new Label("Google Play Games", skin);
        lblTitle.setFontScale(0.6f * resources.getFontScale());
        lblTitle.setAlignment(Align.center);
        lblTitle.setHeight(60);
        float lblTitleX = container.getX(Align.center);
        float lblTitleY = container.getY(Align.top) - (lblTitle.getHeight()/2);
        lblTitle.setPosition(lblTitleX, lblTitleY, Align.center);
        addActor(lblTitle);

        ImageButton btnClose = new ImageButton(skin, "cancel");
        btnClose.setSize(50, 50);
        btnClose.getImageCell().size(25, 26);
        btnClose.getImage().setScaling(Scaling.stretch);
        btnClose.setPosition(500, 300);
        addActor(btnClose);
        setBtnCloseListener(btnClose);

        btnAchievements = new TextButton("Achievements", skin);
        btnAchievements.getLabel().setFontScale(0.45f * resources.getFontScale());
        setBtnAchievementsListener(btnAchievements);

        btnLeaderboards = new TextButton("Leaderboards", skin);
        btnLeaderboards.getLabel().setFontScale(0.45f * resources.getFontScale());
        setBtnLeaderboardsListener(btnLeaderboards);

        mainTable.add(btnAchievements).size(170,45).spaceBottom(10);
        mainTable.row();
        mainTable.add(btnLeaderboards).size(170,45).spaceTop(10);

    }

    private void setBtnCloseListener(Button button) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.close();
            }
        });
    }

    private void setBtnLeaderboardsListener(Button button) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.leaderboards();
            }
        });

    }

    private void setBtnAchievementsListener(Button button) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.achievements();
            }
        });

    }
}
