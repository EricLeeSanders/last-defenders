package com.foxholedefense.game.model.actor.health;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.effects.label.ArmorDestroyedEffect;
import com.foxholedefense.game.service.factory.EffectFactory;
import com.foxholedefense.util.Logger;

/**
 * Created by Eric on 1/6/2017.
 */

public class ArmorIcon extends Actor implements Pool.Poolable {

    private TextureRegion icon;
    private CombatActor actor = null;
    private Pool<ArmorIcon> pool;
    private EffectFactory effectFactory;
    private boolean showDestroyEffect;
    public ArmorIcon(Pool<ArmorIcon> pool, TextureRegion icon, EffectFactory effectFactory){
        this.pool = pool;
        this.icon = icon;
        this.effectFactory = effectFactory;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        if (actor != null && actor.hasArmor()) {
            setY( actor.getPositionCenter().y + 16);
            // If the health bar is showing, place it to the left.
            // Other wise place it above the actor
            if(actor.getHealthPercent() < 100 || actor.getArmorPercent() < 100) {
                setX(actor.getPositionCenter().x - 22);
            } else {
                setX(actor.getPositionCenter().x - 6);
            }

            batch.draw(icon, getX(), getY());
        }
    }

    @Override
    public void act(float delta) {

        super.act(delta);
        if (actor == null || actor.isDead() || !actor.isActive()) {
            pool.free(this);
            return;
        }

        // If the actor is given armor and
        // showDestroyEffect has not been set to true
        // change it to true
        if(!showDestroyEffect && actor.hasArmor()) {
            showDestroyEffect = true;
        }

        // When the armor is broken, show the destroy effect
        if(!actor.hasArmor() && showDestroyEffect){
            showDestroyEffect = false;
            ArmorDestroyedEffect effect =  effectFactory.loadLabelEffect(ArmorDestroyedEffect.class);
            effect.initialize(actor);
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
        showDestroyEffect = false;

    }
}
