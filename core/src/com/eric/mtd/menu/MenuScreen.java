package com.eric.mtd.menu;

import com.eric.mtd.screen.AbstractScreen;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.util.MTDAudio;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * Screen class for the Main Menu. Creates the view and stage
 * 
 * @author Eric
 *
 */
public class MenuScreen extends AbstractScreen {
	private MenuStage stage;
	private MTDAudio audio;
	public MenuScreen(ScreenStateManager screenStateManager, GameStateManager gameStateManager, Resources resources, MTDAudio audio) {
		super(gameStateManager);
		this.audio = audio;
		this.stage = new MenuStage(screenStateManager, resources, audio);
		super.addInputProcessor(stage);
		audio.playMusic();
	}

	@Override
	public void show() {
		super.show();

	}
	@Override
	public void resize(int width, int height) {
		stage.getViewport().setScreenSize(width, height); // update the size of ViewPortS
	    super.resize(width, height);
	}
	@Override
	public void renderElements(float delta) {
		stage.act(delta);
		stage.draw();

	}

	@Override
	public void dispose() {
		super.dispose();
		Logger.info("Menu Screen Dispose");
		stage.dispose();

	}
}