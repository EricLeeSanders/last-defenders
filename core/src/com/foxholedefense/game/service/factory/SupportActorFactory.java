package com.foxholedefense.game.service.factory;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.support.AirStrike;
import com.foxholedefense.game.model.actor.support.Apache;
import com.foxholedefense.game.model.actor.support.LandMine;
import com.foxholedefense.game.model.actor.support.SupplyDrop;
import com.foxholedefense.game.model.actor.support.SupplyDropCrate;
import com.foxholedefense.game.model.actor.support.SupportActor;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 3/31/2017.
 */

public class SupportActorFactory {

    private SupplyDropPool supplyDropPool = new SupplyDropPool();
    private SupplyDropCratePool supplyDropCratePool = new SupplyDropCratePool();
    private SupportActorPool<Apache> apachePool = new SupportActorPool<Apache>(Apache.class);
    private SupportActorPool<AirStrike> airStrikePool = new SupportActorPool<AirStrike>(AirStrike.class);
    private SupportActorPool<LandMine> landMinePool = new SupportActorPool<LandMine>(LandMine.class);

    private Map<String, TextureRegion> loadedTextures = new HashMap<String, TextureRegion>();
    private Map<String, Array<TextureAtlas.AtlasRegion>> loadedAtlasRegions = new HashMap<String, Array<TextureAtlas.AtlasRegion>>();

    private ActorGroups actorGroups;
    private FHDAudio audio;
    private Resources resources;

    private EffectFactory effectFactory;
    private ProjectileFactory projectileFactory;

    public SupportActorFactory(ActorGroups actorGroups, FHDAudio audio, Resources resources, EffectFactory effectFactory, ProjectileFactory projectileFactory, HealthFactory healthFactory){
        this.actorGroups = actorGroups;
        this.audio = audio;
        this.resources = resources;
        this.effectFactory = effectFactory;
        this.projectileFactory = projectileFactory;
    }

    private void initTextures(TextureAtlas actorAtlas){

        Logger.info("Support Actor Factory: initializing textures");

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

        Logger.info("Support Actor Factory: textures initialized");
    }

    /**
     * Obtains a Supply Drop from the pool
     *
     * @return SupplyDrop
     */
    public SupplyDrop loadSupplyDrop() {
        SupplyDrop supplyDrop = supplyDropPool.obtain();
        actorGroups.getSupportGroup().addActor(supplyDrop);
        return supplyDrop;
    }

    /**
     * Obtains a Supply Drop Crate from the pool
     *
     * @return SupplyDropCrate
     */
    public SupplyDropCrate loadSupplyDropCrate() {
        SupplyDropCrate supplyDropCrate = supplyDropCratePool.obtain();
        actorGroups.getSupportGroup().addActor(supplyDropCrate);
        return supplyDropCrate;
    }

    /**
     * Obtain a Support Actor from the pool
     *
     * @param type - The type of support actor
     * @return Support Actor
     */
    public SupportActor loadSupportActor(String type) {
        Logger.info("Actor Factory: loading support actor: " + type);
        SupportActor supportActor = null;
        if (type.equals("Apache")) {
            supportActor = apachePool.obtain();
        } else if(type.equals("AirStrike")) {
            supportActor = airStrikePool.obtain();
        } else if(type.equals("LandMine")) {
            supportActor = landMinePool.obtain();
        }
        return supportActor;
    }

    /**
     * Create a SupplyDrop
     *
     * @return SupplyDrop
     */
    protected SupplyDrop createSupplyDropActor() {
        TextureRegion supplyDropRegion = loadedTextures.get("supply-drop");
        SupplyDrop supplyDrop = new SupplyDrop(supplyDropRegion, supplyDropPool, supplyDropCratePool);
        return supplyDrop;
    }

    /**
     * Create a SupplyDropCrate
     *
     * @return SupplyDropCrate
     */
    protected SupplyDropCrate createSupplyDropCrateActor() {
        TextureRegion supplyDropCrateRegion = loadedTextures.get("supply-drop-crate");
        TextureRegion rangeTexture = loadedTextures.get("range-black");
        SupplyDropCrate supplyDropCrate = new SupplyDropCrate(supplyDropCrateRegion, rangeTexture, supplyDropCratePool, actorGroups.getTowerGroup(), effectFactory);
        return supplyDropCrate;
    }

    /**
     * Create a Support Actor
     *
     * @return SupportActor
     */
    protected SupportActor createSupportActor(Class<? extends SupportActor> type) {
        Logger.info("Actor Factory: creating support actor: " + type.getSimpleName());
        Group targetGroup = actorGroups.getEnemyGroup();
        if (type.equals(Apache.class)) {
            TextureRegion [] textureRegions = loadedAtlasRegions.get("apache").toArray(TextureRegion.class);
            TextureRegion rangeTexture = loadedTextures.get("range");
            TextureRegion stationaryRegion = loadedTextures.get("apache-stationary");
            return new Apache(apachePool, targetGroup, projectileFactory,stationaryRegion, textureRegions, rangeTexture, audio);
        } else if(type.equals(AirStrike.class)){
            TextureRegion textureRegion = loadedTextures.get("airstrike");
            TextureRegion rangeTexture = loadedTextures.get("range-black");
            return new AirStrike(airStrikePool, targetGroup, projectileFactory, textureRegion, rangeTexture, audio);
        } else if (type.equals(LandMine.class)){
            TextureRegion textureRegion = loadedTextures.get("landmine");
            TextureRegion rangeTexture = loadedTextures.get("range");
            return new LandMine(landMinePool, targetGroup, projectileFactory, textureRegion, rangeTexture);
        } else {
            throw new NullPointerException("Actor factory couldn't create: " + type.getSimpleName());
        }
    }

    public class SupplyDropPool extends Pool<SupplyDrop> {
        @Override
        protected SupplyDrop newObject() {
            return createSupplyDropActor();
        }
    }

    public class SupplyDropCratePool extends Pool<SupplyDropCrate> {
        @Override
        protected SupplyDropCrate newObject() {
            return createSupplyDropCrateActor();
        }
    }


    public class SupportActorPool<T extends SupportActor> extends Pool<SupportActor> {
        private final Class<? extends SupportActor> type;

        public SupportActorPool(Class<? extends SupportActor> type) {
            this.type = type;
        }

        @Override
        protected SupportActor newObject() {
            return createSupportActor(type);
        }

    }
}
