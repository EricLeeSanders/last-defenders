package com.eric.mtd.game.ui.view;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.eric.mtd.game.ui.controller.interfaces.IOptionsController;
import com.eric.mtd.game.ui.view.widget.MTDImage;
import com.eric.mtd.game.ui.view.widget.MTDTextButton;
import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

public class OptionsGroup extends Group{
	private IOptionsController controller;
	private MTDTextButton btnResume, btnNewGame, btnMainMenu;
	private MTDImage panel;
	public OptionsGroup(IOptionsController controller){
		this.controller = controller;
		createControls();
	}
	public void createControls(){
		panel = new MTDImage("UI_Options", "panel",Resources.OPTIONS_ATLAS, "panel",true, false);
			panel.getColor().set(1f,1f,1f,.75f);
		addActor(panel);
	
		btnResume =new MTDTextButton("UI_Options", "btnResume","Resume", true);
		setBtnResumeListener();
		addActor(btnResume);
		
		btnNewGame =new MTDTextButton("UI_Options", "btnNewGame","New Game", true);
		setBtnNewGameListener();
		addActor(btnNewGame);
			
		btnMainMenu =new MTDTextButton("UI_Options", "btnMainMenu","Main Menu", true);
		setBtnMainMenuListener();
		addActor(btnMainMenu);
	}
	private void setBtnResumeListener(){
	    btnResume.addListener(new ClickListener() {
	    	@Override
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
	        {
	    		super.touchUp( event, x, y, pointer, button );
	    		if(Logger.DEBUG)System.out.println("Resume Button Pressed");
	    		controller.resumeGame();
	        }
	    } );
	    
	}
	private void setBtnNewGameListener(){
	    btnNewGame.addListener(new ClickListener() {
	    	@Override
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
	        {
	    		super.touchUp( event, x, y, pointer, button );
	    		if(Logger.DEBUG)System.out.println("New game Pressed");
	    		controller.resumeGame();
	        }
	    } );
	    
	}
	private void setBtnMainMenuListener(){
	    btnMainMenu.addListener(new ClickListener() {
	    	@Override
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
	        {
	    		super.touchUp( event, x, y, pointer, button );
	    		if(Logger.DEBUG)System.out.println("Main Menu Button Pressed");
	    		controller.resumeGame();
	        }
	    } );
	    
	}
}
