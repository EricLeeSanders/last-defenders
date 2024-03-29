package com.lastdefenders.game.ui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.ads.AdController;
import com.lastdefenders.ads.AdControllerHelper;
import com.lastdefenders.game.GameStage;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.groups.TowerGroup;
import com.lastdefenders.game.model.actor.support.SupportActor;
import com.lastdefenders.game.model.actor.support.SupportActorCooldown;
import com.lastdefenders.game.model.level.state.LevelStateManager;
import com.lastdefenders.game.ui.presenter.AdPresenter;
import com.lastdefenders.game.ui.presenter.DebugPresenter;
import com.lastdefenders.game.ui.presenter.EnlistPresenter;
import com.lastdefenders.game.ui.presenter.GameOverPresenter;
import com.lastdefenders.game.ui.presenter.HUDPresenter;
import com.lastdefenders.game.ui.presenter.InspectPresenter;
import com.lastdefenders.game.ui.presenter.LevelCompletedPresenter;
import com.lastdefenders.game.ui.presenter.OptionsPresenter;
import com.lastdefenders.game.ui.presenter.PausePresenter;
import com.lastdefenders.game.ui.presenter.SupportPresenter;
import com.lastdefenders.game.ui.presenter.TutorialPresenter;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.game.ui.state.GameUIStateObserver;
import com.lastdefenders.game.ui.view.AdView;
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
import com.lastdefenders.game.ui.view.TutorialView;
import com.lastdefenders.game.ui.view.interfaces.MessageDisplayer;
import com.lastdefenders.game.ui.view.interfaces.Updatable;
import com.lastdefenders.googleplay.GooglePlayServices;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.sound.AudioManager;
import com.lastdefenders.state.GameStateManager;
import com.lastdefenders.store.StoreManager;
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
    private TowerGroup towerGroup;
    private InputMultiplexer imp;
    private Resources resources;
    private MessageDisplayerImpl messageDisplayer;
    private Array<Updatable> updatablePresenters = new Array<>();

    public GameUIStage(Player player, TowerGroup towerGroup, GameUIStateManager uiStateManager,
        LevelStateManager levelStateManager, GameStateManager gameStateManager,
        GooglePlayServices playServices, ScreenChanger screenChanger, InputMultiplexer imp,
        Viewport viewport, Resources resources, AudioManager audio, StoreManager storeManager,
        GameStage gameStage, SpriteBatch spriteBatch, AdControllerHelper adControllerHelper) {

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
        createUI(resources, audio, gameStage, playServices, storeManager, adControllerHelper);
    }


    /**
     * Create and initialize the views and presenters of the Game UI
     */
    private void createUI(Resources resources, AudioManager audio, GameStage gameStage,
        GooglePlayServices playServices, StoreManager storeManager, AdControllerHelper adControllerHelper) {

        Logger.info("GameUIStage: creating ui");

        HUDPresenter hudPresenter = new HUDPresenter(uiStateManager, levelStateManager,
            gameStateManager, player, audio.getSoundPlayer());
        HUDView hudView = new HUDView(hudPresenter, resources);
        addActor(hudView);
        hudView.init();
        hudPresenter.setView(hudView);

        PathDisplayer pathDisplayer = new PathDisplayer(resources, gameStage.getMap());
        addActor(pathDisplayer);

        messageDisplayer = new MessageDisplayerImpl(resources);

        EnlistPresenter enlistPresenter = new EnlistPresenter(uiStateManager, player, audio.getSoundPlayer(),
            gameStage.getTowerPlacement(), messageDisplayer, gameStage.getViewport(), resources);
        EnlistView enlistView = new EnlistView(enlistPresenter, resources);
        addActor(enlistView);
        enlistView.init();
        enlistPresenter.setView(enlistView);
        imp.addProcessor(enlistView);

        java.util.Map<Class<? extends SupportActor>, SupportActorCooldown> supportActorCooldownMapMap = gameStage.getSupportActorCooldownMap();

        SupportPresenter supportPresenter = new SupportPresenter(uiStateManager, player, audio.getSoundPlayer(),
            gameStage.getSupportActorPlacement(), messageDisplayer, gameStage.getViewport());
        SupportView supportView = new SupportView(supportPresenter, resources, supportActorCooldownMapMap);
        addActor(supportView);
        supportView.init();
        supportPresenter.setView(supportView);
        imp.addProcessor(supportView);

        InspectPresenter inspectPresenter = new InspectPresenter(uiStateManager, levelStateManager,
            player, towerGroup, audio.getSoundPlayer(), messageDisplayer, gameStage.getViewport());
        InspectView inspectView = new InspectView(inspectPresenter, resources);
        addActor(inspectView);
        inspectView.init();
        inspectPresenter.setView(inspectView);
        imp.addProcessor(inspectView);

        OptionsPresenter optionsPresenter = new OptionsPresenter(uiStateManager, screenChanger,
            resources, audio, storeManager);
        OptionsView optionsView = new OptionsView(optionsPresenter, resources);
        addActor(optionsView);
        optionsView.init();
        optionsPresenter.setView(optionsView);

        GameOverPresenter gameOverPresenter = new GameOverPresenter(uiStateManager, screenChanger,
            playServices, player, gameStage.getLevel().getActiveLevel(), audio);
        GameOverView gameOverView = new GameOverView(gameOverPresenter, resources);
        addActor(gameOverView);
        gameOverView.init();
        gameOverPresenter.setView(gameOverView);

        LevelCompletedPresenter levelCompletedPresenter = new LevelCompletedPresenter(
            uiStateManager, levelStateManager, screenChanger, audio);
        LevelCompletedView levelCompletedView = new LevelCompletedView(levelCompletedPresenter,
            resources);
        addActor(levelCompletedView);
        levelCompletedView.init();
        levelCompletedPresenter.setView(levelCompletedView);

        DebugPresenter debugPresenter = new DebugPresenter(uiStateManager, resources);
        DebugView debugView = new DebugView(debugPresenter, resources);
        addActor(debugView);
        debugView.init();
        debugPresenter.setView(debugView);

        PausePresenter pausePresenter = new PausePresenter(uiStateManager, gameStateManager,
            screenChanger, audio.getSoundPlayer());
        PauseView pauseView = new PauseView(pausePresenter, resources);
        addActor(pauseView);
        pauseView.init();
        pausePresenter.setView(pauseView);

        if(adControllerHelper.adsEnabled()) {
            AdPresenter adPresenter = new AdPresenter(adControllerHelper, storeManager,
                audio.getSoundPlayer());
            AdView adView = new AdView(adPresenter, resources);
            addActor(adView);
            adView.init();
            adPresenter.setView(adView);
            this.levelStateManager.attach(adPresenter);
        }


        addActor(messageDisplayer);

        /*
         If we're going to show the tutorial for the first game, create the presenter and the view.
         Let the TutorialPresenter display the pathDisplayer when it is done.
         */
        if(resources.getUserPreferences().getShowTutorialTips()) {
            TutorialPresenter tutorialPresenter = new TutorialPresenter(hudPresenter, resources, pathDisplayer);
            TutorialView tutorialView = new TutorialView(tutorialPresenter,
                resources.getAsset(Resources.TUTORIAL_ATLAS, TextureAtlas.class));
            addActor(tutorialView);
            tutorialPresenter.setView(tutorialView);
            tutorialPresenter.showNextTip();
            imp.addProcessor(tutorialView);
        } else {
            // Init the pathDisplayer immediately if there is no tutorial.
            pathDisplayer.init();
        }

        imp.addProcessor(this);

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
        for (Tower tower : towerGroup.getCastedChildren()) {
            tower.setShowRange(showRanges);
        }
    }

    @Override
    public void stateChange(GameUIState state) {

        Logger.info("GameUIStage: changing ui state: " + state.name());

        switch (state) {
            case PLACING_SUPPORT:
            case INSPECTING:
            case PLACING_TOWER:
                showTowerRanges(true);
                break;
            default:
                showTowerRanges(resources.getUserPreferences().getShowTowerRanges());
                break;
        }

    }


    public MessageDisplayer getMessageDisplayer() {

        return messageDisplayer;
    }

}
