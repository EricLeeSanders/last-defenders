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
import com.lastdefenders.game.ui.presenter.GameOverPresenter;
import com.lastdefenders.game.ui.view.interfaces.IGameOverView;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * View for the Game Over Window. This view always shows when the game is over.
 * Regardless of the state of the UI.
 *
 * @author Eric
 */
public class GameOverView extends Group implements IGameOverView {

    private GameOverPresenter presenter;
    private Label lblWavesCompleted;

    public GameOverView(GameOverPresenter presenter, Skin skin) {

        this.presenter = presenter;
        this.setTransform(false);
        createControls(skin);
    }

    /**
     * Create controls
     */
    private void createControls(Skin skin) {

        Logger.info("Game Over View: creating controls");

        Table table = new Table();
        table.setTransform(false);
        table.setBackground(skin.getDrawable("main-panel"));
        table.setSize(325, 300);
        table.setPosition((Resources.VIRTUAL_WIDTH / 2) - (table.getWidth() / 2),
            (Resources.VIRTUAL_HEIGHT / 2) - (table.getHeight() / 2));
        this.addActor(table);

        Label lblTitle = new Label("GAME OVER", skin);
        lblTitle.setFontScale(0.45f);
        lblTitle.setAlignment(Align.center);
        lblTitle.setPosition(table.getX() + (table.getWidth() / 2) - (lblTitle.getWidth() / 2),
            table.getY() + table.getHeight() - lblTitle.getHeight() + 4);
        this.addActor(lblTitle);

        table.row();
        lblWavesCompleted = new Label("0", skin);
        lblWavesCompleted.setFontScale(0.45f);
        lblWavesCompleted.setAlignment(Align.center);
        table.add(lblWavesCompleted).width(275).height(40);

        table.row();
        TextButton btnNewGame = new TextButton("NEW GAME", skin);
        btnNewGame.getLabel().setFontScale(0.45f);
        table.add(btnNewGame).width(150).height(45).spaceTop(10);
        setBtnNewGameListener(btnNewGame);

        table.row();
        TextButton btnHighScores = new TextButton("HIGH SCORES", skin);
        btnHighScores.getLabel().setFontScale(0.45f);
        table.add(btnHighScores).width(150).height(45).spaceTop(10);
        setBtnHighScoresListener(btnHighScores);

        table.row();
        TextButton btnMainMenu = new TextButton("MAIN MENU", skin);
        btnMainMenu.getLabel().setFontScale(0.45f);
        table.add(btnMainMenu).width(150).height(45).spaceTop(10);
        setBtnMainMenuListener(btnMainMenu);

        Logger.info("Game Over View: controls created");
    }

    @Override
    public void standByState() {

        this.setVisible(false);
    }

    @Override
    public void gameOverState() {

        this.setVisible(true);
    }

    @Override
    public void setWavesCompleted(String wavesCompleted) {

        lblWavesCompleted.setText(wavesCompleted + " WAVES COMPLETED");
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

    private void setBtnHighScoresListener(Button button) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.highScores();
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
}
