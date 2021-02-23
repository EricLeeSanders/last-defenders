package com.lastdefenders.game.service.actorplacement;

import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.model.actor.support.Apache;
import com.lastdefenders.game.model.actor.support.CombatSupportActor;
import com.lastdefenders.game.model.actor.support.SupportActor;
import com.lastdefenders.game.model.actor.support.supplydrop.SupplyDrop;
import com.lastdefenders.game.service.factory.SupportActorFactory;
import com.lastdefenders.game.service.validator.SupportActorValidator;
import com.lastdefenders.game.service.validator.ValidationResponseEnum;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import java.util.Map;

public class SupportActorPlacement {
    private SupportActorFactory factory;
    private Map<Class<? extends SupportActor>, SupportActorValidator> validatorsMap;
    private SupportActor currentSupportActor;

    public SupportActorPlacement(SupportActorFactory factory,
            Map<Class<? extends SupportActor>, SupportActorValidator> validatorsMap){
        this.factory = factory;
        this.validatorsMap = validatorsMap;
    }

    /**
     * Determines if support actors can be created
     */
    public ValidationResponseEnum canCreateSupport(Class<? extends SupportActor> type) {
        SupportActorValidator validator = validatorsMap.get(type);
        return validator.canCreateSupportActor();

    }

    public <T extends SupportActor> void createSupportActor(Class<T> type) {

        Logger.info("SupportActorPlacement: Creating supply actor: " + type.getSimpleName());
        ValidationResponseEnum validationResponse = canCreateSupport(type);
        if (validationResponse == ValidationResponseEnum.OK) {
            currentSupportActor = factory.loadSupportActor(type, true);
            currentSupportActor.initialize();
        } else {
            throw new IllegalStateException("Can't create " + type.getSimpleName() + ". Validation Response: " + validationResponse);
        }

    }

    public boolean setPlacement(LDVector2 clickCoords) {

        if (supportActorValidState()) {
            return currentSupportActor.setPlacement(clickCoords);
        }
        return false;
    }


    public void finish() {

        Logger.info("SupportActorPlacement: Finishing Support Actor Placement");
        if (supportActorValidState()) {
            currentSupportActor.ready();
            validatorsMap.get(currentSupportActor.getClass()).beginCooldown();
            currentSupportActor = null;
            Logger.info("SupportActorPlacement: Support Actor Placement Finished");
        } else {
            Logger.info("SupportActorPlacement: Cannot finish support actor placement");
        }
    }

    public boolean supportActorValidState(){

        return isCurrentSupportActor();
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

    public SupportActor getCurrentSupportActor() {

        return currentSupportActor;
    }
}
