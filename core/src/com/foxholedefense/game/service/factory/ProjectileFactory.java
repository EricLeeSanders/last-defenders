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

    private Map<String, TextureRegion> loadedTextures = new HashMap<String, TextureRegion>();
    private Map<String, Array<TextureAtlas.AtlasRegion>> loadedAtlasRegions = new HashMap<String, Array<TextureAtlas.AtlasRegion>>();

    public ProjectileFactory(ActorGroups actorGroups, FHDAudio audio, Resources resources){
        this.actorGroups = actorGroups;
        this.audio = audio;
        this.resources = resources;

        initTextures(resources.getAsset(Resources.ACTOR_ATLAS, TextureAtlas.class));

    }


    private void initTextures(TextureAtlas actorAtlas){

        Logger.info("Projectile Factory: initializing textures");

        loadedTextures.put("range-red", actorAtlas.findRegion("range-red"));
        loadedTextures.put("range", actorAtlas.findRegion("range"));
        loadedTextures.put("range-red-turret", actorAtlas.findRegion("range-red-turret"));
        loadedTextures.put("range-turret", actorAtlas.findRegion("range-turret"));
        loadedTextures.put("range-black", actorAtlas.findRegion("range-black"));
        loadedTextures.put("airstrike", actorAtlas.findRegion("airstrike"));
        loadedTextures.put("bullet", actorAtlas.findRegion("bullet"));
        loadedTextures.put("healthbar-armor", actorAtlas.findRegion("healthbar-armor"));
        loadedTextures.put("healthbar-bg", actorAtlas.findRegion("healthbar-bg"));
        loadedTextures.put("healthbar-life", actorAtlas.findRegion("healthbar-life"));
        loadedTextures.put("humvee", actorAtlas.findRegion("humvee"));
        loadedTextures.put("landmine", actorAtlas.findRegion("landmine"));
        loadedTextures.put("tower-rifle", actorAtlas.findRegion("tower-rifle"));
        loadedTextures.put("tower-machine-gun", actorAtlas.findRegion("tower-machine-gun"));
        loadedTextures.put("tower-sniper", actorAtlas.findRegion("tower-sniper"));
        loadedTextures.put("tower-flame-thrower", actorAtlas.findRegion("tower-flame-thrower"));
        loadedTextures.put("tower-rocket-launcher", actorAtlas.findRegion("tower-rocket-launcher"));
        loadedTextures.put("tower-turret-turret", actorAtlas.findRegion("tower-turret-turret"));
        loadedTextures.put("tower-turret-bags", actorAtlas.findRegion("tower-turret-bags"));
        loadedTextures.put("tower-tank-body", actorAtlas.findRegion("tower-tank-body"));
        loadedTextures.put("tower-tank-turret", actorAtlas.findRegion("tower-tank-turret"));
        loadedTextures.put("enemy-rifle-stationary", actorAtlas.findRegion("enemy-rifle-stationary"));
        loadedTextures.put("enemy-machine-gun-stationary", actorAtlas.findRegion("enemy-machine-gun-stationary"));
        loadedTextures.put("enemy-sniper-stationary", actorAtlas.findRegion("enemy-sniper-stationary"));
        loadedTextures.put("enemy-flame-thrower-stationary", actorAtlas.findRegion("enemy-flame-thrower-stationary"));
        loadedTextures.put("enemy-rocket-launcher-stationary", actorAtlas.findRegion("enemy-rocket-launcher-stationary"));
        loadedTextures.put("enemy-sprinter-stationary", actorAtlas.findRegion("enemy-sprinter-stationary"));
        loadedTextures.put("enemy-tank-body", actorAtlas.findRegion("enemy-tank-body"));
        loadedTextures.put("enemy-tank-turret", actorAtlas.findRegion("enemy-tank-turret"));
        loadedTextures.put("enemy-humvee", actorAtlas.findRegion("enemy-humvee"));
        loadedTextures.put("supply-drop", actorAtlas.findRegion("supply-drop"));
        loadedTextures.put("supply-drop-crate", actorAtlas.findRegion("supply-drop-crate"));
        loadedTextures.put("apache-stationary", actorAtlas.findRegion("apache",1));
        loadedTextures.put("shield", actorAtlas.findRegion("shield"));

        loadedAtlasRegions.put("explosion", actorAtlas.findRegions("explosion"));
        loadedAtlasRegions.put("flame", actorAtlas.findRegions("flame"));
        loadedAtlasRegions.put("blood-splatter", actorAtlas.findRegions("blood-splatter"));
        loadedAtlasRegions.put("smoke-ring", actorAtlas.findRegions("smoke-ring"));
        loadedAtlasRegions.put("enemy-rifle", actorAtlas.findRegions("enemy-rifle"));
        loadedAtlasRegions.put("enemy-flame-thrower", actorAtlas.findRegions("enemy-flame-thrower"));
        loadedAtlasRegions.put("enemy-sniper", actorAtlas.findRegions("enemy-sniper"));
        loadedAtlasRegions.put("enemy-machine-gun", actorAtlas.findRegions("enemy-machine-gun"));
        loadedAtlasRegions.put("enemy-rocket-launcher", actorAtlas.findRegions("enemy-rocket-launcher"));
        loadedAtlasRegions.put("enemy-sprinter", actorAtlas.findRegions("enemy-sprinter"));
        loadedAtlasRegions.put("apache", actorAtlas.findRegions("apache"));
        loadedAtlasRegions.put("shield-destroyed", actorAtlas.findRegions("shield-destroyed"));

        Logger.info("Projectile Factory: textures initialized");
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
        Bullet bullet = new Bullet(bulletPool, loadedTextures.get("bullet"));
        return bullet;

    }

    /**
     * Create an RPG
     *
     * @return RPG
     */
    protected RPG createRPGActor() {
        RPG rpg = new RPG(rpgPool, explosionPool, loadedTextures.get("bullet"));
        return rpg;

    }

    /**
     * Create an AirStrikeBomb
     *
     * @return AirStrikeBomb
     */
    protected AirStrikeBomb createAirStrikeBombActor() {
        AirStrikeBomb airStrikeBomb = new AirStrikeBomb(airStrikeBombPool, explosionPool, loadedTextures.get("bullet"));
        return airStrikeBomb;

    }

    /**
     * Create an Explosion
     *
     * @return Explosion
     */
    protected Explosion createExplosionActor() {
        Array<TextureAtlas.AtlasRegion> atlasRegions = loadedAtlasRegions.get("explosion");
        Explosion explosion = new Explosion(explosionPool, atlasRegions, audio);
        return explosion;

    }

    /**
     * Create a Flame
     *
     * @return Flame
     */
    protected Flame createFlameActor() {
        Array<TextureAtlas.AtlasRegion> atlasRegions = loadedAtlasRegions.get("flame");
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
