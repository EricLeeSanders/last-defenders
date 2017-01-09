package com.foxholedefense.game.model.actor.effects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.tower.Tower;

/**
 * Created by Eric on 1/6/2017.
 */

public class ArmorDestroyedEffect extends Actor implements Pool.Poolable {
    private CombatActor actor = null;
    private Pool<ArmorDestroyedEffect> pool;
    private Animation animation;
    private float stateTime;

    public ArmorDestroyedEffect(Array<AtlasRegion> regions, Pool<ArmorDestroyedEffect> pool){
        animation = new Animation(2, regions);
        animation.setPlayMode(Animation.PlayMode.NORMAL);
        this.pool = pool;
    }

    public Actor initialize(CombatActor actor){
        this.actor = actor;
        return this;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        TextureRegion region = animation.getKeyFrame(stateTime, false);

        if (actor != null) {
            setPosition(actor.getPositionCenter().x - 22, actor.getPositionCenter().y + 16);
            batch.draw(region, getX(), getY(), 12, 13);
        }
    }

    @Override
    public void act(float delta){
        super.act(delta);
        if (actor == null || actor.isDead() ||
                (actor instanceof Tower && !((Tower)actor).isActive())) {
            pool.free(this);
            return;
        }

        stateTime += delta;
        if (animation.isAnimationFinished(stateTime)) {
            pool.free(this);
        }
    }

    @Override
    public void reset() {
        this.actor = null;
        this.remove();
        stateTime = 0;
    }

}
