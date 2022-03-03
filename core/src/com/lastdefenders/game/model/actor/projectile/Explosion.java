package com.lastdefenders.game.model.actor.projectile;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.helper.Damage;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.model.actor.interfaces.Attacker;
import com.lastdefenders.game.service.factory.ProjectileFactory.ProjectilePool;
import com.lastdefenders.sound.LDAudio;
import com.lastdefenders.sound.LDAudio.LDSound;
import com.lastdefenders.util.datastructures.Dimension;

/**
 * Represents an explosion that is the result of an Rocket bullet. Deals AOE
 * damage.
 *
 * @author Eric
 */
public class Explosion extends GameActor implements Pool.Poolable {

    private static final Dimension size = new Dimension(128, 128);

    private Animation<TextureRegion> explosionAnimation;
    private float stateTime; // counter for animation
    private ProjectilePool<Explosion> pool;
    private LDAudio audio;

    /**
     * Constructs an Explosion.
     */
    public Explosion(ProjectilePool<Explosion> pool, Array<AtlasRegion> regions, LDAudio audio) {

        super(size);
        this.pool = pool;
        this.audio = audio;
        explosionAnimation = new Animation<TextureRegion>(0.05f, regions);
        explosionAnimation.setPlayMode(PlayMode.LOOP);
    }

    /**
     * Initializes an Explosion and deals Damage
     */
    public Actor initialize(Attacker attacker, float radius, Vector2 posCenter) {

        audio.playSound(LDSound.ROCKET_EXPLOSION);

        this.setPositionCenter(posCenter);
        Group targetGroup = attacker.getEnemyGroup();
        Damage.dealExplosionDamage(attacker, radius, posCenter, targetGroup.getChildren());

        return this;
    }

    @Override
    public void act(float delta) {

        super.act(delta);
        stateTime += delta;

        if (explosionAnimation.isAnimationFinished(stateTime)) {
            pool.free(this);
        }

        setTextureRegion(explosionAnimation.getKeyFrame(stateTime, true));
    }

    @Override
    public void reset() {

        this.clear();
        this.remove();
        stateTime = 0;
    }
}
