package com.foxholedefense.game.service.factory.interfaces;

import com.foxholedefense.game.model.actor.effects.deatheffect.DeathEffect;
import com.foxholedefense.game.model.actor.effects.deatheffect.DeathEffectType;

public interface IDeathEffectFactory {
	DeathEffect loadDeathEffect(DeathEffectType type);
}
