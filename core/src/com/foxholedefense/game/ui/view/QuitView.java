package com.foxholedefense.game.ui.view;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.foxholedefense.game.ui.presenter.QuitPresenter;
import com.foxholedefense.game.ui.view.interfaces.IQuitView;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

/**
 * Created by Eric on 4/8/2017.
 */

public class QuitView extends Group implements IQuitView {

    private QuitPresenter presenter;

    public QuitView(QuitPresenter presenter, Skin skin) {
        this.presenter = presenter;
        this.setTransform(false);
        createControls(skin);
        standByState();
    }

    /**
     * Create controls
     */
    public void createControls(Skin skin) {
        Logger.info("QuitView: creating controls");

        Table container = new Table();
        container.setTransform(false);
        container.setBackground(skin.getDrawable("quit-panel"));
        container.setSize(190,307);
        container.setPosition((Resources.VIRTUAL_WIDTH/2)-(container.getWidth()/2), (Resources.VIRTUAL_HEIGHT/2)-(container.getHeight()/2));
        //container.debug();
        this.addActor(container);


        Label lblTitle = new Label("QUIT", skin);
        lblTitle.setPosition(container.getX() + (container.getWidth()/2) - (lblTitle.getWidth()/2)
                ,container.getY() + container.getHeight() - lblTitle.getHeight() + 5 );
        lblTitle.setAlignment(Align.center);
        lblTitle.setFontScale(0.7f);
        this.addActor(lblTitle);

        TextButton btnResume = new TextButton("RESUME",skin);
        btnResume.getLabel().setFontScale(0.45f);
        btnResume.setSize(150,45);
        btnResume.pack();
        btnResume.setPosition(245,229);
        addActor(btnResume);
        setBtnResumeListener(btnResume);


        TextButton btnQuit = new TextButton("QUIT",skin);
        btnQuit.getLabel().setFontScale(0.45f);
        btnQuit.setSize(150,45);
        btnQuit.pack();
        btnQuit.setPosition(btnResume.getX(), btnResume.getY() - 57);
        addActor(btnQuit);
        setBtnQuitListener(btnQuit);

        TextButton btnNewGame = new TextButton("NEW GAME",skin);
        btnNewGame.getLabel().setFontScale(0.45f);
        btnNewGame.setSize(150,45);
        btnNewGame.pack();
        btnNewGame.setPosition(btnQuit.getX(), btnQuit.getY() - 57);
        addActor(btnNewGame);
        setBtnNewGameListener(btnNewGame);

        TextButton btnMainMenu = new TextButton("MAIN MENU",skin);
        btnMainMenu.getLabel().setFontScale(0.45f);
        btnMainMenu.setSize(150,45);
        btnMainMenu.pack();
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
    public void quitState() {
        setVisible(true);
    }

    @Override
    public void standByState() {
        setVisible(false);
    }
}
