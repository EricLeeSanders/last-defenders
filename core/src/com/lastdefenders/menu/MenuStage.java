package com.lastdefenders.menu;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.menu.ui.MenuPresenter;
import com.lastdefenders.menu.ui.view.MenuOptionsView;
import com.lastdefenders.menu.ui.view.MenuView;
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
        load(resources);
        createPresenterAndViews(screenChanger, resources, audio);
    }

    private void load(Resources resources){
        resources.loadAsset(Resources.MENU_ATLAS, TextureAtlas.class);
    }

    private void createPresenterAndViews(ScreenChanger screenChanger, Resources resources, LDAudio audio){

        MenuPresenter presenter = new MenuPresenter(screenChanger, audio);
        MenuView menuView = new MenuView(presenter, resources);
        addActor(menuView);
        menuView.init();

        MenuOptionsView menuOptionsView = new MenuOptionsView(presenter, resources);
        addActor(menuOptionsView);
        menuOptionsView.init();

        presenter.setView(menuView, menuOptionsView);

    }

    @Override
    public void dispose() {

        Logger.info("Menu Stage: Dispose");
    }

}
