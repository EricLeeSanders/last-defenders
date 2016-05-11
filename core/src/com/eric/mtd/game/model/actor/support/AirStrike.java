package com.eric.mtd.game.model.actor.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.game.model.actor.interfaces.IAttacker;
import com.eric.mtd.game.model.actor.interfaces.IRpg;
import com.eric.mtd.game.model.actor.projectile.AirStrikeBomb;
import com.eric.mtd.game.model.actor.projectile.Bullet;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.util.AudioUtil;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;
import com.eric.mtd.util.AudioUtil.ProjectileSound;
import com.eric.mtd.util.Dimension;

public class AirStrike extends SupportActor implements IRpg{
	public static final float AIRSTRIKE_DURATION = 2.5f;
	public static final int COST = 1000;
	public static final float ATTACK = 10f;
	public static final float SPEED = 150f;
	public static final float AIRSTRIKE_RADIUS = 50f;
	public static final int MAX_AIRSTRIKES = 3;
	public static final Vector2 GUN_POS = new Vector2(0,0);
	public static final Dimension BULLET_SIZE = new Dimension(20,20);
	private Array<AirStrikeLocation> airStrikeLocations = new Array<AirStrikeLocation>();
	public AirStrike(Pool<SupportActor> pool, TextureRegion textureRegion) {
		super(pool, textureRegion, new Dimension(textureRegion.getRegionWidth(), textureRegion.getRegionHeight())
				,0f,ATTACK, GUN_POS, COST);

	}
	public void addLocation(Vector2 location, Group group){
		if (Logger.DEBUG)
			System.out.println("Adding AirStrike Location at " + location.x + "," + location.y);
		airStrikeLocations.add(new AirStrikeLocation(location,AIRSTRIKE_RADIUS, group ));
	}
	public void beginAirStrike(){
		if(Logger.DEBUG)System.out.println("Beginning Air Strike Run");
		setActive(true);
		this.setRotation(-90);
		setPositionCenter(new Vector2(0-this.getHeight(), Resources.VIRTUAL_HEIGHT/2));
		this.addAction(Actions.moveTo(Resources.VIRTUAL_WIDTH+this.getHeight(), ((Resources.VIRTUAL_HEIGHT/2) - (getHeight()/2)),  AIRSTRIKE_DURATION, Interpolation.linear));
		for(AirStrikeLocation location : airStrikeLocations){
			dropBomb(location);
			location.setShowRange(false);
		}
	}
	@Override
	public void act(float delta) {
		super.act(delta);
		if(isActive()){
			if(this.getActions().size <= 0){
				this.freeActor();
			}
		}
	}
	private void dropBomb(AirStrikeLocation location){
		if (Logger.DEBUG)
			System.out.println("AirStrike: Dropping Bomb at " + location.getLocation());
		AudioUtil.playProjectileSound(ProjectileSound.ROCKET_LAUNCH);
		AirStrikeBomb bomb = ActorFactory.loadAirStrikeBomb();
		bomb.initialize(this, location.getLocation(), this.getEnemyGroup(),this.getGunPos(), BULLET_SIZE, AIRSTRIKE_RADIUS); 
		/*if(Logger.DEBUG)System.out.println("removing location at : " + location.getLocation());
		airStrikeLocations.removeValue(location, false);
		location.remove();
		location.clear();*/
		
		
	}
	public boolean readyToBegin(){
		return (airStrikeLocations.size >= MAX_AIRSTRIKES);
	}
	@Override
	public void reset() {
		if (Logger.DEBUG)
			System.out.println("freeing AirStrike");
		for(AirStrikeLocation location : airStrikeLocations){
			location.remove();
			location.clear();
			
		}
		airStrikeLocations.clear();
		super.reset();
	}
}
