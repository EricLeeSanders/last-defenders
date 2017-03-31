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

    private Map<String, TextureRegion> loadedTextures = new HashMap<String, TextureRegion>();
    private Map<String, Array<TextureAtlas.AtlasRegion>> loadedAtlasRegions = new HashMap<String, Array<TextureAtlas.AtlasRegion>>();

    private ActorGroups actorGroups;
    private Resources resources;
    public EffectFactory(ActorGroups actorGroups, Resources resources){
        this.actorGroups = actorGroups;
        this.resources = resources;
        initTextures(resources.getAsset(Resources.ACTOR_ATLAS, TextureAtlas.class));
    }

    private void initTextures(TextureAtlas actorAtlas){

        Logger.info("Effect Factory: initializing textures");

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

        Logger.info("Effect Factory: textures initialized");
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
            Array<TextureAtlas.AtlasRegion> atlasRegions = loadedAtlasRegions.get("blood-splatter");
            return new BloodSplatter(bloodPool, atlasRegions);
        } else if(type.equals(VehicleExplosion.class)){
            Array<TextureAtlas.AtlasRegion> atlasRegions = loadedAtlasRegions.get("smoke-ring");
            return new VehicleExplosion(vehicleExplosionPool, atlasRegions);
        } else {
            throw new NullPointerException("Effect Factory couldn't create: " + type.getSimpleName());
        }

    }

    protected ArmorDestroyedEffect createArmorDestroyedEffect(){
        Label label = new Label("", resources.getSkin());
        return new ArmorDestroyedEffect(loadedAtlasRegions.get("shield-destroyed"), armorDestroyedEffectPool,label);
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

