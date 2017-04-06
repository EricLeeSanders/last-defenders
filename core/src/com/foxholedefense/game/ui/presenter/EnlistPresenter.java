package com.foxholedefense.game.ui.presenter;

import com.badlogic.gdx.math.Vector2;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.interfaces.IRotatable;
import com.foxholedefense.game.service.actorplacement.TowerPlacement;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.IGameUIStateObserver;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.interfaces.IEnlistView;
import com.foxholedefense.game.ui.view.interfaces.IMessageDisplayer;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.FHDAudio.FHDSound;

/**
 * Presenter for Enlist. Handles enlisting towers
 * 
 * @author Eric
 *
 */
public class EnlistPresenter implements IGameUIStateObserver {
	private TowerPlacement towerPlacement;
	private GameUIStateManager uiStateManager;
	private Player player;
	private IEnlistView view;
	private FHDAudio audio;
	private IMessageDisplayer messageDisplayer;

	public EnlistPresenter(GameUIStateManager uiStateManager, Player player
			, FHDAudio audio, TowerPlacement towerPlacement, IMessageDisplayer messageDisplayer) {
		this.uiStateManager = uiStateManager;
		uiStateManager.attach(this);
		this.player = player;
		this.audio = audio;
		this.towerPlacement = towerPlacement;
		this.messageDisplayer = messageDisplayer;
	}

	/**
	 * Set the view for enlisting
	 * 
	 * @param view
	 */
	public void setView(IEnlistView view) {
		this.view = view;
		changeUIState(uiStateManager.getState());
	}

	/**
	 * Create a tower
	 * 
	 * @param strEnlistTower
	 */
	public void createTower(String strEnlistTower, int cost) {
		Logger.info("Enlist Presenter: creating tower");
		if(canAffordTower(cost)) {
			audio.playSound(FHDSound.SMALL_CLICK);
			towerPlacement.createTower(strEnlistTower.replaceAll(" ", ""));
			uiStateManager.setState(GameUIState.PLACING_TOWER);
			Logger.info("Enlist Presenter: tower created");
		} else {
			Logger.info("Enlist Presenter: cannot afford " + strEnlistTower + " player: " + getPlayerMoney() + " cost: " + cost);
			messageDisplayer.displayMessage("You cannot afford to enlist a " + strEnlistTower + "!");
		}
	}

	/**
	 * Try to place a tower
	 */
	public void placeTower() {
		Logger.info("Enlist Presenter: placing tower");
		int cost = towerPlacement.getCurrentTower().getCost();
		if (towerPlacement.placeTower()) {
			audio.playSound(FHDSound.ACTOR_PLACE);
			uiStateManager.setStateReturn();
			player.spendMoney(cost);
			towerPlacement.removeCurrentTower();
			Logger.info("Enlist Presenter: tower placed");
		} else {
			Logger.info("Enlist Presenter: cannot place tower");
			messageDisplayer.displayMessage("Cannot place a tower here!");
		}
	}

	/**
	 * Cancel enlisting
	 */
	public void cancelEnlist(boolean returnState) {
		Logger.info("Enlist Presenter: canceling enlist: " + returnState);
		audio.playSound(FHDSound.SMALL_CLICK);
		towerPlacement.removeCurrentTower();
		if(returnState){
			uiStateManager.setStateReturn();
		}
		Logger.info("Enlist Presenter: enlist canceled");
	}

	/**
	 * Move the tower
	 *
	 * @param coords
	 *            - Position to move
	 */
	public void moveTower(Vector2 coords) {
		if (towerPlacement.isCurrentTower() && uiStateManager.getState().equals(GameUIState.PLACING_TOWER)) {
			towerPlacement.moveTower(coords);
			view.showBtnPlace();
			if(isTowerRotatable()){
				view.showBtnRotate();
			}
		}
	}

	/**
	 * Rotate the tower
	 */
	public void rotateTower(float delta) {
		towerPlacement.rotateTower(60 * delta);
	}

	/**
	 * Determine if tower is able to be rotated
	 * 
	 * @return
	 */
	public boolean isTowerRotatable() {
		return towerPlacement.getCurrentTower() instanceof IRotatable;
	}
	/**
	 * Get the players amount of money
	 * @return int - player money
	 */
	public int getPlayerMoney(){
		return player.getMoney();
	}
	/**
	 * Determines if the tower can be purchased.
	 * 
	 * @param towerCost - Cost of the tower
	 * @return boolean - if the tower can be purchased.
	 */
	public boolean canAffordTower(int towerCost) {
		return (towerCost <= player.getMoney());
	}

	@Override
	public void changeUIState(GameUIState state) {
		switch (state) {
			case ENLISTING:
				view.enlistingState();
				break;
			case PLACING_TOWER:
				view.placingTowerState();
				break;
			case GAME_OVER:
			case LEVEL_COMPLETED:
				view.standByState();
				cancelEnlist(false);
				break;
			default:
				view.standByState();
				break;
		}

	}

}
