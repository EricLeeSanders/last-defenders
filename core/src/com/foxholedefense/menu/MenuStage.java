package com.foxholedefense.menu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.foxholedefense.menu.ui.MenuPresenter;
import com.foxholedefense.menu.ui.MenuView;
import com.foxholedefense.screen.state.ScreenStateManager;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;

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
	public MenuStage(ScreenStateManager screenStateManager, Resources resources, FHDAudio audio) {
		super(new ScalingViewport(Scaling.stretch, Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT, new OrthographicCamera()));
		this.screenStatemanager = screenStateManager;
		this.resources = resources;
		resources.loadAtlas(Resources.MENU_ATLAS);
		presenter = new MenuPresenter(screenStateManager, audio);
		menuView = new MenuView(presenter, resources);
		presenter.setView(menuView);
		this.addActor(menuView);
		menuView.setBackground(resources.getAtlas(Resources.MENU_ATLAS));
	}
	
	@Override
	public void dispose(){
		Logger.info("Menu Stage Dispose");
	}

}
