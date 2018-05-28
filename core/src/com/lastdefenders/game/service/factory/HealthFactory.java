package com.lastdefenders.game.service.factory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.model.actor.ActorGroups;
import com.lastdefenders.game.model.actor.health.ArmorIcon;
import com.lastdefenders.game.model.actor.health.HealthBar;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * Created by Eric on 3/31/2017.
 */

public class HealthFactory {

    private HealthPool<ArmorIcon> armorIconPool = new HealthPool<>(ArmorIcon.class);
    private HealthPool<HealthBar> healthPool = new HealthPool<>(HealthBar.class);

    private ActorGroups actorGroups;
    private Resources resources;

    public HealthFactory(ActorGroups actorGroups, Resources resources) {

        this.actorGroups = actorGroups;
        this.resources = resources;
    }

    public HealthBar loadHealthBar() {

        Logger.info("Actor Factory: loading healthbar");
        HealthBar healthBar = (HealthBar) healthPool.obtain();
        actorGroups.getHealthGroup().addActor(healthBar);
        return healthBar;
    }

    public ArmorIcon loadArmorIcon() {

        Logger.info("Actor Factory: loading ArmorIcon");
        ArmorIcon armorIcon = (ArmorIcon) armorIconPool.obtain();
        actorGroups.getHealthGroup().addActor(armorIcon);
        return armorIcon;
    }

    @SuppressWarnings("unchecked")
    private <T extends Actor> T createHealthActor(Class<T> type){

        String className = type.getSimpleName();
        T healthActor = null;

        switch(className){
            case "ArmorIcon":
                healthActor = (T) createArmorIcon();
                break;
            case "HealthBar":
                healthActor = (T) createHealthBar();
                break;
            default:
                throw new IllegalArgumentException(className + " is not a valid Health class");
        }

        return healthActor;

    }

    /**
     * Create a Health Bar
     *
     * @return HealthBar
     */
    private HealthBar createHealthBar() {

        Logger.info("Actor Factory: creating healthbar");
        return new HealthBar(healthPool,
            new TextureRegionDrawable(resources.getTexture("healthbar-green")),
            new TextureRegionDrawable(resources.getTexture("healthbar-orange")),
            new TextureRegionDrawable(resources.getTexture("healthbar-red")),
            new TextureRegionDrawable(resources.getTexture("healthbar-gray")),
            new TextureRegionDrawable(resources.getTexture("healthbar-unfilled")));

    }

    private ArmorIcon createArmorIcon() {

        Logger.info("Actor Factory: creating ArmorIcon");
        return new ArmorIcon(armorIconPool, resources.getTexture("shield"));
    }


    public class HealthPool<T extends Actor> extends Pool<Actor> {

        private final Class<T> type;

        HealthPool(Class<T> type){
            this.type = type;
        }

        @Override
        protected T newObject() {

            return createHealthActor(type);
        }
    }
}
