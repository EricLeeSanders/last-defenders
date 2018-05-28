package com.lastdefenders.game.service.factory;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
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

    private ProjectilePool<Bullet> bulletPool = new ProjectilePool<>(Bullet.class);
    private ProjectilePool<Rocket> rocketPool = new ProjectilePool<>(Rocket.class);
    private ProjectilePool<Explosion> explosionPool = new ProjectilePool<>(Explosion.class);
    private ProjectilePool<Flame> flamePool = new ProjectilePool<>(Flame.class);


    private ActorGroups actorGroups;
    private LDAudio audio;
    private Resources resources;

    public ProjectileFactory(ActorGroups actorGroups, LDAudio audio, Resources resources) {

        this.actorGroups = actorGroups;
        this.audio = audio;
        this.resources = resources;
    }

    /**
     * Obtains a Projectile from the respective Pool.
     *
     * @return Projectile
     */
    @SuppressWarnings("unchecked")
    public <T extends Actor> T loadProjectile(Class<T> type) {

        String className = type.getSimpleName();
        T projectile = null;

        switch(className){
            case "Bullet":
                projectile = (T) bulletPool.obtain();
                break;
            case "Explosion":
                projectile = (T) explosionPool.obtain();
                break;
            case "Flame":
                projectile = (T) flamePool.obtain();
                break;
            case "Rocket":
                projectile = (T) rocketPool.obtain();
                break;
            default:
                throw new IllegalArgumentException(className + " is not a valid Projectile");

        }

        actorGroups.getProjectileGroup().addActor(projectile);
        return projectile;
    }

    /**
     * Create a Bullet
     *
     * @return Bullet
     */
    private Bullet createBullet() {

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
    private Explosion createExplosion() {

        Array<TextureAtlas.AtlasRegion> atlasRegions = resources.getAtlasRegion("explosion");
        return new Explosion(explosionPool, atlasRegions, audio);

    }

    /**
     * Create a Flame
     *
     * @return Flame
     */
    private Flame createFlame() {

        Array<TextureAtlas.AtlasRegion> atlasRegions = resources.getAtlasRegion("flame");
        return new Flame(flamePool, atlasRegions);

    }
    @SuppressWarnings("unchecked")
    private <T extends Actor> T createProjectile(Class<T> type){

        String className = type.getSimpleName();
        T projectile = null;

        switch(className){
            case "Bullet":
                projectile = (T) createBullet();
                break;
            case "Explosion":
                projectile = (T) createExplosion();
                break;
            case "Flame":
                projectile = (T) createFlame();
                break;
            case "Rocket":
                projectile = (T) createRocket();
                break;
            default:
                throw new IllegalArgumentException(className + " is not a valid Projectile");
        }

        return projectile;

    }

    public class ProjectilePool<T extends Actor> extends Pool<Actor> {

        private final Class<T> type;

        ProjectilePool(Class<T> type){
            this.type = type;
        }

        @Override
        protected T newObject() {

            return createProjectile(type);
        }
    }
}
