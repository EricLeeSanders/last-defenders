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
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.MTDAudio;
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
	private Resources resources;
	public MenuStage(ScreenStateManager screenStateManager, Resources resources, MTDAudio audio) {
		super(new ScalingViewport(Scaling.stretch, Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT, new OrthographicCamera()));
		this.screenStatemanager = screenStateManager;
		this.resources = resources;
		resources.loadAtlas(Resources.MENU_ATLAS);
		presenter = new MenuPresenter(screenStateManager, audio);
		menuView = new MenuView(presenter, resources.getAtlas(Resources.MENU_ATLAS), resources.getSkin(Resources.SKIN_JSON));
		presenter.setView(menuView);
		this.addActor(menuView);
		menuView.setBackground(resources.getAtlas(Resources.MENU_ATLAS));
	}
	
	@Override
	public void dispose(){
		Logger.info("Menu Stage Dispose");
		resources.unloadAsset(Resources.MENU_ATLAS);
	}

}
