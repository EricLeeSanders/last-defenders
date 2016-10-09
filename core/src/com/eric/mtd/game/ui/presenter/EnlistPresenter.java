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
import com.eric.mtd.game.model.level.Map;
import com.eric.mtd.game.service.actorplacement.TowerPlacement;
import com.eric.mtd.game.service.factory.ActorFactory;
import com.eric.mtd.game.ui.state.IGameUIStateObserver;
import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.game.ui.view.interfaces.IEnlistView;
import com.eric.mtd.util.MTDAudio;
import com.eric.mtd.util.MTDAudio.MTDSound;
import com.eric.mtd.util.Resources;

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
	private ActorGroups actorGroups;
	private MTDAudio audio;
	public EnlistPresenter(GameUIStateManager uiStateManager, Player player
			, ActorGroups actorGroups, ActorFactory actorFactory, Map map, MTDAudio audio) {
		this.uiStateManager = uiStateManager;
		uiStateManager.attach(this);
		this.player = player;
		this.audio = audio;
		this.actorGroups = actorGroups;
		towerPlacement = new TowerPlacement(map, actorGroups, actorFactory);
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
	public void createTower(String strEnlistTower) {
		audio.playSound(MTDSound.SMALL_CLICK);
		towerPlacement.createTower(strEnlistTower);
		uiStateManager.setState(GameUIState.PLACING_TOWER);
	}

	/**
	 * Try to place a tower
	 */
	public void placeTower() {
		int cost = towerPlacement.getCurrentTower().getCost();
		if (towerPlacement.placeTower()) {
			audio.playSound(MTDSound.ACTOR_PLACE);
			uiStateManager.setStateReturn();
			player.spendMoney(cost);
			towerPlacement.removeCurrentTower();
			

		}
	}

	/**
	 * Cancel enlisting
	 */
	public void cancelEnlist() {
		audio.playSound(MTDSound.SMALL_CLICK);
		uiStateManager.setStateReturn();
		towerPlacement.removeCurrentTower();
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
	public void rotateTower() {
		towerPlacement.rotateTower(1);
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
	 * @param tower
	 *            - Tower to be purchased
	 * @return boolean - if the tower can be purchased.
	 */
	public boolean canAffordTower(String tower) {

		try {
			Class<?> myClass = Class.forName("com.eric.mtd.game.model.actor.combat.tower.Tower" + tower);
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
		case ENLISTING:
			view.enlistingState();
			break;
		case PLACING_TOWER:
			view.placingTowerState();
			break;
		default:
			view.standByState();
			break;
		}

	}

}
