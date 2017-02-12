package com.foxholedefense.game.ui.presenter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.interfaces.IRotatable;
import com.foxholedefense.game.model.actor.support.Apache;
import com.foxholedefense.game.model.level.Map;
import com.foxholedefense.game.service.actorplacement.AirStrikePlacement;
import com.foxholedefense.game.service.actorplacement.SupplyDropPlacement;
import com.foxholedefense.game.service.actorplacement.SupportActorPlacement;
import com.foxholedefense.game.service.actorplacement.TowerPlacement;
import com.foxholedefense.game.service.factory.ActorFactory;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.IGameUIStateObserver;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.interfaces.IEnlistView;
import com.foxholedefense.game.ui.view.interfaces.ISupportView;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.FHDAudio.FHDSound;

/**
 * Presenter for Enlist. Handles enlisting towers
 * 
 * @author Eric
 *
 */
public class SupportPresenter implements IGameUIStateObserver {
	private SupportActorPlacement supportActorPlacement;
	private AirStrikePlacement airStrikePlacement;
	private SupplyDropPlacement supplyDropPlacement;
	private GameUIStateManager uiStateManager;
	private Player player;
	private ISupportView view;
	private FHDAudio audio;
	public SupportPresenter(GameUIStateManager uiStateManager, Player player, FHDAudio audio
			, SupportActorPlacement supportActorPlacement, AirStrikePlacement airStrikePlacement, SupplyDropPlacement supplyDropPlacement ) {
		this.uiStateManager = uiStateManager;
		uiStateManager.attach(this);
		this.audio = audio;
		this.player = player;
		this.supplyDropPlacement = supplyDropPlacement;
		this.supportActorPlacement = supportActorPlacement;
		this.airStrikePlacement = airStrikePlacement;
	}

	/**
	 * Set the view for enlisting
	 * 
	 * @param view
	 */
	public void setView(ISupportView view) {
		this.view = view;
		changeUIState(uiStateManager.getState());
	}
	
	/** 
	 * Create a Supply Drop
	 */
	public void createSupplyDrop(){
		audio.playSound(FHDSound.SMALL_CLICK);
		supplyDropPlacement.createSupplyDrop();
		uiStateManager.setState(GameUIState.PLACING_SUPPLYDROP);
	}
	
	
	/**
	 * Place a supply drop
	 */
	public void placeSupplyDrop(Vector2 location){
		supplyDropPlacement.setLocation(location);
		view.showBtnPlace();
	}
	
	private void finishSupplyDropPlacement(){
		if(supplyDropPlacement.isCurrentSupplyDropCrate() && uiStateManager.getState().equals(GameUIState.PLACING_SUPPLYDROP)){
			supplyDropPlacement.finishPlacement();
		}
	}
	
	/**
	 * Create an AirStrike
	 */
	
	public void createAirStrike(){
		audio.playSound(FHDSound.SMALL_CLICK);
		airStrikePlacement.createAirStrike();
		uiStateManager.setState(GameUIState.PLACING_AIRSTRIKE);
	}
	
	/**
	 * Place an Air Strike Location
	 */
	public void placeAirStrikeLocation(Vector2 location){
		if (!airStrikePlacement.getCurrentAirStrike().readyToBegin()) {
			airStrikePlacement.addLocation(location);
			if(airStrikePlacement.getCurrentAirStrike().readyToBegin()){
				view.showBtnPlace();
			}
		}
	}
	
	public void finishAirStrikePlacement(){
		if (airStrikePlacement.isCurrentAirStrike() && uiStateManager.getState().equals(GameUIState.PLACING_AIRSTRIKE)) {
			airStrikePlacement.finishCurrentAirStrike();
		}
	}
	
	/**
	 * Create a Support Actor
	 * 
	 */
	public void createSupportActor(String type) {
		audio.playSound(FHDSound.SMALL_CLICK);
		supportActorPlacement.createSupportActor(type);
		uiStateManager.setState(GameUIState.PLACING_SUPPORT);
	}
	
	/**
	 * Try to place a Support Actor
	 */
	public void placeSupportActor() {
		audio.playSound(FHDSound.SMALL_CLICK);
		if(uiStateManager.getState().equals(GameUIState.PLACING_AIRSTRIKE)){
			int cost = airStrikePlacement.getCurrentAirStrike().getCost();
			player.spendMoney(cost);
			finishAirStrikePlacement();
			uiStateManager.setStateReturn();
		} else if (uiStateManager.getState().equals(GameUIState.PLACING_SUPPORT)){
			int cost = supportActorPlacement.getCurrentSupportActor().getCost();
			if (supportActorPlacement.placeSupportActor()) {
				player.spendMoney(cost);
				supportActorPlacement.removeCurrentSupportActor();
				uiStateManager.setStateReturn();
			}
		} else if (uiStateManager.getState().equals(GameUIState.PLACING_SUPPLYDROP)){
			int cost = supplyDropPlacement.getCurrentSupplyDropCrate().getCost();
			player.spendMoney(cost);
			finishSupplyDropPlacement();
			uiStateManager.setStateReturn();
		}
	}
	
	/**
	 * Cancel Support
	 */
	public void cancelSupport(boolean returnState) {
		audio.playSound(FHDSound.SMALL_CLICK);
		supportActorPlacement.removeCurrentSupportActor();
		airStrikePlacement.removeCurrentAirStrike();	
		supplyDropPlacement.removeCurrentSupplyDropCrate();
		if(returnState) {
			uiStateManager.setStateReturn();
		}
	}
	
	public int getPlayerMoney(){
		return player.getMoney();
	}
	
	/**
	 * Move the Support Actor
	 * 
	 * @param coords
	 *            - Position to move
	 */
	public void screenTouch(Vector2 coords, String touchType) {
		if (supportActorPlacement.isCurrentSupportActor() && uiStateManager.getState().equals(GameUIState.PLACING_SUPPORT)) {
			supportActorPlacement.moveSupportActor(coords);
			view.showBtnPlace();
		} else if (airStrikePlacement.isCurrentAirStrike() && uiStateManager.getState().equals(GameUIState.PLACING_AIRSTRIKE)
				&& touchType.equals("TouchDown")) {
			this.placeAirStrikeLocation(coords);
		} else if (supplyDropPlacement.isCurrentSupplyDropCrate() && uiStateManager.getState().equals(GameUIState.PLACING_SUPPLYDROP)){
			this.placeSupplyDrop(coords);
		}
	}

	/**
	 * Determines if the support actor can be purchased.
	 * 
	 * @param supportCost - Cost of the support actor
	 * @return boolean - if the tower can be purchased.
	 */
	public boolean canAffordSupport(int supportCost) {
		return (supportCost <= player.getMoney());
	}

	@Override
	public void changeUIState(GameUIState state) {
		switch (state) {
		case SUPPORT:
			view.supportState();
			break;
		case PLACING_SUPPORT:
			view.placingSupportState();
			break;
		case PLACING_AIRSTRIKE:
		case PLACING_SUPPLYDROP:
			view.placingSupportState();
			break;
		case GAME_OVER:
		case LEVEL_COMPLETED:
			cancelSupport(false);
			break;
		default:
			view.standByState();
			break;
		}

	}

}
