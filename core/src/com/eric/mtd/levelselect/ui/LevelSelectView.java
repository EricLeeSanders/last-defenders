package com.eric.mtd.levelselect.ui;

import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.eric.mtd.util.MTDAudio;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;
import com.eric.mtd.util.MTDAudio.MTDSound;

/**
 * View for the Level Select Menu
 * 
 * @author Eric
 *
 */
public class LevelSelectView extends Group {
	private LevelSelectPresenter presenter;
	private Label lblLevel;
	private Image level1, level2, level3;
	private Group levelGroup;
	private int selectedLevel;
	private MTDAudio audio;
	public LevelSelectView(LevelSelectPresenter presenter, TextureAtlas levelAtlas, Map<String,BitmapFont> fonts, MTDAudio audio) {
		this.presenter = presenter;
		this.audio = audio;
		createControls(levelAtlas);
		levelGroup = new Group();
		this.addActor(levelGroup);
		levelGroup.setVisible(false);
		levelGroup.setTransform(false);
		this.setTransform(false);
		createConfirmLevelControls(levelAtlas, fonts);
	}

	private void createControls(TextureAtlas levelSelectAtlas) {
		
		ImageButton btnLevel1 = new ImageButton(new TextureRegionDrawable(levelSelectAtlas.findRegion("pointer")));
		btnLevel1.setSize(64, 64);
		btnLevel1.setPosition(253-(btnLevel1.getWidth()/2), 100);
		this.addActor(btnLevel1); 
		setBtnLevelListener(btnLevel1, 1);

		ImageButton btnLevel2 = new ImageButton(new TextureRegionDrawable(levelSelectAtlas.findRegion("pointer")));
		btnLevel2.setSize(64, 64);
		btnLevel2.setPosition(528-(btnLevel2.getWidth()/2), 85);
		this.addActor(btnLevel2);
		setBtnLevelListener(btnLevel2, 2);
		
		//467 228
		ImageButton btnLevel3 = new ImageButton(new TextureRegionDrawable(levelSelectAtlas.findRegion("pointer")));
		btnLevel3.setSize(64, 64);
		btnLevel3.setPosition(467-(btnLevel3.getWidth()/2), 228);
		this.addActor(btnLevel3); 
		setBtnLevelListener(btnLevel3, 3);
		
	}
	public void setBackground(TextureAtlas levelSelectAtlas){
		Image background = new Image(levelSelectAtlas.findRegion("background"));
		background.setFillParent(true);
		this.getStage().addActor(background);
		background.setZIndex(0);
	}
	private void createConfirmLevelControls(TextureAtlas levelSelectAtlas, Map<String,BitmapFont> fonts){
		
		level1 = new Image(levelSelectAtlas.findRegion("level1"));
		level1.setSize(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT);
		level1.setVisible(false);
		levelGroup.addActor(level1);
		
		level2 = new Image(levelSelectAtlas.findRegion("level2"));
		level2.setSize(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT);
		level2.setVisible(false);
		levelGroup.addActor(level2);
		
		level3 = new Image(levelSelectAtlas.findRegion("level3"));
		level3.setSize(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT);
		level3.setVisible(false);
		levelGroup.addActor(level3);
		
		ImageButton btnMap = new ImageButton(new TextureRegionDrawable(levelSelectAtlas.findRegion("map_icon")));
		btnMap.setSize(64,64);
		btnMap.setPosition(15,15);
		levelGroup.addActor(btnMap);
		setBtnMapListener(btnMap);
		
		ImageButton btnPlay = new ImageButton(new TextureRegionDrawable(levelSelectAtlas.findRegion("play")));
		btnPlay.setSize(64,64);
		btnPlay.setPosition(Resources.VIRTUAL_WIDTH - btnPlay.getWidth() - 15 ,15);
		levelGroup.addActor(btnPlay);
		setBtnPlayListener(btnPlay);
		LabelStyle lblLevelStyle = new LabelStyle();
		lblLevelStyle.font =fonts.get("default-font-46");
		lblLevel = new Label("Level X",lblLevelStyle );
		lblLevel.setPosition((Resources.VIRTUAL_WIDTH/2)-(lblLevel.getWidth()/2)
				, Resources.VIRTUAL_HEIGHT-lblLevel.getHeight() - 25);
		levelGroup.addActor(lblLevel);
	}
	private void showConfirmWindow(boolean visible){
		levelGroup.setVisible(visible);
		if(visible){
			if(selectedLevel == 1){
				level1.setVisible(true);
				lblLevel.setText("Level 1");
				level2.setVisible(false);
				level3.setVisible(false);
			} else if(selectedLevel == 2){
				level1.setVisible(false);
				level2.setVisible(true);
				lblLevel.setText("Level 2");
				level3.setVisible(false);
			} else if(selectedLevel == 3){
				level1.setVisible(false);
				level2.setVisible(false);
				level3.setVisible(true);
				lblLevel.setText("Level 3");
			}
		}
	}
	
	private void setBtnLevelListener(Button button, final int level) {
		button.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("Level " + level + " Pressed");
				selectedLevel = level;
				audio.playSound(MTDSound.SMALL_CLICK);
				showConfirmWindow(true);
				
			}
		});
	}
	
	private void setBtnMapListener(Button btnMap) {
		btnMap.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				audio.playSound(MTDSound.LARGE_CLICK);
				showConfirmWindow(false);
				
			}
		});
	}
	
	private void setBtnPlayListener(Button btnPlay) {
		btnPlay.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				audio.playSound(MTDSound.LARGE_CLICK);
				presenter.playLevel(selectedLevel);
				
			}
		});
	}
}
