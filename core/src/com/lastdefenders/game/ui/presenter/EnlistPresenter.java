package com.lastdefenders.game.ui.presenter;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.combat.tower.TowerFlameThrower;
import com.lastdefenders.game.model.actor.combat.tower.TowerHumvee;
import com.lastdefenders.game.model.actor.combat.tower.TowerMachineGun;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import com.lastdefenders.game.model.actor.combat.tower.TowerRocketLauncher;
import com.lastdefenders.game.model.actor.combat.tower.TowerSniper;
import com.lastdefenders.game.model.actor.combat.tower.TowerTank;
import com.lastdefenders.game.model.actor.combat.tower.TowerTurret;
import com.lastdefenders.game.model.actor.interfaces.IRotatable;
import com.lastdefenders.game.service.actorplacement.TowerPlacement;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.game.ui.state.GameUIStateObserver;
import com.lastdefenders.game.ui.view.interfaces.IEnlistView;
import com.lastdefenders.game.ui.view.interfaces.MessageDisplayer;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.LDAudio.LDSound;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import com.lastdefenders.util.UtilPool;
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
    private LDAudio audio;
    private MessageDisplayer messageDisplayer;
    private Map<String, Integer> towerCosts = new HashMap<>();
    private Viewport gameViewport;
    private Resources resources;

    public EnlistPresenter(GameUIStateManager uiStateManager, Player player,
        LDAudio audio, TowerPlacement towerPlacement, MessageDisplayer messageDisplayer,
        Viewport gameViewport, Resources resources) {

        this.uiStateManager = uiStateManager;
        uiStateManager.attach(this);
        this.player = player;
        this.audio = audio;
        this.towerPlacement = towerPlacement;
        this.messageDisplayer = messageDisplayer;
        this.gameViewport = gameViewport;
        this.resources = resources;
        initTowerCostsMap();
    }

    private void initTowerCostsMap() {

        towerCosts.put("Rifle", resources.getTowerAttribute(TowerRifle.class).getCost());
        towerCosts.put("MachineGun", resources.getTowerAttribute(TowerMachineGun.class).getCost());
        towerCosts.put("Sniper", resources.getTowerAttribute(TowerSniper.class).getCost());
        towerCosts.put("FlameThrower", resources.getTowerAttribute(TowerFlameThrower.class).getCost());
        towerCosts.put("RocketLauncher", resources.getTowerAttribute(TowerRocketLauncher.class).getCost());
        towerCosts.put("Tank", resources.getTowerAttribute(TowerTank.class).getCost());
        towerCosts.put("Humvee", resources.getTowerAttribute(TowerHumvee.class).getCost());

    }

    /**
     * Set the view for enlisting
     */
    public void setView(IEnlistView view) {

        this.view = view;
        stateChange(uiStateManager.getState());
    }

    /**
     * Create a tower
     */
    public void createTower(String strEnlistTower) {

        Logger.info("Enlist Presenter: creating tower");
        int cost = towerCosts.get(strEnlistTower);
        audio.playSound(LDSound.SMALL_CLICK);
        if (canCreateTower(cost)) {
            towerPlacement.createTower(strEnlistTower);
            Logger.info("Enlist Presenter: tower created");
            uiStateManager.setState(GameUIState.PLACING_TOWER);
        } else {
            Logger.info(
                "Enlist Presenter: cannot afford " + strEnlistTower + " player: " + getPlayerMoney()
                    + " cost: " + cost);
            messageDisplayer
                .displayMessage("You cannot afford to enlist a " + strEnlistTower + "!");
        }
    }

    /**
     * Try to place a tower
     */
    public void placeTower() {

        Logger.info("Enlist Presenter: placing tower");
        if (canPlaceTower()) {
            if (towerPlacement.placeTower()) {
                audio.playSound(LDSound.ACTOR_PLACE);
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
        audio.playSound(LDSound.SMALL_CLICK);
        uiStateManager.setStateReturn();
    }

    /**
     * Move the tower
     *
     * @param x
     * @param y
     */
    public void moveTower(float x, float y) {

        if (canMoveTower()) {
            LDVector2 coords = (LDVector2) gameViewport.unproject(UtilPool.getVector2(x, y));
            towerPlacement.moveTower(coords);
            coords.free();
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

    public Map<String, Integer> getTowerCostMap(){
        return towerCosts;
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
     */
    private boolean canMoveTower() {

        return uiStateManager.getState().equals(GameUIState.PLACING_TOWER)
            && towerPlacement.isCurrentTower();
    }

    /**
     * Determines if the tower can be placed
     */
    private boolean canPlaceTower() {

        return uiStateManager.getState().equals(GameUIState.PLACING_TOWER)
            && towerPlacement.isCurrentTower();
    }

    /**
     * Determines if the tower can be created
     *
     * @param cost - cost of the tower
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
