package com.eric.mtd.game.service.factory.interfaces;

import com.eric.mtd.game.model.actor.projectile.AirStrikeBomb;
import com.eric.mtd.game.model.actor.projectile.Bullet;
import com.eric.mtd.game.model.actor.projectile.Explosion;
import com.eric.mtd.game.model.actor.projectile.Flame;
import com.eric.mtd.game.model.actor.projectile.RPG;

public interface IProjectileFactory {
	public Bullet loadBullet();
	public RPG loadRPG();
	public AirStrikeBomb loadAirStrikeBomb();
	public Explosion loadExplosion();
	public Flame loadFlame();
	
}
