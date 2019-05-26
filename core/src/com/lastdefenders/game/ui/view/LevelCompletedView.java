package com.lastdefenders.game.ui.view;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.lastdefenders.game.ui.presenter.LevelCompletedPresenter;
import com.lastdefenders.game.ui.view.interfaces.ILevelCompletedView;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

public class LevelCompletedView extends Group implements ILevelCompletedView {

    private LevelCompletedPresenter presenter;
    private Resources resources;

    public LevelCompletedView(LevelCompletedPresenter presenter, Resources resources) {

        this.presenter = presenter;
        this.resources = resources;
        this.setTransform(false);
    }

    public void init(){
        createControls();
    }

    private void createControls() {

        Logger.info("Level Completed View: creating controls");

        Skin skin = resources.getSkin();

        Table table = new Table();
        table.setTransform(false);
        table.setBackground(skin.getDrawable("main-panel"));
        table.setSize(500, 260);
        table.setPosition(getStage().getViewport().getWorldWidth() / 2, getStage().getViewport().getWorldHeight() / 2, Align.center);
        this.addActor(table);

        Label lblTitle = new Label("Level Completed", skin);
        lblTitle.setHeight(42);
        lblTitle.setFontScale(0.70f * resources.getFontScale());
        lblTitle.setAlignment(Align.center);
        lblTitle.setPosition(table.getX() + (table.getWidth() / 2) - (lblTitle.getWidth() / 2),
            table.getY() + table.getHeight() - lblTitle.getHeight());
        this.addActor(lblTitle);

        Label lblContinue = new Label(
            "Do you want to continue playing this\nlevel and compete for a high score?", skin);
        lblContinue.setFontScale(0.55f * resources.getFontScale());
        lblContinue.setAlignment(Align.center);
        table.add(lblContinue).colspan(3).width(380).height(90);

        table.row();
        TextButton btnContinueLevel = new TextButton("Continue", skin);
        btnContinueLevel.getLabel().setFontScale(0.45f * resources.getFontScale());
        table.add(btnContinueLevel).width(130).height(45);
        setBtnContinueLevel(btnContinueLevel);

        TextButton btnNewGame = new TextButton("New Game", skin);
        btnNewGame.getLabel().setFontScale(0.45f * resources.getFontScale());
        table.add(btnNewGame).width(130).height(45).spaceLeft(10).spaceRight(10);
        setbtnNewGameListener(btnNewGame);

        TextButton btnMainMenu = new TextButton("Main Menu", skin);
        btnMainMenu.getLabel().setFontScale(0.45f * resources.getFontScale());
        table.add(btnMainMenu).width(130).height(45);
        setbtnMainMenuListener(btnMainMenu);

        Logger.info("Level Completed View: controls created");
    }

    private void setBtnContinueLevel(Button button) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.continueLevel();
            }
        });

    }

    private void setbtnNewGameListener(Button button) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.levelSelect();
            }
        });

    }

    private void setbtnMainMenuListener(Button button) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.mainMenu();
            }
        });

    }

    @Override
    public void levelCompletedState() {

        this.setVisible(true);

    }

    @Override
    public void standByState() {

        this.setVisible(false);

    }
}
