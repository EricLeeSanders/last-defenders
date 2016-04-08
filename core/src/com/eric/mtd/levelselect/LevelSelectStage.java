package com.eric.mtd.levelselect;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.eric.mtd.levelselect.ui.LevelSelectPresenter;
import com.eric.mtd.levelselect.ui.LevelSelectView;
import com.eric.mtd.menu.ui.MenuPresenter;
import com.eric.mtd.menu.ui.MenuView;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.util.Resources;

public class LevelSelectStage extends Stage{
	private ScreenStateManager screenStatemanager;
	private LevelSelectView levelSelectView;
	private LevelSelectPresenter presenter;
	public LevelSelectStage(ScreenStateManager screenStateManager){
		this.screenStatemanager = screenStateManager;

		presenter = new LevelSelectPresenter(screenStateManager);
		levelSelectView = new LevelSelectView(presenter);

		this.addActor(levelSelectView);
	}

}