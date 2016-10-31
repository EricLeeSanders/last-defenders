package com.foxholedefense.levelselect;

import com.foxholedefense.screen.AbstractScreen;
import com.foxholedefense.screen.state.ScreenStateManager;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;

/**
 * Screen for Level Select Menu
 * 
 * @author Eric
 *
 */
public class LevelSelectScreen extends AbstractScreen {
	private LevelSelectStage stage;
	private FHDAudio audio;
	public LevelSelectScreen(ScreenStateManager screenStateManager, GameStateManager gameStateManager, Resources resources, FHDAudio audio) {
		super(gameStateManager);
		this.audio = audio;
		this.stage = new LevelSelectStage(screenStateManager,resources, audio, getViewport());
		super.addInputProcessor(stage);
	}
	@Override
	public void resize(int width, int height) {
		stage.getViewport().setScreenSize(width, height); // update the size of Viewport
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
		Logger.info("Level Select Screen Dispose");
		super.dispose();
		stage.dispose();
	}
}