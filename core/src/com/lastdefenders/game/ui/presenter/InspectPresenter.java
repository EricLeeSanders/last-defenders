package com.lastdefenders.game.ui.presenter;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.game.helper.CollisionDetection;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.ai.TowerAIType;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.level.state.LevelStateManager;
import com.lastdefenders.game.model.level.state.LevelStateManager.LevelState;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.game.ui.state.GameUIStateObserver;
import com.lastdefenders.game.ui.view.interfaces.IInspectView;
import com.lastdefenders.game.ui.view.interfaces.MessageDisplayer;
import com.lastdefenders.game.ui.view.interfaces.Updatable;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.LDAudio.LDSound;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import com.lastdefenders.util.datastructures.pool.UtilPool;

/**
 * Presenter for Inspect
 *
 * @author Eric
 */
public class InspectPresenter implements Updatable, GameUIStateObserver {

    private GameUIStateManager uiStateManager;
    private LevelStateManager levelStateManager;
    private Tower selectedTower;
    private Player player;
    private Group towerGroup;
    private IInspectView view;
    private LDAudio audio;
    private MessageDisplayer messageDisplayer;
    private Viewport gameViewport;

    public InspectPresenter(GameUIStateManager uiStateManager, LevelStateManager levelStateManager,
        Player player, Group towerGroup, LDAudio audio, MessageDisplayer messageDisplayer,
        Viewport gameViewport) {

        this.uiStateManager = uiStateManager;
        this.levelStateManager = levelStateManager;
        uiStateManager.attach(this);
        this.player = player;
        this.towerGroup = towerGroup;
        this.audio = audio;
        this.messageDisplayer = messageDisplayer;
        this.gameViewport = gameViewport;
    }

    /**
     * Sets the Inspect View
     */
    public void setView(IInspectView view) {

        this.view = view;
        stateChange(uiStateManager.getState());
    }

    @Override
    public void update(float delta) {

        if (!uiStateManager.getState().equals(GameUIState.INSPECTING)) {
            return;
        }

        if (!isTowerInteractable()) {
            closeInspect();
        }

        if (selectedTower != null) {
            view.update(selectedTower);
        }
    }


    /**
     * Close and finishing inspecting
     */
    public void closeInspect() {

        Logger.info("Inspect Presenter: close inspect");
        audio.playSound(LDSound.SMALL_CLICK);
        resetInspect();
        uiStateManager.setStateReturn();

    }

    /**
     * Resets the inspect
     */
    private void resetInspect() {

        Logger.info("Inspect Presenter: reset Inspect");
        selectedTower = null;
    }

    /**
     * Change the target priority of the tower
     */
    public void changeTargetPriority() {

        Logger.info("Inspect Presenter: changing target priority");
        audio.playSound(LDSound.SMALL_CLICK);
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
        audio.playSound(LDSound.SMALL_CLICK);

        if (canUpgradeTower(selectedTower.getAttackIncreaseCost(),
            selectedTower.hasIncreasedAttack())) {
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
        audio.playSound(LDSound.SMALL_CLICK);
        if (canUpgradeTower(selectedTower.getArmorCost(), selectedTower.hasArmor())) {
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
        audio.playSound(LDSound.SMALL_CLICK);
        if (canUpgradeTower(selectedTower.getRangeIncreaseCost(),
            selectedTower.hasIncreasedRange())) {
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
        audio.playSound(LDSound.SMALL_CLICK);
        if (canUpgradeTower(selectedTower.getSpeedIncreaseCost(),
            selectedTower.hasIncreasedSpeed())) {
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
        if (canDischargeTower()) {
            audio.playSound(LDSound.SELL);
            player.giveMoney(selectedTower.getSellCost());
            selectedTower.sellTower();
            closeInspect();
        } else {
            if (isDischargeDisabled()) {
                messageDisplayer.displayMessage("Cannot discharge " + selectedTower.getName()
                    + " while a wave is in progress!");
            }
        }
    }

    /**
     * Open the inspection window for a tower that is clicked.
     */
    public void inspectTower(float x, float y) {

        if (canInspectTowers()) {

            LDVector2 coords = (LDVector2) gameViewport.unproject(UtilPool.getVector2(x, y));
            Actor hitActor = CollisionDetection.towerHit(towerGroup.getChildren(), coords);
            coords.free();
            if (hitActor != null) {
                if (canInspectTower((Tower) hitActor)) {
                    Logger.info("Inspect Presenter: inspecting tower");
                    selectedTower = (Tower) hitActor;
                    uiStateManager.setState(GameUIState.INSPECTING);
                }
            }
        }
    }

    /**
     * Get the players amount of money
     *
     * @return int - player money
     */
    public int getPlayerMoney() {

        return player.getMoney();
    }

    /**
     * Determines if the tower can be inspected
     */
    private boolean canInspectTower(Tower tower) {

        return !tower.isDead() && tower.isActive();
    }

    /**
     * Determines if towers can be inspected
     */
    private boolean canInspectTowers() {

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
     *
     * @return - boolean - if the tower can be upgraded
     */
    private boolean canUpgradeTower(int cost, boolean hasUpgrade) {

        if (!isTowerInteractable() || !uiStateManager.getState().equals(GameUIState.INSPECTING)) {
            return false;
        }

        if (hasUpgrade) {
            Logger.info("Inspect Presenter canUpgradeTower: selectdTower already has this upgrade");
            messageDisplayer.displayMessage(selectedTower.getName() + " already has this upgrade!");
            return false;
        }

        if (!canAffordUpgrade(cost)) {
            Logger.info(
                "Upgrade cannot be afforded: player: " + player.getMoney() + " upgrade: " + cost);
            messageDisplayer.displayMessage("Cannot afford this upgrade!");
            return false;
        }

        return true;
    }

    /**
     * Determines if the the target priority can be changed
     */
    private boolean canChangeTargetPriority() {

        return uiStateManager.getState().equals(GameUIState.INSPECTING)
            && isTowerInteractable();
    }

    /**
     * Determines if the tower is interactable
     */
    private boolean isTowerInteractable() {

        return selectedTower != null && !selectedTower.isDead() && selectedTower.isActive();
    }

    /**
     * Determines if the tower can be discharged
     */
    private boolean canDischargeTower() {

        return uiStateManager.getState().equals(GameUIState.INSPECTING)
            && isTowerInteractable() && !isDischargeDisabled();
    }

    public boolean isDischargeDisabled() {

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
