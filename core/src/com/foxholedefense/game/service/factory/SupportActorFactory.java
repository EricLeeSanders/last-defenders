package com.foxholedefense.game.service.factory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.support.AirStrike;
import com.foxholedefense.game.model.actor.support.AirStrikeLocation;
import com.foxholedefense.game.model.actor.support.Apache;
import com.foxholedefense.game.model.actor.support.LandMine;
import com.foxholedefense.game.model.actor.support.SupplyDrop;
import com.foxholedefense.game.model.actor.support.SupplyDropCrate;
import com.foxholedefense.game.model.actor.support.SupportActor;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.datastructures.pool.FHDVector2;

/**
 * Created by Eric on 3/31/2017.
 */

public class SupportActorFactory {

    private SupplyDropPool supplyDropPool = new SupplyDropPool();
    private SupplyDropCratePool supplyDropCratePool = new SupplyDropCratePool();
    private AirStrikeLocationPool airStrikeLocationPool = new AirStrikeLocationPool();
    private SupportActorPool<Apache> apachePool = new SupportActorPool<>(Apache.class);
    private SupportActorPool<AirStrike> airStrikePool = new SupportActorPool<>(AirStrike.class);
    private SupportActorPool<LandMine> landMinePool = new SupportActorPool<>(LandMine.class);

    private ActorGroups actorGroups;
    private FHDAudio audio;
    private Resources resources;

    private EffectFactory effectFactory;
    private ProjectileFactory projectileFactory;

    public SupportActorFactory(ActorGroups actorGroups, FHDAudio audio, Resources resources, EffectFactory effectFactory, ProjectileFactory projectileFactory){
        this.actorGroups = actorGroups;
        this.audio = audio;
        this.resources = resources;
        this.effectFactory = effectFactory;
        this.projectileFactory = projectileFactory;
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

    public AirStrikeLocation loadAirStrikeLocation(FHDVector2 location, float radius) {
        AirStrikeLocation airStrikeLocation = airStrikeLocationPool.obtain();
        airStrikeLocation.initialize(location, radius);
        actorGroups.getSupportGroup().addActor(airStrikeLocation);

        return airStrikeLocation;
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
    private SupplyDrop createSupplyDropActor() {
        TextureRegion supplyDropRegion = resources.getTexture("supply-drop");
        return new SupplyDrop(supplyDropRegion, supplyDropPool, this);
    }

    /**
     * Create a SupplyDropCrate
     *
     * @return SupplyDropCrate
     */
    private SupplyDropCrate createSupplyDropCrateActor() {
        TextureRegion supplyDropCrateRegion = resources.getTexture("supply-drop-crate");
        TextureRegion rangeTexture = resources.getTexture("range-black");
        return new SupplyDropCrate(supplyDropCrateRegion, rangeTexture, supplyDropCratePool, actorGroups.getTowerGroup(), effectFactory);
    }

    private AirStrikeLocation createAirStrikeLocation() {
        TextureRegion rangeTexture = resources.getTexture("range-black");
        return new AirStrikeLocation(airStrikeLocationPool,rangeTexture );
    }

    /**
     * Create a Support Actor
     *
     * @return SupportActor
     */
    private SupportActor createSupportActor(Class<? extends SupportActor> type) {
        Logger.info("Actor Factory: creating support actor: " + type.getSimpleName());
        Group targetGroup = actorGroups.getEnemyGroup();
        if (type.equals(Apache.class)) {
            TextureRegion [] textureRegions = resources.getAtlasRegion("apache").toArray(TextureRegion.class);
            TextureRegion rangeTexture = resources.getTexture("range");
            TextureRegion stationaryRegion = resources.getTexture("apache-stationary");
            return new Apache(apachePool, targetGroup, projectileFactory,stationaryRegion, textureRegions, rangeTexture, audio);
        } else if(type.equals(AirStrike.class)){
            TextureRegion textureRegion = resources.getTexture("airstrike");
            TextureRegion rangeTexture = resources.getTexture("range-black");
            return new AirStrike(airStrikePool, targetGroup, projectileFactory, textureRegion, rangeTexture, audio);
        } else if (type.equals(LandMine.class)){
            TextureRegion textureRegion = resources.getTexture("landmine");
            TextureRegion rangeTexture = resources.getTexture("range");
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

    public class AirStrikeLocationPool extends Pool<AirStrikeLocation> {
        @Override
        protected AirStrikeLocation newObject() {
            return createAirStrikeLocation();
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
