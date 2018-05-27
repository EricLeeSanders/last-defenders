package com.lastdefenders.game.service.factory;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.model.actor.ActorGroups;
import com.lastdefenders.game.model.actor.effects.label.ArmorDestroyedEffect;
import com.lastdefenders.game.model.actor.effects.label.TowerHealEffect;
import com.lastdefenders.game.model.actor.effects.label.WaveOverCoinEffect;
import com.lastdefenders.game.model.actor.effects.texture.TextureEffect;
import com.lastdefenders.game.model.actor.effects.texture.animation.EnemyCoinEffect;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.BloodSplatter;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffectType;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.VehicleExplosion;
import com.lastdefenders.util.Resources;

/**
 * Created by Eric on 3/31/2017.
 */

public class EffectFactory {

    private EffectPool<VehicleExplosion> vehicleExplosionPool;
    private EffectPool<BloodSplatter> bloodPool;
    private EffectPool<ArmorDestroyedEffect> armorDestroyedEffectPool;
    private EffectPool<TowerHealEffect> towerHealEffectPool;
    private EffectPool<WaveOverCoinEffect> waveOverCoinEffectPool;
    private EffectPool<EnemyCoinEffect> enemyCoinEffectPool;

    private Resources resources;

    public EffectFactory(ActorGroups actorGroups, Resources resources) {

        this.resources = resources;
        init(actorGroups);
    }

    private void init(ActorGroups actorGroups){

        vehicleExplosionPool = new EffectPool<>(VehicleExplosion.class, actorGroups.getDeathEffectGroup());
        bloodPool = new EffectPool<>(BloodSplatter.class, actorGroups.getDeathEffectGroup());
        armorDestroyedEffectPool = new EffectPool<>(ArmorDestroyedEffect.class, actorGroups.getEffectGroup());
        towerHealEffectPool = new EffectPool<>(TowerHealEffect.class, actorGroups.getEffectGroup());
        waveOverCoinEffectPool = new EffectPool<>(WaveOverCoinEffect.class, actorGroups.getEffectGroup());
        enemyCoinEffectPool = new EffectPool<>(EnemyCoinEffect.class, actorGroups.getEffectGroup());
    }

    public <T extends TextureEffect> T loadDeathEffect(DeathEffectType type, boolean addToGroup){

        EffectPool<? extends Actor> effectPool = null;
        switch(type){
            case BLOOD:
                effectPool = bloodPool;
                break;
            case VEHCILE_EXPLOSION:
                effectPool = vehicleExplosionPool;
                break;
            default:
                throw new NullPointerException("Effect Factory couldn't load " + type);
        }

        @SuppressWarnings("unchecked")
        T effect = (T) effectPool.obtain();

        if(addToGroup){
            effectPool.getGroup().addActor(effect);
        }
        return effect;
    }

    /**
     * Obtains an effect from the respective pool with the option to add to the respective group.
     *
     * @param type
     * @param addToGroup
     * @param <T>
     *
     * @return effect
     */
    public <T extends Actor> T loadEffect(Class<T> type, boolean addToGroup) {

        String className = type.getSimpleName();
        EffectPool<? extends Actor> effectPool = null;
        switch(className){
            case "EnemyCoinEffect":
                effectPool = enemyCoinEffectPool;
                break;
            case "ArmorDestroyedEffect":
                effectPool = armorDestroyedEffectPool;
                break;
            case "TowerHealEffect":
                effectPool = towerHealEffectPool;
                break;
            case "WaveOverCoinEffect":
                effectPool = waveOverCoinEffectPool;
                break;
            default:
                throw new IllegalArgumentException(className + " is not a valid Effect class");
        }

        @SuppressWarnings("unchecked")
        T effect = (T) effectPool.obtain();

        if(addToGroup){
            effectPool.getGroup().addActor(effect);
        }
        return effect;
    }



    @SuppressWarnings("unchecked")
    private <T extends Actor> T createEffect(Class<T> type) {

        String className = type.getSimpleName();
        T effect = null;
        switch(className){
            case "EnemyCoinEffect":
                effect = (T) createEnemyCoinEffect();
                break;
            case "ArmorDestroyedEffect":
                effect = (T) createArmorDestroyedEffect();
                break;
            case "TowerHealEffect":
                effect = (T) createTowerHealEffect();
                break;
            case "WaveOverCoinEffect":
                effect = (T) createWaveOverCoinEffect();
                break;
            case "BloodSplatter":
                effect = (T) createBloodSplatter();
                break;
            case "VehicleExplosion":
                effect = (T) createVehicleExplosion();
                break;
            default:
                throw new IllegalArgumentException(className + " is not a valid Effect class");
        }
        return effect;
    }

    private VehicleExplosion createVehicleExplosion(){

        Array<AtlasRegion> atlasRegions = resources.getAtlasRegion("smoke-ring");
        return new VehicleExplosion(vehicleExplosionPool, atlasRegions);
    }

    private BloodSplatter createBloodSplatter(){

        TextureRegion textureRegion = resources.getTexture("blood");
        return new BloodSplatter(bloodPool, textureRegion);
    }

    private WaveOverCoinEffect createWaveOverCoinEffect(){

        Array<AtlasRegion> atlasRegions = resources.getAtlasRegion("coin");
        return new WaveOverCoinEffect(waveOverCoinEffectPool, resources.getSkin(),
            atlasRegions, resources.getFontScale());
    }

    private TowerHealEffect createTowerHealEffect(){

        return new TowerHealEffect(towerHealEffectPool, resources.getSkin(), resources.getFontScale());
    }

    private ArmorDestroyedEffect createArmorDestroyedEffect(){

        Array<AtlasRegion> atlasRegions = resources.getAtlasRegion("shield-destroyed");
        return new ArmorDestroyedEffect(atlasRegions, armorDestroyedEffectPool,
            resources.getSkin(), resources.getFontScale());
    }

    private EnemyCoinEffect createEnemyCoinEffect(){

        Array<AtlasRegion> atlasRegions = resources.getAtlasRegion("coin");
        return new EnemyCoinEffect(enemyCoinEffectPool, atlasRegions);
    }

    public class EffectPool<T extends Actor> extends Pool<Actor> {

        private final Class<T> type;
        private final Group group;

        EffectPool(Class<T> type, Group group) {

            this.type = type;
            this.group = group;
        }

        @Override
        protected T newObject() {

            return createEffect(type);
        }

        private Group getGroup(){

            return group;
        }
    }
}
