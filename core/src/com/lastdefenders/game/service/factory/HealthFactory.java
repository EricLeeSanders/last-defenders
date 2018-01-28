package com.lastdefenders.game.service.factory;

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

    private ArmorIconPool armorIconPool = new ArmorIconPool();
    private HealthPool healthPool = new HealthPool();

    private ActorGroups actorGroups;
    private Resources resources;

    public HealthFactory(ActorGroups actorGroups, Resources resources) {

        this.actorGroups = actorGroups;
        this.resources = resources;
    }

    public HealthBar loadHealthBar() {

        Logger.info("Actor Factory: loading healthbar");
        HealthBar healthBar = healthPool.obtain();
        actorGroups.getHealthGroup().addActor(healthBar);
        return healthBar;
    }

    public ArmorIcon loadArmorIcon() {

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
    private HealthBar createHealthBarActor() {

        Logger.info("Actor Factory: creating healthbar");
        return new HealthBar(healthPool, resources.getTexture("healthbar-bg"),
            resources.getTexture("healthbar-life"), resources.getTexture("healthbar-armor"));

    }

    private ArmorIcon createArmorIcon() {

        Logger.info("Actor Factory: creating ArmorIcon");
        return new ArmorIcon(armorIconPool, resources.getTexture("shield"));
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