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
import com.foxholedefense.game.model.level.Map;
import com.foxholedefense.game.service.actorplacement.TowerPlacement;
import com.foxholedefense.game.service.factory.ActorFactory;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.IGameUIStateObserver;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.interfaces.IEnlistView;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;
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
	private ActorGroups actorGroups;
	private FHDAudio audio;
	public EnlistPresenter(GameUIStateManager uiStateManager, Player player
			, ActorGroups actorGroups, ActorFactory actorFactory, Map map, FHDAudio audio) {
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
		audio.playSound(FHDSound.SMALL_CLICK);
		towerPlacement.createTower(strEnlistTower);
		uiStateManager.setState(GameUIState.PLACING_TOWER);
	}

	/**
	 * Try to place a tower
	 */
	public void placeTower() {
		int cost = towerPlacement.getCurrentTower().getCost();
		if (towerPlacement.placeTower()) {
			audio.playSound(FHDSound.ACTOR_PLACE);
			uiStateManager.setStateReturn();
			player.spendMoney(cost);
			towerPlacement.removeCurrentTower();
			

		}
	}

	/**
	 * Cancel enlisting
	 */
	public void cancelEnlist() {
		audio.playSound(FHDSound.SMALL_CLICK);
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
			Class<?> myClass = Class.forName("com.foxholedefense.game.model.actor.combat.tower.Tower" + tower);
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
