package com.foxholedefense.game.ui.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.foxholedefense.game.ui.presenter.HUDPresenter;
import com.foxholedefense.game.ui.view.interfaces.IHUDView;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

/**
 * View for the Game HUD
 * 
 * @author Eric
 *
 */
public class HUDView extends Group implements IHUDView {

	private Image imgMoney, imgLife;
	private ImageButton btnWave, btnEnlist, btnSupport, btnOptions, btnPause, btnResume;
	private Label lblMoney, lblLives, lblWaveCount;
	private HUDPresenter presenter;
	private Resources resources;
	private Label messageLabel;

	public HUDView(HUDPresenter presenter,Skin skin, Resources resources) {
		this.presenter = presenter;
		this.setTransform(false);
		this.resources = resources;
		createControls(skin);
		createMessageDisplayLabel(skin);
	}

	private void createMessageDisplayLabel(Skin skin){
		LabelStyle messageDisplayLabelStyle = new Label.LabelStyle(skin.get(LabelStyle.class));
		messageDisplayLabelStyle.fontColor = Color.RED;

		messageLabel = new Label("", resources.getSkin());
		messageLabel.setStyle(messageDisplayLabelStyle);
	}

	/**
	 * Create the controls
	 */
	private void createControls(Skin skin) {
		Logger.info("HUD View: creating controls");

		btnWave = new ImageButton(skin, "wave");
		btnWave.setSize(64, 64);
		btnWave.getImageCell().size(50,16);
		btnWave.getImage().setScaling(Scaling.stretch);
		btnWave.setPosition(10,10);
		setBtnWaveListener();
		addActor(btnWave);

		btnPause = new ImageButton(skin, "pause");
		btnPause.setSize(50,50);
		btnPause.getImageCell().size(17,23);
		btnPause.getImage().setScaling(Scaling.stretch);
		btnPause.setPosition(10, 10);
		setBtnPauseListener();
		addActor(btnPause);

		btnResume = new ImageButton(skin, "resume");
		btnResume.setSize(50,50);
		btnResume.getImageCell().size(27,30);
		btnResume.getImage().setScaling(Scaling.stretch);
		btnResume.setPosition(10, 10);
		setBtnResumeListener();
		addActor(btnResume);

		btnEnlist = new ImageButton(skin, "enlist_hud");
		btnEnlist.setSize(50, 50);
		btnEnlist.getImageCell().size(32,35);
		btnEnlist.getImage().setScaling(Scaling.stretch);
		btnEnlist.setPosition(Resources.VIRTUAL_WIDTH - 60, 10);
		setBtnEnlistListener();
		addActor(btnEnlist);

		btnSupport = new ImageButton(skin, "support_hud");
		btnSupport.setSize(50, 50);
		btnSupport.getImageCell().size(23,35);
		btnSupport.getImage().setScaling(Scaling.stretch);
		btnSupport.setPosition(Resources.VIRTUAL_WIDTH - 60, btnEnlist.getY() + 60);
		setBtnSupportListener();
		addActor(btnSupport);
		

		btnOptions = new ImageButton(skin, "options");
		btnOptions.setSize(50, 50);
		btnOptions.getImageCell().size(25,26);
		btnOptions.getImage().setScaling(Scaling.stretch);
		btnOptions.setPosition(Resources.VIRTUAL_WIDTH - 60, Resources.VIRTUAL_HEIGHT - 60);
		setBtnOptionsListener();
		addActor(btnOptions);
		
		Table statsTable = new Table();
		statsTable.setTransform(false);
		statsTable.setFillParent(true);
		addActor(statsTable);
		
		imgLife = new Image(skin.getAtlas().findRegion("heart"));
		statsTable.add(imgLife).size(32,32).padRight(3);
		lblLives = new Label("0", skin);
		lblLives.setAlignment(Align.left);
		lblLives.setFontScale(0.5f);
		statsTable.add(lblLives).size(30,19).spaceRight(10);
		
		imgMoney = new Image(skin.getAtlas().findRegion("money"));
		statsTable.add(imgMoney).size(32,32).padRight(3);
		
		lblMoney = new Label("0", skin);
		lblMoney.setAlignment(Align.left);
		lblMoney.setFontScale(0.5f);
		statsTable.add(lblMoney).size(72,19);
		
		statsTable.row();
		
		lblWaveCount = new Label("0", skin);
		lblWaveCount.setAlignment(Align.left);
		lblWaveCount.setFontScale(0.5f);
		statsTable.add(lblWaveCount).size(145,19).colspan(4).padTop(5);
		statsTable.top().left();
		this.setSize(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT);

		Logger.info("HUD View: controls created");
	}

