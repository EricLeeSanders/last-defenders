package com.eric.mtd.game.stage;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.eric.mtd.MTDGame;
import com.eric.mtd.game.helper.CollisionDetection;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.enemy.Enemy;
import com.eric.mtd.game.model.actor.enemy.EnemyRifle;
import com.eric.mtd.game.model.actor.projectile.Flame;
import com.eric.mtd.game.model.actor.tower.Tower;
import com.eric.mtd.game.model.actor.tower.TowerTank;
import com.eric.mtd.game.model.ai.TowerAI;
import com.eric.mtd.game.model.factory.ActorFactory;
import com.eric.mtd.game.model.level.Level;
import com.eric.mtd.game.model.level.state.ILevelStateObserver;
import com.eric.mtd.game.model.level.state.LevelStateManager;
import com.eric.mtd.game.model.level.state.LevelStateManager.LevelState;
import com.eric.mtd.game.model.placement.TowerPlacement;
import com.eric.mtd.game.ui.MTDTiledMapRenderer;
import com.eric.mtd.game.ui.controller.EnlistController;
import com.eric.mtd.game.ui.controller.HUDController;
import com.eric.mtd.game.ui.controller.InspectController;
import com.eric.mtd.game.ui.controller.OptionsController;
import com.eric.mtd.game.ui.controller.PerksController;
import com.eric.mtd.game.ui.controller.interfaces.IEnlistController;
import com.eric.mtd.game.ui.controller.interfaces.IHUDController;
import com.eric.mtd.game.ui.controller.interfaces.IInspectController;
import com.eric.mtd.game.ui.controller.interfaces.IOptionsController;
import com.eric.mtd.game.ui.controller.interfaces.IPerksController;
import com.eric.mtd.game.ui.state.IGameUIStateObserver;
import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.game.ui.view.EnlistGroup;
import com.eric.mtd.game.ui.view.HUDGroup;
import com.eric.mtd.game.ui.view.InspectGroup;
import com.eric.mtd.game.ui.view.OptionsGroup;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

public class GameStage extends Stage implements IGameUIStateObserver, ILevelStateObserver{
	// I create the groups here because I found it to be the best place.
	// If I put them in the current level and tower placement classes, then referencing them elsewhere would be difficult.
	// For this reason, I create them here and in the current level and tower placement classes, I call the getTower method from this stage class.
	
	//Question:Make attributes static? That way I don't have to pass a reference of this object to the classes.
	private LevelStateManager levelStateManager;
	private GameUIStateManager uiStateManager;
	private Level level;
	private Player player;
	private int intLevel;
	private ActorGroups actorGroups;
	private MTDTiledMapRenderer mapRenderer;
	private static final float TIME_STEP = 1/30f;
	private float accumulator;
	public static float totalLevelDelta = 0;
	public GameStage( int intLevel, Player player, ActorGroups actorGroups, LevelStateManager levelStateManager, GameUIStateManager uiStateManager) {
    	//if(Logger.DEBUG)System.out.println("game stage created");
    	this.player = player;
    	this.actorGroups = actorGroups;
    	this.levelStateManager = levelStateManager;
    	levelStateManager.attach(this);
    	this.uiStateManager = uiStateManager;
    	uiStateManager.attach(this);
    	this.intLevel = intLevel;
		createGroups();
    	level = new Level(intLevel, levelStateManager, getActorGroups());
    	level.loadNextWave();
    	mapRenderer = new MTDTiledMapRenderer(intLevel, this);
    	player.setLives(level.getLives());
    	player.setMoney(level.getMoney());


 
		
    }
	public void createGroups(){
		this.addActor(getActorGroups().getTowerGroup());
		this.addActor(getActorGroups().getEnemyGroup());
		this.addActor(getActorGroups().getHealthBarGroup());
		this.addActor(getActorGroups().getExplosionGroup());
		this.addActor(getActorGroups().getFlameGroup());
		this.addActor(getActorGroups().getBulletGroup());
		this.addActor(getActorGroups().getSandbagGroup());
	}

    @Override
    public void act(float delta) {
        super.act(delta);
       /* accumulator += delta;

        while (accumulator >= delta) {
            level.update(delta);
            accumulator -= TIME_STEP;
        }*/
        level.update(delta);
        /*if((levelStateManager.getState().equals(LevelState.WAVE_IN_PROGRESS))||((levelStateManager.getState().equals(LevelState.WAVE_IN_PROGRESS)))){
        	totalLevelDelta += delta;
        }*/
        if(levelStateManager.getState().equals(LevelState.WAVE_IN_PROGRESS)){
        	isWaveOver();
        }
    }

    @Override
    public void draw() {
        mapRenderer.update();
    	super.draw();
    }
    
    @Override
    public void dispose() {
    	if(Logger.DEBUG)System.out.println("Game Stage Dispose");
    	mapRenderer.dispose();
        super.dispose();
    }
 
	public void isWaveOver(){
		if(getActorGroups().getEnemyGroup().getChildren().size<=0){
			if(!(levelStateManager.getState().equals(LevelState.LEVEL_OVER))){
				player.giveMoney((int)( 100 * (float)level.getCurrentWave()));
				levelStateManager.setState(LevelState.STANDBY);
				player.setWaveCount(player.getWaveCount() + 1);
				//if(Logger.DEBUG)System.out.println("Flame Damage " + Flame.totalDamage);
				//if(Logger.DEBUG)System.out.println("Total Spawning Delta " + level.totalSpawningDelta);
				//if(Logger.DEBUG)System.out.println("Total Level Delta " + this.totalLevelDelta);
				//if(Logger.DEBUG)System.out.println("Total Difference " + Enemy.totalDifference);
				Flame.totalDamage = 0;
				Level.totalSpawningDelta = 0;
				this.totalLevelDelta = 0;
				Enemy.totalDifference = 0;
				level.loadNextWave(); //Load the next wave, always have a wave loaded for optimization
			}
		}
		
	}
	public int getIntLevel(){
		return intLevel;
	}
	public void enemyReachedEnd(){
		if(player.getLives()>0){ //Only subtract if we have lives.
			player.setLives(player.getLives()-1);
		}
		if(player.getLives()<=0){ //end game
			levelStateManager.setState(LevelState.LEVEL_OVER);
			uiStateManager.setState(GameUIState.LEVEL_OVER);
		}
	}
	public void showTowerRanges(boolean bool){
		for(Actor tower : getActorGroups().getTowerGroup().getChildren()){
				if(tower instanceof Tower){
					((GameActor)tower).setShowRange(bool);
				}
		}
	}


	
	public ActorGroups getActorGroups() {
		return actorGroups;
	}
	public void setActorGroups(ActorGroups actorGroups) {
		this.actorGroups = actorGroups;
	}
	@Override
	public void changeLevelState(LevelState state) {
		switch(state){
		case SPAWNING_ENEMIES:
			//startWave();
			showTowerRanges(true);
			break;
		default:
			break;
		
		}
		
	}
	@Override
	public void changeUIState(GameUIState state) {
		switch(state){
		case PLACING_TOWER:
			showTowerRanges(true);
			break;
		default:
			break;
		
		}
		
	}

}

