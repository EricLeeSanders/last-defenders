package com.eric.mtd.menu.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
	private TextButton btnPlay;
	private ImageButton btnSound, btnMusic;

	public MenuView(MenuPresenter presenter) {
		this.presenter = presenter;
		createControls();
	}

	public void createControls() {
		Skin skin = Resources.getSkin(Resources.SKIN_JSON);
		
		btnPlay = new TextButton("Play", skin);
		btnPlay.setSize(200, 75);
		btnPlay.setPosition(0 - btnPlay.getWidth(), 100);
		btnPlay.addAction(Actions.moveTo(225, 100, PLAY_MOVE_DURATION));
		this.addActor(btnPlay);
		setBtnPlayListener();
		
		btnSound = new ImageButton(skin, "sound");
		btnSound.setSize(64, 64);
		btnSound.setPosition(10, 10);
		this.addActor(btnSound);
		setBtnSoundListener();
		
		
		btnMusic = new ImageButton(skin, "music");
		btnMusic.setSize(64, 64);
		btnMusic.setPosition(110,10);
		this.addActor(btnMusic);
		setBtnMusicListener();
	}

	private void setBtnPlayListener() {
		btnPlay.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.playGame();
				if (Logger.DEBUG) System.out.println("Play Pressed");
			}
		});

	}
	
	private void setBtnSoundListener(){
		btnSound.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.soundPressed();
				if (Logger.DEBUG) System.out.println("Sound Pressed");
			}
		});
	}
	
	private void setBtnMusicListener(){
		btnMusic.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.musicPressed();
				if (Logger.DEBUG) System.out.println("Music Pressed");
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
