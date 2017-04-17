package com.foxholedefense.menu.ui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

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
	private CheckBox chkBoxSound, chkBoxMusic;
	private Image volSliderBg;
	private float sliderEndPos, sliderStartPos;
	private Table optionsTable;
	private Label lblOptionsTitle;
	public MenuView(MenuPresenter presenter, Resources resources) {
		this.presenter = presenter;
		this.setTransform(false);
		createControls(resources);
		createOptionControls(resources);
		optionsTable.setVisible(false);
		lblOptionsTitle.setVisible(false);
	}

	public void act(float delta){
		super.act(delta);
		//This is a bit of a hack, but I need this here for the initial load of the screen.
        float startX = sliderStartPos + sliderEndPos * presenter.getMasterVolume();
        volSliderBg.setX(startX);
        volSliderBg.setWidth(sliderEndPos - sliderEndPos * presenter.getMasterVolume());
	}
	
	public void createControls(Resources resources) {
		Logger.info("Menu view: initializing view");

		Skin skin = resources.getSkin();

		Label lblTitle = new Label("FOXHOLE DEFENSE", skin);
		float lblTitleX = ActorUtil.calcXBotLeftFromCenter(Resources.VIRTUAL_WIDTH / 2, lblTitle.getWidth());
		float lblTitleY = ActorUtil.calcYBotLeftFromCenter(Resources.VIRTUAL_HEIGHT / 2, lblTitle.getHeight()) + 110;
		lblTitle.setPosition(lblTitleX, lblTitleY);
		addActor(lblTitle);

		TextButton btnPlay = new TextButton("PLAY", skin, "transparent");
		btnPlay.setSize(126, 56);
		float btnPlayX = ActorUtil.calcXBotLeftFromCenter(Resources.VIRTUAL_WIDTH / 2, btnPlay.getWidth());
		float btnPlayY = ActorUtil.calcYBotLeftFromCenter(Resources.VIRTUAL_HEIGHT / 2, btnPlay.getHeight());
		btnPlay.setPosition(btnPlayX, btnPlayY);
		addActor(btnPlay);
		setBtnPlayListener(btnPlay);
		
		btnSound = new ImageButton(skin, "sound");
		btnSound.setSize(64, 64);
		btnSound.getImageCell().size(34, 32);
		btnSound.setPosition(175,22);
		addActor(btnSound);
		setBtnSoundListener(btnSound);
		
		
		btnMusic = new ImageButton(skin, "music");
		btnMusic.setSize(64, 64);
		btnMusic.getImageCell().size(36, 36);
		btnMusic.setPosition(250, 22);
		addActor(btnMusic);
		setBtnMusicListener(btnMusic);
		
		ImageButton btnOptions = new ImageButton(skin, "options");
		btnOptions.setSize(64, 64);
		btnOptions.getImageCell().size(34, 35);
		btnOptions.setPosition(325, 22);
		addActor(btnOptions);
		setBtnOptionsListener(btnOptions);

		ImageButton btnPlayServices = new ImageButton(skin, "games");
		btnPlayServices.setSize(64, 64);
		btnPlayServices.getImageCell().size(36, 33);
		btnPlayServices.setPosition(400, 22);
		addActor(btnPlayServices);

		Logger.info("Menu view: view initialized");
	}
	public void createOptionControls(Resources resources) {
		Logger.info("Menu view: creating option controls");
		Skin skin = resources.getSkin();
		
		optionsTable = new Table();
		optionsTable.setTransform(false);
		optionsTable.setBackground(skin.getDrawable("main-panel"));
		optionsTable.setSize(500,300);
		optionsTable.setPosition((Resources.VIRTUAL_WIDTH/2)-(optionsTable.getWidth()/2), (Resources.VIRTUAL_HEIGHT/2)-(optionsTable.getHeight()/2));
		//table.debug();
		this.addActor(optionsTable);
		
		lblOptionsTitle = new Label("Options", skin);
		lblOptionsTitle.setPosition(optionsTable.getX() + (optionsTable.getWidth()/2) - (lblOptionsTitle.getWidth()/2)
					,optionsTable.getY() + optionsTable.getHeight() - lblOptionsTitle.getHeight() );
		lblOptionsTitle.setAlignment(Align.center);
		lblOptionsTitle.setFontScale(0.7f);
		this.addActor(lblOptionsTitle);
	
		chkBoxSound = new CheckBox(" Sound On", skin);
		chkBoxSound.getLabel().setFontScale(0.45f);
		chkBoxSound.getImageCell().width(32).height(32);
		chkBoxSound.getImage().setScaling(Scaling.stretch);
		
		chkBoxMusic = new CheckBox(" Music On", skin);
		chkBoxMusic.getLabel().setFontScale(0.45f);
		chkBoxMusic.getImageCell().width(32).height(32);
		chkBoxMusic.getImage().setScaling(Scaling.stretch);
		
		Label lblVol = new Label("Volume", skin);
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
	
		
		Image volSliderFull = new Image(resources.getAsset(Resources.SKIN_ATLAS, TextureAtlas.class).findRegion("slider-full"));
		volSliderFull.setSize(300, 22);
		
		volSliderBg = new Image(resources.getAsset(Resources.SKIN_ATLAS, TextureAtlas.class).findRegion("slider-bg"));
		volSliderBg.setSize(300, 22);
	
		
		this.sliderStartPos = volSliderBg.getX();
		this.sliderEndPos = volSliderBg.getX() + volSliderBg.getWidth() - 4;
		
		volumeStack.add(volSliderFull);
		volumeStack.add(volSliderBg);
		volumeStack.add(volumeSlider);
		
		
		optionsTable.add(chkBoxMusic).left().spaceLeft(15).spaceBottom(10);
		optionsTable.row();
		optionsTable.add(chkBoxSound).left().spaceLeft(15).spaceBottom(10);
		optionsTable.row();
		optionsTable.add(lblVol).colspan(2);
		optionsTable.row();
		optionsTable.add(volumeStack).colspan(2).width(300).height(18);

		Logger.info("Menu view: option controls created");
	}
	
	public void setBackground(TextureAtlas menuAtlas){
		Image background = new Image(menuAtlas.findRegion("main-menu-screen"));
		background.setFillParent(true);
		this.getStage().addActor(background);
		background.setZIndex(0);
	}
	
	
	private void volSliderListener(final Slider slider){
		slider.addListener(new ClickListener(){
			@Override
			public void touchDragged (InputEvent event, float x, float y, int pointer) {
				super.touchDragged(event, x, y, pointer);
				moveSlider();
			}
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
	
	private void setBtnOptionsListener(Button btnOptions ) {
		btnOptions.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				optionsTable.setVisible(true);
				lblOptionsTitle.setVisible(true);
			}
		});

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
