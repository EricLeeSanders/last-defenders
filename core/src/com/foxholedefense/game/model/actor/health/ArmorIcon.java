package com.foxholedefense.game.model.actor.health;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.util.Logger;

/**
 * Created by Eric on 1/6/2017.
 */

public class ArmorIcon extends Actor implements Pool.Poolable {

    public static final float Y_OFFSET = 16;
    public static final float X_HEALTH_BAR_DISPALYING_OFFSET = -22;
    public static final float X_OFFSET = -6;
    private TextureRegion icon;
    private CombatActor actor = null;
    private Pool<ArmorIcon> pool;
    public ArmorIcon(Pool<ArmorIcon> pool, TextureRegion icon){
        this.pool = pool;
        this.icon = icon;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        if (actor != null && actor.hasArmor()) {
            setY(actor.getPositionCenter().y + Y_OFFSET);
            // If the health bar is showing, place it to the left.
            // Other wise place it above the actor
            if(actor.getHealthPercent() < 1 || actor.getArmorPercent() < 1) {
                setX(actor.getPositionCenter().x + X_HEALTH_BAR_DISPALYING_OFFSET);
            } else {
                setX(actor.getPositionCenter().x + X_OFFSET);
            }

            batch.draw(icon, getX(), getY());
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
        this.setSize(icon.getRegionWidth(), icon.getRegionHeight());

    }

    @Override
    public void reset() {
        Logger.info("ArmorIcon: resetting");
        this.actor = null;
        this.remove();

    }
}
