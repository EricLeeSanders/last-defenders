package com.foxholedefense.game.service.factory.interfaces;

import com.foxholedefense.game.model.actor.projectile.AirStrikeBomb;
import com.foxholedefense.game.model.actor.projectile.Bullet;
import com.foxholedefense.game.model.actor.projectile.Explosion;
import com.foxholedefense.game.model.actor.projectile.Flame;
import com.foxholedefense.game.model.actor.projectile.RPG;

public interface IProjectileFactory {
	Bullet loadBullet();
	RPG loadRPG();
	AirStrikeBomb loadAirStrikeBomb();
	Explosion loadExplosion();
	Flame loadFlame();
	
}
