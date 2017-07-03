package com.foxholedefense.game.ui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.foxholedefense.game.GameStage;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.level.state.LevelStateManager;
import com.foxholedefense.game.ui.presenter.DebugPresenter;
import com.foxholedefense.game.ui.presenter.EnlistPresenter;
import com.foxholedefense.game.ui.presenter.GameOverPresenter;
import com.foxholedefense.game.ui.presenter.HUDPresenter;
import com.foxholedefense.game.ui.presenter.InspectPresenter;
import com.foxholedefense.game.ui.presenter.LevelCompletedPresenter;
import com.foxholedefense.game.ui.presenter.OptionsPresenter;
import com.foxholedefense.game.ui.presenter.PausePresenter;
import com.foxholedefense.game.ui.presenter.SupportPresenter;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.state.GameUIStateObserver;
import com.foxholedefense.game.ui.view.DebugView;
import com.foxholedefense.game.ui.view.EnlistView;
import com.foxholedefense.game.ui.view.GameOverView;
import com.foxholedefense.game.ui.view.HUDView;
import com.foxholedefense.game.ui.view.InspectView;
import com.foxholedefense.game.ui.view.LevelCompletedView;
import com.foxholedefense.game.ui.view.OptionsView;
import com.foxholedefense.game.ui.view.PauseView;
import com.foxholedefense.game.ui.view.SupportView;
import com.foxholedefense.game.ui.view.interfaces.MessageDisplayer;
import com.foxholedefense.game.ui.view.interfaces.Updatable;
import com.foxholedefense.screen.ScreenChanger;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

/**
 * Stage for handling the UI of the game
 *
 * @author Eric
 */
public class GameUIStage extends Stage implements GameUIStateObserver {

    private Player player;
    private GameUIStateManager uiStateManager;
    private LevelStateManager levelStateManager;
    private GameStateManager gameStateManager;
    private ScreenChanger screenChanger;
    private Group towerGroup;
    private InputMultiplexer imp;
    private Resources resources;
    private com.foxholedefense.game.ui.view.MessageDisplayer messageDisplayer;
    private Array<Updatable> updatablePresenters = new Array<>();

    public GameUIStage(Player player, Group towerGroup, GameUIStateManager uiStateManager,
        LevelStateManager levelStateManager, GameStateManager gameStateManager,
        ScreenChanger screenChanger, InputMultiplexer imp, Viewport viewport, Resources resources,
        FHDAudio audio, GameStage gameStage, SpriteBatch spriteBatch) {

        super(viewport, spriteBatch);
        this.imp = imp;
        this.player = player;
        this.towerGroup = towerGroup;
        this.uiStateManager = uiStateManager;
        this.levelStateManager = levelStateManager;
        this.gameStateManager = gameStateManager;
        this.resources = resources;
        this.screenChanger = screenChanger;
        uiStateManager.attach(this);
        imp.addProcessor(this);
        createUI(resources, audio, gameStage);
    }


    /**
     * Create and initialize the views and presenters of the Game UI
     */
    private void createUI(Resources resources, FHDAudio audio, GameStage gameStage) {

        Logger.info("GameUIStage: creating ui");
        Skin skin = resources.getSkin();

        HUDPresenter hudPresenter = new HUDPresenter(uiStateManager, levelStateManager,
            gameStateManager, player, audio);
        HUDView hudView = new HUDView(hudPresenter, skin, resources);
        hudPresenter.setView(hudView);

        messageDisplayer = new com.foxholedefense.game.ui.view.MessageDisplayer(skin);

        EnlistPresenter enlistPresenter = new EnlistPresenter(uiStateManager, player, audio,
            gameStage.getTowerPlacement(), messageDisplayer);
        EnlistView enlistView = new EnlistView(enlistPresenter, skin);
        enlistPresenter.setView(enlistView);

        SupportPresenter supportPresenter = new SupportPresenter(uiStateManager, player, audio,
            gameStage.getSupportActorPlacement(), gameStage.getAirStrikePlacement(),
            gameStage.getSupplyDropPlacement(), messageDisplayer);
        SupportView supportView = new SupportView(supportPresenter, skin);
        supportPresenter.setView(supportView);

        InspectPresenter inspectPresenter = new InspectPresenter(uiStateManager, levelStateManager,
            player, towerGroup, audio, messageDisplayer);
        InspectView inspectView = new InspectView(inspectPresenter, skin);
        inspectPresenter.setView(inspectView);

        OptionsPresenter optionsPresenter = new OptionsPresenter(uiStateManager, screenChanger,
            resources, audio);
        OptionsView optionsView = new OptionsView(optionsPresenter, resources);
        optionsPresenter.setView(optionsView);

        GameOverPresenter gameOverPresenter = new GameOverPresenter(uiStateManager, screenChanger,
            player, audio);
        GameOverView gameOverView = new GameOverView(gameOverPresenter, skin);
        gameOverPresenter.setView(gameOverView);

        LevelCompletedPresenter levelCompletedPresenter = new LevelCompletedPresenter(
            uiStateManager, screenChanger, audio);
        LevelCompletedView levelCompletedView = new LevelCompletedView(levelCompletedPresenter,
            skin);
        levelCompletedPresenter.setView(levelCompletedView);

        DebugPresenter debugPresenter = new DebugPresenter(uiStateManager, gameStateManager);
        DebugView debugView = new DebugView(debugPresenter, resources.getSkin());
        debugPresenter.setView(debugView);

        PausePresenter pausePresenter = new PausePresenter(uiStateManager, gameStateManager,
            screenChanger, audio);
        PauseView pauseView = new PauseView(pausePresenter, resources.getSkin());
        pausePresenter.setView(pauseView);

        this.addActor(hudView);
        this.addActor(enlistView);
        this.addActor(supportView);
        this.addActor(inspectView);
        this.addActor(optionsView);
        this.addActor(gameOverView);
        this.addActor(levelCompletedView);
        this.addActor(debugView);
        this.addActor(pauseView);
        this.addActor(messageDisplayer);

        imp.addProcessor(this);
        imp.addProcessor(enlistView);
        imp.addProcessor(supportView);
        imp.addProcessor(inspectView);

        updatablePresenters.add(inspectPresenter);

        Logger.info("GameUIStage: ui created");
    }

    @Override
    public void act(float delta) {

        super.act(delta);
        for (Updatable presenter : updatablePresenters) {
            presenter.update(delta);
        }
    }

    /**
     * Show/Hide tower ranges for all towers
     */
    private void showTowerRanges(boolean showRanges) {

        Logger.info("GameUIStage: showTowerRanges: " + showRanges);
        for (Actor tower : towerGroup.getChildren()) {
            if (tower instanceof Tower) {
                ((Tower) tower).setShowRange(showRanges);
            }
        }
    }

    @Override
    public void stateChange(GameUIState state) {

        Logger.info("GameUIStage: changing ui state: " + state.name());

        switch (state) {
            case PLACING_SUPPORT:
            case PLACING_AIRSTRIKE:
            case INSPECTING:
            case PLACING_TOWER:
                showTowerRanges(true);
                break;
            default:
                showTowerRanges(resources.getUserPreferences().getPreferences()
                    .getBoolean("showRanges", false));
                break;
        }

    }


    public MessageDisplayer getMessageDisplayer() {

        return messageDisplayer;
    }

}
