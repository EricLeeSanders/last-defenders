package com.eric.mtd.game.ui.presenter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.combat.tower.Tower;
import com.eric.mtd.game.model.actor.interfaces.IRotatable;
import com.eric.mtd.game.model.actor.support.Apache;
import com.eric.mtd.game.model.level.Map;
import com.eric.mtd.game.service.actorplacement.AirStrikePlacement;
import com.eric.mtd.game.service.actorplacement.SupportActorPlacement;
import com.eric.mtd.game.service.actorplacement.TowerPlacement;
import com.eric.mtd.game.ui.state.IGameUIStateObserver;
import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.game.ui.view.interfaces.IEnlistView;
import com.eric.mtd.game.ui.view.interfaces.ISupportView;
import com.eric.mtd.util.AudioUtil;
import com.eric.mtd.util.Resources;
import com.eric.mtd.util.AudioUtil.MTDSound;

/**
 * Presenter for Enlist. Handles enlisting towers
 * 
 * @author Eric
 *
 */
public class SupportPresenter implements IGameUIStateObserver {
	private SupportActorPlacement supportActorPlacement;
	private AirStrikePlacement airStrikePlacement;
	private GameUIStateManager uiStateManager;
	private Player player;
	private ISupportView view;
	private ActorGroups actorGroups;

	public SupportPresenter(GameUIStateManager uiStateManager, Player player, ActorGroups actorGroups) {
		this.uiStateManager = uiStateManager;
		uiStateManager.attach(this);
		this.player = player;
		this.actorGroups = actorGroups;
		supportActorPlacement = new SupportActorPlacement(actorGroups);
		airStrikePlacement = new AirStrikePlacement(actorGroups);
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
	 * Create an AirStrike
	 */
	
	public void createAirStrike(){
		AudioUtil.playSound(MTDSound.SMALL_CLICK);
		airStrikePlacement.createAirStrike();
		uiStateManager.setState(GameUIState.PLACING_AIRSTRIKE);
	}
	
	/**
	 * Place an Air Strike Location
	 */
	public void placeAirStrikeLocation(Vector2 location){
		AudioUtil.playSound(MTDSound.SMALL_CLICK);
		if (!airStrikePlacement.getCurrentAirStrike().readyToBegin()) {
			airStrikePlacement.addLocation(location);
			if(airStrikePlacement.getCurrentAirStrike().readyToBegin()){
				view.showBtnPlace();
			}
		}
	}
	
	public void finishAirStrikePlacement(){
		AudioUtil.playSound(MTDSound.SMALL_CLICK);
		if (airStrikePlacement.isCurrentAirStrike() && uiStateManager.getState().equals(GameUIState.PLACING_AIRSTRIKE)) {
			airStrikePlacement.getCurrentAirStrike().beginAirStrike();
			airStrikePlacement.finishCurrentAirStrike();
		}
	}
	
	/**
	 * Create a Support Actor
	 * 
	 */
	public void createSupportActor(String type) {
		AudioUtil.playSound(MTDSound.SMALL_CLICK);
		supportActorPlacement.createSupportActor(type);
		uiStateManager.setState(GameUIState.PLACING_SUPPORT);
	}
	
	/**
	 * Try to place a Support Actor
	 */
	public void placeSupportActor() {
		AudioUtil.playSound(MTDSound.SMALL_CLICK);
		int cost;
		if(uiStateManager.getState().equals(GameUIState.PLACING_AIRSTRIKE)){
			cost = airStrikePlacement.getCurrentAirStrike().getCost();
			player.spendMoney(cost);
			finishAirStrikePlacement();
			uiStateManager.setStateReturn();
		}else if (uiStateManager.getState().equals(GameUIState.PLACING_SUPPORT)){
			cost = supportActorPlacement.getCurrentSupportActor().getCost();
			if (supportActorPlacement.placeSupportActor()) {
				player.spendMoney(cost);
				supportActorPlacement.removeCurrentSupportActor();
				uiStateManager.setStateReturn();
			}
		}
	}

	/**
	 * Cancel Support
	 */
	public void cancelSupport() {
		AudioUtil.playSound(MTDSound.SMALL_CLICK);
		supportActorPlacement.removeCurrentSupportActor();
		airStrikePlacement.removeCurrentAirStrike();		
		uiStateManager.setStateReturn();
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
		}
		if (airStrikePlacement.isCurrentAirStrike() && uiStateManager.getState().equals(GameUIState.PLACING_AIRSTRIKE)
				&& touchType.equals("TouchDown")) {
			this.placeAirStrikeLocation(coords);
			
		}
	}

	/**
	 * Determines if the support actor can be purchased.
	 * 
	 * @param supportActor
	 *            - Support Actor to be purchased
	 * @return boolean - if the tower can be purchased.
	 */
	public boolean canAffordTower(String supportActor) {

		try {
			Class<?> myClass = Class.forName("com.eric.mtd.game.model.actor.support." + supportActor);
			Field field = ClassReflection.getDeclaredField(myClass, "COST");
			field.setAccessible(true);
			int cost = (Integer) field.get(null);
			if (cost > player.getMoney()) {
				return false;
			} else {
				return true;
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ReflectionException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
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
			view.placingSupportState();
			break;
		default:
			view.standByState();
			break;
		}

	}

}
