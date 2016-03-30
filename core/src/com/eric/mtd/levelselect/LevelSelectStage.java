package com.eric.mtd.levelselect;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.eric.mtd.Resources;
import com.eric.mtd.levelselect.ui.ILevelSelectController;
import com.eric.mtd.levelselect.ui.LevelSelectController;
import com.eric.mtd.levelselect.ui.LevelSelectTable;
import com.eric.mtd.menu.ui.IMenuController;
import com.eric.mtd.menu.ui.MenuController;
import com.eric.mtd.menu.ui.MenuGroup;
import com.eric.mtd.screen.state.ScreenStateManager;

public class LevelSelectStage extends Stage{
	private ScreenStateManager screenStatemanager;
	private LevelSelectTable levelSelectTable;
	private ILevelSelectController levelSelectController;
	public LevelSelectStage(ScreenStateManager screenStateManager){
		this.screenStatemanager = screenStateManager;

		levelSelectController = new LevelSelectController(screenStateManager);
		levelSelectTable = new LevelSelectTable(levelSelectController);

		this.addActor(levelSelectTable);
	}

}