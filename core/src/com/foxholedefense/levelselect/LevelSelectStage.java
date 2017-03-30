package com.foxholedefense.levelselect;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.foxholedefense.levelselect.ui.LevelSelectPresenter;
import com.foxholedefense.levelselect.ui.LevelSelectView;
import com.foxholedefense.screen.IScreenChanger;
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
	public LevelSelectStage(IScreenChanger screenChanger, Resources resources, FHDAudio audio, Viewport viewport) {
		super(viewport);
		this.resources = resources;
		LevelSelectPresenter presenter = new LevelSelectPresenter(screenChanger);
		resources.loadAsset(Resources.LEVEL_SELECT_ATLAS, TextureAtlas.class);
		LevelSelectView levelSelectView = new LevelSelectView(presenter, resources, audio);
		this.addActor(levelSelectView);
		levelSelectView.setBackground(resources.getAsset(Resources.LEVEL_SELECT_ATLAS, TextureAtlas.class));
	}
	@Override
	public void dispose(){
		Logger.info("Level Select Stage Dispose");
	}

}