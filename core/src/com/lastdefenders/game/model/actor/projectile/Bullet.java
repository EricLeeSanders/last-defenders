package com.lastdefenders.game.model.actor.projectile;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.lastdefenders.game.helper.Damage;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.model.actor.interfaces.Attacker;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.game.service.factory.ProjectileFactory.ProjectilePool;
import com.lastdefenders.util.UtilPool;
import com.lastdefenders.util.action.LDOneTimeAction;
import com.lastdefenders.util.datastructures.Dimension;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Represents a bullet that is shot from an Actor. Has an attacker and a target
 *
 * @author Eric
 */
public class Bullet extends GameActor implements Pool.Poolable {

    private static final float SPEED = 350f;
    private Targetable target;
    private Attacker attacker;
    private ProjectilePool<Bullet> pool;

    public Bullet(ProjectilePool<Bullet> pool, TextureRegion bulletTexture) {

        this.pool = pool;
        setTextureRegion(bulletTexture);
    }

    public Bullet(){}

    /**
     * Initializes the bullet with the following parameters
     *
     * @param attacker - The attacker
     * @param target - The target
     * @param size - The size of the bullet
     */
    public Actor initialize(Attacker attacker, Targetable target, Dimension size) {

        this.target = target;
        this.attacker = attacker;

        setSize(size.getWidth(), size.getHeight());
        setOrigin(size.getWidth() / 2, size.getHeight() / 2);

        Vector2 startPos = attacker.getGunPos();
        this.setPositionCenter(startPos);

        Vector2 end = target.getPositionCenter();
        float duration = end.dst(startPos) / SPEED;

        DealBulletDamageAction dealBulletDamageAction = Pools.get(DealBulletDamageAction.class).obtain();
        dealBulletDamageAction.initialize(target, attacker);

        addAction(
            Actions.sequence(
                Actions.moveToAligned(end.x, end.y, Align.center,
                    duration, Interpolation.linear),
                dealBulletDamageAction,
                UtilPool.getFreeActorAction(pool)
            )
        );

        return this;
    }

    @Override
    public void reset() {

        this.clear();
        target = null;
        attacker = null;
        this.remove();
    }

    /**
     * Inner class used to deal bullet damage. Creating a nested class rather than an anonymous
     * class so that it can be pooled.
     */
    public static class DealBulletDamageAction extends Action {

        private Targetable target;
        private Attacker attacker;

        public void initialize(Targetable target, Attacker attacker){
            this.target = target;
            this.attacker = attacker;
        }

        @Override
        public boolean act(float delta) {

            Targetable target = this.target;
            if (target.isActive() && !target.isDead()) {
                Damage.dealBulletDamage(attacker, target);
            }
            Pools.free(this);
            return true;
        }

        @Override
        public void reset(){
            super.reset();
            target = null;
            attacker = null;
        }
    }
}
