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
		super.addInputProcessor(stage);
		AudioUtil.load();
		AudioUtil.playMusic();
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
		if (Logger.DEBUG)
			System.out.println("Disposing menu screen");
		stage.dispose();
		AudioUtil.dispose();

	}
}