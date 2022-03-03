package com.lastdefenders.game.service.factory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.model.actor.groups.ActorGroups;
import com.lastdefenders.game.model.actor.support.AirStrike;
import com.lastdefenders.game.model.actor.support.AirStrike.AirStrikeLocation;
import com.lastdefenders.game.model.actor.support.Apache;
import com.lastdefenders.game.model.actor.support.LandMine;
import com.lastdefenders.game.model.actor.support.SupportActor;
import com.lastdefenders.game.model.actor.support.supplydrop.SupplyDrop;
import com.lastdefenders.game.model.actor.support.supplydrop.SupplyDropPlane;
import com.lastdefenders.sound.LDAudio;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * Created by Eric on 3/31/2017.
 */

public class SupportActorFactory {

    private SupportActorPool<SupplyDrop> supplyDropPool;
    private SupportActorPool<SupplyDrop> supplyDropCratePool;
    private SupportActorPool<AirStrikeLocation> airStrikeLocationPool;
    private SupportActorPool<Apache> apachePool;
    private SupportActorPool<AirStrike> airStrikePool;
    private SupportActorPool<LandMine> landMinePool;

    private LDAudio audio;
    private Resources resources;
    private EffectFactory effectFactory;
    private ProjectileFactory projectileFactory;
    private ActorGroups actorGroups;

    public SupportActorFactory(ActorGroups actorGroups, LDAudio audio, Resources resources,
        EffectFactory effectFactory, ProjectileFactory projectileFactory) {

        this.actorGroups = actorGroups;
        this.audio = audio;
        this.resources = resources;
        this.effectFactory = effectFactory;
        this.projectileFactory = projectileFactory;

        init();
    }

    private void init(){
        supplyDropPool = new SupportActorPool<>(SupplyDrop.class, actorGroups.getSupportGroup());
        supplyDropCratePool = new SupportActorPool<>(SupplyDrop.class, actorGroups.getSupportGroup());
        airStrikeLocationPool = new SupportActorPool<>(AirStrikeLocation.class, actorGroups.getSupportGroup());
        apachePool = new SupportActorPool<>(Apache.class, actorGroups.getSupportGroup());
        airStrikePool = new SupportActorPool<>(AirStrike.class, actorGroups.getSupportGroup());
        landMinePool = new SupportActorPool<>(LandMine.class, actorGroups.getLandmineGroup());

    }

    /**
     * Obtain a Support Actor from the pool
     *
     * @param type - The type of support actor
     * @param addToGroup
     * @return Support Actor
     */
    public <T extends SupportActor> T loadSupportActor(Class<T> type, boolean addToGroup) {

        Logger.info("Actor Factory: loading support actor: " + type.getSimpleName());
        String className = type.getSimpleName();
        SupportActorPool<? extends Actor> supportActorPool = null;

        switch (className) {
            case "Apache":
                supportActorPool = apachePool;
                break;
            case "AirStrike":
                supportActorPool = airStrikePool;
                break;
            case "LandMine":
                supportActorPool = landMinePool;
                break;
            case "AirStrikeLocation":
                supportActorPool = airStrikeLocationPool;
                break;
            case "SupplyDropCrate":
                supportActorPool = supplyDropCratePool;
                break;
            case "SupplyDrop":
                supportActorPool = supplyDropPool;
                break;
            default:
                throw new IllegalArgumentException(className + " is not a valid Support Actor");

        }

        @SuppressWarnings("unchecked")
        T supportActor = (T) supportActorPool.obtain();

        if(addToGroup){
            supportActorPool.getGroup().addActor(supportActor);
        }

        return supportActor;
    }

    private SupplyDrop createSupplyDrop() {

        TextureRegion supplyDropPlaneRegion = resources.getTexture("supply-drop-plane");
        TextureRegion supplyDropCrateRegion = resources.getTexture("supply-drop-crate");
        TextureRegion rangeTexture = resources.getTexture("range-black");
        SupplyDropPlane plane = new SupplyDropPlane(supplyDropPlaneRegion, audio);
        return new SupplyDrop(supplyDropCrateRegion, rangeTexture, supplyDropPool, actorGroups.getTowerGroup(),effectFactory, plane);
    }

    private AirStrikeLocation createAirStrikeLocation() {

        TextureRegion rangeTexture = resources.getTexture("range-black");
        return new AirStrikeLocation(rangeTexture);
    }

    private Apache createApache(){

        TextureRegion[] textureRegions = resources.getAtlasRegion("apache")
            .toArray(TextureRegion.class);
        TextureRegion rangeTexture = resources.getTexture("range");
        TextureRegion stationaryRegion = resources.getTexture("apache-stationary");
        return new Apache(apachePool, actorGroups.getEnemyGroup(), projectileFactory, stationaryRegion,
            textureRegions, rangeTexture, audio);
    }

    private AirStrike createAirStrike(){
        TextureRegion textureRegion = resources.getTexture("airstrike");
        TextureRegion rangeTexture = resources.getTexture("range-black");

        Array<AirStrikeLocation> airStrikeLocations = new Array<>();
        for(int i = 0; i < AirStrike.MAX_AIRSTRIKES; i++){
            AirStrikeLocation airStrikeLocation = createAirStrikeLocation();
            airStrikeLocations.add(airStrikeLocation);
        }

        return new AirStrike(airStrikePool, actorGroups.getEnemyGroup(), projectileFactory, textureRegion,
            rangeTexture, airStrikeLocations, audio);
    }

    private LandMine createLandMine(){
        TextureRegion textureRegion = resources.getTexture("landmine");
        TextureRegion rangeTexture = resources.getTexture("range");
        return new LandMine(landMinePool, actorGroups.getEnemyGroup(), projectileFactory, textureRegion,
            rangeTexture);
    }

    /**
     * Create a Support Actor
     *
     * @return SupportActor
     */
    @SuppressWarnings("unchecked")
    private <T extends Actor> T createSupportActor(Class<T> type) {

        Logger.info("Actor Factory: creating support actor: " + type.getSimpleName());
        String className = type.getSimpleName();
        T actor = null;

        switch(className){
            case "Apache":
                actor = (T) createApache();
                break;
            case "AirStrike":
                actor = (T) createAirStrike();
                break;
            case "LandMine":
                actor = (T) createLandMine();
                break;
            case "AirStrikeLocation":
                actor = (T) createAirStrikeLocation();
                break;
            case "SupplyDrop":
                actor = (T) createSupplyDrop();
                break;
            default:
                throw new IllegalArgumentException(className + " is not a valid Support Actor");

        }

        return actor;
    }

    public class SupportActorPool<T extends Actor> extends Pool<Actor> {

        private final Class<T> type;
        private final Group group;

        public SupportActorPool(Class<T> type, Group group) {

            this.type = type;
            this.group = group;
        }

        @Override
        protected T newObject() {

            return createSupportActor(type);
        }

        private Group getGroup(){
            return group;
        }

    }
}
