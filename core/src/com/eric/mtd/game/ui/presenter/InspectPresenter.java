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
import com.eric.mtd.util.MTDAudio;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.MTDAudio.MTDSound;

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
	private MTDAudio audio;
	public InspectPresenter(GameUIStateManager uiStateManager, LevelStateManager levelStateManager, Player player, ActorGroups actorGroups, MTDAudio audio) {
		this.uiStateManager = uiStateManager;
		uiStateManager.attach(this);
		this.levelStateManager = levelStateManager;
		levelStateManager.attach(this);
		this.player = player;
		this.actorGroups = actorGroups;
		this.audio = audio;
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
		audio.playSound(MTDSound.SMALL_CLICK);
		resetInspect();
		uiStateManager.setStateReturn();

	}

	/**
	 * Resets the inspect
	 */
	private void resetInspect(){
		Logger.info("Detaching Inspect from Tower");
		if(selectedTower != null){
			selectedTower.detach(this);
			selectedTower = null;
		}
	}
	
	/**
	 * Change the target priority of the tower
	 */
	public void changeTargetPriority() {
		audio.playSound(MTDSound.SMALL_CLICK);
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
		audio.playSound(MTDSound.SMALL_CLICK);
		if (selectedTower != null) {
			if (!selectedTower.hasIncreasedAttack()) {
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
		audio.playSound(MTDSound.SMALL_CLICK);
		if (selectedTower != null) {
			if (!selectedTower.hasArmor()) {
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
		audio.playSound(MTDSound.SMALL_CLICK);
		if (selectedTower != null) {
			if (!selectedTower.hasIncreasedRange()) {
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
		audio.playSound(MTDSound.SMALL_CLICK);
		if (selectedTower != null) {
			if (!selectedTower.hasIncreasedSpeed()) {
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
			audio.playSound(MTDSound.SELL);
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
	 * Gets the type/name of the selected tower
	 * @return
	 */
	public String getTowerName(){
		return selectedTower.getName();
	}
	
	/**
	 * Get the players amount of money
	 * @return int - player money
	 */
	public int getPlayerMoney(){
		return player.getMoney();
	}
	
	/**
	 * Determine if the the upgrade is affordable
	 * 
	 * @param upgradeCost - Cost of the upgrade
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
		default:
			resetInspect();
			view.standByState();
			break;
		}

	}

	@Override
	public void changeLevelState(LevelState state) {
		switch(state){
		case WAVE_IN_PROGRESS:
			view.dischargeEnabled(false);
			break;
		case STANDBY:
			view.dischargeEnabled(true);
			break;
		default:
			resetInspect();
			view.standByState();
			break;
		}
		
	}

	@Override
	public void notifty() {
		if(selectedTower == null
			|| selectedTower.isDead()){
			closeInspect();
		}
		else{
			view.update(selectedTower);
		}
		
	}

}
