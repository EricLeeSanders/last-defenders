package com.eric.mtd.game.model.actor.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.MTDGame;
import com.eric.mtd.game.GameStage;
import com.eric.mtd.game.helper.Damage;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.combat.tower.Tower;
import com.eric.mtd.game.model.actor.interfaces.IAttacker;
import com.eric.mtd.game.model.actor.interfaces.ITargetable;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.util.AudioUtil;
import com.eric.mtd.util.AudioUtil.ProjectileSound;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * Represents an explosion that is the result of an RPG bullet. Deals AOE
 * damage.
 * 
 * @author Eric
 *
 */
public class Explosion extends Actor implements Pool.Poolable {
	private Animation explosionAnimation;
	private TextureRegion currentExplosion; // current explosion from the
											// animation to draw
	private float stateTime; // counter for animation
	private TextureRegion[] explosionRegions = new TextureRegion[16];
	private ITargetable target;
	private IAttacker attacker;
	private Pool<Explosion> pool;
	/**
	 * Constructs an Explosion.
	 */
	public Explosion(Pool<Explosion> pool) {
		this.pool = pool;
		TextureAtlas actorAtlas = Resources.getAtlas(Resources.ACTOR_ATLAS);
		for (int i = 0; i < 16; i++) {
			explosionRegions[i] = actorAtlas.findRegion("Explosion" + (i + 1));
		}

	}

	/**
	 * Initializes an Explosion and deals Damage
	 */
	public void initialize(IAttacker attacker, float radius, ITargetable target, Group targetGroup, Vector2 position) {
		if (Logger.DEBUG)
			System.out.println("Setting Explosion");
		AudioUtil.playProjectileSound(ProjectileSound.RPG_EXPLOSION);
		this.attacker = attacker;
		this.target = target;
		if (targetGroup.getStage() instanceof GameStage) {
			((GameStage) targetGroup.getStage()).getActorGroups().getProjectileGroup().addActor(this);
		}
		stateTime = 0;
		explosionAnimation = new Animation(0.05f, explosionRegions);
		explosionAnimation.setPlayMode(PlayMode.NORMAL);
		this.setPosition(position.x, position.y);
		System.out.println("exp 1) enemy group size: " + targetGroup.getChildren().size);
		Damage.dealExplosionDamage(attacker, radius, position, target, targetGroup.getChildren());
	}

	/**
	 * Draws the explosion and frees it when it is done.
	 */
	@Override
	public void draw(Batch batch, float alpha) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		stateTime += (Gdx.graphics.getDeltaTime() * MTDGame.gameSpeed);
		currentExplosion = explosionAnimation.getKeyFrame(stateTime, false);

		batch.draw(currentExplosion, this.getX() - (currentExplosion.getRegionWidth() / 2), this.getY() - (currentExplosion.getRegionHeight() / 2));
		if (explosionAnimation.isAnimationFinished(stateTime)) {
			pool.free(this);
		}
	}

	@Override
	public void reset() {
		if (Logger.DEBUG)
			System.out.println("freeing explosion");
		this.clear();
		this.remove();
		explosionAnimation = null;
	}
	
	

}
