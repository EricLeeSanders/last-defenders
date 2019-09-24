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
    private Resources resources;

    public GameOverView(GameOverPresenter presenter, Resources resources) {

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

        Logger.info("Game Over View: creating controls");

        Skin skin = resources.getSkin();

        Table table = new Table();
        table.setTransform(false);
        table.setBackground(skin.getDrawable("main-panel"));
        table.setSize(325, 300);
        table.setPosition(getStage().getViewport().getWorldWidth() / 2, getStage().getViewport().getWorldHeight() / 2, Align.center );
        this.addActor(table);

        Label lblTitle = new Label("Game Over", skin);
        lblTitle.setHeight(50);
        float lblTitleX = table.getX(Align.center);
        float lblTitleY = table.getY(Align.top) - (lblTitle.getHeight()/2);
        lblTitle.setPosition(lblTitleX, lblTitleY, Align.center);
        lblTitle.setFontScale(0.7f * resources.getFontScale());
        lblTitle.setAlignment(Align.center);
        this.addActor(lblTitle);

        table.row();
        lblWavesCompleted = new Label("0", skin);
        lblWavesCompleted.setFontScale(0.45f * resources.getFontScale());
        lblWavesCompleted.setAlignment(Align.center);
        table.add(lblWavesCompleted).width(275).height(40);

        table.row();
        TextButton btnNewGame = new TextButton("New Game", skin);
        btnNewGame.getLabel().setFontScale(0.45f * resources.getFontScale());
        table.add(btnNewGame).width(150).height(45).spaceTop(10);
        setBtnNewGameListener(btnNewGame);

        if(presenter.isSignedInToGPS()) {
            table.row();
            TextButton btnLeaderboard = new TextButton("Leaderboard", skin);
            btnLeaderboard.getLabel().setFontScale(0.45f * resources.getFontScale());
            table.add(btnLeaderboard).width(150).height(45).spaceTop(10);
            setBtnLeaderboardListener(btnLeaderboard);
        }

        table.row();
        TextButton btnMainMenu = new TextButton("Main Menu", skin);
        btnMainMenu.getLabel().setFontScale(0.45f * resources.getFontScale());
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

        lblWavesCompleted.setText(wavesCompleted + " Waves Completed");
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

    private void setBtnLeaderboardListener(Button button) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.leaderboard();
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
