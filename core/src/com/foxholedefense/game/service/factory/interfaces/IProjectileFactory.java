package com.foxholedefense.game.service.factory.interfaces;

import com.foxholedefense.game.model.actor.projectile.AirStrikeBomb;
import com.foxholedefense.game.model.actor.projectile.Bullet;
import com.foxholedefense.game.model.actor.projectile.Explosion;
import com.foxholedefense.game.model.actor.projectile.Flame;
import com.foxholedefense.game.model.actor.projectile.RPG;

public interface IProjectileFactory {
	public Bullet loadBullet();
	public RPG loadRPG();
	public AirStrikeBomb loadAirStrikeBomb();
	public Explosion loadExplosion();
	public Flame loadFlame();
	
}
