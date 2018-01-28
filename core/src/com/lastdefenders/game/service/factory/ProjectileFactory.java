package com.lastdefenders.game.service.factory;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.model.actor.ActorGroups;
import com.lastdefenders.game.model.actor.projectile.Bullet;
import com.lastdefenders.game.model.actor.projectile.Explosion;
import com.lastdefenders.game.model.actor.projectile.Flame;
import com.lastdefenders.game.model.actor.projectile.Rocket;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.Resources;

/**
 * Created by Eric on 3/31/2017.
 */

public class ProjectileFactory {

    private BulletPool bulletPool = new BulletPool();
    private RocketPool rocketPool = new RocketPool();
    private ExplosionPool explosionPool = new ExplosionPool();
    private FlamePool flamePool = new FlamePool();


    private ActorGroups actorGroups;
    private LDAudio audio;
    private Resources resources;

    public ProjectileFactory(ActorGroups actorGroups, LDAudio audio, Resources resources) {

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
    private Bullet createBulletActor() {

        return new Bullet(bulletPool, resources.getTexture("bullet"));

    }

    /**
     * Create an Rocket
     *
     * @return Rocket
     */
    private Rocket createRocket() {

        return new Rocket(rocketPool, this, resources.getTexture("rocket"));

    }

    /**
     * Create an Explosion
     *
     * @return Explosion
     */
    private Explosion createExplosionActor() {

        Array<TextureAtlas.AtlasRegion> atlasRegions = resources.getAtlasRegion("explosion");
        return new Explosion(explosionPool, atlasRegions, audio);

    }

    /**
     * Create a Flame
     *
     * @return Flame
     */
    private Flame createFlameActor() {

        Array<TextureAtlas.AtlasRegion> atlasRegions = resources.getAtlasRegion("flame");
        return new Flame(flamePool, atlasRegions);

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

    public class RocketPool extends Pool<Rocket> {

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