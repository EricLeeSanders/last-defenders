package com.eric.mtd.game.ui.controller;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.eric.mtd.Logger;
import com.eric.mtd.Resources;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.interfaces.IRotatable;
import com.eric.mtd.game.model.actor.tower.TowerTank;
import com.eric.mtd.game.model.placement.TowerPlacement;
import com.eric.mtd.game.ui.controller.interfaces.IEnlistController;
import com.eric.mtd.game.ui.state.IGameUIStateObserver;
import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.game.ui.view.EnlistGroup;

public class EnlistController implements IEnlistController {
	private TowerPlacement towerPlacement;
	private GameUIStateManager uiStateManager;
	private String strEnlistTower;
	private Player player;
	public EnlistController(GameUIStateManager uiStateManager, Player player, int intLevel, ActorGroups actorGroups){
		this.uiStateManager = uiStateManager;
		this.player = player;
		towerPlacement = new TowerPlacement(Resources.getMap(intLevel), actorGroups);
	}
	public void enlistTower(String strEnlistTower){
		this.strEnlistTower = strEnlistTower;
	}
	public void createTower(){
		towerPlacement.createTower(strEnlistTower);
        uiStateManager.setState(GameUIState.PLACING_TOWER);
	}
	public void placeTower(){
		int cost = towerPlacement.getCurrentTower().getCost();
        if(towerPlacement.placeTower()){
        	uiStateManager.setState(GameUIState.STANDBY);
        	if(towerPlacement == null){
        		if(Logger.DEBUG)System.out.println("towerPlacement null");
        	}
        	else{
        		if(Logger.DEBUG)System.out.println("towerPlacement not null");
        	}
			player.spendMoney(cost);
        	towerPlacement.removeCurrentTower();
        	
        }
	}
	public void cancelEnlist(){
		uiStateManager.setState(GameUIState.STANDBY);
		towerPlacement.removeCurrentTower();
	}
	public void moveTower(Vector2 coords){
		if(towerPlacement.isCurrentTower()){
			if(Logger.DEBUG)System.out.println("Trying to move tower");
			//btnPlace.setVisible(true); //only show the place button when the tower has been shown on the screen
			towerPlacement.moveTower(coords);
		}
	}
	public void rotateTower(){
		towerPlacement.rotateTower(1);
	}
	public boolean isTowerRotatable(){
		if(towerPlacement.getCurrentTower() instanceof IRotatable){
			return true;
		}
		else{
			return false;
		}
	}
	@Override
	public boolean canAffordTower(String tower) {
		
		try {
			Class<?> myClass = Class.forName("com.eric.mtd.game.model.actor.tower.Tower"+tower);
			Field field=ClassReflection.getDeclaredField(myClass,"COST");
			field.setAccessible(true);
			int cost = (Integer) field.get(null);
			if(cost > player.getMoney()){
				return false;
			}
			else{
				return true;
			}
			    //field.set(object,value);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReflectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}



}
