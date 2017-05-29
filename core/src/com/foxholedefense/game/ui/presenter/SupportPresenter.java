package com.foxholedefense.game.ui.presenter;

import com.badlogic.gdx.math.Vector2;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.support.AirStrike;
import com.foxholedefense.game.model.actor.support.SupplyDropCrate;
import com.foxholedefense.game.service.actorplacement.AirStrikePlacement;
import com.foxholedefense.game.service.actorplacement.SupplyDropPlacement;
import com.foxholedefense.game.service.actorplacement.SupportActorPlacement;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateObserver;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.interfaces.IMessageDisplayer;
import com.foxholedefense.game.ui.view.interfaces.ISupportView;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.FHDAudio.FHDSound;
import com.foxholedefense.util.datastructures.pool.UtilPool;

/**
 * Presenter for Enlist. Handles enlisting towers
 * 
 * @author Eric
 *
 */
public class SupportPresenter implements GameUIStateObserver {
	private SupportActorPlacement supportActorPlacement;
	private AirStrikePlacement airStrikePlacement;
	private SupplyDropPlacement supplyDropPlacement;
	private GameUIStateManager uiStateManager;
	private Player player;
	private ISupportView view;
	private FHDAudio audio;
	private IMessageDisplayer messageDisplayer;
	public SupportPresenter(GameUIStateManager uiStateManager, Player player, FHDAudio audio
			, SupportActorPlacement supportActorPlacement, AirStrikePlacement airStrikePlacement, SupplyDropPlacement supplyDropPlacement
			, IMessageDisplayer messageDisplayer) {
		this.uiStateManager = uiStateManager;
		uiStateManager.attach(this);
		this.audio = audio;
		this.player = player;
		this.supplyDropPlacement = supplyDropPlacement;
		this.supportActorPlacement = supportActorPlacement;
		this.airStrikePlacement = airStrikePlacement;
		this.messageDisplayer = messageDisplayer;
	}

	/**
	 * Set the view for enlisting
	 * 
	 * @param view
	 */
	public void setView(ISupportView view) {
		this.view = view;
		stateChange(uiStateManager.getState());
	}
	
	/** 
	 * Create a Supply Drop
	 */
	public void createSupplyDrop(){
		Logger.info("Support Presenter: creating supply drop");
		audio.playSound(FHDSound.SMALL_CLICK);
		if(canAffordSupport(SupplyDropCrate.COST)) {
			supplyDropPlacement.createSupplyDrop();
			uiStateManager.setState(GameUIState.PLACING_SUPPLYDROP);
			Logger.info("Support Presenter: supply drop created");
		} else {
			Logger.info("Support Presenter: cannot afford supply drop player: " + getPlayerMoney() + " cost: " + SupplyDropCrate.COST);
			messageDisplayer.displayMessage("You cannot afford a supply drop!");
		}

	}
	
	
	/**
	 * Place a supply drop
	 */
	public void placeSupplyDrop(Vector2 location){
		Logger.info("Support Presenter: place supply drop");
		supplyDropPlacement.setLocation(location);
		view.showBtnPlace();
	}
	
	private void finishSupplyDropPlacement(){
		Logger.info("Support Presenter: finishing supply drop placement");
		if(supplyDropPlacement.isCurrentSupplyDropCrate() && uiStateManager.getState().equals(GameUIState.PLACING_SUPPLYDROP)){
			supplyDropPlacement.finishPlacement();
			Logger.info("Support Presenter: supply drop placement finished");
		}
	}
	
	/**
	 * Create an AirStrike
	 */
	public void createAirStrike(){
		Logger.info("Support Presenter: creating air strike");
		audio.playSound(FHDSound.SMALL_CLICK);
		if(canAffordSupport(AirStrike.COST)) {
			airStrikePlacement.createAirStrike();
			uiStateManager.setState(GameUIState.PLACING_AIRSTRIKE);
			Logger.info("Support Presenter: air strike created");
		} else {
			Logger.info("Support Presenter: cannot afford airstrike player: " + getPlayerMoney() + " cost: " + SupplyDropCrate.COST);
			messageDisplayer.displayMessage("You cannot afford an airstrike!");
		}
	}
	
