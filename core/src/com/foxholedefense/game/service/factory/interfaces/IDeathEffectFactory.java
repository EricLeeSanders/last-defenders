package com.foxholedefense.game.service.factory.interfaces;

import com.foxholedefense.game.model.actor.deatheffect.DeathEffect;
import com.foxholedefense.game.model.actor.deatheffect.DeathEffectType;

public interface IDeathEffectFactory {
	public DeathEffect loadDeathEffect(DeathEffectType type);
}
