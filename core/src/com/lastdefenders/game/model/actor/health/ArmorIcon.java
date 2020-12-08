package com.lastdefenders.game.model.actor.health;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.datastructures.Dimension;

/**
 * Created by Eric on 1/6/2017.
 */

public class ArmorIcon extends GameActor {

    private static final Dimension TEXTURE_SIZE = new Dimension(12, 13);

    private boolean healthBarShowing;

    public ArmorIcon(TextureRegion icon) {
        super(TEXTURE_SIZE);
        this.setTextureRegion(icon);
        setY(0, Align.center);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(healthBarShowing){
            setX(-TEXTURE_SIZE.getWidth(), Align.right);
        } else {
            setX(0, Align.center);
        }
    }

    public void setHealthBarShowing(boolean healthBarShowing){
        this.healthBarShowing = healthBarShowing;
    }




}
