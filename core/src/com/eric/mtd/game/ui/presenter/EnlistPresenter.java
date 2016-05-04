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
import com.eric.mtd.game.ui.state.IGameUIStateObserver;
import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.game.ui.view.interfaces.IEnlistView;
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

	public EnlistPresenter(GameUIStateManager uiStateManager, Player player
			, ActorGroups actorGroups, Map map) {
		this.uiStateManager = uiStateManager;
		uiStateManager.attach(this);
		this.player = player;
		this.actorGroups = actorGroups;
		towerPlacement = new TowerPlacement(map, actorGroups);
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
		towerPlacement.createTower(strEnlistTower);
		uiStateManager.setState(GameUIState.PLACING_TOWER);
	}

	/**
	 * Try to place a tower
	 */
	public void placeTower() {
		int cost = towerPlacement.getCurrentTower().getCost();
		if (towerPlacement.placeTower()) {
			uiStateManager.setStateReturn();
			player.spendMoney(cost);
			towerPlacement.removeCurrentTower();

		}
	}

	/**
	 * Cancel enlisting
	 */
	public void cancelEnlist() {
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
		if (towerPlacement.getCurrentTower() instanceof IRotatable) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Show/Hide tower ranges for all towers
	 * 
	 * @param showRanges
	 */
	public void showTowerRanges(boolean showRanges) {
		if (showRanges){
			for (Actor tower : actorGroups.getTowerGroup().getChildren()) {
				if (tower instanceof Tower) {
					((CombatActor) tower).setShowRange(showRanges);
				}
			}
		}
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
			showTowerRanges(false);
			break;
		case PLACING_TOWER:
			view.placingTowerState();
			showTowerRanges(true);
			break;
		default:
			view.standByState();
			showTowerRanges(false);
			break;
		}

	}

}
