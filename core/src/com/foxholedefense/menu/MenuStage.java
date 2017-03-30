package com.foxholedefense.menu;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
	public MenuStage(IScreenChanger screenChanger, Resources resources, FHDAudio audio, Viewport viewport) {
		super(viewport);
		resources.loadAsset(Resources.MENU_ATLAS, TextureAtlas.class);
		presenter = new MenuPresenter(screenChanger, audio);
		menuView = new MenuView(presenter, resources);
		presenter.setView(menuView);
		this.addActor(menuView);
		menuView.setBackground(resources.getAsset(Resources.MENU_ATLAS, TextureAtlas.class));
	}
	
	@Override
	public void dispose(){
		Logger.info("Menu Stage: Dispose");
	}

}
