package com.foxholedefense.game.service.factory.interfaces;

import com.foxholedefense.game.model.actor.effects.ArmorDestroyedEffect;
import com.foxholedefense.game.model.actor.effects.TowerHealEffect;
import com.foxholedefense.game.model.actor.health.ArmorIcon;
import com.foxholedefense.game.model.actor.health.HealthBar;

public interface IHealthFactory {
	public HealthBar loadHealthBar();
	public ArmorIcon loadArmorIcon();
	public ArmorDestroyedEffect loadArmorDestroyedEffect();
	public TowerHealEffect loadTowerHealEffect();
}
