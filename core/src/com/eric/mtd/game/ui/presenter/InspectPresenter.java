package com.eric.mtd.game.ui.presenter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.eric.mtd.game.helper.CollisionDetection;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.ai.TowerTargetPriority;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.combat.ICombatActorObserver;
import com.eric.mtd.game.model.actor.combat.tower.Tower;
import com.eric.mtd.game.model.level.state.ILevelStateObserver;
import com.eric.mtd.game.model.level.state.LevelStateManager;
import com.eric.mtd.game.model.level.state.LevelStateManager.LevelState;
import com.eric.mtd.game.ui.state.IGameUIStateObserver;
import com.eric.mtd.game.ui.view.interfaces.IInspectView;
import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.util.AudioUtil;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.AudioUtil.MTDSound;

/**
 * Presenter for Inspect
 * 
 * @author Eric
 *
 */
public class InspectPresenter implements IGameUIStateObserver, ILevelStateObserver, ICombatActorObserver {
	private GameUIStateManager uiStateManager;
	private LevelStateManager levelStateManager;
	private Tower selectedTower;
	private Player player;
	private ActorGroups actorGroups;
	private IInspectView view;

	public InspectPresenter(GameUIStateManager uiStateManager, LevelStateManager levelStateManager, Player player, ActorGroups actorGroups) {
		this.uiStateManager = uiStateManager;
		uiStateManager.attach(this);
		this.levelStateManager = levelStateManager;
		levelStateManager.attach(this);
		this.player = player;
		this.actorGroups = actorGroups;
	}

	/**
	 * Sets the Inspect View
	 * 
	 * @param view
	 */
	public void setView(IInspectView view) {
		this.view = view;
		changeUIState(uiStateManager.getState());
	}

	/**
	 * Close and finishing inspecting
	 */
	public void closeInspect() {
		selectedTower.detach(this);
		view.standByState();
		uiStateManager.setStateReturn();
		
		selectedTower = null;

	}

	/**
	 * Change the target priority of the tower
	 */
	public void changeTargetPriority() {
		if (selectedTower != null) {
			TowerTargetPriority priority = TowerTargetPriority.values()[(selectedTower.getTargetPriority().getPosition() + 1) % 4];
			selectedTower.setTargetPriority(priority);
			view.update(selectedTower);
		}

	}

	/**
	 * Increase the tower's attack
	 */
	public void increaseAttack() {
		if (selectedTower != null) {
			if (selectedTower.getAttackLevel() < Tower.TOWER_ATTACK_LEVEL_MAX) {
				player.spendMoney(selectedTower.getAttackIncreaseCost());
				selectedTower.increaseAttack();
				view.update(selectedTower);
			}
		}
	}

	/**
	 * Give the tower armor
	 */
	public void giveArmor() {
		if (selectedTower != null) {
			if (!(selectedTower.hasArmor())) {
				player.spendMoney(selectedTower.getArmorCost());
				selectedTower.setHasArmor(true);
				view.update(selectedTower);
			}
		}
	}

	/**
	 * Increase the tower's range
	 */
	public void increaseRange() {
		if (selectedTower != null) {
			if (selectedTower.getRangeLevel() < Tower.TOWER_RANGE_LEVEL_MAX) {
				player.spendMoney(selectedTower.getRangeIncreaseCost());
				selectedTower.increaseRange();
				view.update(selectedTower);
			}
		}
	}

	/**
	 * Increase the tower's attack speed
	 */
	public void increaseSpeed() {
		if (selectedTower != null) {
			if (selectedTower.getSpeedLevel() < Tower.TOWER_ATTACK_SPEED_LEVEL_MAX) {
				player.spendMoney(selectedTower.getSpeedIncreaseCost());
				selectedTower.increaseSpeed();
				view.update(selectedTower);
			}
		}
	}

	/**
	 * Discharge and Sell the tower
	 */
	public void dishcharge() {
		if (selectedTower != null) {
			AudioUtil.playSound(MTDSound.SELL);
			player.giveMoney(selectedTower.getSellCost());
			selectedTower.sellTower();
			closeInspect();

		}
	}

	/**
	 * Open the inspection window for a tower that is clicked. Only open the
	 * tower inspect if the state of the UI is in Standby or Wave In Progress
	 * 
	 * @param coords
	 */
	public void inspectTower(Vector2 coords) {
		if (uiStateManager.getState().equals(GameUIState.STANDBY) || uiStateManager.getState().equals(GameUIState.WAVE_IN_PROGRESS)) {
			Actor hitActor = CollisionDetection.towerHit(actorGroups.getTowerGroup().getChildren(), coords);
			if (Logger.DEBUG)
				System.out.println("Screen X:" + coords.x + " Screen Y:" + coords.y);
			if (hitActor != null) {
				if (hitActor instanceof Tower) {
					selectedTower = (Tower) hitActor;
					selectedTower.attach(this);
					uiStateManager.setState(GameUIState.INSPECTING);
				}
			}
		}
	}

	/**
	 * Determine if the the upgrade is affordable
	 * 
	 * @param upgradeCost
	 *            - Cost of the upgrade
	 * @return boolean
	 */
	public boolean canAffordUpgrade(int upgradeCost) {
		if (upgradeCost <= player.getMoney()) {
			return true;
		}
		return false;
	}

	@Override
	public void changeUIState(GameUIState state) {
		switch (state) {
		case INSPECTING:
			view.inspectingState();
			view.update(selectedTower);
			break;
		case STANDBY:
			view.standByState();
			break;
		default:
			break;
		}

	}

	@Override
	public void changeLevelState(LevelState state) {
		switch(state){
		case SPAWNING_ENEMIES:
		case WAVE_IN_PROGRESS:
			view.dischargeEnabled(false);
			break;
		case STANDBY:
			view.dischargeEnabled(true);
		default:
			break;
		}
		
	}

	@Override
	public void notifty() {
		if(selectedTower.isDead()
			|| selectedTower == null){
			closeInspect();
		}
		else{
			view.update(selectedTower);
		}
		
	}

}
