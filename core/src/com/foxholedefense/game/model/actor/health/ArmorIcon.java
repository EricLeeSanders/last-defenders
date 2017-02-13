package com.foxholedefense.game.model.actor.health;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.effects.ArmorDestroyedEffect;

/**
 * Created by Eric on 1/6/2017.
 */

public class ArmorIcon extends Actor implements Pool.Poolable {

    private TextureRegion icon;
    private CombatActor actor = null;
    private Pool<ArmorIcon> pool;
    private Pool<ArmorDestroyedEffect> armorDestroyedEffectPool;
    private boolean showDestroyEffect;
    public ArmorIcon(Pool<ArmorIcon> pool, TextureRegion icon, Pool<ArmorDestroyedEffect> armorDestroyedEffectPool){
        this.pool = pool;
        this.icon = icon;
        this.armorDestroyedEffectPool = armorDestroyedEffectPool;
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

            batch.draw(icon, getX(), getY(), 12, 13);
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
            System.out.println("SHOW DESTROY");
            showDestroyEffect = false;
            ArmorDestroyedEffect effect = armorDestroyedEffectPool.obtain();
            this.getParent().addActor(effect);
            effect.initialize(actor);
        }

    }

    public void setActor(CombatActor actor) {
        this.actor = actor;
        this.setSize(30, 4);

    }

    @Override
    public void reset() {
        System.out.println("Armor Icon RESET");
        this.actor = null;
        this.remove();
        showDestroyEffect = false;

    }
}
