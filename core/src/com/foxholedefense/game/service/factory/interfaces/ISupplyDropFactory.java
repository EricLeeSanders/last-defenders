package com.foxholedefense.game.service.factory.interfaces;

import com.foxholedefense.game.model.actor.support.SupplyDrop;
import com.foxholedefense.game.model.actor.support.SupplyDropCrate;

public interface ISupplyDropFactory {
	SupplyDrop loadSupplyDrop();
	SupplyDropCrate loadSupplyDropCrate();
}	
