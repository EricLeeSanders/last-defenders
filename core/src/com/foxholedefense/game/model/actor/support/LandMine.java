package com.foxholedefense.game.model.actor.support;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.helper.CollisionDetection;
import com.foxholedefense.game.helper.Damage;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.interfaces.IRpg;
import com.foxholedefense.game.model.actor.projectile.Explosion;
import com.foxholedefense.game.service.factory.ActorFactory;
import com.foxholedefense.game.service.factory.ActorFactory.ExplosionPool;
import com.foxholedefense.game.service.factory.ActorFactory.SupportActorPool;
import com.foxholedefense.game.service.factory.interfaces.IProjectileFactory;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.Dimension;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

public class LandMine extends SupportActor implements IRpg{
	public static final int COST = 300;
	private static final float ATTACK = 15f;
	private static final float RANGE = 50;
	private static final Vector2 GUN_POS = new Vector2(0,0);
	private Circle body;
	private ShapeRenderer debugBody = Resources.getShapeRenderer();
	private IProjectileFactory projectileFactory;
	public LandMine(SupportActorPool<LandMine> pool, Group targetGroup, IProjectileFactory projectileFactory, TextureRegion textureRegion, TextureRegion rangeTexture) {
		super(pool, targetGroup, textureRegion, rangeTexture, RANGE, ATTACK, GUN_POS, COST);
		this.projectileFactory = projectileFactory;
		this.body = new Circle(this.getPositionCenter(), this.getWidth()/2);
	}
	@Override
	public void act(float delta) {
		super.act(delta);
		if(isActive()){
			for(Actor enemy : getTargetGroup().getChildren()){
				if(enemy instanceof Enemy){
					if(CollisionDetection.landMineAndEnemy(((Enemy)enemy).getBody(),getBody())){
						explode();
						return;
					}
				}
			}
		}
	}
	@Override
	public void draw(Batch batch, float alpha) {
		if (Logger.DEBUG) {
			batch.end();

			debugBody.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			debugBody.begin(ShapeType.Line);
			debugBody.setColor(Color.YELLOW);
			debugBody.circle(this.getPositionCenter().x, this.getPositionCenter().y, this.getWidth() / 2);
			debugBody.end();

			batch.begin();
		}

		super.draw(batch, alpha);
	}
	private void explode(){
		projectileFactory.loadExplosion().initialize(this,RANGE, null, getTargetGroup(), this.getPositionCenter());
		this.freeActor();
	}
	private Circle getBody(){
		body.setPosition(getPositionCenter().x, getPositionCenter().y);
		return body;
	}
	
}
