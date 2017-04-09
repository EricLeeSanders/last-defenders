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

        Table table = new Table();
        table.setTransform(false);
        table.setBackground(skin.getDrawable("main-panel"));
        table.setSize(325,300);
        table.setPosition((Resources.VIRTUAL_WIDTH/2)-(table.getWidth()/2), (Resources.VIRTUAL_HEIGHT/2)-(table.getHeight()/2));
        this.addActor(table);


        Label lblTitle = new Label("PAUSED", skin);
        lblTitle.setFontScale(0.45f);
        lblTitle.setAlignment(Align.center);
        lblTitle.setPosition(table.getX() + (table.getWidth()/2) - (lblTitle.getWidth()/2)
                ,table.getY() + table.getHeight() - lblTitle.getHeight() + 4);
        this.addActor(lblTitle);

        table.row();
        TextButton btnResume = new TextButton("RESUME", skin);
        btnResume.getLabel().setFontScale(0.45f);
        table.add(btnResume).width(150).height(45).spaceTop(10).padTop(15);
        setBtnResumeListener(btnResume);

        table.row();
        TextButton btnQuit = new TextButton("QUIT", skin);
        btnQuit.getLabel().setFontScale(0.45f);
        table.add(btnQuit).width(150).height(45).spaceTop(10);
        setBtnQuitListener(btnQuit);

        table.row();
        TextButton btnNewGame = new TextButton("NEW GAME", skin);
        btnNewGame.getLabel().setFontScale(0.45f);
        table.add(btnNewGame).width(150).height(45).spaceTop(10);
        setBtnNewGameListener(btnNewGame);

        table.row();
        TextButton btnMainMenu = new TextButton("MAIN MENU", skin);
        btnMainMenu.getLabel().setFontScale(0.45f);
        table.add(btnMainMenu).width(150).height(45).spaceTop(10);
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
