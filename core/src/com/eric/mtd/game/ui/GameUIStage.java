package com.eric.mtd.game.ui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.level.state.LevelStateManager;
import com.eric.mtd.game.ui.presenter.EnlistPresenter;
import com.eric.mtd.game.ui.presenter.GameOverPresenter;
import com.eric.mtd.game.ui.presenter.HUDPresenter;
import com.eric.mtd.game.ui.presenter.InspectPresenter;
import com.eric.mtd.game.ui.presenter.OptionsPresenter;
import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.view.EnlistView;
import com.eric.mtd.game.ui.view.GameOverView;
import com.eric.mtd.game.ui.view.HUDView;
import com.eric.mtd.game.ui.view.InspectView;
import com.eric.mtd.game.ui.view.OptionsView;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.util.Resources;

/**
 * Stage for handling the UI of the game
 * 
 * @author Eric
 *
 */
public class GameUIStage extends Stage {
	private HUDView hudView;
	private InspectView inspectView;
	private EnlistView enlistView;
	private EnlistPresenter enlistPresenter;
	private HUDPresenter hudPresenter;
	private InspectPresenter inspectPresenter;
	private OptionsView optionsView;
	private OptionsPresenter optionsPresenter;
	private GameOverPresenter gameOverPresenter;
	private GameOverView gameOverView;

	private Player player;
	private int intLevel;
	private GameUIStateManager uiStateManager;
	private LevelStateManager levelStateManager;
	private GameStateManager gameStateManager;
	private ScreenStateManager screenStateManager;
	private ActorGroups actorGroups;
	private InputMultiplexer imp;

	public GameUIStage(int intLevel, Player player, ActorGroups actorGroups, GameUIStateManager uiStateManager, LevelStateManager levelStateManager, GameStateManager gameStateManager, ScreenStateManager screenStateManager, InputMultiplexer imp) {
		super(new ScalingViewport(Scaling.stretch, Resources.SCREEN_WIDTH, Resources.SCREEN_HEIGHT, new OrthographicCamera()));
		this.imp = imp;
		this.intLevel = intLevel;
		this.player = player;
		this.actorGroups = actorGroups;
		this.uiStateManager = uiStateManager;
		this.levelStateManager = levelStateManager;
		this.gameStateManager = gameStateManager;
		this.screenStateManager = screenStateManager;
		imp.addProcessor(this);
		createUI();
	}

	/**
	 * Create and initialize the views and presenters of the Game UI
	 */
	public void createUI() {
		this.enlistPresenter = new EnlistPresenter(uiStateManager, player, intLevel, actorGroups);
		this.enlistView = new EnlistView(enlistPresenter);
		enlistPresenter.setView(enlistView);

		this.hudPresenter = new HUDPresenter(uiStateManager, levelStateManager, gameStateManager, player);
		this.hudView = new HUDView(hudPresenter);
		hudPresenter.setView(hudView);

		this.inspectPresenter = new InspectPresenter(uiStateManager, levelStateManager, player, actorGroups);
		this.inspectView = new InspectView(inspectPresenter);
		inspectPresenter.setView(inspectView);

		this.optionsPresenter = new OptionsPresenter(uiStateManager, gameStateManager, screenStateManager);
		this.optionsView = new OptionsView(optionsPresenter);
		optionsPresenter.setView(optionsView);

		this.gameOverPresenter = new GameOverPresenter(uiStateManager, screenStateManager, player);
		this.gameOverView = new GameOverView(gameOverPresenter);
		gameOverPresenter.setView(gameOverView);

		this.addActor(hudView);
		this.addActor(enlistView);
		this.addActor(inspectView);
		this.addActor(optionsView);
		this.addActor(gameOverView);

		imp.addProcessor(this);
		imp.addProcessor(enlistView);
		imp.addProcessor(inspectView);
	}

}
