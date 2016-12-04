package com.foxholedefense.levelselect.ui;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.FHDAudio.FHDSound;

/**
 * View for the Level Select Menu
 * 
 * @author Eric
 *
 */
public class LevelSelectView extends Group {
	private LevelSelectPresenter presenter;
	private Label lblLevel;
	private Image level1, level2, level3, level4, level5;
	private Group levelGroup;
	private int selectedLevel;
	private FHDAudio audio;
	public LevelSelectView(LevelSelectPresenter presenter, Resources resources, FHDAudio audio) {
		this.presenter = presenter;
		this.audio = audio;
		TextureAtlas levelSelectAtlas = resources.getAtlas(Resources.LEVEL_SELECT_ATLAS);
		Skin skin = resources.getSkin(Resources.SKIN_JSON);
		createControls(levelSelectAtlas, skin);
		levelGroup = new Group();
		this.addActor(levelGroup);
		levelGroup.setVisible(false);
		levelGroup.setTransform(false);
		this.setTransform(false);
		createConfirmLevelControls(levelSelectAtlas, skin);
	}

	private void createControls(TextureAtlas levelSelectAtlas, Skin skin) {
	
		
		ImageButton btnLevel1 = new ImageButton(new TextureRegionDrawable(levelSelectAtlas.findRegion("pointer")));
		btnLevel1.setSize(64, 64);
		btnLevel1.setPosition(240-(btnLevel1.getWidth()/2), 40);
		this.addActor(btnLevel1); 
		setBtnLevelListener(btnLevel1, 1);
		
		ImageButton btnLevel2 = new ImageButton(new TextureRegionDrawable(levelSelectAtlas.findRegion("pointer")));
		btnLevel2.setSize(64, 64);
		btnLevel2.setPosition(280-(btnLevel2.getWidth()/2), 100);
		this.addActor(btnLevel2); 
		setBtnLevelListener(btnLevel2, 2);

		ImageButton btnLevel3 = new ImageButton(new TextureRegionDrawable(levelSelectAtlas.findRegion("pointer")));
		btnLevel3.setSize(64, 64);
		btnLevel3.setPosition(528-(btnLevel3.getWidth()/2), 85);
		this.addActor(btnLevel3);
		setBtnLevelListener(btnLevel3, 3);

		ImageButton btnLevel4 = new ImageButton(new TextureRegionDrawable(levelSelectAtlas.findRegion("pointer")));
		btnLevel4.setSize(64, 64);
		btnLevel4.setPosition(467-(btnLevel4.getWidth()/2), 228);
		this.addActor(btnLevel4); 
		setBtnLevelListener(btnLevel4, 4);
		
		ImageButton btnLevel5 = new ImageButton(new TextureRegionDrawable(levelSelectAtlas.findRegion("pointer")));
		btnLevel5.setSize(64, 64);
		btnLevel5.setPosition(380-(btnLevel5.getWidth()/2), 300);
		this.addActor(btnLevel5); 
		setBtnLevelListener(btnLevel5, 5);
		
		ImageButton btnMenu = new ImageButton(skin, "arrow-left");
		btnMenu.setSize(64,64);
		btnMenu.getImageCell().size(40,27);
		btnMenu.getImage().setScaling(Scaling.stretch);
		btnMenu.setPosition(15,15);
		this.addActor(btnMenu);
		setBtnMenuListener(btnMenu);
		
		
	}
	public void setBackground(TextureAtlas levelSelectAtlas){
		Image background = new Image(levelSelectAtlas.findRegion("background"));
		background.setFillParent(true);
		this.getStage().addActor(background);
		background.setZIndex(0);
	}
	private void createConfirmLevelControls(TextureAtlas levelSelectAtlas, Skin skin){
		
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
		
		level4 = new Image(levelSelectAtlas.findRegion("level4"));
		level4.setSize(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT);
		level4.setVisible(false);
		levelGroup.addActor(level4);
		
		level5 = new Image(levelSelectAtlas.findRegion("level5"));
		level5.setSize(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT);
		level5.setVisible(false);
		levelGroup.addActor(level5);
		
		ImageButton btnMap = new ImageButton(new TextureRegionDrawable(levelSelectAtlas.findRegion("map_icon")));
		btnMap.setSize(64,64);
		btnMap.setPosition(15,15);
		levelGroup.addActor(btnMap);
		setBtnMapListener(btnMap);
		
		ImageButton btnPlay = new ImageButton(skin, "arrow-right");
		btnPlay.setSize(64,64);
		btnPlay.getImageCell().size(40,27);
		btnPlay.getImage().setScaling(Scaling.stretch);
		btnPlay.setPosition(Resources.VIRTUAL_WIDTH - btnPlay.getWidth() - 15 ,15);
		levelGroup.addActor(btnPlay);
		setBtnPlayListener(btnPlay);


		lblLevel = new Label("LEVEL X", skin);
		lblLevel.setPosition((Resources.VIRTUAL_WIDTH/2)-(lblLevel.getWidth()/2)
				, Resources.VIRTUAL_HEIGHT-lblLevel.getHeight() - 25);
		levelGroup.addActor(lblLevel);
	}
	private void showConfirmWindow(boolean visible){
		levelGroup.setVisible(visible);
		if(visible){
			if(selectedLevel == 1){
				lblLevel.setText("LEVEL 1");
				level1.setVisible(true);
				level2.setVisible(false);
				level3.setVisible(false);
				level4.setVisible(false);
				level5.setVisible(false);
			} else if(selectedLevel == 2){
				lblLevel.setText("LEVEL 2");
				level1.setVisible(false);
				level2.setVisible(true);
				level3.setVisible(false);
				level4.setVisible(false);
				level5.setVisible(false);
			} else if(selectedLevel == 3){
				lblLevel.setText("LEVEL 3");
				level1.setVisible(false);
				level2.setVisible(false);
				level3.setVisible(true);
				level4.setVisible(false);
				level5.setVisible(false);
			} else if(selectedLevel == 4){
				lblLevel.setText("LEVEL 4");
				level1.setVisible(false);
				level2.setVisible(false);
				level3.setVisible(false);
				level4.setVisible(true);
				level5.setVisible(false);
			} else if(selectedLevel == 5){
				lblLevel.setText("LEVEL 5");
				level1.setVisible(false);
				level2.setVisible(false);
				level3.setVisible(false);
				level4.setVisible(false);
				level5.setVisible(true);
			}
		}
	}
	
	private void setBtnMenuListener(Button btnMenu) {
		btnMenu.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				audio.playSound(FHDSound.LARGE_CLICK);
				presenter.mainMenu();
				
			}
		});
	}
	
	private void setBtnLevelListener(Button button, final int level) {
		button.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				selectedLevel = level;
				audio.playSound(FHDSound.SMALL_CLICK);
				showConfirmWindow(true);
				
			}
		});
	}
	
	private void setBtnMapListener(Button btnMap) {
		btnMap.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				audio.playSound(FHDSound.LARGE_CLICK);
				showConfirmWindow(false);
				
			}
		});
	}
	
	private void setBtnPlayListener(Button btnPlay) {
		btnPlay.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				audio.playSound(FHDSound.LARGE_CLICK);
				presenter.loadLevel(selectedLevel);
				
			}
		});
	}
}
