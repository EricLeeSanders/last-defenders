package com.foxholedefense.game.model.actor.support;

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
import com.foxholedefense.game.GameStage;
import com.foxholedefense.game.model.actor.interfaces.IAttacker;
import com.foxholedefense.game.model.actor.interfaces.IRpg;
import com.foxholedefense.game.model.actor.projectile.AirStrikeBomb;
import com.foxholedefense.game.model.actor.projectile.Bullet;
import com.foxholedefense.game.service.factory.ActorFactory;
import com.foxholedefense.game.service.factory.ActorFactory.AirStrikeBombPool;
import com.foxholedefense.game.service.factory.ActorFactory.SupportActorPool;
import com.foxholedefense.game.service.factory.interfaces.IProjectileFactory;
import com.foxholedefense.util.Dimension;
import com.foxholedefense.util.FHDVector2;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.FHDAudio.FHDSound;
import com.foxholedefense.util.UtilPool;

public class AirStrike extends SupportActor implements IRpg{
	public static final float AIRSTRIKE_DURATION = 2.5f;
	public static final int COST = 1000;
	public static final float ATTACK = 10f;
	public static final float SPEED = 150f;
	public static final float AIRSTRIKE_RADIUS = 60;
	public static final int MAX_AIRSTRIKES = 3;
	public static final Vector2 GUN_POS = UtilPool.getVector2();
	public static final Dimension BULLET_SIZE = new Dimension(10, 10);
	private Array<AirStrikeLocation> airStrikeLocations = new Array<AirStrikeLocation>();
	private IProjectileFactory projectileFactory;
	private FHDAudio audio;
	private TextureRegion rangeTexture;

	public AirStrike(SupportActorPool<AirStrike> pool, Group targetGroup, IProjectileFactory projectileFactory, TextureRegion textureRegion, TextureRegion rangeTexture, FHDAudio audio) {
		super(pool, targetGroup, textureRegion, rangeTexture, AIRSTRIKE_RADIUS, ATTACK, GUN_POS, COST);
		this.audio = audio;
		this.projectileFactory = projectileFactory;
		this.rangeTexture = rangeTexture;

	}
	public void addLocation(AirStrikeLocation location){
		airStrikeLocations.add(location);
	}
	public void beginAirStrike(){
		Logger.info("AirStrike: Beginning Air Strike Run");
		setActive(true);
		FHDVector2 centerPos = UtilPool.getVector2(0-this.getHeight(), Resources.VIRTUAL_HEIGHT/2);
		setPositionCenter(centerPos);
		centerPos.free();
		this.addAction(Actions.moveTo(Resources.VIRTUAL_WIDTH+this.getWidth(), ((Resources.VIRTUAL_HEIGHT/2) - (getHeight()/2)),  AIRSTRIKE_DURATION, Interpolation.linear));
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
		audio.playSound(FHDSound.ROCKET_LAUNCH);
		projectileFactory.loadAirStrikeBomb().initialize(this, location.getLocation(), this.getTargetGroup(),this.getGunPos(), BULLET_SIZE, AIRSTRIKE_RADIUS); 
		
		
	}
	public boolean readyToBegin(){
		return (airStrikeLocations.size >= MAX_AIRSTRIKES);
	}
	@Override
	public void reset() {
		Logger.info("AirStrike: Resetting");
		for(AirStrikeLocation location : airStrikeLocations){
			location.remove();
			location.clear();
		}
		airStrikeLocations.clear();
		super.reset();
	}
}
