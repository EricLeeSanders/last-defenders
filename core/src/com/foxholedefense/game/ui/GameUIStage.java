package com.foxholedefense.game.ui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.level.Map;
import com.foxholedefense.game.model.level.state.LevelStateManager;
import com.foxholedefense.game.service.factory.ActorFactory;
import com.foxholedefense.game.ui.presenter.EnlistPresenter;
import com.foxholedefense.game.ui.presenter.GameOverPresenter;
import com.foxholedefense.game.ui.presenter.HUDPresenter;
import com.foxholedefense.game.ui.presenter.InspectPresenter;
import com.foxholedefense.game.ui.presenter.LevelCompletedPresenter;
import com.foxholedefense.game.ui.presenter.OptionsPresenter;
import com.foxholedefense.game.ui.presenter.SupportPresenter;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.IGameUIStateObserver;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.EnlistView;
import com.foxholedefense.game.ui.view.GameOverView;
import com.foxholedefense.game.ui.view.HUDView;
import com.foxholedefense.game.ui.view.InspectView;
import com.foxholedefense.game.ui.view.LevelCompletedView;
import com.foxholedefense.game.ui.view.OptionsView;
import com.foxholedefense.game.ui.view.SupportView;
import com.foxholedefense.screen.IScreenChanger;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;

/**
 * Stage for handling the UI of the game
 * 
 * @author Eric
 *
 */
public class GameUIStage extends Stage implements IGameUIStateObserver{
	private HUDView hudView;
	private InspectView inspectView;
	private EnlistView enlistView;
	private EnlistPresenter enlistPresenter;
	private SupportView supportView;
	private SupportPresenter supportPresenter;
	private HUDPresenter hudPresenter;
	private InspectPresenter inspectPresenter;
	private OptionsView optionsView;
	private OptionsPresenter optionsPresenter;
	private GameOverPresenter gameOverPresenter;
	private GameOverView gameOverView;
	private LevelCompletedPresenter levelCompletedPresenter;
	private LevelCompletedView levelCompletedView;
	private Player player;
	private GameUIStateManager uiStateManager;
	private LevelStateManager levelStateManager;
	private GameStateManager gameStateManager;
	private IScreenChanger screenChanger;
	private ActorGroups actorGroups;
	private ActorFactory actorFactory;
	private InputMultiplexer imp;
	private Map map;
	private Resources resources;
	public GameUIStage(Player player, ActorGroups actorGroups, ActorFactory actorFactory
			, GameUIStateManager uiStateManager, LevelStateManager levelStateManager
			, GameStateManager gameStateManager, IScreenChanger screenChanger
			, InputMultiplexer imp, Viewport viewport, Map map, Resources resources, FHDAudio audio) {
		super(viewport);
		this.map = map;
		this.imp = imp;
		this.player = player;
		this.actorGroups = actorGroups;
		this.actorFactory = actorFactory;
		this.uiStateManager = uiStateManager;
		this.levelStateManager = levelStateManager;
		this.gameStateManager = gameStateManager;
		this.resources = resources;
		this.screenChanger = screenChanger;
		uiStateManager.attach(this);
		imp.addProcessor(this);
		createUI(resources, audio);
	}

	/**
	 * Create and initialize the views and presenters of the Game UI
	 */
	public void createUI(Resources resources, FHDAudio audio) {
		Skin skin = resources.getSkin(Resources.SKIN_JSON);
		this.enlistPresenter = new EnlistPresenter(uiStateManager, player, actorGroups, actorFactory, map, audio);
		this.enlistView = new EnlistView(enlistPresenter, skin);
		enlistPresenter.setView(enlistView);
		
		this.supportPresenter = new SupportPresenter(uiStateManager, player, actorGroups, actorFactory, audio);
		this.supportView = new SupportView(supportPresenter, skin);
		supportPresenter.setView(supportView);
		
		this.hudPresenter = new HUDPresenter(uiStateManager, levelStateManager, gameStateManager, player, resources, audio);
		this.hudView = new HUDView(hudPresenter, skin, resources);
		hudPresenter.setView(hudView);
		//hudView.setFillParent(true);

		this.inspectPresenter = new InspectPresenter(uiStateManager, levelStateManager, player, actorGroups, audio);
		this.inspectView = new InspectView(inspectPresenter, skin);
		inspectPresenter.setView(inspectView);

		this.optionsPresenter = new OptionsPresenter(uiStateManager, gameStateManager, screenChanger, resources, audio);
		this.optionsView = new OptionsView(optionsPresenter, resources);
		optionsPresenter.setView(optionsView);

		this.gameOverPresenter = new GameOverPresenter(uiStateManager, screenChanger, player, audio);
		this.gameOverView = new GameOverView(gameOverPresenter, skin);
		gameOverPresenter.setView(gameOverView);
		
		this.levelCompletedPresenter = new LevelCompletedPresenter(player, gameStateManager, uiStateManager, screenChanger, audio);
		this.levelCompletedView = new LevelCompletedView(levelCompletedPresenter, skin);
		levelCompletedPresenter.setView(levelCompletedView);
		
		this.addActor(hudView);
		this.addActor(enlistView);
		this.addActor(supportView);
		this.addActor(inspectView);
		this.addActor(optionsView);
		this.addActor(gameOverView);
		this.addActor(levelCompletedView);

		imp.addProcessor(this);
		imp.addProcessor(enlistView);
		imp.addProcessor(supportView);
		imp.addProcessor(inspectView);
	}
	
	/**
	 * Show/Hide tower ranges for all towers
	 * 
	 * @param showRanges
	 */
	private void showTowerRanges(boolean showRanges) {
		for (Actor tower : actorGroups.getTowerGroup().getChildren()) {
			if (tower instanceof Tower) {
				((Tower) tower).setShowRange(showRanges);
			}
		}
	}
	@Override
	public void changeUIState(GameUIState state) {
		if(resources.getUserPreferences().getPreferences().getBoolean("showRanges", false)){
			showTowerRanges(true);
			return;
		}
		switch (state) {
		case PLACING_SUPPORT:
		case PLACING_AIRSTRIKE:
		case INSPECTING:
		case PLACING_TOWER:
			showTowerRanges(true);
			break;
		default:
			showTowerRanges(false);
			break;
		}
		
	}

}
