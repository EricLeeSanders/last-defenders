package com.foxholedefense.menu;

import com.foxholedefense.screen.AbstractScreen;
import com.foxholedefense.screen.state.ScreenStateManager;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;

/**
 * Screen class for the Main Menu. Creates the view and stage
 * 
 * @author Eric
 *
 */
public class MenuScreen extends AbstractScreen {
	private MenuStage stage;
	private FHDAudio audio;
	public MenuScreen(ScreenStateManager screenStateManager, GameStateManager gameStateManager, Resources resources, FHDAudio audio) {
		super(gameStateManager);
		this.audio = audio;
		this.stage = new MenuStage(screenStateManager, resources, audio, getViewport());
		super.addInputProcessor(stage);
		audio.playMusic();
	}

	@Override
	public void show() {
		super.show();

	}
	@Override
	public void resize(int width, int height) {
		stage.getViewport().setScreenSize(width, height); // update the size of Viewport
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