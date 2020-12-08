package com.lastdefenders.game.service.factory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.game.model.actor.groups.ActorGroups;
import com.lastdefenders.game.model.actor.health.ArmorIcon;
import com.lastdefenders.game.model.actor.health.HealthBar;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * Created by Eric on 3/31/2017.
 */

public class HealthFactory {

    private Resources resources;

    public HealthFactory(Resources resources) {
        this.resources = resources;
    }


    /**
     * Create a Health Bar
     *
     * @return HealthBar
     */
    public HealthBar createHealthBar(CombatActor actor) {

        Logger.info("Health Factory: creating healthbar");

        ArmorIcon armorIcon = new ArmorIcon(resources.getTexture("shield"));

        return new HealthBar(
            new TextureRegionDrawable(resources.getTexture("healthbar-green")),
            new TextureRegionDrawable(resources.getTexture("healthbar-orange")),
            new TextureRegionDrawable(resources.getTexture("healthbar-red")),
            new TextureRegionDrawable(resources.getTexture("healthbar-gray")),
            new TextureRegionDrawable(resources.getTexture("healthbar-unfilled")),
            actor, armorIcon);

    }

}
