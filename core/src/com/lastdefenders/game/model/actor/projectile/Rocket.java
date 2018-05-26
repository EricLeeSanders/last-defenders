package com.lastdefenders.game.model.actor.projectile;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.model.actor.interfaces.Attacker;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.game.service.factory.ProjectileFactory.ProjectilePool;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.datastructures.Dimension;

/**
 * Represents an Rocket
 *
 * @author Eric
 */
public class Rocket extends GameActor implements Pool.Poolable {

    private static final float SPEED = 350f;

    private Attacker attacker;
    private Vector2 destination = new Vector2(0, 0);
    private ProjectilePool<Rocket> pool;
    private float radius;
    private ProjectileFactory projectileFactory;

    public Rocket(ProjectilePool<Rocket> pool, ProjectileFactory projectileFactory,
        TextureRegion rocketTexture) {

        this.pool = pool;
        this.projectileFactory = projectileFactory;
        setTextureRegion(rocketTexture);
    }

    /**
     * Initializes an Rocket
     */
    public Actor initialize(Attacker attacker, Vector2 destination, Dimension size, float radius) {

        this.attacker = attacker;
        this.radius = radius;
        this.destination.set(destination);

        setRotation(ActorUtil.calculateRotation(destination, attacker.getPositionCenter()));
        setSize(size.getWidth(), size.getHeight());
        setOrigin(size.getWidth() / 2, size.getHeight() / 2);

        Vector2 startPos = attacker.getGunPos();
        setPositionCenter(startPos);

        float duration = destination.dst(startPos) / SPEED;
        MoveToAction moveAction = Actions
            .moveTo(destination.x, destination.y, duration, Interpolation.linear);
        moveAction.setAlignment(Align.center);
        addAction(moveAction);

        return this;
    }

    /**
     * Determines when the rpg has reached its destination and when it should be
     * freed to the pool. If the attacker is a tower, then it handles giving the
     * Tower a kill.
     * <p>
     * When the Rocket reaches its destination, create an explosion
     */
    @Override
    public void act(float delta) {

        super.act(delta);
        if (this.getActions().size == 0) {
            projectileFactory.loadProjectile(Explosion.class).initialize(attacker, radius, destination);
            pool.free(this);
        }
    }

    @Override
    public void reset() {

        this.clear();
        attacker = null;
        radius = 0;
        this.remove();
    }
}
