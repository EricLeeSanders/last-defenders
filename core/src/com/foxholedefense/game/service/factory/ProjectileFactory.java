package com.foxholedefense.game.service.factory;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.projectile.AirStrikeBomb;
import com.foxholedefense.game.model.actor.projectile.Bullet;
import com.foxholedefense.game.model.actor.projectile.Explosion;
import com.foxholedefense.game.model.actor.projectile.Flame;
import com.foxholedefense.game.model.actor.projectile.RPG;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 3/31/2017.
 */

public class ProjectileFactory {
    private BulletPool bulletPool = new BulletPool();
    private RPGPool rpgPool = new RPGPool();
    private AirStrikeBombPool airStrikeBombPool = new AirStrikeBombPool();
    private ExplosionPool explosionPool = new ExplosionPool();
    private FlamePool flamePool = new FlamePool();

    private ActorGroups actorGroups;
    private FHDAudio audio;
    private Resources resources;

    public ProjectileFactory(ActorGroups actorGroups, FHDAudio audio, Resources resources){
        this.actorGroups = actorGroups;
        this.audio = audio;
        this.resources = resources;
    }

    /**
     * Obtains a bullet from the pool
     *
     * @return Bullet
     */
    public Bullet loadBullet() {
        Bullet bullet = bulletPool.obtain();
        actorGroups.getProjectileGroup().addActor(bullet);
        return bullet;
    }

    /**
     * Obtains an RPG from the pool
     *
     * @return RPG
     */
    public RPG loadRPG() {
        RPG rpg = rpgPool.obtain();
        actorGroups.getProjectileGroup().addActor(rpg);
        return rpg;
    }

    /**
     * Obtains an AirStrike Bomb from the pool
     *
     * @return AirStrikeBomb
     */
    public AirStrikeBomb loadAirStrikeBomb() {
        AirStrikeBomb airStrikeBomb = airStrikeBombPool.obtain();
        actorGroups.getProjectileGroup().addActor(airStrikeBomb);
        return airStrikeBomb;
    }

    /**
     * Obtains an Explosion from the pool
     *
     * @return Explosion
     */
    public Explosion loadExplosion() {
        Explosion explosion = explosionPool.obtain();
        actorGroups.getProjectileGroup().addActor(explosion);
        return explosion;
    }

    /**
     * Obtains a flame from the pool
     *
     * @return Flame
     */
    public Flame loadFlame() {
        Flame flame = flamePool.obtain();
        actorGroups.getProjectileGroup().addActor(flame);
        return flame;
    }

    /**
     * Create a Bullet
     *
     * @return Bullet
     */
    protected Bullet createBulletActor() {
        Bullet bullet = new Bullet(bulletPool, resources.getTexture("bullet"));
        return bullet;

    }

    /**
     * Create an RPG
     *
     * @return RPG
     */
    protected RPG createRPGActor() {
        RPG rpg = new RPG(rpgPool, explosionPool, resources.getTexture("bullet"));
        return rpg;

    }

    /**
     * Create an AirStrikeBomb
     *
     * @return AirStrikeBomb
     */
    protected AirStrikeBomb createAirStrikeBombActor() {
        AirStrikeBomb airStrikeBomb = new AirStrikeBomb(airStrikeBombPool, explosionPool, resources.getTexture("bullet"));
        return airStrikeBomb;

    }

    /**
     * Create an Explosion
     *
     * @return Explosion
     */
    protected Explosion createExplosionActor() {
        Array<TextureAtlas.AtlasRegion> atlasRegions = resources.getAtlasRegion("explosion");
        Explosion explosion = new Explosion(explosionPool, atlasRegions, audio);
        return explosion;

    }

    /**
     * Create a Flame
     *
     * @return Flame
     */
    protected Flame createFlameActor() {
        Array<TextureAtlas.AtlasRegion> atlasRegions = resources.getAtlasRegion("flame");
        Flame flame = new Flame(flamePool, atlasRegions);
        return flame;

    }

    public class ExplosionPool extends Pool<Explosion> {
        @Override
        protected Explosion newObject() {
            return createExplosionActor();
        }
    }

    public class BulletPool extends Pool<Bullet> {
        @Override
        protected Bullet newObject() {
            return createBulletActor();
        }
    }

    public class RPGPool extends Pool<RPG> {
        @Override
        protected RPG newObject() {
            return createRPGActor();
        }
    }

    public class AirStrikeBombPool extends Pool<AirStrikeBomb> {
        @Override
        protected AirStrikeBomb newObject() {
            return createAirStrikeBombActor();
        }
    }

    public class FlamePool extends Pool<Flame> {
        @Override
        protected Flame newObject() {
            return createFlameActor();
        }
    }


}
