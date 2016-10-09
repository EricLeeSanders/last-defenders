package com.eric.mtd.game.model.actor.support;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.game.helper.CollisionDetection;
import com.eric.mtd.game.helper.Damage;
import com.eric.mtd.game.model.actor.combat.enemy.Enemy;
import com.eric.mtd.game.model.actor.interfaces.IRpg;
import com.eric.mtd.game.model.actor.projectile.Explosion;
import com.eric.mtd.game.service.factory.ActorFactory;
import com.eric.mtd.game.service.factory.ActorFactory.ExplosionPool;
import com.eric.mtd.game.service.factory.ActorFactory.SupportActorPool;
import com.eric.mtd.util.Dimension;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

public class LandMine extends SupportActor implements IRpg{
	public static final int COST = 300;
	private static final float ATTACK = 15f;
	private static final float RANGE = 50;
	private static final Vector2 GUN_POS = new Vector2(0,0);
	private static final float SCALE = 0.30f;
	private ShapeRenderer debugBody = Resources.getShapeRenderer();
	private ExplosionPool explosionPool;
	public LandMine(SupportActorPool<LandMine> pool, ExplosionPool explosionPool, TextureRegion textureRegion) {
		super(pool, textureRegion, new Dimension(textureRegion.getRegionWidth()*SCALE, textureRegion.getRegionHeight()*SCALE)
				,RANGE,ATTACK, GUN_POS, COST);
		this.explosionPool = explosionPool;

	}
	@Override
	public void act(float delta) {
		super.act(delta);
		if(isActive()){
			for(Actor enemy : getEnemyGroup().getChildren()){
				if(enemy instanceof Enemy){
					if(CollisionDetection.landMineAndEnemy(getBody(), ((Enemy)enemy).getBody())){
						explode();
						return;
					}
				}
			}
		}
	}
	@Override
	public void draw(Batch batch, float alpha) {
		batch.end();
		if (Logger.DEBUG) {
			debugBody.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			debugBody.begin(ShapeType.Line);
			debugBody.setColor(Color.YELLOW);
			debugBody.rect(getBody().x,getBody().y,getBody().width,getBody().height);
			debugBody.end();
		}
		batch.begin();
		super.draw(batch, alpha);
	}
	private void explode(){
		getProjectileGroup().addActor(explosionPool.obtain().initialize(this,RANGE, null, getEnemyGroup(), this.getPositionCenter()));
		this.freeActor();
	}
	private Rectangle getBody(){
		return new Rectangle(this.getX(),this.getY(),this.getWidth(),this.getHeight());
	}
	
}
