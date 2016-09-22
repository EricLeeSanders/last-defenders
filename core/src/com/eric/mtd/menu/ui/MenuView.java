package com.eric.mtd.menu.ui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * View for the Main Menu
 * 
 * @author Eric
 *
 */
public class MenuView extends Group implements IMenuView {
	private static final float PLAY_MOVE_DURATION = 0.5f;
	private MenuPresenter presenter;
	private ImageButton btnSound, btnMusic;

	public MenuView(MenuPresenter presenter, TextureAtlas menuAtlas, Skin skin) {
		this.presenter = presenter;
		this.setTransform(false);
		createControls(menuAtlas, skin);
	}

	public void createControls(TextureAtlas menuAtlas, Skin skin) {
				
		TextButton btnPlay = new TextButton("Play", skin);
		btnPlay.setSize(200, 75);
		btnPlay.setPosition(0 - btnPlay.getWidth(), 100);
		btnPlay.addAction(Actions.moveTo(225, 100, PLAY_MOVE_DURATION));
		this.addActor(btnPlay);
		setBtnPlayListener(btnPlay);
		
		btnSound = new ImageButton(skin, "sound");
		btnSound.setSize(64, 64);
		btnSound.setPosition(10, 10);
		this.addActor(btnSound);
		setBtnSoundListener(btnSound);
		
		
		btnMusic = new ImageButton(skin, "music");
		btnMusic.setSize(64, 64);
		btnMusic.setPosition(110,10);
		this.addActor(btnMusic);
		setBtnMusicListener(btnMusic);
	}
	public void setBackground(TextureAtlas menuAtlas){
		Image background = new Image(menuAtlas.findRegion("background"));
		background.setFillParent(true);
		this.getStage().addActor(background);
		background.setZIndex(0);
	}
	private void setBtnPlayListener(Button btnPlay ) {
		btnPlay.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.playGame();
			}
		});

	}
	
	private void setBtnSoundListener(Button btnSound){
		btnSound.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.soundPressed();
			}
		});
	}
	
	private void setBtnMusicListener(Button btnMusic){
		btnMusic.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.musicPressed();
			}
		});
	}

	@Override
	public void setBtnSoundOn(boolean soundOn) {
		btnSound.setChecked(soundOn);
		
	}

	@Override
	public void setBtnMusicOn(boolean musicOn) {
		btnMusic.setChecked(musicOn);
		
	}
}
