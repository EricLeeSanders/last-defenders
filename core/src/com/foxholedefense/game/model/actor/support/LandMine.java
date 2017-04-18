package com.foxholedefense.game.model.actor.support;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.helper.CollisionDetection;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.interfaces.IRocket;
import com.foxholedefense.game.service.factory.SupportActorFactory.SupportActorPool;
import com.foxholedefense.game.service.factory.ProjectileFactory;
import com.foxholedefense.util.DebugOptions;
import com.foxholedefense.util.datastructures.Dimension;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.datastructures.pool.UtilPool;

public class LandMine extends SupportActor implements IRocket {
	public static final int COST = 300;
	private static final float ATTACK = 15f;
	private static final float RANGE = 50;
	private static final Vector2 GUN_POS = UtilPool.getVector2(0,0);
	private static final Dimension TEXTURE_SIZE = new Dimension(30, 30);
	private Circle body;
	private ProjectileFactory projectileFactory;
	public LandMine(SupportActorPool<LandMine> pool, Group targetGroup, ProjectileFactory projectileFactory, TextureRegion textureRegion, TextureRegion rangeTexture) {
		super(pool, targetGroup, textureRegion, TEXTURE_SIZE, rangeTexture, RANGE, ATTACK, GUN_POS, COST);
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
		super.draw(batch, alpha);
		if (DebugOptions.showTextureBoundaries) {
			drawDebugBody(batch);
		}
	}

	private void drawDebugBody(Batch batch){
		ShapeRenderer debugBody = Resources.getShapeRenderer();
		batch.end();

		debugBody.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
		debugBody.begin(ShapeType.Line);
		debugBody.setColor(Color.YELLOW);
		debugBody.circle(this.getPositionCenter().x, this.getPositionCenter().y, this.getWidth() / 2);
		debugBody.end();

		batch.begin();
	}
	private void explode(){
		Logger.info("Landmine: exploding");
		projectileFactory.loadExplosion().initialize(this, RANGE, getTargetGroup(), this.getPositionCenter());
		this.freeActor();
	}
	private Circle getBody(){
		body.setPosition(getPositionCenter().x, getPositionCenter().y);
		return body;
	}
	
}
