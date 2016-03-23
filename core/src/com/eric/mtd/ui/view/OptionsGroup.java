package com.eric.mtd.ui.view;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.eric.mtd.helper.Logger;
import com.eric.mtd.helper.Resources;
import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;
import com.eric.mtd.ui.controller.interfaces.IOptionsController;
import com.eric.mtd.ui.view.widget.MTDImage;
import com.eric.mtd.ui.view.widget.MTDTextButton;

public class OptionsGroup extends Group{
	private IOptionsController controller;
	private MTDTextButton btnResume;
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
}
