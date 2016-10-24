package com.eric.mtd.levelselect;

import com.eric.mtd.screen.AbstractScreen;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.MTDAudio;
import com.eric.mtd.util.Resources;

/**
 * Screen for Level Select Menu
 * 
 * @author Eric
 *
 */
public class LevelSelectScreen extends AbstractScreen {
	private LevelSelectStage stage;
	private MTDAudio audio;
	public LevelSelectScreen(ScreenStateManager screenStateManager, GameStateManager gameStateManager, Resources resources, MTDAudio audio) {
		super(gameStateManager);
		this.audio = audio;
		this.stage = new LevelSelectStage(screenStateManager,resources, audio);
		super.addInputProcessor(stage);
	}
	@Override
	public void resize(int width, int height) {
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