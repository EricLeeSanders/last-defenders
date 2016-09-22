package com.eric.mtd.levelselect;

import java.util.Map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.eric.mtd.levelselect.ui.LevelSelectPresenter;
import com.eric.mtd.levelselect.ui.LevelSelectView;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.util.MTDAudio;
import com.eric.mtd.util.Resources;

/**
 * Stage for Level Select Menu
 * 
 * @author Eric
 *
 */
public class LevelSelectStage extends Stage {
	private Resources resources;
	public LevelSelectStage(ScreenStateManager screenStateManager, Resources resources, MTDAudio audio) {
		super(new ScalingViewport(Scaling.stretch, Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT, new OrthographicCamera()));
		this.resources = resources;
		LevelSelectPresenter presenter = new LevelSelectPresenter(screenStateManager);
		resources.loadAtlas(Resources.LEVEL_SELECT_ATLAS);
		LevelSelectView levelSelectView = new LevelSelectView(presenter, resources.getAtlas(Resources.LEVEL_SELECT_ATLAS), resources.getFonts(), audio);
		this.addActor(levelSelectView);
		levelSelectView.setBackground(resources.getAtlas(Resources.LEVEL_SELECT_ATLAS));
	}
	@Override
	public void dispose(){
		resources.unloadAsset(Resources.LEVEL_SELECT_ATLAS);
	}

}