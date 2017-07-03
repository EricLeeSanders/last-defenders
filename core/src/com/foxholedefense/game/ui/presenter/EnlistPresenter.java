package com.foxholedefense.game.ui.presenter;

import com.badlogic.gdx.math.Vector2;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.combat.tower.TowerFlameThrower;
import com.foxholedefense.game.model.actor.combat.tower.TowerMachineGun;
import com.foxholedefense.game.model.actor.combat.tower.TowerRifle;
import com.foxholedefense.game.model.actor.combat.tower.TowerRocketLauncher;
import com.foxholedefense.game.model.actor.combat.tower.TowerSniper;
import com.foxholedefense.game.model.actor.combat.tower.TowerTank;
import com.foxholedefense.game.model.actor.combat.tower.TowerTurret;
import com.foxholedefense.game.model.actor.interfaces.IRotatable;
import com.foxholedefense.game.service.actorplacement.TowerPlacement;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.state.GameUIStateObserver;
import com.foxholedefense.game.ui.view.interfaces.IEnlistView;
import com.foxholedefense.game.ui.view.interfaces.MessageDisplayer;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;
import com.foxholedefense.util.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Presenter for Enlist. Handles enlisting towers
 *
 * @author Eric
 */
public class EnlistPresenter implements GameUIStateObserver {

    private TowerPlacement towerPlacement;
    private GameUIStateManager uiStateManager;
    private Player player;
    private IEnlistView view;
    private FHDAudio audio;
    private MessageDisplayer messageDisplayer;
    private Map<String, Integer> towerCosts = new HashMap<>();

    public EnlistPresenter(GameUIStateManager uiStateManager, Player player,
                           FHDAudio audio, TowerPlacement towerPlacement, MessageDisplayer messageDisplayer) {

        this.uiStateManager = uiStateManager;
        uiStateManager.attach(this);
        this.player = player;
        this.audio = audio;
        this.towerPlacement = towerPlacement;
        this.messageDisplayer = messageDisplayer;
        initTowerCostsMap();
    }

    private void initTowerCostsMap() {

        towerCosts.put("Rifle", TowerRifle.COST);
        towerCosts.put("MachineGun", TowerMachineGun.COST);
        towerCosts.put("Sniper", TowerSniper.COST);
        towerCosts.put("FlameThrower", TowerFlameThrower.COST);
        towerCosts.put("RocketLauncher", TowerRocketLauncher.COST);
        towerCosts.put("Tank", TowerTank.COST);
        towerCosts.put("Turret", TowerTurret.COST);

    }

    /**
     * Set the view for enlisting
     *
     * @param view
     */
    public void setView(IEnlistView view) {
        this.view = view;
        stateChange(uiStateManager.getState());
    }

    /**
     * Create a tower
     *
     * @param strEnlistTower
     */
    public void createTower(String strEnlistTower) {
        Logger.info("Enlist Presenter: creating tower");
        int cost = towerCosts.get(strEnlistTower);
        audio.playSound(FHDSound.SMALL_CLICK);
        if (canCreateTower(cost)) {
            towerPlacement.createTower(strEnlistTower);
            Logger.info("Enlist Presenter: tower created");
            uiStateManager.setState(GameUIState.PLACING_TOWER);
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
        if (canPlaceTower()) {
            if (towerPlacement.placeTower()) {
                audio.playSound(FHDSound.ACTOR_PLACE);
                int cost = towerPlacement.getCurrentTower().getCost();
                player.spendMoney(cost);
                Logger.info("Enlist Presenter: tower placed");
                towerPlacement.removeCurrentTower(false);
                uiStateManager.setStateReturn();
            } else {
                Logger.info("Enlist Presenter: cannot place tower");
                messageDisplayer.displayMessage("Cannot place a tower here!");
            }
        }
    }


    private void cancelEnlist() {
        Logger.info("Enlist Presenter: canceling enlist");
        towerPlacement.removeCurrentTower(true);
    }

    /**
     * Cancel enlisting
     */
    public void cancel() {
        Logger.info("Enlist Presenter: cancel");
        uiStateManager.setStateReturn();
    }

    /**
     * Move the tower
     *
     * @param coords - Position to move
     */
    public void moveTower(Vector2 coords) {

        if (canMoveTower()) {
            towerPlacement.moveTower(coords);
            view.showBtnPlace();
            if (isTowerRotatable()) {
                view.showBtnRotate();
            }
        }
    }

    /**
     * Rotate the tower
     */
    public void rotateTower(float delta) {

        if (isTowerRotatable()) {
            towerPlacement.rotateTower(60 * delta);
        }
    }

    /**
     * Determine if tower is able to be rotated
     *
     * @return
     */
    private boolean isTowerRotatable() {
        return towerPlacement.getCurrentTower() instanceof IRotatable;
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
     * Determines if the tower can be purchased.
     *
     * @param towerCost - Cost of the tower
     * @return boolean - if the tower can be purchased.
     */
    public boolean canAffordTower(int towerCost) {
        return (towerCost <= player.getMoney());
    }

    /**
     * Determines if the tower can be moved
     *
     * @return
     */
    private boolean canMoveTower() {

        return uiStateManager.getState().equals(GameUIState.PLACING_TOWER)
                && towerPlacement.isCurrentTower();
    }

    /**
     * Determines if the tower can be placed
     *
     * @return
     */
    private boolean canPlaceTower() {

        return uiStateManager.getState().equals(GameUIState.PLACING_TOWER)
                && towerPlacement.isCurrentTower();
    }

    /**
     * Determines if the tower can be created
     *
     * @param cost - cost of the tower
     * @return
     */
    private boolean canCreateTower(int cost) {

        return uiStateManager.getState().equals(GameUIState.ENLISTING)
                && canAffordTower(cost);
    }

    @Override
    public void stateChange(GameUIState state) {

        switch (state) {
            case ENLISTING:
                view.enlistingState();
                break;
            case PLACING_TOWER:
                view.placingTowerState();
                break;
            default:
                view.standByState();
                cancelEnlist();
                break;
        }

    }

}
