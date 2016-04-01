package com.eric.mtd.game.model.placement;

//import java.lang.reflect.Field;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.eric.mtd.MTDGame;
import com.eric.mtd.game.helper.CollisionDetection;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.health.HealthBar;
import com.eric.mtd.game.model.actor.interfaces.IRotatable;
import com.eric.mtd.game.model.actor.perks.Sandbag;
import com.eric.mtd.game.model.actor.tower.Tower;
import com.eric.mtd.game.model.factory.ActorFactory;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.util.Logger;

import sun.util.calendar.CalendarSystem;

public class SandbagPlacement{
	private Vector2 clickCoords;
	private TiledMap tiledMap;
	private Sandbag currentSandbag;
	private Player player;
	private ActorGroups actorGroups;
	public SandbagPlacement(Player player, TiledMap tiledMap, ActorGroups actorGroups){
		this.tiledMap = tiledMap;
		this.player = player;
		this.actorGroups = actorGroups;
	}
	public void createSandbag(){
		currentSandbag = ActorFactory.loadSandbag(new Vector2(0,0));
		//stage.addActor(currentSandbag);
		currentSandbag.setVisible(false);
	}
	public void moveSandbag(Vector2 clickCoords){
		if(currentSandbag!=null){
			if(!currentSandbag.isVisible()){
				currentSandbag.setVisible(true);
			}
			currentSandbag.setPositionCenter(clickCoords); 
			if(Logger.DEBUG)System.out.println(currentSandbag.getPositionCenter());
			if(sandbagCollides()){
				if(Logger.DEBUG)System.out.println("Sandbag collides");
				
			}
		}
	}
	public void rotateSandbag(float rotation){
		if(currentSandbag!=null){
			currentSandbag.setRotation(currentSandbag.getRotation()-rotation);//rotate clockwise
			if(Logger.DEBUG)System.out.println(currentSandbag.getPositionCenter());
			if(sandbagCollides()){
				if(Logger.DEBUG)System.out.println("Sandbag collides");
				
			}
		}
	}
	public boolean placeSandbag(){
		if(currentSandbag != null){
			if(!sandbagCollides()){
	
				currentSandbag.remove();
				//currentSandbag.setActive(true);
				actorGroups.getSandbagGroup().addActor(currentSandbag);

				//player.spendMoney(currentSandbag.getCost());
				currentSandbag = null;
				return true;
			}
		}
		return false;
	}
	public boolean sandbagCollides(){
		SnapshotArray<Actor> towers = actorGroups.getTowerGroup().getChildren();
		SnapshotArray<Actor> sandbags = actorGroups.getSandbagGroup().getChildren();
		SnapshotArray<Actor> actors = new SnapshotArray<Actor>();
		actors.addAll(towers);
		actors.addAll(sandbags);
		/*if(CollisionDetection.CollisionWithPath(tiledMap, currentSandbag)){
			return true;
		}
		else if(CollisionDetection.CollisionWithActors(actors, currentSandbag)){
			return true;
		}*/
		return false;
		
	}
	public void removeCurrentSandbag(){
		if(currentSandbag != null){
			currentSandbag.freeActor();
			currentSandbag.remove();
			currentSandbag = null;
		}
	}
	public boolean isCurrentSandbag(){
		if(currentSandbag == null){
			return false;
		}
		else{
			return true;
		}
	}
	public Sandbag getCurrentSandbag(){
		return currentSandbag;
	}
	
}
