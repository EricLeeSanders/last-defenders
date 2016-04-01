package com.eric.mtd.menu;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.eric.mtd.levelselect.ui.ILevelSelectController;
import com.eric.mtd.levelselect.ui.LevelSelectController;
import com.eric.mtd.levelselect.ui.LevelSelectTable;
import com.eric.mtd.menu.ui.IMenuController;
import com.eric.mtd.menu.ui.MenuController;
import com.eric.mtd.menu.ui.MenuGroup;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.util.Resources;

public class MenuStage extends Stage{
	private ScreenStateManager screenStatemanager;
	private IMenuController menuController;
	private MenuGroup menuGroup;
	public MenuStage(ScreenStateManager screenStateManager){
		this.screenStatemanager = screenStateManager;

		createBackground();
		menuController = new MenuController(screenStateManager);
		menuGroup = new MenuGroup(menuController);
		this.addActor(menuGroup);
	}
	public void createBackground(){
	    TextureAtlas menuAtlas = Resources.getAtlas(Resources.MENU_ATLAS);
	    
	    Image background = new Image(menuAtlas.findRegion("background"));
    	background.setFillParent(true);
    	this.addActor(background);
	}

}
