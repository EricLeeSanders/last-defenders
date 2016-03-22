package com.eric.mtd.ui.view;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.eric.mtd.helper.CollisionDetection;
import com.eric.mtd.helper.Logger;
import com.eric.mtd.helper.Resources;
import com.eric.mtd.model.Player;
import com.eric.mtd.model.actor.interfaces.IRotatable;
import com.eric.mtd.model.actor.tower.Tower;
import com.eric.mtd.model.actor.tower.TowerTank;
import com.eric.mtd.model.placement.TowerPlacement;
import com.eric.mtd.ui.state.UIStateManager;
import com.eric.mtd.ui.state.IUIStateObserver;
import com.eric.mtd.ui.state.UIStateManager.UIState;
import com.eric.mtd.ui.controller.EnlistController;
import com.eric.mtd.ui.controller.interfaces.IEnlistController;
import com.eric.mtd.ui.view.widget.MTDImage;
import com.eric.mtd.ui.view.widget.MTDImageButton;
import com.eric.mtd.ui.view.widget.MTDTextButton;
import com.eric.mtd.ui.view.widget.enlist.MTDTowerButton;

public class EnlistGroup extends Group implements InputProcessor,IUIStateObserver{
	private MTDImage pnlEnlist;
	private MTDTowerButton btnTank, btnFlameThrower, btnTurret, btnSniper, btnMachine, btnRocketLauncher, btnRifle;
	private MTDImageButton btnCancel, btnPlace, btnRotate;
	private IEnlistController controller;
	private UIStateManager uiStateManager;
	private Group choosingGroup;
	public EnlistGroup(IEnlistController controller, UIStateManager UIStateManager){
		this.controller = controller;
		this.uiStateManager = UIStateManager;
		this.uiStateManager.attach(this);
		choosingGroup = new Group();
		addActor(choosingGroup);
		createControls();
	}
	public void createControls(){
		pnlEnlist = new MTDImage("UI_Enlist", "panel",Resources.ENLIST_ATLAS, "enlist_bg",true, false);
			pnlEnlist.getColor().set(1f,1f,1f,.75f);
			choosingGroup.addActor(pnlEnlist);
			

		btnRifle = new MTDTowerButton("UI_Enlist", "btnRifle",Resources.ENLIST_ATLAS, "rifleEnabled","rifleDisabled","Rifle",true, false);
			setRifleListener();
			choosingGroup.addActor(btnRifle);

		btnTank =new MTDTowerButton("UI_Enlist", "btnTank",Resources.ENLIST_ATLAS, "tankEnabled","tankDisabled","Tank",true, false);
			setTankListener();
			choosingGroup.addActor(btnTank);
			
		btnFlameThrower = new MTDTowerButton("UI_Enlist", "btnFlameThrower",Resources.ENLIST_ATLAS, "flamethrowerEnabled","flamethrowerDisabled",
				"FlameThrower",true, false);
			setFlameThrowerListener();
			choosingGroup.addActor(btnFlameThrower);

			
		btnTurret = new MTDTowerButton("UI_Enlist", "btnTurret",Resources.ENLIST_ATLAS, "turretEnabled","turretDisabled","Turret",true, false);
			setTurretListener();
			choosingGroup.addActor(btnTurret);

			
		btnSniper = new MTDTowerButton("UI_Enlist", "btnSniper",Resources.ENLIST_ATLAS, "sniperEnabled","sniperDisabled","Sniper",true, false);
			setSniperListener();
			choosingGroup.addActor(btnSniper);

			
		btnMachine = new MTDTowerButton("UI_Enlist", "btnMachine",Resources.ENLIST_ATLAS, "machineEnabled","machineDisabled","Machine",true, false);
			setMachineListener();
			choosingGroup.addActor(btnMachine);

			
		btnRocketLauncher = new MTDTowerButton("UI_Enlist", "btnRocketLauncher",Resources.ENLIST_ATLAS, "rocketlauncherEnabled","rocketlauncherDisabled",
				"RocketLauncher",true, false);
			setRocketLauncherListener();
			choosingGroup.addActor(btnRocketLauncher);
		
		btnCancel = new MTDImageButton("UI_Enlist","btnCancel",Resources.ENLIST_ATLAS,"cancel",false, false);
			setCancelListener();
			addActor(btnCancel);
		btnPlace = new MTDImageButton("UI_Enlist","btnPlace",Resources.ENLIST_ATLAS,"place",false, false);
			setPlaceListener();
			addActor(btnPlace);
		btnRotate = new MTDImageButton("UI_Enlist","btnRotate",Resources.ENLIST_ATLAS,"rotate",false, true);
			setRotateListener();
			addActor(btnRotate);
			
	}
	public void towerRotatable(boolean rotatable){
		if(controller.isTowerRotatable()){
			btnRotate.setVisible(true);
		}
		else{
			btnRotate.setVisible(false);
		}
	}
	