	private void setBtnResumeListener() {
		btnResume.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.resume();
				btnResume.setVisible(false);
				btnPause.setVisible(true);
			}
		});
	}

	private void setBtnPauseListener() {
		btnPause.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.pause();
				btnResume.setVisible(true);
				btnPause.setVisible(false);
			}
		});
	}

	private void setBtnOptionsListener() {
		btnOptions.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.options();
			}
		});
	}

	private void setBtnWaveListener() {
		btnWave.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.startWave();
			}
		});
	}

	private void setBtnEnlistListener() {
		btnEnlist.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.enlist();

			}
		});
	}
	
	private void setBtnSupportListener() {
		btnSupport.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.addSupport();

			}
		});
	}
	private void setNormalButtonsVisible(boolean visible){
		btnEnlist.setVisible(visible);
		btnSupport.setVisible(visible);
		btnWave.setVisible(visible);
		btnOptions.setVisible(visible);
	}

	private void setPauseResumeButtonsVisible(boolean visible){
		btnResume.setVisible(visible);
		btnPause.setVisible(visible);
	}
	@Override
	public void setMoney(String money) {
		lblMoney.setText(money);
	}

	@Override
	public void setLives(String lives) {
		lblLives.setText(lives);
	}

	@Override
	public void setWaveCount(String waveCount) {
		lblWaveCount.setText("WAVE: " + waveCount);
	}

	@Override
	public void standByState() {
		btnEnlist.setTouchable(Touchable.enabled);
		btnSupport.setTouchable(Touchable.enabled);
		btnWave.setTouchable(Touchable.enabled);
		btnOptions.setTouchable(Touchable.enabled);
		btnPause.setVisible(false);
		btnResume.setVisible(false);
		setNormalButtonsVisible(true);
	}
	@Override
	public void supportState() {
		setNormalButtonsVisible(false);
		setPauseResumeButtonsVisible(false);
	}
	
	@Override
	public void enlistingState() {
		setNormalButtonsVisible(false);
		setPauseResumeButtonsVisible(false);
	}
	
	@Override
	public void inspectingState() {
		setNormalButtonsVisible(false);
		setPauseResumeButtonsVisible(false);
	}

	@Override
	public void quitState() {
		setNormalButtonsVisible(false);
		setPauseResumeButtonsVisible(false);
	}

	@Override
	public void optionsState() {
		btnEnlist.setTouchable(Touchable.disabled);
		btnSupport.setTouchable(Touchable.disabled);
		btnWave.setTouchable(Touchable.disabled);
		btnPause.setTouchable(Touchable.disabled);
		btnResume.setTouchable(Touchable.disabled);

	}

	@Override
	public void gameOverState() {
		btnEnlist.setTouchable(Touchable.disabled);
		btnSupport.setTouchable(Touchable.disabled);
		btnWave.setTouchable(Touchable.disabled);
		btnOptions.setTouchable(Touchable.disabled);
		btnPause.setTouchable(Touchable.disabled);
		btnResume.setTouchable(Touchable.disabled);
	}

	@Override
	public void waveInProgressState() {
		btnEnlist.setTouchable(Touchable.enabled);
		btnSupport.setTouchable(Touchable.enabled);
		btnWave.setTouchable(Touchable.enabled);
		btnOptions.setTouchable(Touchable.enabled);
		btnPause.setTouchable(Touchable.enabled);
		btnResume.setTouchable(Touchable.enabled);
		btnWave.setVisible(false);
		btnEnlist.setVisible(true);
		btnSupport.setVisible(true);
		btnOptions.setVisible(true);
		btnPause.setVisible(!presenter.isGamePaused());
		btnResume.setVisible(presenter.isGamePaused());

	}
}