	/**
	 * Place an Air Strike Location
	 */
	public void placeAirStrikeLocation(Vector2 location){
		Logger.info("Support Presenter: placing air strike location");
		if (!airStrikePlacement.getCurrentAirStrike().isReadyToBegin()) {
			airStrikePlacement.addLocation(UtilPool.getVector2(location));
			Logger.info("Support Presenter: air strike location placed");
			if(airStrikePlacement.getCurrentAirStrike().isReadyToBegin()){
				Logger.info("Support Presenter: air strike ready to begin");
				view.showBtnPlace();
			}
		}
	}
	
	public void finishAirStrikePlacement(){
		Logger.info("Support Presenter: finishing air strike placement");
		if (airStrikePlacement.isCurrentAirStrike() && uiStateManager.getState().equals(GameUIState.PLACING_AIRSTRIKE)) {
			airStrikePlacement.finishCurrentAirStrike();
			Logger.info("Support Presenter: air strike placement finished");
		}
	}
	
	/**
	 * Create a Support Actor
	 * 
	 */
	public void createSupportActor(String type, int cost) {
		Logger.info("Support Presenter: creating support actor");
		if(canAffordSupport(cost)) {
			audio.playSound(FHDSound.SMALL_CLICK);
			supportActorPlacement.createSupportActor(type);
			uiStateManager.setState(GameUIState.PLACING_SUPPORT);
			Logger.info("Support Presenter: support actor created");
		} else {
			Logger.info("Support Presenter: cannot afford " + type + " player: " + getPlayerMoney() + " cost: " + cost);
			messageDisplayer.displayMessage("You cannot afford a " + type + "!");
		}


	}
	
	/**
	 * Try to place a Support Actor
	 */
	public void placeSupportActor() {
		Logger.info("Support Presenter: placing support actor");
		audio.playSound(FHDSound.SMALL_CLICK);
		if(uiStateManager.getState().equals(GameUIState.PLACING_AIRSTRIKE)){
			Logger.info("Support Presenter: placing air strike");
			int cost = airStrikePlacement.getCurrentAirStrike().getCost();
			player.spendMoney(cost);
			finishAirStrikePlacement();
			uiStateManager.setStateReturn();
			Logger.info("Support Presenter: air strike placed");
		} else if (uiStateManager.getState().equals(GameUIState.PLACING_SUPPORT)){
			Logger.info("Support Presenter: placing support actor");
			int cost = supportActorPlacement.getCurrentSupportActor().getCost();
			if (supportActorPlacement.placeSupportActor()) {
				player.spendMoney(cost);
				supportActorPlacement.removeCurrentSupportActor();
				uiStateManager.setStateReturn();
				Logger.info("Support Presenter: support actor placed");
			}
		} else if (uiStateManager.getState().equals(GameUIState.PLACING_SUPPLYDROP)){
			Logger.info("Support Presenter: placing supply drop");
			int cost = supplyDropPlacement.getCurrentSupplyDropCrate().getCost();
			player.spendMoney(cost);
			finishSupplyDropPlacement();
			uiStateManager.setStateReturn();
			Logger.info("Support Presenter: supply drop placed");
		}
	}
	
	/**
	 * Cancel Support
	 */
	private void cancelSupport() {
		Logger.info("Support Presenter: canceling support");
		supportActorPlacement.removeCurrentSupportActor();
		airStrikePlacement.removeCurrentAirStrike();	
		supplyDropPlacement.removeCurrentSupplyDropCrate();
	}

	public void cancel(){
		Logger.info("Support Presenter: cancel");
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
	public void stateChange(GameUIState state) {

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
		default:
			view.standByState();
			cancelSupport();
			break;
		}

	}

}
