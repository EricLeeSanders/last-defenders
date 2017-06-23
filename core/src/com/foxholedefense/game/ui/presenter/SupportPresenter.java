package com.foxholedefense.game.ui.presenter;

import com.badlogic.gdx.math.Vector2;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.support.AirStrike;
import com.foxholedefense.game.model.actor.support.Apache;
import com.foxholedefense.game.model.actor.support.LandMine;
import com.foxholedefense.game.model.actor.support.SupplyDropCrate;
import com.foxholedefense.game.service.actorplacement.AirStrikePlacement;
import com.foxholedefense.game.service.actorplacement.SupplyDropPlacement;
import com.foxholedefense.game.service.actorplacement.SupportActorPlacement;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateObserver;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.interfaces.MessageDisplayer;
import com.foxholedefense.game.ui.view.interfaces.ISupportView;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.FHDAudio.FHDSound;
import com.foxholedefense.util.datastructures.pool.UtilPool;

import java.util.HashMap;
import java.util.Map;

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
	private MessageDisplayer messageDisplayer;
	private Map<String, Integer> supportCosts = new HashMap<>();

	public SupportPresenter(GameUIStateManager uiStateManager, Player player, FHDAudio audio
			, SupportActorPlacement supportActorPlacement, AirStrikePlacement airStrikePlacement, SupplyDropPlacement supplyDropPlacement
			, MessageDisplayer messageDisplayer) {

		this.uiStateManager = uiStateManager;
		uiStateManager.attach(this);
		this.audio = audio;
		this.player = player;
		this.supplyDropPlacement = supplyDropPlacement;
		this.supportActorPlacement = supportActorPlacement;
		this.airStrikePlacement = airStrikePlacement;
		this.messageDisplayer = messageDisplayer;
		initSupportCostsMap();
	}

	private void initSupportCostsMap(){

		supportCosts.put("Apache", Apache.COST);
		supportCosts.put("LandMine", LandMine.COST);
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
		if(canCreateSupport(SupplyDropCrate.COST)) {
			supplyDropPlacement.createSupplyDrop();
			uiStateManager.setState(GameUIState.PLACING_SUPPLYDROP);
			Logger.info("Support Presenter: supply drop created");
		} else {
			Logger.info("Support Presenter: cannot afford supply drop player: " + getPlayerMoney() + " cost: " + SupplyDropCrate.COST);
			messageDisplayer.displayMessage("You cannot afford a supply drop!");
		}

	}
	
	
	/**
	 * Move a supply drop
	 */
	public void moveSupplyDrop(Vector2 location){

		Logger.info("Support Presenter: move supply drop");
		if(canMoveSupplyDrop()){
			supplyDropPlacement.setLocation(location);
			view.showBtnPlace();
		}
	}

	/**
	 * Determine if a supply drop can be moved
	 * @return
     */
	private boolean canMoveSupplyDrop(){

		return uiStateManager.getState().equals(GameUIState.PLACING_SUPPLYDROP)
				&& supplyDropPlacement.isCurrentSupplyDropCrate();
	}
	
	private void placeSupplyDrop(){
		Logger.info("Support Presenter: place supply drop");
		if(canPlaceSupplyDrop()){
			int cost = SupplyDropCrate.COST;
			player.spendMoney(cost);
			supplyDropPlacement.placeSupplyDrop();
			uiStateManager.setStateReturn();
		}
	}

	private boolean canPlaceSupplyDrop(){

		return uiStateManager.getState().equals(GameUIState.PLACING_SUPPLYDROP)
				&& supplyDropPlacement.isCurrentSupplyDropCrate();
	}
	
	/**
	 * Create an AirStrike
	 */
	public void createAirStrike(){

		Logger.info("Support Presenter: creating air strike");
		audio.playSound(FHDSound.SMALL_CLICK);
		if(canCreateSupport(AirStrike.COST)) {
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
		if (canPlaceAirStrikeLocation()) {
			airStrikePlacement.addLocation(UtilPool.getVector2(location));
			if(airStrikePlacement.isReadyToBegin()){
				Logger.info("Support Presenter: air strike ready to begin");
				view.showBtnPlace();
			}
		}
	}

	/**
	 * Determines if an air strike location can be placed
	 * @return
     */
	private boolean canPlaceAirStrikeLocation(){

		return uiStateManager.getState().equals(GameUIState.PLACING_AIRSTRIKE)
				&& airStrikePlacement.isCurrentAirStrike()
				&& !airStrikePlacement.isReadyToBegin();
	}
	
	private void finishAirStrikePlacement(){

		Logger.info("Support Presenter: finishing air strike placement");
		if (canFinishAirStrikePlacement()) {
			int cost = AirStrike.COST;
			player.spendMoney(cost);
			airStrikePlacement.finishCurrentAirStrike();
			uiStateManager.setStateReturn();
		}
	}

	private boolean canFinishAirStrikePlacement(){

		return uiStateManager.getState().equals(GameUIState.PLACING_AIRSTRIKE)
				&& airStrikePlacement.isCurrentAirStrike()
				&& airStrikePlacement.isReadyToBegin();
	}
	
	/**
	 * Create a Support Actor
	 * 
	 */
	public void createSupportActor(String type) {

		Logger.info("Support Presenter: creating support actor");
		int cost = supportCosts.get(type);
		if(canCreateSupport(cost)) {
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
	 * Move a support actor
	 * @param location
     */
	public void moveSupportActor(Vector2 location){

		Logger.info("Support Presenter: move support actor");
		if(canMoveSupportActor()) {
			supportActorPlacement.moveSupportActor(location);
			view.showBtnPlace();
		}
	}

	/**
	 * Determine if a support actor can be moved
	 * @return
     */
	private boolean canMoveSupportActor(){

		return uiStateManager.getState().equals(GameUIState.PLACING_SUPPORT)
				&& supportActorPlacement.isCurrentSupportActor();

	}

	/**
	 * Place a support actor
	 * @return
     */
	private void placeSupportActor(){

		Logger.info("Support Presenter: placing support actor");
		if (canPlaceSupportActor()) {
			int cost = supportActorPlacement.getCurrentSupportActor().getCost();
			supportActorPlacement.placeSupportActor();
			player.spendMoney(cost);
			supportActorPlacement.removeCurrentSupportActor();
			uiStateManager.setStateReturn();
			Logger.info("Support Presenter: support actor placed");
		}
	}

	private boolean canPlaceSupportActor(){

		return uiStateManager.getState().equals(GameUIState.PLACING_SUPPORT)
				&& supportActorPlacement.isCurrentSupportActor();
	}

	/**
	 * Try to place actor
	 */
	public void placeActor() {
		Logger.info("Support Presenter: placing support actor");
		audio.playSound(FHDSound.SMALL_CLICK);
		if(uiStateManager.getState().equals(GameUIState.PLACING_AIRSTRIKE)){
			finishAirStrikePlacement();
		} else if (uiStateManager.getState().equals(GameUIState.PLACING_SUPPORT)){
			placeSupportActor();
		} else if (uiStateManager.getState().equals(GameUIState.PLACING_SUPPLYDROP)){
			placeSupplyDrop();
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
		cancelSupport();
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
			moveSupportActor(coords);
		} else if (airStrikePlacement.isCurrentAirStrike() && uiStateManager.getState().equals(GameUIState.PLACING_AIRSTRIKE)
				&& touchType.equals("TouchDown")) {
			placeAirStrikeLocation(coords);
		} else if (supplyDropPlacement.isCurrentSupplyDropCrate() && uiStateManager.getState().equals(GameUIState.PLACING_SUPPLYDROP)){
			moveSupplyDrop(coords);
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

	/**
	 * Determines if support actors can be created
	 * @param supportCost
	 * @return
     */
	private boolean canCreateSupport(int supportCost){

		return uiStateManager.getState().equals(GameUIState.SUPPORT)
				&& canAffordSupport(supportCost);

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
