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
import com.lastdefenders.game.ui.presenter.PausePresenter;
import com.lastdefenders.game.ui.view.interfaces.IPauseView;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * Created by Eric on 4/8/2017.
 */

public class PauseView extends Group implements IPauseView {

    private PausePresenter presenter;
    private Resources resources;

    public PauseView(PausePresenter presenter, Resources resources) {

        this.presenter = presenter;
        this.resources = resources;
        this.setTransform(false);
    }

    public void init(){

        createControls();
        standByState();
    }

    /**
     * Create controls
     */
    private void createControls() {

        Logger.info("PauseView: creating controls");

        Skin skin = resources.getSkin();

        Table container = new Table();
        container.setTransform(false);
        container.setBackground(skin.getDrawable("pause-panel"));
        container.setSize(190, 307);
        container.setPosition(getStage().getViewport().getWorldWidth() / 2, getStage().getViewport().getWorldHeight() / 2, Align.center);
        this.addActor(container);

        Label lblTitle = new Label("PAUSED", skin);
        lblTitle.setAlignment(Align.center);
        lblTitle.setFontScale(0.7f * resources.getFontScale());

        TextButton btnResume = new TextButton("RESUME", skin);
        btnResume.getLabel().setFontScale(0.45f * resources.getFontScale());
        setBtnResumeListener(btnResume);

        TextButton btnQuit = new TextButton("QUIT", skin);
        btnQuit.getLabel().setFontScale(0.45f * resources.getFontScale());
        setBtnQuitListener(btnQuit);

        TextButton btnNewGame = new TextButton("NEW GAME", skin);
        btnNewGame.getLabel().setFontScale(0.45f * resources.getFontScale());
        setBtnNewGameListener(btnNewGame);

        TextButton btnMainMenu = new TextButton("MAIN MENU", skin);
        btnMainMenu.getLabel().setFontScale(0.45f * resources.getFontScale());
        setBtnMainMenuListener(btnMainMenu);

        container.top();
        container.add(lblTitle).padTop(3);
        container.row();
        container.add(btnResume).size(150,45).spaceTop(15);
        container.row();
        container.add(btnQuit).size(150,45).spaceTop(11.5f);
        container.row();
        container.add(btnNewGame).size(150,45).spaceTop(11.5f);
        container.row();
        container.add(btnMainMenu).size(150,45).spaceTop(11.5f);

    }

    private void setBtnResumeListener(Button button) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.resume();
            }
        });
    }

    private void setBtnNewGameListener(Button button) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.newGame();
            }
        });

    }

    private void setBtnMainMenuListener(Button button) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.mainMenu();
            }
        });

    }

    private void setBtnQuitListener(Button button) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.quit();
            }
        });

    }

    @Override
    public void pauseState() {

        setVisible(true);
    }

    @Override
    public void standByState() {

        setVisible(false);
    }
}
