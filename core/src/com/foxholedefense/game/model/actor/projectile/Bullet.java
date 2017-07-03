package com.foxholedefense.game.model.actor.projectile;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.helper.Damage;
import com.foxholedefense.game.model.actor.GameActor;
import com.foxholedefense.game.model.actor.interfaces.Attacker;
import com.foxholedefense.game.model.actor.interfaces.Targetable;
import com.foxholedefense.util.datastructures.Dimension;

/**
 * Represents a bullet that is shot from an Actor. Has an attacker and a target
 *
 * @author Eric
 */
public class Bullet extends GameActor implements Pool.Poolable {
    private static final float SPEED = 350f;
    private Targetable target;
    private Attacker attacker;
    private Pool<Bullet> pool;
    private boolean targetRemoved;

    public Bullet(Pool<Bullet> pool, TextureRegion bulletTexture) {
        this.pool = pool;
        setTextureRegion(bulletTexture);
    }

    /**
     * Initializes the bullet with the following parameters
     *
     * @param attacker - The attacker
     * @param target   - The target
     * @param size     - The size of the bullet
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
        MoveToAction moveAction = Actions.moveTo(end.x, end.y, duration, Interpolation.linear);
        moveAction.setAlignment(Align.center);
        addAction(moveAction);

        return this;
    }

    /**
     * Determines when the bullet has reached its destination and when it should
     * be freed to the pool. If the target is killed/removed, then finish the bullet animation
     * but the target should not take damage. See issue #259.
     */
    @Override
    public void act(float delta) {
        super.act(delta);
        if (!target.isActive() || target.isDead()) {
            targetRemoved = true;
        }
        if (this.getActions().size == 0) {
            if (!targetRemoved) {
                Damage.dealBulletDamage(attacker, target);
            }
            pool.free(this);
        }
    }

    @Override
    public void reset() {
        this.clear();
        target = null;
        attacker = null;
        targetRemoved = false;
        this.remove();
    }
}
