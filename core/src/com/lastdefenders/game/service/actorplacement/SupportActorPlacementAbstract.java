package com.lastdefenders.game.service.actorplacement;

import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.service.factory.SupportActorFactory;
import com.lastdefenders.game.service.validator.SupportActorValidator;
import com.lastdefenders.util.datastructures.pool.LDVector2;

public abstract class SupportActorPlacementAbstract {
    private SupportActorFactory factory;
    private SupportActorValidator validator;
    private GameActor currentSupportActor;

    public SupportActorPlacementAbstract(SupportActorFactory factory, SupportActorValidator validator){
        this.factory = factory;
        this.validator = validator;
    }

    public abstract void initSupportActorPlacement();

    public void setPosition(LDVector2 position){
        currentSupportActor.setVisible(true);
//        currentSupportActor.setShowRange(true);
//        currentSupportActor.setPositionCenter(clickCoords);
    }
}
