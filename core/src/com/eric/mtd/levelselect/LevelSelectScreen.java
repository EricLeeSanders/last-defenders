package com.eric.mtd.levelselect;

import com.eric.mtd.screen.AbstractScreen;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.state.GameStateManager;

/**
 * Screen for Level Select Menu
 * 
 * @author Eric
 *
 */
public class LevelSelectScreen extends AbstractScreen {
	private ScreenStateManager screenStateManager;
	private LevelSelectStage stage;

	public LevelSelectScreen(ScreenStateManager screenStateManager, GameStateManager gameStateManager) {
		super(gameStateManager);
		this.screenStateManager = screenStateManager;
		this.stage = new LevelSelectStage(screenStateManager);
		super.addInputProcessor(stage);
	}
	@Override
	public void resize(int width, int height) {
		stage.getViewport().setScreenSize(width, height); // update the size of ViewPort
	    super.resize(width, height);
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