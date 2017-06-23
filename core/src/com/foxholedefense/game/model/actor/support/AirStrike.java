package com.foxholedefense.game.model.actor.support;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.foxholedefense.game.model.actor.interfaces.IRocket;
import com.foxholedefense.game.service.factory.SupportActorFactory.SupportActorPool;
import com.foxholedefense.game.service.factory.ProjectileFactory;
import com.foxholedefense.util.datastructures.Dimension;
import com.foxholedefense.util.datastructures.pool.FHDVector2;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.FHDAudio.FHDSound;
import com.foxholedefense.util.datastructures.pool.UtilPool;

public class AirStrike extends SupportActor implements IRocket {

	public static final float AIRSTRIKE_DURATION = 2.5f;
	public static final int COST = 1000;
	private static final float ATTACK = 10f;
	public static final float AIRSTRIKE_RADIUS = 60;
	private static final int MAX_AIRSTRIKES = 3;

	private static final Vector2 GUN_POS = UtilPool.getVector2();
	private static final Dimension ROCKET_SIZE = new Dimension(46, 10);
	private static final Dimension TEXTURE_SIZE = new Dimension(203, 125);

	private Array<AirStrikeLocation> airStrikeLocations = new Array<AirStrikeLocation>();
	private ProjectileFactory projectileFactory;
	private FHDAudio audio;

	public AirStrike(SupportActorPool<AirStrike> pool, Group targetGroup, ProjectileFactory projectileFactory, TextureRegion textureRegion, TextureRegion rangeTexture, FHDAudio audio) {
		super(pool, targetGroup, textureRegion, TEXTURE_SIZE, rangeTexture, AIRSTRIKE_RADIUS, ATTACK, GUN_POS, COST);
		this.audio = audio;
		this.projectileFactory = projectileFactory;

	}
	public void addLocation(AirStrikeLocation location){
		airStrikeLocations.add(location);
	}

	public void beginAirStrike(){
		Logger.info("AirStrike: Beginning Air Strike Run");

		setActive(true);
		FHDVector2 centerPos = UtilPool.getVector2(-getWidth() / 2, Resources.VIRTUAL_HEIGHT/2);
		setPositionCenter(centerPos);
		centerPos.free();

		MoveToAction moveAction = Actions.moveTo(Resources.VIRTUAL_WIDTH + getWidth(), (Resources.VIRTUAL_HEIGHT / 2), AIRSTRIKE_DURATION, Interpolation.linear);
		moveAction.setAlignment(Align.center);
		addAction(moveAction);

		audio.playSound(FHDSound.ROCKET_LAUNCH);
		for(AirStrikeLocation location : airStrikeLocations){
			dropBomb(location);
			location.setShowRange(false);
		}
	}
	@Override
	public void act(float delta) {
		super.act(delta);
		if(isActive()){
			if(getActions().size <= 0){
				freeActor();
			}
		}
	}
	private void dropBomb(AirStrikeLocation location){
		projectileFactory.loadRocket().initialize(this, location.getLocation(), ROCKET_SIZE, AIRSTRIKE_RADIUS);
		
		
	}
	public boolean isReadyToBegin(){
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
