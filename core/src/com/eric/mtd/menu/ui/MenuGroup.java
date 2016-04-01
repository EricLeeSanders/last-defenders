package com.eric.mtd.menu.ui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

public class MenuGroup extends Group{
	private static final float PLAY_MOVE_DURATION = 0.5f;
	private IMenuController controller;
	private TextButton btnPlay;
	public MenuGroup(IMenuController controller){
		this.controller = controller;
		createControls();
	}

	public void createControls(){
        
        btnPlay = new TextButton("Play", Resources.getSkin(Resources.SKIN_JSON));
        	btnPlay.setSize(200, 75);
        	btnPlay.setPosition(0-btnPlay.getWidth(), 100);
        	btnPlay.addAction(Actions.moveTo(225, 100, PLAY_MOVE_DURATION));
        	this.addActor(btnPlay);
        	setBtnPlayListener();
	}
	
	private void setBtnPlayListener(){
		btnPlay.addListener(new ClickListener() {
		 	@Override
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
	        {
	    		super.touchUp( event, x, y, pointer, button );
	    		controller.playGame();
	            if(Logger.DEBUG)System.out.println("Play Pressed");
	        }
	    } );
	    
	}
}
