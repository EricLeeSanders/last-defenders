package com.lastdefenders.game.model.actor.health;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.game.service.factory.HealthFactory.HealthPool;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.datastructures.Dimension;

/**
 * Created by Eric on 1/6/2017.
 */

public class ArmorIcon extends GameActor implements Pool.Poolable {

    public static final float Y_OFFSET = 16;
    public static final float X_HEALTH_BAR_DISPALYING_OFFSET = -22;
    public static final float X_OFFSET = -6;
    private static final Dimension TEXTURE_SIZE = new Dimension(12, 13);
    private CombatActor actor = null;
    private HealthPool<ArmorIcon> pool;

    public ArmorIcon(HealthPool<ArmorIcon> pool, TextureRegion icon) {
        super(TEXTURE_SIZE);
        this.pool = pool;
        this.setTextureRegion(icon);
    }

    @Override
    public void draw(Batch batch, float alpha) {

        if (actor != null && actor.hasArmor()) {
            setY(actor.getPositionCenter().y + Y_OFFSET);
            // If the health bar is showing, place it to the left.
            // Other wise place it above the actor
            if (actor.getHealthPercent() < 1 || actor.getArmorPercent() < 1) {
                setX(actor.getPositionCenter().x + X_HEALTH_BAR_DISPALYING_OFFSET);
            } else {
                setX(actor.getPositionCenter().x + X_OFFSET);
            }

            super.draw(batch, alpha);
        }
    }

    @Override
    public void act(float delta) {

        super.act(delta);
        if (actor == null || actor.isDead() || !actor.isActive()) {
            pool.free(this);
        }

    }

    public void setActor(CombatActor actor) {

        Logger.info("ArmorIcon: setting actor: " + actor.getClass().getSimpleName());
        this.actor = actor;

    }

    @Override
    public void reset() {

        Logger.info("ArmorIcon: resetting");
        this.actor = null;
        this.remove();

    }
}
