package com.foxholedefense.game.model.actor.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.FHDGame;
import com.foxholedefense.game.GameStage;
import com.foxholedefense.game.helper.Damage;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.interfaces.IAttacker;
import com.foxholedefense.game.model.actor.interfaces.ITargetable;
import com.foxholedefense.game.service.factory.ActorFactory;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.FHDAudio.FHDSound;

/**
 * Represents an explosion that is the result of an RPG bullet. Deals AOE
 * damage.
 * 
 * @author Eric
 *
 */
public class Explosion extends Actor implements Pool.Poolable {
	private Animation explosionAnimation;
	private float stateTime; // counter for animation
	private Pool<Explosion> pool;
	private FHDAudio audio;
	/**
	 * Constructs an Explosion.
	 */
	public Explosion(Pool<Explosion> pool, Array<AtlasRegion> regions, FHDAudio audio) {
		this.pool = pool;
		this.audio = audio;
	
		explosionAnimation = new Animation(0.05f, regions);
		explosionAnimation.setPlayMode(PlayMode.LOOP);
	}

	/**
	 * Initializes an Explosion and deals Damage
	 */
	public Actor initialize(IAttacker attacker, float radius, ITargetable target, Group targetGroup, Vector2 position) {
		audio.playSound(FHDSound.RPG_EXPLOSION);
		if (targetGroup.getStage() instanceof GameStage) {
			((GameStage) targetGroup.getStage()).getActorGroups().getProjectileGroup().addActor(this);
		}
		this.setPosition(position.x, position.y);
		Damage.dealExplosionDamage(attacker, radius, position, target, targetGroup.getChildren());
		return this;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		stateTime += delta;
	}

	/**
	 * Draws the explosion and frees it when it is done.
	 */
	@Override
	public void draw(Batch batch, float alpha) {
		TextureRegion currentExplosion = explosionAnimation.getKeyFrame(stateTime, true);

		if (explosionAnimation.isAnimationFinished(stateTime)) {
			pool.free(this);
		}

		batch.draw(currentExplosion, this.getX() - (currentExplosion.getRegionWidth() / 2), this.getY() - (currentExplosion.getRegionHeight() / 2));
	}

	@Override
	public void reset() {
		this.clear();
		this.remove();
		stateTime = 0;
	}
	
	

}
