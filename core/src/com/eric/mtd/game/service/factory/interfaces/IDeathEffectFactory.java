package com.eric.mtd.game.service.factory.interfaces;

import com.eric.mtd.game.model.actor.deatheffect.DeathEffectType;
import com.eric.mtd.game.model.actor.deatheffect.DeathEffect;

public interface IDeathEffectFactory {
	public DeathEffect loadDeathEffect(DeathEffectType type);
}
