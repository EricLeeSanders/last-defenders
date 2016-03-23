package com.eric.mtd.game.ui.stage;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.level.state.LevelStateManager;
import com.eric.mtd.game.ui.controller.EnlistController;
import com.eric.mtd.game.ui.controller.GameOverController;
import com.eric.mtd.game.ui.controller.HUDController;
import com.eric.mtd.game.ui.controller.InspectController;
import com.eric.mtd.game.ui.controller.OptionsController;
import com.eric.mtd.game.ui.controller.PerksController;
import com.eric.mtd.game.ui.controller.interfaces.IEnlistController;
import com.eric.mtd.game.ui.controller.interfaces.IGameOverController;
import com.eric.mtd.game.ui.controller.interfaces.IHUDController;
import com.eric.mtd.game.ui.controller.interfaces.IInspectController;
import com.eric.mtd.game.ui.controller.interfaces.IOptionsController;
import com.eric.mtd.game.ui.controller.interfaces.IPerksController;
import com.eric.mtd.game.ui.state.IUIStateObserver;
import com.eric.mtd.game.ui.state.UIStateManager;
import com.eric.mtd.game.ui.state.UIStateManager.UIState;
import com.eric.mtd.game.ui.view.EnlistGroup;
import com.eric.mtd.game.ui.view.GameOverGroup;
import com.eric.mtd.game.ui.view.HUDGroup;
import com.eric.mtd.game.ui.view.InspectGroup;
import com.eric.mtd.game.ui.view.OptionsGroup;
import com.eric.mtd.game.ui.view.PerksGroup;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.state.GameStateManager;

public class GameUIStage extends Stage implements IUIStateObserver{
	private HUDGroup hudGroup;
	private InspectGroup inspectGroup;
	private EnlistGroup enlistGroup;
	private IEnlistController enlistController;
	private IHUDController hudController;
	private IInspectController inspectController;
	private IPerksController perksController;
	private PerksGroup perksGroup;
	private OptionsGroup optionsGroup;
	private IOptionsController optionsController;
	private IGameOverController gameOverController;
	private GameOverGroup gameOverGroup;
	
	private Player player;
	private int intLevel;
	private UIStateManager uiStateManager;
	private LevelStateManager levelStateManager;
	private GameStateManager gameStateManager;
	private ScreenStateManager screenStateManager;
	private ActorGroups actorGroups;
	public GameUIStage(int intLevel, Player player, ActorGroups actorGroups, UIStateManager uiStateManager, 
			LevelStateManager levelStateManager, GameStateManager gameStateManager, ScreenStateManager screenStateManager){
		this.intLevel = intLevel;
		this.player = player;
		this.actorGroups = actorGroups;
		this.uiStateManager = uiStateManager;
		uiStateManager.attach(this);
		this.levelStateManager = levelStateManager;
		this.gameStateManager = gameStateManager;
		this.screenStateManager = screenStateManager;
		createUI();
	}
	public void createUI(){
	    this.enlistController = new EnlistController(uiStateManager, player, intLevel, actorGroups);
	    this.enlistGroup = new EnlistGroup(enlistController, uiStateManager);
	    	enlistGroup.setVisible(false);
	    	
	    this.hudController = new HUDController(uiStateManager, levelStateManager, gameStateManager, player);
	    this.hudGroup = new HUDGroup(uiStateManager, levelStateManager, hudController);
	    	hudGroup.setVisible(true);
	    	
	    this.inspectController = new InspectController(uiStateManager, player, actorGroups);
	    this.inspectGroup = new InspectGroup(uiStateManager, inspectController);
	    	inspectGroup.setVisible(false);
	    	
	    this.perksController = new PerksController(levelStateManager, player, intLevel, actorGroups);
	    this.perksGroup = new PerksGroup(uiStateManager, perksController);
	    	perksGroup.setVisible(false);
	    	
	    this.optionsController = new OptionsController(uiStateManager, gameStateManager);
	    this.optionsGroup = new OptionsGroup(optionsController);
	    	optionsGroup.setVisible(false);
	    	
	    this.gameOverController = new GameOverController(screenStateManager, player);
	    this.gameOverGroup = new GameOverGroup(gameOverController);
	    	gameOverGroup.setVisible(false);
	    
		this.addActor(hudGroup);
		this.addActor(enlistGroup);
		this.addActor(inspectGroup);
		this.addActor(perksGroup);
		this.addActor(optionsGroup);
		this.addActor(gameOverGroup);
	}
	
    @Override
    public void act(float delta) {
        super.act(delta);
		hudGroup.update();
    }
	@Override
	public void changeUIState(UIState state) {
		switch(state){
		case STANDBY:
			perksGroup.setVisible(false);
			inspectGroup.setVisible(false);
			enlistGroup.setVisible(false);
			optionsGroup.setVisible(false);
			break;
		case PERKS:
			perksGroup.setVisible(true);
			enlistGroup.setVisible(false);
			inspectGroup.setVisible(false);
			break;
		case ENLIST:
			perksGroup.setVisible(false);
			enlistGroup.setVisible(true);
			inspectGroup.setVisible(false);
			break;
		case INSPECTING:
			perksGroup.setVisible(false);
			inspectGroup.setVisible(true);
			inspectGroup.updateInspect();
			enlistGroup.setVisible(false);
			break;	
		case OPTIONS:
			perksGroup.setVisible(false);
			inspectGroup.setVisible(false);
			enlistGroup.setVisible(false);
			optionsGroup.setVisible(true);
			break;
		case LEVEL_OVER:
			perksGroup.setVisible(false);
			inspectGroup.setVisible(false);
			enlistGroup.setVisible(false);
			optionsGroup.setVisible(false);
			gameOverGroup.update();
			gameOverGroup.setVisible(true);
			break;
		default:
			break;
		}
	}
	public HUDGroup getHudGroup() {
		return hudGroup;
	}

	public void setHudGroup(HUDGroup hudGroup) {
		this.hudGroup = hudGroup;
	}

	public InspectGroup getInspectGroup() {
		return inspectGroup;
	}

	public void setInspectGroup(InspectGroup inspectGroup) {
		this.inspectGroup = inspectGroup;
	}

	public EnlistGroup getEnlistGroup() {
		return enlistGroup;
	}

	public void setEnlistGroup(EnlistGroup enlistGroup) {
		this.enlistGroup = enlistGroup;
	}
		
}
