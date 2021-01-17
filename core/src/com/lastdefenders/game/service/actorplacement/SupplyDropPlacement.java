package com.lastdefenders.game.service.actorplacement;

import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.game.model.actor.support.supplydrop.SupplyDrop;
import com.lastdefenders.game.service.factory.SupportActorFactory;
import com.lastdefenders.game.service.validator.SupportActorValidator;
import com.lastdefenders.util.Logger;

public class SupplyDropPlacement {

    private SupplyDrop currentSupplyDrop;
    private SupportActorFactory supportActorFactory;
    private SupportActorValidator supplyDropValidator;

    public SupplyDropPlacement(SupportActorFactory supportActorFactory, SupportActorValidator supplyDropValidator) {

        this.supportActorFactory = supportActorFactory;
        this.supplyDropValidator = supplyDropValidator;
    }

    public void createSupplyDrop() {

        Logger.info("SupplyDropPlacement: creating supply drop");
        currentSupplyDrop = supportActorFactory.loadSupportActor(SupplyDrop.class, true);
        currentSupplyDrop.setPosition(0, 0);
        currentSupplyDrop.setActive(false);
        currentSupplyDrop.setVisible(false);
    }

    public void setLocation(Vector2 location) {

        currentSupplyDrop.setVisible(true);
        currentSupplyDrop.setShowRange(true);
        currentSupplyDrop.setPositionCenter(location);
    }

    public void placeSupplyDrop() {

        if (canPlaceSupplyDrop()) {
            Logger.info("SupplyDropPlacement: finishing placement");
            supportActorFactory.loadSupportActor(SupplyDrop.class, true)
                .beginSupplyDrop(currentSupplyDrop.getPositionCenter());
            currentSupplyDrop.setShowRange(false);
            currentSupplyDrop.freeActor();
            currentSupplyDrop = null;
        }
    }

    private boolean canPlaceSupplyDrop(){
        return isCurrentSupplyDropCrate();
    }

    public void removeCurrentSupplyDropCrate() {

        if (isCurrentSupplyDropCrate()) {
            Logger.info("SupplyDropPlacement: remove supply drop");
            currentSupplyDrop.freeActor();
            currentSupplyDrop = null;
        }
    }

    public boolean isCurrentSupplyDropCrate() {

        return (currentSupplyDrop != null);
    }

    public SupplyDrop getCurrentSupplyDropCrate() {

        return currentSupplyDrop;
    }
}
