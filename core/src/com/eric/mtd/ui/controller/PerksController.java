package com.eric.mtd.ui.controller;

import com.badlogic.gdx.math.Vector2;
import com.eric.mtd.helper.Logger;
import com.eric.mtd.helper.Resources;
import com.eric.mtd.model.Player;
import com.eric.mtd.model.actor.ActorGroups;
import com.eric.mtd.model.level.state.LevelStateManager;
import com.eric.mtd.model.level.state.LevelStateManager.LevelState;
import com.eric.mtd.model.placement.SandbagPlacement;
import com.eric.mtd.ui.controller.interfaces.IPerksController;

public class PerksController implements IPerksController{
	private LevelStateManager gameStateManager;
	private Player player;
	private SandbagPlacement sandbagPlacement;
	public PerksController(LevelStateManager gameStateManager, Player player, int intLevel, ActorGroups actorGroups){
		this.gameStateManager = gameStateManager;
		this.player = player;
		sandbagPlacement = new SandbagPlacement(player, Resources.getMap(intLevel), actorGroups);;
	}
	@Override
	public void createSandbag() {
		sandbagPlacement.createSandbag();
		//gameStateManager.setState(LevelState.PLACING_SANDBAG);
		
	}
	@Override
	public void placeSandbag() {
		if(sandbagPlacement.placeSandbag()){
			gameStateManager.setState(LevelState.STANDBY);
			sandbagPlacement.removeCurrentSandbag();
		}
		
	}
	@Override
	public void moveSandbag(Vector2 coords) {
		if(sandbagPlacement.isCurrentSandbag()){
			if(Logger.DEBUG)System.out.println("Trying to place sandbag");
			sandbagPlacement.moveSandbag(coords);
		}
		
	}
	@Override
	public void rotateSandbag() {
		sandbagPlacement.rotateSandbag(1);
		
	}
	@Override
	public void cancelPerks() {
		gameStateManager.setState(LevelState.STANDBY);
		sandbagPlacement.removeCurrentSandbag();
	}
}
