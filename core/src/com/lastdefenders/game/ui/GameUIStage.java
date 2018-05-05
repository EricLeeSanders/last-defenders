package com.lastdefenders.game.ui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.game.GameStage;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.level.state.LevelStateManager;
import com.lastdefenders.game.ui.presenter.DebugPresenter;
import com.lastdefenders.game.ui.presenter.EnlistPresenter;
import com.lastdefenders.game.ui.presenter.GameOverPresenter;
import com.lastdefenders.game.ui.presenter.HUDPresenter;
import com.lastdefenders.game.ui.presenter.InspectPresenter;
import com.lastdefenders.game.ui.presenter.LevelCompletedPresenter;
import com.lastdefenders.game.ui.presenter.OptionsPresenter;
import com.lastdefenders.game.ui.presenter.PausePresenter;
import com.lastdefenders.game.ui.presenter.SupportPresenter;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.game.ui.state.GameUIStateObserver;
import com.lastdefenders.game.ui.view.DebugView;
import com.lastdefenders.game.ui.view.EnlistView;
import com.lastdefenders.game.ui.view.GameOverView;
import com.lastdefenders.game.ui.view.HUDView;
import com.lastdefenders.game.ui.view.InspectView;
import com.lastdefenders.game.ui.view.LevelCompletedView;
import com.lastdefenders.game.ui.view.MessageDisplayerImpl;
import com.lastdefenders.game.ui.view.OptionsView;
import com.lastdefenders.game.ui.view.PathDisplayer;
import com.lastdefenders.game.ui.view.PauseView;
import com.lastdefenders.game.ui.view.SupportView;
import com.lastdefenders.game.ui.view.interfaces.MessageDisplayer;
import com.lastdefenders.game.ui.view.interfaces.Updatable;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.state.GameStateManager;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

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
    private MessageDisplayerImpl messageDisplayer;
    private Array<Updatable> updatablePresenters = new Array<>();

    public GameUIStage(Player player, Group towerGroup, GameUIStateManager uiStateManager,
        LevelStateManager levelStateManager, GameStateManager gameStateManager,
        ScreenChanger screenChanger, InputMultiplexer imp, Viewport viewport, Resources resources,
        LDAudio audio, GameStage gameStage, SpriteBatch spriteBatch) {

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
    private void createUI(Resources resources, LDAudio audio, GameStage gameStage) {

        Logger.info("GameUIStage: creating ui");
        Skin skin = resources.getSkin();

        HUDPresenter hudPresenter = new HUDPresenter(uiStateManager, levelStateManager,
            gameStateManager, player, audio);
        HUDView hudView = new HUDView(hudPresenter, resources);
        this.addActor(hudView);
        hudView.init();
        hudPresenter.setView(hudView);

        messageDisplayer = new MessageDisplayerImpl(resources);


        EnlistPresenter enlistPresenter = new EnlistPresenter(uiStateManager, player, audio,
            gameStage.getTowerPlacement(), messageDisplayer, gameStage.getViewport());
        EnlistView enlistView = new EnlistView(enlistPresenter, resources);
        this.addActor(enlistView);
        enlistView.init();
        enlistPresenter.setView(enlistView);

        SupportPresenter supportPresenter = new SupportPresenter(uiStateManager, player, audio,
            gameStage.getSupportActorPlacement(), gameStage.getAirStrikePlacement(),
            gameStage.getSupplyDropPlacement(), messageDisplayer, gameStage.getViewport());
        SupportView supportView = new SupportView(supportPresenter, resources);
        this.addActor(supportView);
        supportView.init();
        supportPresenter.setView(supportView);

        InspectPresenter inspectPresenter = new InspectPresenter(uiStateManager, levelStateManager,
            player, towerGroup, audio, messageDisplayer, gameStage.getViewport());
        InspectView inspectView = new InspectView(inspectPresenter, resources);
        this.addActor(inspectView);
        inspectView.init();
        inspectPresenter.setView(inspectView);

        OptionsPresenter optionsPresenter = new OptionsPresenter(uiStateManager, screenChanger,
            resources, audio);
        OptionsView optionsView = new OptionsView(optionsPresenter, resources);
        this.addActor(optionsView);
        optionsView.init();
        optionsPresenter.setView(optionsView);

        GameOverPresenter gameOverPresenter = new GameOverPresenter(uiStateManager, screenChanger,
            player, audio);
        GameOverView gameOverView = new GameOverView(gameOverPresenter, resources);
        this.addActor(gameOverView);
        gameOverView.init();
        gameOverPresenter.setView(gameOverView);

        LevelCompletedPresenter levelCompletedPresenter = new LevelCompletedPresenter(
            uiStateManager, screenChanger, audio);
        LevelCompletedView levelCompletedView = new LevelCompletedView(levelCompletedPresenter,
            resources);
        this.addActor(levelCompletedView);
        levelCompletedView.init();
        levelCompletedPresenter.setView(levelCompletedView);

        DebugPresenter debugPresenter = new DebugPresenter(uiStateManager, gameStateManager);
        DebugView debugView = new DebugView(debugPresenter, resources);
        this.addActor(debugView);
        debugView.init();
        debugPresenter.setView(debugView);


        PausePresenter pausePresenter = new PausePresenter(uiStateManager, gameStateManager,
            screenChanger, audio);
        PauseView pauseView = new PauseView(pausePresenter, resources);
        this.addActor(pauseView);
        pauseView.init();
        pausePresenter.setView(pauseView);

        this.addActor(messageDisplayer);

        PathDisplayer pathDisplayer = new PathDisplayer(resources, gameStage.getMap());
        this.addActor(pathDisplayer);

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
