package com.eric.mtd.levelselect.ui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * View for the Level Select Menu
 * 
 * @author Eric
 *
 */
public class LevelSelectView extends Group {
	private LevelSelectPresenter presenter;
	//private ImageButton close, btnLevel1, btnLevel2;
	private ImageButton btnLevel1, btnLevel2, btnLevel3;
	private ImageButton btnCancel, btnPlay;
	private Group confirmLevelGroup = new Group();
	private Image confirmBackground;
	public LevelSelectView(LevelSelectPresenter presenter) {
		this.presenter = presenter;
		createControls();
		createConfirmLevelControls();
		this.addActor(confirmLevelGroup);
		confirmLevelGroup.setVisible(false);
	}

	private void createControls() {
		TextureAtlas levelAtlas = Resources.getAtlas(Resources.LEVEL_SELECT_ATLAS);		
		

		btnLevel1 = new ImageButton(new TextureRegionDrawable(levelAtlas.findRegion("pointer")));
		btnLevel1.setSize(64, 64);
		btnLevel1.setPosition(253-(btnLevel1.getWidth()/2), 100);
		this.addActor(btnLevel1); 
		setBtnLevelListener(btnLevel1, 1);

		btnLevel2 = new ImageButton(new TextureRegionDrawable(levelAtlas.findRegion("pointer")));
		btnLevel2.setSize(64, 64);
		btnLevel2.setPosition(528-(btnLevel2.getWidth()/2), 85);
		this.addActor(btnLevel2);
		setBtnLevelListener(btnLevel2, 2);
		
		//467 228
		
		btnLevel3 = new ImageButton(new TextureRegionDrawable(levelAtlas.findRegion("pointer")));
		btnLevel3.setSize(64, 64);
		btnLevel3.setPosition(467-(btnLevel3.getWidth()/2), 228);
		this.addActor(btnLevel3); 
		setBtnLevelListener(btnLevel3, 3);
		
	}
	
	private void createConfirmLevelControls(){
		confirmBackground = new Image(Resources.getAtlas(Resources.SKIN_ATLAS).findRegion("panel"));
		confirmBackground.setSize(300, 150);
		confirmBackground.setPosition((Resources.VIRTUAL_WIDTH/2) - (confirmBackground.getWidth()/2)
				, (Resources.VIRTUAL_HEIGHT/2) - (confirmBackground.getHeight()/2));
		confirmLevelGroup.addActor(confirmBackground);
		
		btnPlay = new ImageButton(new TextureRegionDrawable(Resources.getAtlas(Resources.LEVEL_SELECT_ATLAS).findRegion("play")));
		btnPlay.setSize(40,40);
		btnPlay.setPosition(confirmBackground.getX() + (confirmBackground.getWidth()/2) - (btnPlay.getWidth()/2) - 40	
				, confirmBackground.getY() +  5);
		confirmLevelGroup.addActor(btnPlay);
		
		btnCancel = new ImageButton(new TextureRegionDrawable(Resources.getAtlas(Resources.LEVEL_SELECT_ATLAS).findRegion("cancel")));
		btnCancel.setSize(40,40);
		btnCancel.setPosition(confirmBackground.getX() + (confirmBackground.getWidth()/2) - (btnCancel.getWidth()/2) + 40	
				, confirmBackground.getY() +  5);
		confirmLevelGroup.addActor(btnCancel);
	}
	
	private void setBtnLevelListener(ImageButton button, final int level) {
		button.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("Level " + level + " Pressed");
				presenter.levelSelected(level);
			}
		});
	}
}
