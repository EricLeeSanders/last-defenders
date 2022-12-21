package com.lastdefenders.game.ui.presenter;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.support.AirStrike;
import com.lastdefenders.game.model.actor.support.Apache;
import com.lastdefenders.game.model.actor.support.LandMine;
import com.lastdefenders.game.model.actor.support.SupportActor;
import com.lastdefenders.game.model.actor.support.supplydrop.SupplyDrop;
import com.lastdefenders.game.service.actorplacement.SupportActorPlacement;
import com.lastdefenders.game.service.validator.ValidationResponseEnum;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.game.ui.state.GameUIStateObserver;
import com.lastdefenders.game.ui.view.interfaces.ISupportView;
import com.lastdefenders.game.ui.view.interfaces.MessageDisplayer;
import com.lastdefenders.sound.LDAudio;
import com.lastdefenders.sound.LDAudio.LDSound;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import com.lastdefenders.util.UtilPool;
import java.util.HashMap;
import java.util.Map;

/**
 * Presenter for Enlist. Handles enlisting towers
 *
 * @author Eric
 */
public class SupportPresenter implements GameUIStateObserver {

    private SupportActorPlacement supportActorPlacement;
    private GameUIStateManager uiStateManager;
    private Player player;
    private ISupportView view;
    private LDAudio audio;
    private MessageDisplayer messageDisplayer;
    private Map<Class<?>, Integer> supportCosts = new HashMap<>();
    private Viewport gameViewport;


    public SupportPresenter(GameUIStateManager uiStateManager, Player player, LDAudio audio,
        SupportActorPlacement supportActorPlacement, MessageDisplayer messageDisplayer,
        Viewport gameViewport) {

        this.uiStateManager = uiStateManager;
        uiStateManager.attach(this);
        this.audio = audio;
        this.player = player;
        this.supportActorPlacement = supportActorPlacement;
        this.messageDisplayer = messageDisplayer;
        this.gameViewport = gameViewport;
        initSupportCostsMap();
    }

    private void initSupportCostsMap() {

        supportCosts.put(Apache.class, Apache.COST);
        supportCosts.put(LandMine.class, LandMine.COST);
        supportCosts.put(AirStrike.class, AirStrike.COST);
        supportCosts.put(SupplyDrop.class, SupplyDrop.COST);
    }

    /**
     * Set the view for enlisting
     */
    public void setView(ISupportView view) {

        this.view = view;
        stateChange(uiStateManager.getState());
    }

    /**
     * Create a Support Actor
     */
    public <T extends SupportActor> void createSupportActor(Class<T> type ) {

        Logger.info("Support Presenter: creating support actor - " + type.getSimpleName());
        ValidationResponseEnum validationResponse = supportActorPlacement.canCreateSupport(type);

        if (validationResponse == ValidationResponseEnum.OK) {
            audio.playSound(LDSound.SMALL_CLICK);
            supportActorPlacement.createSupportActor(type);
            uiStateManager.setState(GameUIState.PLACING_SUPPORT);
            Logger.info("Support Presenter: support actor created - " + type.getSimpleName());
        } else {

            if(validationResponse == ValidationResponseEnum.INSUFFICIENT_MONEY){
                Logger.info("Support Presenter: cannot afford " + type.getSimpleName() + " player: " + getPlayerMoney()
                    + " cost: " + supportCosts.get(type));
                messageDisplayer.displayMessage("You cannot afford a " + type.getSimpleName() + "!");
            } else {
                Logger.info("Support Presenter: " + type.getSimpleName() + " is on cooldown");
                messageDisplayer.displayMessage(type.getSimpleName() + " is on cooldown!");
            }


        }
    }

    /**
     * Move a support actor
     */
    public void moveSupportActor(LDVector2 location) {

        if (canMoveSupportActor()) {
            boolean validPlacement = supportActorPlacement.setPlacement(location);
            if(validPlacement) {
                view.showBtnPlace();
            }
        }
    }

    /**
     * Determine if a support actor can be moved
     */
    private boolean canMoveSupportActor() {

        return uiStateManager.getState().equals(GameUIState.PLACING_SUPPORT)
            && supportActorPlacement.supportActorValidState();

    }

    /**
     * Finish Support Placement
     */
    public void finishPlacement() {

        Logger.info("Support Presenter: placing support actor");
        if (canPlaceSupportActor()) {
            int cost = supportActorPlacement.getCurrentSupportActor().getCost();
            supportActorPlacement.finish();
            player.spendMoney(cost);
            supportActorPlacement.removeCurrentSupportActor();
            uiStateManager.setStateReturn();
            Logger.info("Support Presenter: support actor placed");
        }
    }

    private boolean canPlaceSupportActor() {

        return uiStateManager.getState().equals(GameUIState.PLACING_SUPPORT)
            && supportActorPlacement.supportActorValidState();
    }

    /**
     * Cancel Support
     */
    private void cancelSupport() {

        Logger.info("Support Presenter: canceling support");
        supportActorPlacement.removeCurrentSupportActor();
    }

    public void cancel() {

        Logger.info("Support Presenter: cancel");
        audio.playSound(LDSound.SMALL_CLICK);
        uiStateManager.setStateReturn();
        cancelSupport();
    }

    public int getPlayerMoney() {

        return player.getMoney();
    }

    /**
     * Move the Support Actor
     *
     */
    public void screenTouch(float x, float y, String touchType) {

        LDVector2 coords = (LDVector2) gameViewport.unproject(UtilPool.getVector2(x, y));

        if (supportActorPlacement.supportActorValidState()){

            if(touchType.equals("Dragged") && supportActorPlacement.getCurrentSupportActor() instanceof AirStrike){
                return;
            }

            moveSupportActor(coords);

        }

        coords.free();
    }

    /**
     * Determines if the support actor can be purchased.
     *
     * @param supportCost - Cost of the support actor
     * @return boolean - if the tower can be purchased.
     */
    public boolean canAffordSupport(int supportCost) {

        return (supportCost <= player.getMoney());
    }

    @Override
    public void stateChange(GameUIState state) {

        switch (state) {
            case SUPPORT:
                view.supportState();
                break;
            case PLACING_SUPPORT:
                view.placingSupportState();
                break;
            default:
                view.standByState();
                cancelSupport();
                break;
        }

    }

}
