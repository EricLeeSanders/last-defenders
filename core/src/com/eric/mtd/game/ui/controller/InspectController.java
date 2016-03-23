package com.eric.mtd.game.ui.controller;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.eric.mtd.game.GameStage;
import com.eric.mtd.game.helper.CollisionDetection;
import com.eric.mtd.game.helper.Logger;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.tower.Tower;
import com.eric.mtd.game.model.ai.TowerTargetPriority;
import com.eric.mtd.game.ui.controller.interfaces.IInspectController;
import com.eric.mtd.game.ui.state.IUIStateObserver;
import com.eric.mtd.game.ui.state.UIStateManager;
import com.eric.mtd.game.ui.state.UIStateManager.UIState;

public class InspectController implements IInspectController, IUIStateObserver {
	private UIStateManager uiStateManager;
	private Tower selectedTower;
	private Player player;
	private ActorGroups actorGroups;
	public InspectController(UIStateManager uiStateManager, Player player, ActorGroups actorGroups){
		this.uiStateManager = uiStateManager;
		this.player = player;
		this.actorGroups = actorGroups;
	}
	@Override
	public void closeInspect() {
		uiStateManager.setState(UIState.STANDBY);
		
	}

	@Override
	public void changeTargetPriority() {
		if(selectedTower != null){
			TowerTargetPriority p = TowerTargetPriority.valueOf(selectedTower.getTargetPriority());
			String s = TowerTargetPriority.values()[(p.getPosition()+1)%4].name();
			selectedTower.setTargetPriority(s);
		}
		
	}

	@Override
	public void increaseAttack() {
		if(selectedTower != null){
			if(selectedTower.getAttackLevel() < Tower.TOWER_ATTACK_LEVEL_MAX){
				player.spendMoney(selectedTower.getAttackIncreaseCost());
				selectedTower.increaseAttack();
			}
		}
		
	}

	@Override
	public void giveArmor() {
		if(selectedTower != null){
			if(!(selectedTower.hasArmor())){
				player.spendMoney(selectedTower.getArmorCost());
				selectedTower.setHasArmor(true);
			}
		}
	}

	@Override
	public void increaseRange() {
		if(selectedTower != null){
			if(selectedTower.getRangeLevel() < Tower.TOWER_RANGE_LEVEL_MAX){
				player.spendMoney(selectedTower.getRangeIncreaseCost());
				selectedTower.increaseRange();
			}
		}
	}

	@Override
	public void increaseSpeed() {
		if(selectedTower != null){
			if(selectedTower.getSpeedLevel() < Tower.TOWER_ATTACK_SPEED_LEVEL_MAX){
				player.spendMoney(selectedTower.getSpeedIncreaseCost());
				selectedTower.increaseSpeed();
			}
		}
	}

	@Override
	public void dishcharge() {
		if(selectedTower != null){
			player.giveMoney(selectedTower.getSellCost());
			selectedTower.sellTower();
			uiStateManager.setState(UIState.STANDBY);
		}
	}
	
	@Override
	public void setSelectedTower(Tower selectedTower) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getTowerTargetPriority() {
		if(selectedTower != null){
			return selectedTower.getTargetPriority();
		}
		return null;
	}
	public void inspectTower(Vector2 coords){
		if(uiStateManager.getState().equals(UIState.STANDBY)){
			Actor hitActor = CollisionDetection.towerHit(actorGroups.getTowerGroup().getChildren(), coords);
		    if(Logger.DEBUG)System.out.println("Screen X:" + coords.x + " Screen Y:" + coords.y);
		    if(hitActor != null){
				if(hitActor instanceof Tower){
					selectedTower = (Tower) hitActor;
					selectedTower.setShowRange(true);
					uiStateManager.setState(UIState.INSPECTING);
				}
		    }
		}
	}
	@Override
	public int getArmorCost() {
		if(selectedTower != null){
			return selectedTower.getArmorCost();
		}
		return 0;
	}
	@Override
	public int getAttackCost() {
		if(selectedTower != null){
			return selectedTower.getAttackIncreaseCost();
		}
		return 0;
	}
	@Override
	public int getRangeCost() {
		if(selectedTower != null){
			return selectedTower.getRangeIncreaseCost();
		}
		return 0;
	}
	@Override
	public int getSpeedCost() {
		if(selectedTower != null){
			return selectedTower.getSpeedIncreaseCost();
		}
		return 0;
	}
	@Override
	public int getSellPrice() {
		if(selectedTower != null){
			return selectedTower.getSellCost();
		}
		return 0;
	}
	@Override
	public int getKills() {
		if(selectedTower != null){
			return selectedTower.getNumOfKills();
		}
		return 0;
	}
	@Override
	public boolean hasArmor() {
		if(selectedTower != null){
			return selectedTower.hasArmor();
		}
		return false;
	}
	@Override
	public int getAttackLevel() {
		if(selectedTower != null){
			return selectedTower.getAttackLevel();
		}
		return 0;
	}
	@Override
	public int getRangeLevel() {
		if(selectedTower != null){
			return selectedTower.getRangeLevel();
		}
		return 0;
	}
	@Override
	public int getSpeedLevel() {
		if(selectedTower != null){
			return selectedTower.getSpeedLevel();
		}
		return 0;
	}
	@Override
	public void changeUIState(UIState state) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean canAffordUpgrade(int upgradeCost) {
		if(upgradeCost <= player.getMoney()){
			return true;
		}
		return false;
	}

	/*
	 * 	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector2 coords = stage.screenToStageCoordinates(new Vector2((float)screenX,(float)screenY));
		//if there is a current tower, then move it to wherever is clicked
		if(towerPlacement.isCurrentTower()){
			if(Logger.DEBUG)System.out.println("Trying to place tower");
			btnPlace.setVisible(true); //only show the place button when the tower has been shown on the screen
			towerPlacement.moveTower(coords);
			if(towerPlacement.getCurrentTower() instanceof TowerTank){
				btnRotate.setVisible(true);
	            
			}
		}
		else{
	      Actor hitActor = CollisionDetection.towerHit(GameStage.towerGroup.getChildren(), coords);
	      if(Logger.DEBUG)System.out.println("Screen X:" + coords.x + " Screen Y:" + coords.y);

	      if(hitActor != null){
	    	  if(hitActor instanceof Tower){
	    		  selectedTower = (Tower) hitActor;
	    		  showTowerRanges(false);
	    		  selectedTower.setShowRange(true);
	    		  if(!stage.isWaveActive()){
	    			  if(!pnlEnlist.isVisible()){ 
		    			  showUpgrade(true);
		      			  updateUpgrade();
	    			  }
	    		  }
	    	  }
	      }
		}
		return true;
	}*/

}
