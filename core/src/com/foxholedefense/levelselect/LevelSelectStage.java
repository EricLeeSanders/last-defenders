package com.foxholedefense.levelselect;

import java.util.Map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.foxholedefense.levelselect.ui.LevelSelectPresenter;
import com.foxholedefense.levelselect.ui.LevelSelectView;
import com.foxholedefense.screen.state.ScreenStateManager;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;

/**
 * Stage for Level Select Menu
 * 
 * @author Eric
 *
 */
public class LevelSelectStage extends Stage {
	private Resources resources;
	public LevelSelectStage(ScreenStateManager screenStateManager, Resources resources, FHDAudio audio, Viewport viewport) {
		super(viewport);
		this.resources = resources;
		LevelSelectPresenter presenter = new LevelSelectPresenter(screenStateManager);
		resources.loadAtlas(Resources.LEVEL_SELECT_ATLAS);
		LevelSelectView levelSelectView = new LevelSelectView(presenter, resources, audio);
		this.addActor(levelSelectView);
		levelSelectView.setBackground(resources.getAtlas(Resources.LEVEL_SELECT_ATLAS));
	}
	@Override
	public void dispose(){
		Logger.info("Level Select Stage Dispose");
	}

}