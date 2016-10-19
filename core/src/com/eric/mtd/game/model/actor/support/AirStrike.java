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
import com.eric.mtd.game.GameStage;
import com.eric.mtd.game.model.actor.interfaces.IAttacker;
import com.eric.mtd.game.model.actor.interfaces.IRpg;
import com.eric.mtd.game.model.actor.projectile.AirStrikeBomb;
import com.eric.mtd.game.model.actor.projectile.Bullet;
import com.eric.mtd.game.service.factory.ActorFactory;
import com.eric.mtd.game.service.factory.ActorFactory.AirStrikeBombPool;
import com.eric.mtd.game.service.factory.ActorFactory.SupportActorPool;
import com.eric.mtd.game.service.factory.interfaces.IProjectileFactory;
import com.eric.mtd.util.MTDAudio;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;
import com.eric.mtd.util.MTDAudio.ProjectileSound;
import com.eric.mtd.util.Dimension;

public class AirStrike extends SupportActor implements IRpg{
	public static final float AIRSTRIKE_DURATION = 2.5f;
	public static final int COST = 1000;
	public static final float ATTACK = 10f;
	public static final float SPEED = 150f;
	public static final float AIRSTRIKE_RADIUS = 75;
	public static final int MAX_AIRSTRIKES = 3;
	public static final Vector2 GUN_POS = new Vector2(0,0);
	public static final Dimension BULLET_SIZE = new Dimension(20,20);
	private Array<AirStrikeLocation> airStrikeLocations = new Array<AirStrikeLocation>();
	private IProjectileFactory projectileFactory;
	private MTDAudio audio;
	public AirStrike(SupportActorPool<AirStrike> pool, Group targetGroup, IProjectileFactory projectileFactory, TextureRegion textureRegion, MTDAudio audio) {
		super(pool, targetGroup, textureRegion, new Dimension(textureRegion.getRegionWidth(), textureRegion.getRegionHeight())
				,0f,ATTACK, GUN_POS, COST);
		this.audio = audio;
		this.projectileFactory = projectileFactory;

	}
	public void addLocation(Vector2 location, Group group){
		airStrikeLocations.add(new AirStrikeLocation(location,AIRSTRIKE_RADIUS, group ));
	}
	public void beginAirStrike(){
		Logger.info("Beginning Air Strike Run");
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
		audio.playProjectileSound(ProjectileSound.ROCKET_LAUNCH);
		projectileFactory.loadAirStrikeBomb().initialize(this, location.getLocation(), this.getTargetGroup(),this.getGunPos(), BULLET_SIZE, AIRSTRIKE_RADIUS); 
		
		
	}
	public boolean readyToBegin(){
		return (airStrikeLocations.size >= MAX_AIRSTRIKES);
	}
	@Override
	public void reset() {
		for(AirStrikeLocation location : airStrikeLocations){
			location.remove();
			location.clear();
			
		}
		airStrikeLocations.clear();
		super.reset();
	}
}
