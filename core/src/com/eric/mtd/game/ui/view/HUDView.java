package com.eric.mtd.game.ui.view;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.level.state.ILevelStateObserver;
import com.eric.mtd.game.model.level.state.LevelStateManager;
import com.eric.mtd.game.model.level.state.LevelStateManager.LevelState;
import com.eric.mtd.game.ui.state.IGameUIStateObserver;
import com.eric.mtd.game.ui.presenter.HUDPresenter;
import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.game.ui.view.interfaces.IHUDView;
import com.eric.mtd.game.ui.view.widget.*;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;
public class HUDView extends Group implements IHUDView{
	private MTDImage pnlTitle, imgMoney, imgLife;
	private MTDImageButton btnWave, btnEnlist, btnQuit, btnOptions, btnNormalSpeed, btnDoubleSpeed;
	private MTDLabel lblMoney, lblLives, lblWaveCount;
	private HUDPresenter presenter;
	private Group btnSpeedGroup = new Group();
	public HUDView(HUDPresenter presenter){
		this.presenter = presenter;
		createControls();
	}
	public void createControls(){
		//pnlTitle = new MTDImage("UI_HUD", "panel", Resources.HUD_ATLAS, "Title_bg",true, false);
		//addActor(pnlTitle);
		btnWave = new MTDImageButton("UI_HUD","btnWave",Resources.HUD_ATLAS,"wave",true,false);
			setBtnWaveListener();
		addActor(btnWave);

		btnDoubleSpeed = new MTDImageButton("UI_HUD","btnSpeed",Resources.HUD_ATLAS,"doubleSpeed",true,false);
			setBtnDoubleSpeedListener();
		btnSpeedGroup.addActor(btnDoubleSpeed);
		
		btnNormalSpeed = new MTDImageButton("UI_HUD","btnSpeed",Resources.HUD_ATLAS,"normalSpeed",false,false);
			setBtnNormalSpeedListener();
		btnSpeedGroup.addActor(btnNormalSpeed);
		btnSpeedGroup.setVisible(true);
		addActor(btnSpeedGroup);
		
		btnEnlist = new MTDImageButton("UI_HUD","btnEnlist",Resources.HUD_ATLAS,"enlist",true,true);
			setBtnEnlistListener();
		addActor(btnEnlist);
		/*btnQuit = new MTDImageButton("UI_HUD","btnQuit",Resources.HUD_ATLAS,"quit",true, false);
			setBtnQuitListener();
		addActor(btnQuit);*/
		btnOptions = new MTDImageButton("UI_HUD","btnOptions",Resources.HUD_ATLAS,"options",true,false);
			setBtnOptionsListener();
		addActor(btnOptions);
		imgMoney = new MTDImage("UI_HUD","imgMoney",Resources.HUD_ATLAS,"dollarsign",true, false);
		addActor(imgMoney);
		lblMoney = new MTDLabel("UI_HUD", "lblMoney","0",true, Color.WHITE, Align.left, 0.5f);
		addActor(lblMoney);
		imgLife = new MTDImage("UI_HUD","imgLife",Resources.HUD_ATLAS,"lives",true, false);
		addActor(imgLife);
		lblLives = new MTDLabel("UI_HUD", "lblLife","0",true, Color.WHITE, Align.left, 0.5f);
		addActor(lblLives);
		lblWaveCount = new MTDLabel("UI_HUD", "lblWaveCount","0",true, Color.WHITE, Align.left, 0.5f);
		addActor(lblWaveCount);
	}
	private void setBtnNormalSpeedListener(){
		btnNormalSpeed.addListener(new ClickListener() {
	    	@Override
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
	        {
	    		super.touchUp( event, x, y, pointer, button );
	            if(Logger.DEBUG)System.out.println("Normal Speed");
	            presenter.changeGameSpeed();
	        }
		} );
	}
	private void setBtnDoubleSpeedListener(){
		btnDoubleSpeed.addListener(new ClickListener() {
	    	@Override
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
	        {
	    		super.touchUp( event, x, y, pointer, button );
	            if(Logger.DEBUG)System.out.println("Double Speed");
	            presenter.changeGameSpeed();
	        }
		} );
	}
	private void setBtnOptionsListener(){
		btnOptions.addListener(new ClickListener() {
	    	@Override
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
	        {
	    		super.touchUp( event, x, y, pointer, button );
	            if(Logger.DEBUG)System.out.println("Button Options Pressed");
	            presenter.options();
	        }
		} );
	}
	private void setBtnQuitListener(){
		btnQuit.addListener(new ClickListener() {
	    	@Override
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
	        {
	    		super.touchUp( event, x, y, pointer, button );
	            if(Logger.DEBUG)System.out.println("Button Quit Pressed");
	            presenter.quit();
	        }
		} );
	}
	private void setBtnWaveListener(){
		 btnWave.addListener(new ClickListener() {
	        	@Override
	            public void touchUp(InputEvent event, float x, float y, int pointer, int button )
	            {
	        		super.touchUp( event, x, y, pointer, button );
	                if(Logger.DEBUG)System.out.println("Button Wave Pressed");
	                presenter.startWave();
	            }
	        } );
	}
	private void setBtnEnlistListener(){
		 btnEnlist.addListener(new ClickListener() {
	        	@Override
	            public void touchUp(InputEvent event, float x, float y, int pointer, int button )
	            {
	        		super.touchUp( event, x, y, pointer, button );
	                if(Logger.DEBUG)System.out.println("Button Enlist Pressed");
	                presenter.enlist();
	                
	            }
	        } );
	}
	public void setMoney(String money){
		lblMoney.setText(money);
	}
	public void setLives(String lives){
		lblLives.setText(lives);
	}
	public void setWaveCount(String waveCount){
		lblWaveCount.setText("Wave: " + waveCount);
	}
	public void changeSpeed(boolean isNormalSpeed){
		if(isNormalSpeed){
            btnNormalSpeed.setVisible(false);
            btnDoubleSpeed.setVisible(true);
		}
		else{
            btnNormalSpeed.setVisible(true);
            btnDoubleSpeed.setVisible(false);
		}
	}
	public void standByState(){
		btnEnlist.setTouchable(Touchable.enabled);
		btnWave.setTouchable(Touchable.enabled);
		btnSpeedGroup.setTouchable(Touchable.enabled);
		btnOptions.setTouchable(Touchable.enabled);
		btnWave.setVisible(true);
		btnEnlist.setVisible(true);
		btnOptions.setVisible(true);
		btnSpeedGroup.setVisible(true);
	}
	public void enlistingState(){
		btnEnlist.setVisible(false);
		btnWave.setVisible(false);
		btnOptions.setVisible(false);
		btnSpeedGroup.setVisible(false);
	}
	public void optionsState(){
		btnEnlist.setTouchable(Touchable.disabled);
		btnWave.setTouchable(Touchable.disabled);
		btnSpeedGroup.setTouchable(Touchable.disabled);
		btnOptions.setTouchable(Touchable.disabled);
	}
	public void gameOverState(){
		optionsState();
	}
	public void waveInProgress(){
		btnWave.setVisible(false);
	}
	/*@Override
	public void changeLevelState(LevelState state) {
		switch(state){
		case SPAWNING_ENEMIES:
		case WAVE_IN_PROGRESS:
			btnWave.setVisible(false);
			btnSpeedGroup.setVisible(true);
			break;
		case STANDBY:
			btnWave.setVisible(true);
			btnSpeedGroup.setVisible(true);
			break;
		default:
			break;
		}
		
	}*/
/*	@Override
	public void changeUIState(GameUIState state) {
		switch(state){
		case GAME_OVER:
		case OPTIONS:
			btnEnlist.setTouchable(Touchable.disabled);
			btnWave.setTouchable(Touchable.disabled);
			btnSpeedGroup.setTouchable(Touchable.disabled);
			btnOptions.setTouchable(Touchable.disabled);
			break;
		case ENLISTING:
			btnEnlist.setVisible(false);
			btnWave.setVisible(false);
			btnOptions.setVisible(false);
			btnSpeedGroup.setVisible(false);
			break;
		case STANDBY:
			btnEnlist.setTouchable(Touchable.enabled);
			btnWave.setTouchable(Touchable.enabled);
			btnSpeedGroup.setTouchable(Touchable.enabled);
			btnOptions.setTouchable(Touchable.enabled);
			btnWave.setVisible(true);
			btnEnlist.setVisible(true);
			btnOptions.setVisible(true);
			btnSpeedGroup.setVisible(true);
			if(Logger.DEBUG)System.out.println("Syncing with level state manager");
			changeLevelState(levelStateManager.getState()); //Sync with the level
			if(Logger.DEBUG)System.out.println("Synced with level state manager");
			break;
		default:
			break;
		
		}
		
	}*/
}
