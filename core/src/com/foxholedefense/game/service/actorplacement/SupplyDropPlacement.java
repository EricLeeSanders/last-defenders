package com.foxholedefense.game.service.actorplacement;

import com.badlogic.gdx.math.Vector2;
import com.foxholedefense.game.model.actor.support.SupplyDropCrate;
import com.foxholedefense.game.service.factory.SupportActorFactory;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.Logger;

public class SupplyDropPlacement {
	private SupplyDropCrate currentSupplyDropCrate;
	private SupportActorFactory supportActorFactory;
	public SupplyDropPlacement(SupportActorFactory supportActorFactory) {
		this.supportActorFactory = supportActorFactory;
	}
	public void createSupplyDrop() {
		Logger.info("SupplyDropPlacement: creating supply drop");
		currentSupplyDropCrate = supportActorFactory.loadSupplyDropCrate();
		currentSupplyDropCrate.setPosition(0, 0);
		currentSupplyDropCrate.setActive(false);
		currentSupplyDropCrate.setVisible(false);
	}
	public void setLocation(Vector2 location){
		Logger.info("SupplyDropPlacement: creating location");
		currentSupplyDropCrate.setVisible(true);
		currentSupplyDropCrate.setShowRange(true);
		currentSupplyDropCrate.setPositionCenter(location);
	}
	public void finishPlacement() {
		Logger.info("SupplyDropPlacement: finishing placement");
		if (isCurrentSupplyDropCrate()) {
			float x = ActorUtil.calcXCenterFromBotLeft(currentSupplyDropCrate.getX(), currentSupplyDropCrate.getWidth());
			float y = ActorUtil.calcYCenterFromBotLeft(currentSupplyDropCrate.getY(), currentSupplyDropCrate.getHeight());
			supportActorFactory.loadSupplyDrop().beginSupplyDrop(x, y);
			currentSupplyDropCrate.setShowRange(false);
			currentSupplyDropCrate.freeActor();
			currentSupplyDropCrate = null;
		}
	}
	
	public void removeCurrentSupplyDropCrate(){
		Logger.info("SupplyDropPlacement: remove supply drop");
		if (isCurrentSupplyDropCrate()) {
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
