package com.eric.mtd.game.model.placement;

//import java.lang.reflect.Field;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.eric.mtd.MTDGame;
import com.eric.mtd.game.GameStage;
import com.eric.mtd.game.helper.CollisionDetection;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.health.HealthBar;
import com.eric.mtd.game.model.actor.interfaces.IRotatable;
import com.eric.mtd.game.model.actor.tower.Tower;
import com.eric.mtd.game.model.factory.ActorFactory;

import sun.util.calendar.CalendarSystem;

public class TowerPlacement{
	private Vector2 clickCoords;
	//private String towerType;
	private TiledMap tiledMap;
	private Tower currentTower;
	private ActorGroups actorGroups;
	private Array<RectangleMapObject> pathBoundary = new Array<RectangleMapObject>();
	public TowerPlacement(TiledMap tiledMap, ActorGroups actorGroups){
		this.tiledMap = tiledMap;
		this.actorGroups = actorGroups;
		MapObjects boundaries = tiledMap.getLayers().get("PathBoundary").getObjects();
		for(MapObject boundry : boundaries){
			if(boundry.getClass().equals(com.badlogic.gdx.maps.objects.RectangleMapObject.class)){
				pathBoundary.add((RectangleMapObject)boundry);
			}
		}
	}
/*	public void setTower(Tower tower){
		currentTower = tower;
		if(currentTower!=null){
			currentTowerBody = currentTower.getBody();
			currentTower.remove();
			//stage.addActor(currentTower);
			currentTower.setActive(false);
		}
	}*/
	public boolean isTowerRotatable(){
		if(getCurrentTower() instanceof IRotatable){
			return true;
		}
		else{
			return false;
		
		}
	}
	public void createTower(String type){
		currentTower = ActorFactory.loadTower(new Vector2(0,0),type);
		actorGroups.getTowerGroup().addActor(currentTower);
		currentTower.setVisible(false);
	}
	public void moveTower(Vector2 clickCoords){
		if(currentTower!=null){
			if(!currentTower.isVisible()){
				currentTower.setVisible(true);
			}
			currentTower.setPositionCenter(clickCoords); 
			//if(Logger.DEBUG)System.out.println(currentTower.getPositionCenter());
			if(towerCollides()){
				currentTower.setShowRange(true);
				currentTower.setRangeColor(1f,0f,0f,0.5f);//Red
				
			}
			else{
				currentTower.setShowRange(true);
				currentTower.setRangeColor(1f,1f,1f,.5f);
			}
		}
	}
	public void rotateTower(float rotation){
		if(currentTower!=null){
			currentTower.setRotation(currentTower.getRotation()-rotation);//rotate clockwise
			//if(Logger.DEBUG)System.out.println(currentTower.getPositionCenter());
			if(towerCollides()){
				currentTower.setShowRange(true);
				currentTower.setRangeColor(1f,0f,0f,0.5f);//Red
				
			}
			else{
				currentTower.setShowRange(true);
				currentTower.setRangeColor(1f,1f,1f,.5f);
			}
		}
	}
	public boolean placeTower(){
		if(currentTower != null){
			if(!towerCollides()){
	
				currentTower.setShowRange(false);
				currentTower.remove();
				currentTower.setActive(true);
				actorGroups.getTowerGroup().addActor(currentTower);
				HealthBar healthBar = ActorFactory.loadHealthBar();
				healthBar.setActor(currentTower, actorGroups);
				currentTower = null;
				return true;
			}
		}
		return false;
	}
	public boolean towerCollides(){
		SnapshotArray<Actor> towers = actorGroups.getTowerGroup().getChildren();
		/*SnapshotArray<Actor> sandbags = actorGroups.getSandbagGroup().getChildren();
		SnapshotArray<Actor> actors = new SnapshotArray<Actor>();
		actors.addAll(towers);
		actors.addAll(sandbags);*/

		if(CollisionDetection.CollisionWithPath(pathBoundary, currentTower)){
			return true;
		}
		else if(CollisionDetection.CollisionWithActors(towers, currentTower)){
			return true;
		}
		return false;
		
	}
	public void removeCurrentTower(){
		if(currentTower != null){
			currentTower.freeActor();
			currentTower.remove();
			currentTower = null;
		}
	}
	public boolean isCurrentTower(){
		if(currentTower == null){
			return false;
		}
		else{
			return true;
		}
	}
	public Tower getCurrentTower(){
		return currentTower;
	}
	
}
