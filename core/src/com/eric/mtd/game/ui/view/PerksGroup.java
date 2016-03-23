package com.eric.mtd.game.ui.view;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.eric.mtd.game.helper.Logger;
import com.eric.mtd.game.helper.Resources;
import com.eric.mtd.game.ui.controller.interfaces.IPerksController;
import com.eric.mtd.game.ui.state.IUIStateObserver;
import com.eric.mtd.game.ui.state.UIStateManager;
import com.eric.mtd.game.ui.state.UIStateManager.UIState;
import com.eric.mtd.game.ui.view.widget.MTDImage;
import com.eric.mtd.game.ui.view.widget.MTDTextButton;

public class PerksGroup extends Group implements InputProcessor, IUIStateObserver{
	private MTDImage pnlPerks;
	private MTDTextButton btnSandbags, btnLandmines, btnCarpetBomb, btnApache, btnArtilleryStrike;
	private UIStateManager uiStateManager;
	private IPerksController controller;
	public PerksGroup(UIStateManager uiStateManager, IPerksController controller){
		this.uiStateManager = uiStateManager;
		this.controller = controller;
		uiStateManager.attach(this);
		//createControls();
	}
	private void createControls(){
		if(Logger.DEBUG)System.out.println("creating perks controls");
		pnlPerks = new MTDImage("UI_Perks", "Panel",Resources.PERKS_ATLAS, "perks_bg",true, false); 
		pnlPerks.getColor().set(1f,1f,1f,.75f);
		this.addActor(pnlPerks);
		
		btnSandbags = new MTDTextButton("UI_Perks","btnSandbags","Sandbags",true);	
			setSandbagsListener();
			addActor(btnSandbags);
			
		btnLandmines = new MTDTextButton("UI_Perks","btnLandMines","Land mines",true);
			setLandminesListener();
			addActor(btnLandmines);
			
		btnCarpetBomb = new MTDTextButton("UI_Perks","btnCarpetBomb","Carpet \nBomb",true);
			setCarpetBombListener();
			addActor(btnCarpetBomb);
			
		btnApache = new MTDTextButton("UI_Perks","btnApache","Apache",true);
			setApacheListener();
			addActor(btnApache);
			
		btnArtilleryStrike = new MTDTextButton("UI_Perks","btnArtilleryStrike","Artillery \nStrike",true);
			setArtilleryStrikeListener();
			addActor(btnArtilleryStrike);
	}
	private void setSandbagsListener(){
		btnSandbags.addListener(new ClickListener() {
	        	@Override
	            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button )
	            {
	        		super.touchDown(event, x, y, pointer, button);
	                if(Logger.DEBUG)System.out.println("Sandbags Pressed");
	                controller.createSandbag();
	                return true;
	            }
	        } );
	}
	private void setLandminesListener(){
		btnLandmines.addListener(new ClickListener() {
	        	@Override
	            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button )
	            {
	        		super.touchDown(event, x, y, pointer, button);
	                if(Logger.DEBUG)System.out.println("Landmines Pressed");
	                return true;
	            }
	        } );
	}
	private void setCarpetBombListener(){
		btnCarpetBomb.addListener(new ClickListener() {
	        	@Override
	            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button )
	            {
	        		super.touchDown(event, x, y, pointer, button);
	                if(Logger.DEBUG)System.out.println("Carpet Bomb Pressed");
	                return true;
	            }
	        } );
	}
	private void setApacheListener(){
		btnApache.addListener(new ClickListener() {
	        	@Override
	            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button )
	            {
	        		super.touchDown(event, x, y, pointer, button);
	                if(Logger.DEBUG)System.out.println("Apache Pressed");
	                return true;
	            }
	        } );
	}
	private void setArtilleryStrikeListener(){
		btnArtilleryStrike.addListener(new ClickListener() {
	        	@Override
	            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button )
	            {
	        		super.touchDown(event, x, y, pointer, button);
	                if(Logger.DEBUG)System.out.println("Artillery Strike Pressed");
	                return true;
	            }
	        } );
	}
	public void showPerks(boolean bool){
		for(Actor a : this.getChildren()){
			a.setVisible(bool);
		}
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
/*		if(uiStateManager.getState().equals(UIState.PLACING_SANDBAG)){
			Vector2 coords = this.getStage().stageToScreenCoordinates(new Vector2((float)screenX,(float)screenY));
			if(Logger.DEBUG)System.out.println("Enlist Touch Down");
			controller.moveSandbag(coords);
			//btnPlace.setVisible(true);
		}*/
		return false;
	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
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
		/*case PLACING_SANDBAG:
			showPerks(false);
			break;*/
		default:
			break;
		
		}
		
	}
}

