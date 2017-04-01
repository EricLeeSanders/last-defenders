package com.foxholedefense.game.service.factory;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.effects.ArmorDestroyedEffect;
import com.foxholedefense.game.model.actor.effects.TowerHealEffect;
import com.foxholedefense.game.model.actor.effects.deatheffect.BloodSplatter;
import com.foxholedefense.game.model.actor.effects.deatheffect.DeathEffect;
import com.foxholedefense.game.model.actor.effects.deatheffect.DeathEffectType;
import com.foxholedefense.game.model.actor.effects.deatheffect.VehicleExplosion;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 3/31/2017.
 */

public class EffectFactory {
    private DeathEffectPool<DeathEffect> vehicleExplosionPool = new DeathEffectPool<DeathEffect>(VehicleExplosion.class);
    private DeathEffectPool<DeathEffect> bloodPool = new DeathEffectPool<DeathEffect>(BloodSplatter.class);
    private ArmorDestroyedEffectPool armorDestroyedEffectPool = new ArmorDestroyedEffectPool();
    private TowerHealEffectPool towerHealEffectPool = new TowerHealEffectPool();


    private ActorGroups actorGroups;
    private Resources resources;
    public EffectFactory(ActorGroups actorGroups, Resources resources){
        this.actorGroups = actorGroups;
        this.resources = resources;
    }

    /**
     * Obtains a Death Effect from the pool
     *
     * @param type - The type of Death Effect
     * @return DeathEffect
     */
    public DeathEffect loadDeathEffect(DeathEffectType type){

        DeathEffect deathEffect = null;
        switch(type) {
            case BLOOD:
                deathEffect = bloodPool.obtain();
                break;
            case VEHCILE_EXPLOSION:
                deathEffect = vehicleExplosionPool.obtain();
                break;
        }

        actorGroups.getDeathEffectGroup().addActor(deathEffect);
        return deathEffect;
    }

    public ArmorDestroyedEffect loadArmorDestroyedEffect(){
        ArmorDestroyedEffect armorDestroyedEffect = armorDestroyedEffectPool.obtain();
        actorGroups.getHealthGroup().addActor(armorDestroyedEffect);
        return armorDestroyedEffect;
    }

    public TowerHealEffect loadTowerHealEffect(){
        TowerHealEffect towerHealEffect = towerHealEffectPool.obtain();
        actorGroups.getHealthGroup().addActor(towerHealEffect);
        return towerHealEffect;
    }

    /**
     * Create a Death Effect
     *
     * @return DeathEffect
     */
    protected DeathEffect createDeathEffect(Class<? extends DeathEffect> type) {

        if (type.equals(BloodSplatter.class)) {
            Array<TextureAtlas.AtlasRegion> atlasRegions = resources.getAtlasRegion("blood-splatter");
            return new BloodSplatter(bloodPool, atlasRegions);
        } else if(type.equals(VehicleExplosion.class)){
            Array<TextureAtlas.AtlasRegion> atlasRegions = resources.getAtlasRegion("smoke-ring");
            return new VehicleExplosion(vehicleExplosionPool, atlasRegions);
        } else {
            throw new NullPointerException("Effect Factory couldn't create: " + type.getSimpleName());
        }

    }

    protected ArmorDestroyedEffect createArmorDestroyedEffect(){
        Label label = new Label("", resources.getSkin());
        return new ArmorDestroyedEffect(resources.getAtlasRegion("shield-destroyed"), armorDestroyedEffectPool,label);
    }

    protected TowerHealEffect createTowerHealEffect(){
        return new TowerHealEffect(towerHealEffectPool, resources.getSkin());
    }


    public class ArmorDestroyedEffectPool extends Pool<ArmorDestroyedEffect> {
        @Override
        protected ArmorDestroyedEffect newObject() {
            return createArmorDestroyedEffect();
        }
    }

    public class TowerHealEffectPool extends Pool<TowerHealEffect> {
        @Override
        protected TowerHealEffect newObject() {
            return createTowerHealEffect();
        }
    }


    public class DeathEffectPool<T extends DeathEffect> extends Pool<DeathEffect> {
        private final Class<? extends DeathEffect> type;

        public DeathEffectPool(Class<? extends DeathEffect> type) {
            this.type = type;
        }

        @Override
        protected DeathEffect newObject() {
            return createDeathEffect(type);
        }

    }
}