	private void updateTowerButtons(){
		for(Actor button : choosingGroup.getChildren()){
			if(button instanceof MTDTowerButton){
				if(controller.canAffordTower(((MTDTowerButton) button).getTowerName())){ //Tower is affordable
					if(Logger.DEBUG)System.out.println("Setting " + ((MTDTowerButton) button).getTowerName() + " to Enabled");
					((MTDTowerButton) button).setDisabled(false);
					button.setTouchable(Touchable.enabled); //TODO: Question: Not sure why I have to do this
				}
				else{
					if(Logger.DEBUG)System.out.println("Setting " + ((MTDTowerButton) button).getTowerName() + " to Disabled");
					((MTDTowerButton) button).setDisabled(true);
					button.setTouchable(Touchable.disabled);
				}
			}
		}
		
	}
	@Override
	public void act (float delta) {
		super.act(delta);
		if(btnRotate.isPressed()){
			controller.rotateTower();
		}                                         
	}
	private void setRotateListener(){
		btnRotate.addListener(new ClickListener() {
	        	@Override
	            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button )
	            {
	        		super.touchDown(event, x, y, pointer, button);
	                if(Logger.DEBUG)System.out.println("Rotate Pressed");
	                return true;
	            }
	        } );
	}
	private void setRifleListener(){
		if(Logger.DEBUG)System.out.println("creating rifle listener");
	    btnRifle.addListener(new ClickListener() {
	    	@Override
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
	        {
	    		super.touchUp( event, x, y, pointer, button );
	            controller.enlistTower("Rifle"); 
	            controller.createTower();
	        }
	    } );
	    
	}
	private void setTankListener(){
		 btnTank.addListener(new ClickListener() {
		    	@Override
		        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
		        {
		    		super.touchUp( event, x, y, pointer, button );
		            controller.enlistTower("Tank"); 
		            controller.createTower();
		        }
		    } );
	}
	private void setFlameThrowerListener(){
		 btnFlameThrower.addListener(new ClickListener() {
		    	@Override
		        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
		        {
		    		super.touchUp( event, x, y, pointer, button );
		            if(Logger.DEBUG)System.out.println("Flame Thrower Button Pressed");
		            controller.enlistTower("FlameThrower"); 
		            controller.createTower();
		        }
		    } );
	}
	private void setSniperListener(){
		 btnSniper.addListener(new ClickListener() {
		    	@Override
		        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
		        {
		    		super.touchUp( event, x, y, pointer, button );
		            if(Logger.DEBUG)System.out.println("Sniper Button Pressed");
		            controller.enlistTower("Sniper"); 
		            controller.createTower();
		        }
		    } );
	}
	private void setMachineListener(){
		 btnMachine.addListener(new ClickListener() {
		    	@Override
		        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
		        {
		    		super.touchUp( event, x, y, pointer, button );
		            if(Logger.DEBUG)System.out.println("Machine Button Pressed");
		            controller.enlistTower("Machine"); 
		            controller.createTower();
		        }
		    } );
	}
	private void setRocketLauncherListener(){
		 btnRocketLauncher.addListener(new ClickListener() {
		    	@Override
		        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
		        {
		    		super.touchUp( event, x, y, pointer, button );
		            if(Logger.DEBUG)System.out.println("RocketLauncher Button Pressed");
		            controller.enlistTower("RocketLauncher"); 
		            controller.createTower();
		        }
		    } );
	}
	private void setTurretListener(){
		 btnTurret.addListener(new ClickListener() {
		    	@Override
		        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
		        {
		    		super.touchUp( event, x, y, pointer, button );
		            if(Logger.DEBUG)System.out.println("Turret Button Pressed");
		            controller.enlistTower("Turret"); 
		            controller.createTower();
		        }
		    } );
	}
	private void setPlaceListener(){
		btnPlace.addListener(new ClickListener() {
        	@Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button )
            {
        		super.touchUp( event, x, y, pointer, button );
                if(Logger.DEBUG)System.out.println("Place Pressed");
                controller.placeTower();
               // towerRotatable(false);
                //btnPlace.setVisible(false);
            }
        } );
	}
	private void setCancelListener(){
		btnCancel.addListener(new ClickListener() {
        	@Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button )
            {
        		super.touchUp( event, x, y, pointer, button );
                if(Logger.DEBUG)System.out.println("Cancel Pressed");
                controller.cancelEnlist();
               // towerRotatable(false);
                //btnPlace.setVisible(false);
            }
        } );
	}
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(uiStateManager.getState().equals(UIState.PLACING_TOWER)){
			Vector2 coords = this.getStage().screenToStageCoordinates(new Vector2((float)screenX,(float)screenY));
			if(Logger.DEBUG)System.out.println("Enlist Touch Down");
			controller.moveTower(coords);
			towerRotatable(controller.isTowerRotatable());
			btnPlace.setVisible(true);
		}
		return false;
	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(uiStateManager.getState().equals(UIState.PLACING_TOWER)){
			Vector2 coords = this.getStage().screenToStageCoordinates(new Vector2((float)screenX,(float)screenY));
			controller.moveTower(coords);
			btnPlace.setVisible(true);
			towerRotatable(controller.isTowerRotatable());
		}
		return false;
		
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public void changeUIState(UIState state) {
		switch(state){
		case STANDBY:
			btnCancel.setVisible(false);
			choosingGroup.setVisible(false);
			btnRotate.setVisible(false);
			btnPlace.setVisible(false);
			btnCancel.setVisible(false);
			//enlistTable.setVisible(false);
			break;
		case ENLIST:
			if(Logger.DEBUG)System.out.println("Enlisting");
			updateTowerButtons();
			choosingGroup.setVisible(true);
			btnCancel.setVisible(true);
			break;
		case PLACING_TOWER:
			btnCancel.setVisible(true);
			choosingGroup.setVisible(false);
			break;
		default:
			break;
		}
		
	}

}
