package com.foxholedefense.game.ui.view;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.foxholedefense.game.ui.presenter.OptionsPresenter;
import com.foxholedefense.game.ui.view.interfaces.IOptionsView;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

/**
 * View for the options window
 * 
 * @author Eric
 *
 */
public class OptionsView extends Group implements IOptionsView {
	private OptionsPresenter presenter;
	private TextButton btnResume, btnNewGame, btnMainMenu;
	private CheckBox btnShowRanges, btnSound, btnMusic;
	private Image volSliderBg;
	private float sliderEndPos, sliderStartPos;
	public OptionsView(OptionsPresenter presenter, Resources resources) {
		this.presenter = presenter;
		this.setTransform(false);
		createControls(resources);
	}
	
	public void act(float delta){
		super.act(delta);
		//This is a bit of a hack, but I need this here for the initial load of the screen.
        float startX = sliderStartPos + sliderEndPos * presenter.getMasterVolume();
        volSliderBg.setX(startX);
        volSliderBg.setWidth(sliderEndPos - sliderEndPos * presenter.getMasterVolume());
	}

	/**
	 * Create controls
	 */
	public void createControls(Resources resources) {
		Skin skin = resources.getSkin(Resources.SKIN_JSON);
		Table mainTable = new Table();
		mainTable.setBackground(skin.getDrawable("main-panel-hollow"));
		mainTable.setSize(500,360);
		mainTable.setPosition((Resources.VIRTUAL_WIDTH/2)-(mainTable.getWidth()/2), (Resources.VIRTUAL_HEIGHT/2)-(mainTable.getHeight()/2));
		//table.debug();
		this.addActor(mainTable);
		
		Label lblTitle = new Label("OPTIONS", skin);
		lblTitle.setPosition(mainTable.getX() + (mainTable.getWidth()/2) - (lblTitle.getWidth()/2)
					,mainTable.getY() + mainTable.getHeight() - lblTitle.getHeight() );
		lblTitle.setAlignment(Align.center);
		lblTitle.setFontScale(0.7f);
		this.addActor(lblTitle);
		
		//mainTable.row();

		btnResume = new TextButton("RESUME",skin);
		btnResume.getLabel().setFontScale(0.45f);
		setBtnResumeListener();
		
		btnNewGame = new TextButton("NEW GAME",skin);
		btnNewGame.getLabel().setFontScale(0.45f);
		setBtnNewGameListener();

		btnMainMenu = new TextButton("MAIN MENU",skin);
		btnMainMenu.getLabel().setFontScale(0.45f);
		setBtnMainMenuListener();
		
		btnSound = new CheckBox(" SOUND ON", skin);
		btnSound.getLabel().setFontScale(0.45f);
		btnSound.getImageCell().width(32).height(32);
		btnSound.getImage().setScaling(Scaling.stretch);
		setBtnSoundListener(btnSound);
		
		btnMusic = new CheckBox(" MUSIC ON", skin);
		btnMusic.getLabel().setFontScale(0.45f);
		btnMusic.getImageCell().width(32).height(32);
		btnMusic.getImage().setScaling(Scaling.stretch);
		setBtnMusicListener(btnMusic);
		
		btnShowRanges = new CheckBox(" SHOW RANGES", skin);
		btnShowRanges.getLabel().setFontScale(0.45f);
		btnShowRanges.getImageCell().width(32).height(32);
		btnShowRanges.getImage().setScaling(Scaling.stretch);
		setBtnShowRangesListener(btnShowRanges);
		
		Label lblVol = new Label("VOLUME", skin);
		lblVol.setFontScale(0.5f);
		
		Stack volumeStack = new Stack();
		volumeStack.setTransform(false);
		
		
		Slider volumeSlider = new Slider(0, 1f, 0.01f, false, skin);
		volumeSlider.getStyle().knob.setMinWidth(33);
		volumeSlider.getStyle().knob.setMinHeight(24);
		volumeSlider.getStyle().background.setMinHeight(22);
		volumeSlider.getStyle().background.setMinWidth(300);
		volumeSlider.setValue(presenter.getMasterVolume());
		volSliderListener(volumeSlider);
	
		
		Image volSliderFull = new Image(resources.getAtlas(Resources.SKIN_ATLAS).findRegion("slider-full"));
		volSliderFull.setSize(300, 22);
		
		volSliderBg = new Image(resources.getAtlas(Resources.SKIN_ATLAS).findRegion("slider-bg"));
		volSliderBg.setSize(300, 22);
	
		
		this.sliderStartPos = volSliderBg.getX() + 2;
		this.sliderEndPos = volSliderBg.getX() + volSliderBg.getWidth() - 6;
		
		volumeStack.add(volSliderFull);
		volumeStack.add(volSliderBg);
		volumeStack.add(volumeSlider);
		
		mainTable.add(btnResume).width(128).height(41).spaceBottom(10);
		mainTable.add(btnShowRanges).left().spaceLeft(15).spaceBottom(10);
		
		mainTable.row();
		
		mainTable.add(btnNewGame).width(128).height(41).spaceBottom(10);
		mainTable.add(btnMusic).left().spaceLeft(15).spaceBottom(10);
		
		mainTable.row();
		
		mainTable.add(btnMainMenu).width(128).height(41).spaceBottom(10);
		mainTable.add(btnSound).left().spaceLeft(15).spaceBottom(10);
		
		mainTable.row();
		
		mainTable.add(lblVol).colspan(2);
		
		mainTable.row();
		
		mainTable.add(volumeStack).colspan(2).width(300).height(18);
        
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
	
	private void setBtnShowRangesListener(Button btnShowRanges) {
		btnShowRanges.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.showRangesPressed();
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
	
	private void volSliderListener(final Slider slider){
		slider.addListener(new ClickListener(){
			@Override
			public void touchDragged (InputEvent event, float x, float y, int pointer) {
				super.touchDragged(event, x, y, pointer);
				moveSlider();
			}
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				super.touchDown(event, x, y, pointer, button);
				moveSlider();
				return true;
			}
			
			private void moveSlider(){
		        float startX = sliderStartPos + sliderEndPos * slider.getValue();
		        presenter.volumeChanged(slider.getValue());
		        volSliderBg.setX(startX);
		        volSliderBg.setWidth(sliderEndPos - sliderEndPos * slider.getValue());
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
	public void setBtnShowRangesOn(boolean showRangesOn) {
		btnShowRanges.setChecked(showRangesOn);
		
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
