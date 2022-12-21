package com.lastdefenders.levelselect;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.googleplay.GooglePlayServices;
import com.lastdefenders.levelselect.ui.LevelSelectPresenter;
import com.lastdefenders.levelselect.ui.LevelSelectView;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.sound.LDAudio;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * Stage for Level Select Menu
 *
 * @author Eric
 */
class LevelSelectStage extends Stage {

    public LevelSelectStage(ScreenChanger screenChanger, Resources resources, LDAudio audio,
        Viewport viewport, GooglePlayServices playServices) {

        super(viewport);

        LevelSelectPresenter presenter = new LevelSelectPresenter(screenChanger, playServices);
        resources.loadAsset(Resources.LEVEL_SELECT_ATLAS, TextureAtlas.class);
        LevelSelectView levelSelectView = new LevelSelectView(presenter, resources, audio);
        addActor(levelSelectView);

        levelSelectView.init();

    }

    @Override
    public void dispose() {

        Logger.info("Level Select Stage Dispose");
    }

}
