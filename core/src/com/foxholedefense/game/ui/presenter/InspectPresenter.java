package com.foxholedefense.game.ui.presenter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.helper.CollisionDetection;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.ai.TowerAI;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.ICombatActorObserver;
import com.foxholedefense.game.model.actor.combat.tower.ITowerObserver;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.level.state.ILevelStateObserver;
import com.foxholedefense.game.model.level.state.LevelStateManager;
import com.foxholedefense.game.model.level.state.LevelStateManager.LevelState;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.IGameUIStateObserver;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.interfaces.IInspectView;
import com.foxholedefense.game.ui.view.interfaces.IMessageDisplayer;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;

/**
 * Presenter for Inspect
 * 
 * @author Eric
 *
 */
public class InspectPresenter implements IGameUIStateObserver, ILevelStateObserver, ITowerObserver, ICombatActorObserver {
	private GameUIStateManager uiStateManager;
	private LevelStateManager levelStateManager;
	private Tower selectedTower;
	private Player player;
	private Group towerGroup;
	private IInspectView view;
	private FHDAudio audio;
	private IMessageDisplayer messageDisplayer;
	private boolean dischargeDisabled;
	public InspectPresenter(GameUIStateManager uiStateManager, LevelStateManager levelStateManager, Player player, Group towerGroup, FHDAudio audio, IMessageDisplayer messageDisplayer) {
		this.uiStateManager = uiStateManager;
		uiStateManager.attach(this);
		this.levelStateManager = levelStateManager;
		levelStateManager.attach(this);
		this.player = player;
		this.towerGroup = towerGroup;
		this.audio = audio;
		this.messageDisplayer = messageDisplayer;
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
		Logger.info("Inspect Presenter: close inspect");
		audio.playSound(FHDSound.SMALL_CLICK);
		resetInspect();
		uiStateManager.setStateReturn();

	}

	/**
	 * Resets the inspect
	 */
	private void resetInspect(){
		Logger.info("Inspect Presenter: reset Inspect");
		if(selectedTower != null){
			selectedTower.detachCombatActor(this);
			selectedTower = null;
		}
	}
	
	/**
	 * Change the target priority of the tower
	 */
	public void changeTargetPriority() {
		Logger.info("Inspect Presenter: changing target priority");
		audio.playSound(FHDSound.SMALL_CLICK);
		if (selectedTower != null) {
			TowerAI ai = TowerAI.values()[(selectedTower.getAI().getPosition() + 1) % 4];
			selectedTower.setAI(ai);
			view.update(selectedTower);
		}

	}

	/**
	 * Increase the tower's attack
	 */
	public void increaseAttack() {
		Logger.info("Inspect Presenter: increasing attack");
		audio.playSound(FHDSound.SMALL_CLICK);

		if(canUpgradeTower(selectedTower.getAttackIncreaseCost(), selectedTower.hasIncreasedAttack())) {
			Logger.info("Inspect Presenter: increased tower attack");
			player.spendMoney(selectedTower.getAttackIncreaseCost());
			selectedTower.increaseAttack();
			view.update(selectedTower);
		}
	}

	/**
	 * Give the tower armor
	 */
	public void giveArmor() {
		Logger.info("Inspect Presenter: giving armor");
		audio.playSound(FHDSound.SMALL_CLICK);
		if(canUpgradeTower(selectedTower.getArmorCost(), selectedTower.hasArmor())) {
			Logger.info("Inspect Presenter: tower given armor");
			player.spendMoney(selectedTower.getArmorCost());
			selectedTower.setHasArmor(true);
			view.update(selectedTower);
		}
	}

	/**
	 * Increase the tower's range
	 */
	public void increaseRange() {
		Logger.info("Inspect Presenter: increasing range");
		audio.playSound(FHDSound.SMALL_CLICK);
		if(canUpgradeTower(selectedTower.getRangeIncreaseCost(), selectedTower.hasIncreasedRange())) {
			Logger.info("Inspect Presenter: increased tower range");
			player.spendMoney(selectedTower.getRangeIncreaseCost());
			selectedTower.increaseRange();
			view.update(selectedTower);
		}
	}

	/**
	 * Increase the tower's attack speed
	 */
	public void increaseSpeed() {
		Logger.info("Inspect Presenter: increasing speed");
		audio.playSound(FHDSound.SMALL_CLICK);
		if(canUpgradeTower(selectedTower.getSpeedIncreaseCost(), selectedTower.hasIncreasedSpeed())) {
			Logger.info("Inspect Presenter: increased tower speed");
			player.spendMoney(selectedTower.getSpeedIncreaseCost());
			selectedTower.increaseSpeed();
			view.update(selectedTower);
		}
	}

	/**
	 * Checks if we can upgrade the tower. May display a message to the user.
	 * @param cost
	 * @param hasUpgrade
     * @return - boolean - if the tower can be upgraded
     */
	private boolean canUpgradeTower(int cost, boolean hasUpgrade){

		if (selectedTower == null) {
			Logger.info("Inspect Presenter canUpgradeTower: selectdTower is null");
			return false;
		}

		if(hasUpgrade){
			Logger.info("Inspect Presenter canUpgradeTower: selectdTower already has this upgrade");
			messageDisplayer.displayMessage(selectedTower.getName() + " already has this upgrade!");
			return false;
		}

		if(!canAffordUpgrade(cost)){
			Logger.info("Upgrade cannot be afforded: player: " + player.getMoney() + " upgrade: " + cost);
			messageDisplayer.displayMessage("Cannot afford this upgrade!");
			return false;
		}

		return true;
	}

	/**
	 * Discharge and Sell the tower
	 */
	public void dishcharge() {
		Logger.info("Inspect Presenter: discharging");
		if(isDischargeDisabled()){
			messageDisplayer.displayMessage("Cannot discharge " + selectedTower.getName() + " while a wave is in progress!");
			return;
		}
		if (selectedTower != null) {
			audio.playSound(FHDSound.SELL);
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
			Actor hitActor = CollisionDetection.towerHit(towerGroup.getChildren(), coords);
			if (hitActor != null) {
				if (hitActor instanceof Tower) {
					Logger.info("Inspect Presenter: inspecting tower");
					selectedTower = (Tower) hitActor;
					selectedTower.attachTower(this);
					selectedTower.attachCombatActor(this);
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

	public boolean isDischargeDisabled(){
		return dischargeDisabled;
	}

	public void setDischargeDisabled(boolean disabled){
		this.dischargeDisabled = disabled;
		view.dischargeDisabled(disabled);
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
			setDischargeDisabled(true);
			break;
		case STANDBY:
			setDischargeDisabled(false);
			break;
		default:
			break;
		}
		
	}

	@Override
	public void notifyCombatActor(CombatActor actor, CombatActorEvent event){

		if(selectedTower == null
			|| event.equals(CombatActorEvent.DEAD)){
			closeInspect();
		}
	}

	@Override
	public void notifyTower(Tower tower, TowerEvent event) {

		view.update(tower);
	}
}
