package com.foxholedefense.menu;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.foxholedefense.menu.ui.MenuPresenter;
import com.foxholedefense.menu.ui.MenuView;
import com.foxholedefense.screen.IScreenChanger;
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
	private MenuPresenter presenter;
	private MenuView menuView;
	private Resources resources;
	public MenuStage(IScreenChanger screenChanger, Resources resources, FHDAudio audio, Viewport viewport) {
		super(viewport);
		this.resources = resources;
		resources.loadAtlas(Resources.MENU_ATLAS);
		presenter = new MenuPresenter(screenChanger, audio);
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
