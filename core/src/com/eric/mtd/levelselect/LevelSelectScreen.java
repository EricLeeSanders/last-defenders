package com.eric.mtd.levelselect;

import com.eric.mtd.screen.AbstractScreen;
import com.eric.mtd.screen.state.ScreenStateManager;

/**
 * Screen for Level Select Menu
 * 
 * @author Eric
 *
 */
public class LevelSelectScreen extends AbstractScreen {
	private ScreenStateManager screenStateManager;
	private LevelSelectStage stage;

	public LevelSelectScreen(ScreenStateManager screenStateManager) {
		this.screenStateManager = screenStateManager;
		this.stage = new LevelSelectStage(screenStateManager);
		stage.setViewport(getViewport());
		super.addInputProcessor(stage);
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void renderElements(float delta) {
		stage.act(delta);
		stage.draw();

	}

	@Override
	public void dispose() {
		super.dispose();
		stage.dispose();

	}
}