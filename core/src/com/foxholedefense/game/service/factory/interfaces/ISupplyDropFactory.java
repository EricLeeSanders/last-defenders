package com.foxholedefense.game.service.factory.interfaces;

import com.foxholedefense.game.model.actor.support.SupplyDrop;
import com.foxholedefense.game.model.actor.support.SupplyDropCrate;

public interface ISupplyDropFactory {
	public SupplyDrop loadSupplyDrop();
	public SupplyDropCrate loadSupplyDropCrate();
}	
