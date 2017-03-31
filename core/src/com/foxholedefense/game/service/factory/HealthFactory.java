package com.foxholedefense.game.service.factory;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.health.ArmorIcon;
import com.foxholedefense.game.model.actor.health.HealthBar;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 3/31/2017.
 */

public class HealthFactory {
    private ArmorIconPool armorIconPool = new ArmorIconPool();
    private HealthPool healthPool = new HealthPool();

    private Map<String, TextureRegion> loadedTextures = new HashMap<String, TextureRegion>();

    private ActorGroups actorGroups;
    private EffectFactory effectFactory;

    public HealthFactory(ActorGroups actorGroups, Resources resources, EffectFactory effectFactory){
        this.actorGroups = actorGroups;
        this.effectFactory = effectFactory;
        initTextures(resources.getAsset(Resources.ACTOR_ATLAS, TextureAtlas.class));
    }

    private void initTextures(TextureAtlas actorAtlas){

        Logger.info("Health Factory: initializing textures");

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

        Logger.info("Health Factory: textures initialized");
    }


    public HealthBar loadHealthBar() {
        Logger.info("Actor Factory: loading healthbar");
        HealthBar healthBar = healthPool.obtain();
        actorGroups.getHealthGroup().addActor(healthBar);
        return healthBar;
    }

    public ArmorIcon loadArmorIcon(){
        Logger.info("Actor Factory: loading ArmorIcon");
        ArmorIcon armorIcon = armorIconPool.obtain();
        actorGroups.getHealthGroup().addActor(armorIcon);
        return armorIcon;
    }
    /**
     * Create a Health Bar
     *
     * @return HealthBar
     */
    protected HealthBar createHealthBarActor() {
        Logger.info("Actor Factory: creating healthbar");
        HealthBar healthBar = new HealthBar(healthPool, loadedTextures.get("healthbar-bg"), loadedTextures.get("healthbar-life"), loadedTextures.get("healthbar-armor"));
        return healthBar;

    }

    protected ArmorIcon createArmorIcon(){
        Logger.info("Actor Factory: creating ArmorIcon");
        return new ArmorIcon(armorIconPool, loadedTextures.get("shield"), effectFactory);
    }

    public class HealthPool extends Pool<HealthBar> {
        @Override
        protected HealthBar newObject() {
            return createHealthBarActor();
        }
    }

    public class ArmorIconPool extends Pool<ArmorIcon> {
        @Override
        protected ArmorIcon newObject() {
            return createArmorIcon();
        }
    }
}
