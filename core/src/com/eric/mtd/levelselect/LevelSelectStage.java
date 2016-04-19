package com.eric.mtd.levelselect;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eric.mtd.levelselect.ui.LevelSelectPresenter;
import com.eric.mtd.levelselect.ui.LevelSelectView;
import com.eric.mtd.screen.state.ScreenStateManager;

/**
 * Stage for Level Select Menu
 * 
 * @author Eric
 *
 */
public class LevelSelectStage extends Stage {
	private ScreenStateManager screenStateManager;
	private LevelSelectView levelSelectView;
	private LevelSelectPresenter presenter;

	public LevelSelectStage(ScreenStateManager screenStateManager) {
		this.screenStateManager = screenStateManager;

		presenter = new LevelSelectPresenter(screenStateManager);
		levelSelectView = new LevelSelectView(presenter);

		this.addActor(levelSelectView);
	}

}