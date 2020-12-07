package com.lastdefenders.game.service.actorplacement;

import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.game.model.actor.support.SupplyDrop;
import com.lastdefenders.game.model.actor.support.SupplyDropCrate;
import com.lastdefenders.game.service.factory.SupportActorFactory;
import com.lastdefenders.util.Logger;

public class SupplyDropPlacement {

    private SupplyDropCrate currentSupplyDropCrate;
    private SupportActorFactory supportActorFactory;

    public SupplyDropPlacement(SupportActorFactory supportActorFactory) {

        this.supportActorFactory = supportActorFactory;
    }

    public void createSupplyDrop() {

        //Logger.info("SupplyDropPlacement: creating supply drop");
        currentSupplyDropCrate = supportActorFactory.loadSupportActor(SupplyDropCrate.class, true);
        currentSupplyDropCrate.setPosition(0, 0);
        currentSupplyDropCrate.setActive(false);
        currentSupplyDropCrate.setVisible(false);
    }

    public void setLocation(Vector2 location) {

        currentSupplyDropCrate.setVisible(true);
        currentSupplyDropCrate.setShowRange(true);
        currentSupplyDropCrate.setPositionCenter(location);
    }

    public void placeSupplyDrop() {

        if (isCurrentSupplyDropCrate()) {
            Logger.info("SupplyDropPlacement: finishing placement");
            supportActorFactory.loadSupportActor(SupplyDrop.class, true)
                .beginSupplyDrop(currentSupplyDropCrate.getPositionCenter());
            currentSupplyDropCrate.setShowRange(false);
            currentSupplyDropCrate.freeActor();
            currentSupplyDropCrate = null;
        }
    }

    public void removeCurrentSupplyDropCrate() {

        if (isCurrentSupplyDropCrate()) {
            Logger.info("SupplyDropPlacement: remove supply drop");
            currentSupplyDropCrate.freeActor();
            currentSupplyDropCrate = null;
        }
    }

    public boolean isCurrentSupplyDropCrate() {

        return (currentSupplyDropCrate != null);
    }

    public SupplyDropCrate getCurrentSupplyDropCrate() {

        return currentSupplyDropCrate;
    }
}
