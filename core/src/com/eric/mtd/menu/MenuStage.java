package com.eric.mtd.menu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.eric.mtd.menu.ui.MenuPresenter;
import com.eric.mtd.menu.ui.MenuView;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.util.Resources;

/**
 * Stage class for the Main menu.
 * 
 * @author Eric
 *
 */
public class MenuStage extends Stage {
	private ScreenStateManager screenStatemanager;
	private MenuPresenter presenter;
	private MenuView menuView;

	public MenuStage(ScreenStateManager screenStateManager) {
		super(new ScalingViewport(Scaling.stretch, Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT, new OrthographicCamera()));
		this.screenStatemanager = screenStateManager;

		createBackground();
		presenter = new MenuPresenter(screenStateManager);
		menuView = new MenuView(presenter);
		this.addActor(menuView);
	}

	public void createBackground() {
		TextureAtlas menuAtlas = Resources.getAtlas(Resources.MENU_ATLAS);

		Image background = new Image(menuAtlas.findRegion("background"));
		background.setFillParent(true);
		this.addActor(background);
	}

}
