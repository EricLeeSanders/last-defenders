package com.eric.mtd.menu;

import com.eric.mtd.screen.AbstractScreen;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.util.AudioUtil;
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

	public MenuScreen(ScreenStateManager screenStateManager, GameStateManager gameStateManager) {
		super(gameStateManager);
		Resources.loadGraphics();
		this.stage = new MenuStage(screenStateManager);
		//stage.setViewport(getViewport());
		super.addInputProcessor(stage);
		AudioUtil.load();
		AudioUtil.playMusic();
	}

	@Override
	public void show() {
		super.show();
		// retrieve the default table actor

	}

	@Override
	public void renderElements(float delta) {
		stage.act(delta);
		stage.draw();

	}

	@Override
	public void dispose() {
		super.dispose();
		if (Logger.DEBUG)
			System.out.println("Disposing menu screen");
		stage.dispose();
		AudioUtil.dispose();

	}
}