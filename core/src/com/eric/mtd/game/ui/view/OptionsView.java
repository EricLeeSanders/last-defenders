package com.eric.mtd.game.ui.view;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.eric.mtd.game.ui.presenter.OptionsPresenter;
import com.eric.mtd.game.ui.view.interfaces.IOptionsView;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * View for the options window
 * 
 * @author Eric
 *
 */
public class OptionsView extends Group implements IOptionsView {
	private OptionsPresenter presenter;
	private TextButton btnResume, btnNewGame, btnMainMenu;
	private Label lblTitle;
	private ImageButton btnSound, btnMusic;

	public OptionsView(OptionsPresenter presenter, Skin skin) {
		this.presenter = presenter;
		this.setTransform(false);
		createControls(skin);
	}

	/**
	 * Create controls with MTD Widgets
	 */
	public void createControls(Skin skin) {
		Table table = new Table();
		table.setBackground(skin.getDrawable("main-panel-hollow"));
		table.setSize(322,360);
		table.setPosition((Resources.VIRTUAL_WIDTH/2)-(table.getWidth()/2), (Resources.VIRTUAL_HEIGHT/2)-(table.getHeight()/2));
		//table.debug();
		this.addActor(table);
		
		lblTitle = new Label("Options", skin);
		lblTitle.setPosition(table.getX() + (table.getWidth()/2) - (lblTitle.getWidth()/2)
					,table.getY() + table.getHeight() - lblTitle.getHeight() );
		lblTitle.setAlignment(Align.center);
		lblTitle.setFontScale(0.7f);
		this.addActor(lblTitle);
		
		table.row();
		btnResume = new TextButton("Resume",skin);
		btnResume.getLabel().setFontScale(0.45f);
		table.add(btnResume).width(128).height(44).spaceBottom(10).padTop(30).colspan(2);
		setBtnResumeListener();
		
		table.row();
		btnNewGame = new TextButton("New Game",skin);
		btnNewGame.getLabel().setFontScale(0.45f);
		table.add(btnNewGame).width(128).height(44).spaceBottom(10).colspan(2);
		setBtnNewGameListener();

		table.row();
		btnMainMenu = new TextButton("Main Menu",skin);
		btnMainMenu.getLabel().setFontScale(0.45f);
		table.add(btnMainMenu).width(128).height(44).spaceBottom(10).colspan(2);
		setBtnMainMenuListener();
		
		table.row();
		btnSound = new ImageButton(skin, "sound");
		setBtnSoundListener();
		table.add(btnSound).width(64).height(64);
		
		btnMusic = new ImageButton(skin, "music");
		setBtnMusicListener();
		table.add(btnMusic).width(64).height(64).spaceRight(10);
	}

	private void setBtnResumeListener() {
		btnResume.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.resumeGame();
			}
		});

	}

	private void setBtnNewGameListener() {
		btnNewGame.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.newGame();
			}
		});

	}

	private void setBtnMainMenuListener() {
		btnMainMenu.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.mainMenu();
			}
		});

	}

	private void setBtnSoundListener(){
		btnSound.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.soundPressed();
			}
		});
	}
	
	private void setBtnMusicListener(){
		btnMusic.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.musicPressed();
			}
		});
	}
	
	@Override
	public void optionsState() {
		this.setVisible(true);

	}

	@Override
	public void standByState() {
		this.setVisible(false);

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
