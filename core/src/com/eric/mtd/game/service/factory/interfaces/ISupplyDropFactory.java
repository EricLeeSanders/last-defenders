package com.eric.mtd.game.service.factory.interfaces;

import com.eric.mtd.game.model.actor.support.SupplyDrop;
import com.eric.mtd.game.model.actor.support.SupplyDropCrate;

public interface ISupplyDropFactory {
	public SupplyDrop loadSupplyDrop();
	public SupplyDropCrate loadSupplyDropCrate();
}	
