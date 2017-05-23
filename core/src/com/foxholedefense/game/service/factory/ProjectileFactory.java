package com.foxholedefense.game.service.factory;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.projectile.Bullet;
import com.foxholedefense.game.model.actor.projectile.Explosion;
import com.foxholedefense.game.model.actor.projectile.Flame;
import com.foxholedefense.game.model.actor.projectile.Rocket;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;

/**
 * Created by Eric on 3/31/2017.
 */

public class ProjectileFactory {
    private BulletPool bulletPool = new BulletPool();
    private rocketPool rocketPool = new rocketPool();
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
     * Obtains an Rocket from the pool
     *
     * @return Rocket
     */
    public Rocket loadRocket() {
        Rocket rocket = rocketPool.obtain();
        actorGroups.getProjectileGroup().addActor(rocket);
        return rocket;
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
     * Create an Rocket
     *
     * @return Rocket
     */
    protected Rocket createRocket() {
        Rocket rocket = new Rocket(rocketPool, this, resources.getTexture("rocket"));
        return rocket;

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

    public class rocketPool extends Pool<Rocket> {
        @Override
        protected Rocket newObject() {
            return createRocket();
        }
    }

    public class FlamePool extends Pool<Flame> {
        @Override
        protected Flame newObject() {
            return createFlameActor();
        }
    }


}
