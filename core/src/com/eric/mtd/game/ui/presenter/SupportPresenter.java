package com.eric.mtd.game.ui.presenter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.interfaces.IRotatable;
import com.eric.mtd.game.model.actor.tower.Tower;
import com.eric.mtd.game.model.level.Map;
import com.eric.mtd.game.service.actorplacement.TowerPlacement;
import com.eric.mtd.game.ui.state.IGameUIStateObserver;
import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.game.ui.view.interfaces.IEnlistView;
import com.eric.mtd.game.ui.view.interfaces.ISupportView;
import com.eric.mtd.util.Resources;

/**
 * Presenter for Enlist. Handles enlisting towers
 * 
 * @author Eric
 *
 */
public class SupportPresenter implements IGameUIStateObserver {
	private GameUIStateManager uiStateManager;
	private Player player;
	private ISupportView view;
	private ActorGroups actorGroups;

	public SupportPresenter(GameUIStateManager uiStateManager, Player player, ActorGroups actorGroups) {
		this.uiStateManager = uiStateManager;
		uiStateManager.attach(this);
		this.player = player;
		this.actorGroups = actorGroups;
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
	 * Cancel Support
	 */
	public void cancelSupport() {
		uiStateManager.setStateReturn();
	}
	
	/**
	 * Show/Hide tower ranges for all towers
	 * 
	 * @param showRanges
	 */
	public void showTowerRanges(boolean showRanges) {
		if (showRanges)
			for (Actor tower : actorGroups.getTowerGroup().getChildren()) {
				if (tower instanceof Tower) {
					if (showRanges)
						((GameActor) tower).setShowRange(showRanges);
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
			Class<?> myClass = Class.forName("com.eric.mtd.game.model.actor.tower.Tower" + tower);
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
			showTowerRanges(false);
			break;
		case PLACING_SUPPORT:
			view.placingSupportState();
			showTowerRanges(true);
			break;
		default:
			view.standByState();
			showTowerRanges(false);
			break;
		}

	}

}
