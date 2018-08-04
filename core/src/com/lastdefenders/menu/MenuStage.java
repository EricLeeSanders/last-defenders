package com.lastdefenders.menu;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.googleplay.GooglePlayServices;
import com.lastdefenders.menu.ui.MenuPresenter;
import com.lastdefenders.menu.ui.view.MenuOptionsView;
import com.lastdefenders.menu.ui.view.MenuView;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.ui.presenter.impl.GooglePlayServicesPresenterImpl;
import com.lastdefenders.ui.view.impl.GooglePlayServicesViewImpl;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * Stage class for the Main menu.
 *
 * @author Eric
 */
class MenuStage extends Stage {

    private MenuPresenter menuPresenter;

    public MenuStage(ScreenChanger screenChanger, Resources resources, LDAudio audio,
        Viewport viewport, GooglePlayServices playServices) {

        super(viewport);
        load(resources);
        createPresenterAndViews(screenChanger, resources, audio, playServices);
    }

    private void load(Resources resources){
        resources.loadAsset(Resources.MENU_ATLAS, TextureAtlas.class);
    }

    private void createPresenterAndViews(ScreenChanger screenChanger, Resources resources,
        LDAudio audio, GooglePlayServices playServices){

        // Only create the GPS presenter if GPS is enabled
        GooglePlayServicesPresenterImpl gpsPresenter = null;
        if(playServices.isGooglePlayServicesAvailable()) {
            gpsPresenter = new GooglePlayServicesPresenterImpl(audio, playServices);
        }

        // Might pass null as the GPS presenter.
        menuPresenter = new MenuPresenter(screenChanger, audio, gpsPresenter);
        MenuView menuView = new MenuView(menuPresenter, resources);
        addActor(menuView);
        menuView.init();

        MenuOptionsView menuOptionsView = new MenuOptionsView(menuPresenter, resources);
        addActor(menuOptionsView);
        menuOptionsView.init();

        menuPresenter.setView(menuView, menuOptionsView);

        // Only create the view if GPS is enabled
        if(playServices.isGooglePlayServicesAvailable()) {
            GooglePlayServicesViewImpl gpsView = new GooglePlayServicesViewImpl(gpsPresenter,
                resources);
            addActor(gpsView);
            gpsView.init();
            gpsPresenter.setView(gpsView);
        }

    }

    /**
     * Handles a back/escape request. Returns true if the event was handled.
     * @return - boolean
     */
    boolean handleBack(){
        return menuPresenter.handleBack();
    }

    @Override
    public void dispose() {

        Logger.info("Menu Stage: Dispose");
    }

}
