package com.foxholedefense.game.ui.view;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.foxholedefense.game.ui.presenter.PausePresenter;
import com.foxholedefense.game.ui.view.interfaces.IPauseView;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

/**
 * Created by Eric on 4/8/2017.
 */

public class PauseView extends Group implements IPauseView {

    private PausePresenter presenter;

    public PauseView(PausePresenter presenter, Skin skin) {

        this.presenter = presenter;
        this.setTransform(false);
        createControls(skin);
        standByState();
    }

    /**
     * Create controls
     */
    private void createControls(Skin skin) {

        Logger.info("PauseView: creating controls");

        Table container = new Table();
        container.setTransform(false);
        container.setBackground(skin.getDrawable("pause-panel"));
        container.setSize(190, 307);
        container.setPosition((Resources.VIRTUAL_WIDTH / 2) - (container.getWidth() / 2),
            (Resources.VIRTUAL_HEIGHT / 2) - (container.getHeight() / 2));
        this.addActor(container);

        Label lblTitle = new Label("PAUSED", skin);
        lblTitle
            .setPosition(container.getX() + (container.getWidth() / 2) - (lblTitle.getWidth() / 2)
                , container.getY() + container.getHeight() - lblTitle.getHeight() + 5);
        lblTitle.setAlignment(Align.center);
        lblTitle.setFontScale(0.7f);
        this.addActor(lblTitle);

        TextButton btnResume = new TextButton("RESUME", skin);
        btnResume.getLabel().setFontScale(0.45f);
        btnResume.pack();
        btnResume.setSize(150, 45);
        btnResume.setPosition(245, 229);
        addActor(btnResume);
        setBtnResumeListener(btnResume);

        TextButton btnQuit = new TextButton("QUIT", skin);
        btnQuit.getLabel().setFontScale(0.45f);
        btnQuit.pack();
        btnQuit.setSize(150, 45);
        btnQuit.setPosition(btnResume.getX(), btnResume.getY() - 57);
        addActor(btnQuit);
        setBtnQuitListener(btnQuit);

        TextButton btnNewGame = new TextButton("NEW GAME", skin);
        btnNewGame.getLabel().setFontScale(0.45f);
        btnNewGame.pack();
        btnNewGame.setSize(150, 45);
        btnNewGame.setPosition(btnQuit.getX(), btnQuit.getY() - 57);
        addActor(btnNewGame);
        setBtnNewGameListener(btnNewGame);

        TextButton btnMainMenu = new TextButton("MAIN MENU", skin);
        btnMainMenu.getLabel().setFontScale(0.45f);
        btnMainMenu.pack();
        btnMainMenu.setSize(150, 45);
        btnMainMenu.setPosition(btnNewGame.getX(), btnNewGame.getY() - 56);
        addActor(btnMainMenu);
        setBtnMainMenuListener(btnMainMenu);

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
