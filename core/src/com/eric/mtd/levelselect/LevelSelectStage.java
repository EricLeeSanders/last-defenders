package com.eric.mtd.levelselect;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.eric.mtd.levelselect.ui.LevelSelectPresenter;
import com.eric.mtd.levelselect.ui.LevelSelectView;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.util.Resources;

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
		super(new ScalingViewport(Scaling.stretch, Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT, new OrthographicCamera()));
		this.screenStateManager = screenStateManager;

		presenter = new LevelSelectPresenter(screenStateManager);
		levelSelectView = new LevelSelectView(presenter);

		this.addActor(levelSelectView);
	}

}