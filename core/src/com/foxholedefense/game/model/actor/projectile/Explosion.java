package com.foxholedefense.game.model.actor.projectile;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.helper.Damage;
import com.foxholedefense.game.model.actor.GameActor;
import com.foxholedefense.game.model.actor.interfaces.Attacker;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;
import com.foxholedefense.util.datastructures.Dimension;

/**
 * Represents an explosion that is the result of an Rocket bullet. Deals AOE
 * damage.
 * 
 * @author Eric
 *
 */
public class Explosion extends GameActor implements Pool.Poolable {

	private static final Dimension size = new Dimension(128,128);

	private Animation<TextureRegion> explosionAnimation;
	private float stateTime; // counter for animation
	private Pool<Explosion> pool;
	private FHDAudio audio;
	/**
	 * Constructs an Explosion.
	 */
	public Explosion(Pool<Explosion> pool, Array<AtlasRegion> regions, FHDAudio audio) {
		super(size);
		this.pool = pool;
		this.audio = audio;
		setRotation(90);
		explosionAnimation = new Animation<TextureRegion>(0.05f, regions);
		explosionAnimation.setPlayMode(PlayMode.LOOP);
	}

	/**
	 * Initializes an Explosion and deals Damage
	 */
	public Actor initialize(Attacker attacker, float radius, Vector2 posCenter) {
		audio.playSound(FHDSound.ROCKET_EXPLOSION);

		this.setPositionCenter(posCenter);
		Group targetGroup = attacker.getTargetGroup();
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
