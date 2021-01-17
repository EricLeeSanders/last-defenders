package com.lastdefenders.game.service.actorplacement;

import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.model.actor.support.Apache;
import com.lastdefenders.game.model.actor.support.CombatSupportActor;
import com.lastdefenders.game.service.factory.SupportActorFactory;
import com.lastdefenders.game.service.validator.SupportActorValidator;
import com.lastdefenders.util.Logger;
import java.util.Map;

public class SupportActorPlacement {

    private CombatSupportActor currentSupportActor;
    private SupportActorFactory supportActorFactory;
    private Map<Class<? extends GameActor>, SupportActorValidator> supportActorValidatorMap;

    public SupportActorPlacement(SupportActorFactory supportActorFactory,
        Map<Class<? extends GameActor>, SupportActorValidator> supportActorValidatorMap) {

        this.supportActorFactory = supportActorFactory;
        this.supportActorValidatorMap = supportActorValidatorMap;
    }

    public <T extends CombatSupportActor> void createSupportActor(Class<T> type) {

        Logger.info("SupportActorPlacement: creating supply actor: " + type.getSimpleName());
        currentSupportActor = supportActorFactory.loadSupportActor(type, true);
        currentSupportActor.setPosition(0, 0);
        currentSupportActor.setActive(false);
        currentSupportActor.setVisible(false);

    }

    public void moveSupportActor(Vector2 clickCoords) {

        if (currentSupportActor != null) {
            currentSupportActor.setVisible(true);
            currentSupportActor.setShowRange(true);
            currentSupportActor.setPositionCenter(clickCoords);
        }
    }

    public void placeSupportActor() {

        Logger.info("SupportActorPlacement: trying to place Support Actor");
        if (canPlaceSupportActor()) {
            //If it is an Apache that is being placed, then we need to call it's initialize method
            if (currentSupportActor instanceof Apache) {
                ((Apache) currentSupportActor).initialize(currentSupportActor.getPositionCenter());
            }
            currentSupportActor.setActive(true);
            currentSupportActor.setShowRange(false);
            currentSupportActor = null;
            Logger.info("SupportActorPlacement: placing Support Actor");
        }
    }

    private boolean canPlaceSupportActor(){

        return currentSupportActor != null;
    }

    public void removeCurrentSupportActor() {

        Logger.info("SupportActorPlacement: removing Support Actor");
        if (isCurrentSupportActor()) {
            currentSupportActor.freeActor();
            currentSupportActor = null;
        }
    }

    public boolean isCurrentSupportActor() {

        return (currentSupportActor != null);
    }

    public CombatSupportActor getCurrentSupportActor() {

        return currentSupportActor;
    }

}
