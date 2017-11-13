package com.lastdefenders.menu;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.menu.ui.MenuPresenter;
import com.lastdefenders.menu.ui.MenuView;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * Stage class for the Main menu.
 *
 * @author Eric
 */
class MenuStage extends Stage {

    public MenuStage(ScreenChanger screenChanger, Resources resources, LDAudio audio,
        Viewport viewport) {

        super(viewport);
        resources.loadAsset(Resources.MENU_ATLAS, TextureAtlas.class);
        MenuPresenter presenter = new MenuPresenter(screenChanger, audio);
        MenuView menuView = new MenuView(presenter, resources);
        presenter.setView(menuView);
        this.addActor(menuView);
        menuView.setBackground(resources.getAsset(Resources.MENU_ATLAS, TextureAtlas.class));
    }

    @Override
    public void dispose() {

        Logger.info("Menu Stage: Dispose");
    }

}
