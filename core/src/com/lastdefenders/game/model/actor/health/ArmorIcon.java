package com.lastdefenders.game.model.actor.health;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.datastructures.Dimension;

/**
 * Created by Eric on 1/6/2017.
 */

public class ArmorIcon extends GameActor {

    public static final float Y_OFFSET = 16;
    public static final float X_HEALTH_BAR_DISPLAYING_OFFSET = -22;
    public static final float X_OFFSET = -6;
    private static final Dimension TEXTURE_SIZE = new Dimension(12, 13);
    private CombatActor actor;

    public ArmorIcon(TextureRegion icon, CombatActor actor) {
        super(TEXTURE_SIZE);
        this.setTextureRegion(icon);
        this.actor = actor;
    }

    @Override
    public void draw(Batch batch, float alpha) {

        if (actor != null && actor.hasArmor()) {
            setY(actor.getPositionCenter().y + Y_OFFSET);
            // If the health bar is showing, place it to the left.
            // Other wise place it above the actor
            if (actor.getHealthPercent() < 1 || actor.getArmorPercent() < 1) {
                setX(actor.getPositionCenter().x + X_HEALTH_BAR_DISPLAYING_OFFSET);
            } else {
                setX(actor.getPositionCenter().x + X_OFFSET);
            }

            super.draw(batch, alpha);
        }
    }

}
