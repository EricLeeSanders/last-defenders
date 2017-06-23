package com.foxholedefense.game.ui.presenter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.helper.CollisionDetection;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.ai.TowerAIType;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.level.state.LevelStateManager;
import com.foxholedefense.game.model.level.state.LevelStateManager.LevelState;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.state.GameUIStateObserver;
import com.foxholedefense.game.ui.view.interfaces.IInspectView;
import com.foxholedefense.game.ui.view.interfaces.MessageDisplayer;
import com.foxholedefense.game.ui.view.interfaces.Updatable;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;

/**
 * Presenter for Inspect
 * 
 * @author Eric
 *
 */
public class InspectPresenter implements Updatable, GameUIStateObserver{

	private GameUIStateManager uiStateManager;
	private LevelStateManager levelStateManager;
	private Tower selectedTower;
	private Player player;
	private Group towerGroup;
	private IInspectView view;
	private FHDAudio audio;
	private MessageDisplayer messageDisplayer;

	public InspectPresenter(GameUIStateManager uiStateManager, LevelStateManager levelStateManager, Player player, Group towerGroup, FHDAudio audio, MessageDisplayer messageDisplayer) {

		this.uiStateManager = uiStateManager;
		this.levelStateManager = levelStateManager;
		uiStateManager.attach(this);
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
		stateChange(uiStateManager.getState());
	}

	@Override
	public void update(float delta){

		if(!uiStateManager.getState().equals(GameUIState.INSPECTING)){
			return;
		}

		if(!isTowerInteractable()){
			closeInspect();
		}

		if(selectedTower != null){
			view.update(selectedTower);
		}
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
		selectedTower = null;
	}
	
	/**
	 * Change the target priority of the tower
	 */
	public void changeTargetPriority() {

		Logger.info("Inspect Presenter: changing target priority");
		audio.playSound(FHDSound.SMALL_CLICK);
		if (canChangeTargetPriority()) {
			TowerAIType ai = selectedTower.getAI().getNextTowerAIType();
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
	 * Discharge and Sell the tower
	 */
	public void dishcharge() {

		Logger.info("Inspect Presenter: discharging");
		if(canDischargeTower()) {
			audio.playSound(FHDSound.SELL);
			player.giveMoney(selectedTower.getSellCost());
			selectedTower.sellTower();
			closeInspect();
		} else {
			if(isDischargeDisabled()) {
				messageDisplayer.displayMessage("Cannot discharge " + selectedTower.getName() + " while a wave is in progress!");
			}
		}
	}

	/**
	 * Open the inspection window for a tower that is clicked.
	 * 
	 * @param coords
	 */
	public void inspectTower(Vector2 coords) {

		if (canInspectTowers()) {
			Actor hitActor = CollisionDetection.towerHit(towerGroup.getChildren(), coords);
			if (hitActor != null) {
				if (hitActor instanceof Tower && canInspectTower((Tower) hitActor)) {
					Logger.info("Inspect Presenter: inspecting tower");
					selectedTower = (Tower) hitActor;
					uiStateManager.setState(GameUIState.INSPECTING);
				}
			}
		}
	}

	/**
	 * Get the players amount of money
	 * @return int - player money
	 */
	public int getPlayerMoney(){
		return player.getMoney();
	}

	/**
	 * Determines if the tower can be inspected
	 * @return
     */
	private boolean canInspectTower(Tower tower){

		return !tower.isDead() && tower.isActive();
	}

	/**
	 * Determines if towers can be inspected
	 * @return
	 */
	private boolean canInspectTowers(){

		return uiStateManager.getState().equals(GameUIState.STANDBY)
				|| uiStateManager.getState().equals(GameUIState.WAVE_IN_PROGRESS);
	}
	
	/**
	 * Determine if the the upgrade is affordable
	 * 
	 * @param upgradeCost - Cost of the upgrade
	 * @return boolean
	 */
	public boolean canAffordUpgrade(int upgradeCost) {
		return upgradeCost <= player.getMoney();
	}

	/**
	 * Checks if we can upgrade the tower. May display a message to the user.
	 * @param cost
	 * @param hasUpgrade
	 * @return - boolean - if the tower can be upgraded
	 */
	private boolean canUpgradeTower(int cost, boolean hasUpgrade){

		if (!isTowerInteractable() || !uiStateManager.getState().equals(GameUIState.INSPECTING)) {
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
	 * Determines if the the target priority can be changed
	 * @return
     */
	private boolean canChangeTargetPriority(){

		return uiStateManager.getState().equals(GameUIState.INSPECTING)
			&& isTowerInteractable();
	}

	/**
	 * Determines if the tower is interactable
	 * @return
     */
	private boolean isTowerInteractable(){

		return selectedTower != null && !selectedTower.isDead() && selectedTower.isActive();
	}

	/**
	 * Determines if the tower can be discharged
	 * @return
     */
	private boolean canDischargeTower(){

		return uiStateManager.getState().equals(GameUIState.INSPECTING)
				&& isTowerInteractable() && !isDischargeDisabled();
	}

	private boolean isDischargeDisabled(){

		return levelStateManager.getState().equals(LevelState.WAVE_IN_PROGRESS);
	}

	@Override
	public void stateChange(GameUIState state) {

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
}
