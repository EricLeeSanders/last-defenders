package com.eric.mtd.game.ui.view;

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
import com.eric.mtd.game.ui.presenter.HUDPresenter;
import com.eric.mtd.game.ui.view.interfaces.IHUDView;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * View for the Game HUD
 * 
 * @author Eric
 *
 */
public class HUDView extends Group implements IHUDView {
	private Image imgMoney, imgLife;
	
	private ImageButton btnSpeed, btnWave, btnEnlist, btnSupport, btnQuit, btnOptions;
	private Label lblMoney, lblLives, lblWaveCount;
	private HUDPresenter presenter;

	public HUDView(HUDPresenter presenter) {
		this.presenter = presenter;
		createControls();
	}

	/**
	 * Create the controls using MTD Widgets.
	 */
	public void createControls() {
		Skin skin = Resources.getSkin(Resources.SKIN_JSON);
		btnSpeed = new ImageButton(skin, "speed");
		btnSpeed.setSize(50, 50);
		btnSpeed.setPosition(10,10);
		btnSpeed.setChecked(false);
		setBtnSpeedListener();
		addActor(btnSpeed);
		
	
		btnWave = new ImageButton(skin, "wave");
		btnWave.setSize(50, 50);
		btnWave.setPosition(btnSpeed.getX() + 60, btnSpeed.getY());
		setBtnWaveListener();
		addActor(btnWave);

		btnEnlist = new ImageButton(skin, "enlist_hud");
		btnEnlist.setSize(50, 50);
		btnEnlist.setPosition(Resources.VIRTUAL_WIDTH - 60, 10);
		setBtnEnlistListener();
		addActor(btnEnlist);

		btnSupport = new ImageButton(skin, "support_hud");
		btnSupport.setSize(50, 50);
		btnSupport.setPosition(Resources.VIRTUAL_WIDTH - 60, btnEnlist.getY() + 60);
		setBtnSupportListener();
		addActor(btnSupport);
		

		btnOptions = new ImageButton(skin, "options");
		btnOptions.setSize(50, 50);
		btnOptions.setPosition(Resources.VIRTUAL_WIDTH - 60, Resources.VIRTUAL_HEIGHT - 60);
		setBtnOptionsListener();
		addActor(btnOptions);
		
		Table statsTable = new Table();
		statsTable.setFillParent(true);
		addActor(statsTable);
		
		imgLife = new Image(skin, "lives");
		statsTable.add(imgLife).size(32,32).padRight(3);

		lblLives = new Label("0", skin);
		lblLives.setAlignment(Align.left);
		lblLives.setFontScale(0.5f);
		statsTable.add(lblLives).size(30,19).spaceRight(10);
		
		imgMoney = new Image(skin, "money");
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
	}

	private void setBtnSpeedListener() {
		btnSpeed.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("Speed button pressed");
				presenter.changeGameSpeed();
			}
		});
	}


	private void setBtnOptionsListener() {
		btnOptions.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("Button Options Pressed");
				presenter.options();
			}
		});
	}

	private void setBtnWaveListener() {
		btnWave.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("Button Wave Pressed");
				presenter.startWave();
			}
		});
	}

	private void setBtnEnlistListener() {
		btnEnlist.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("Button Enlist Pressed");
				presenter.enlist();

			}
		});
	}
	
	private void setBtnSupportListener() {
		btnSupport.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("Button Support Pressed");
				presenter.support();

			}
		});
	}
	private void setButtonsVisible(boolean visible){
		btnEnlist.setVisible(visible);
		btnSupport.setVisible(visible);
		btnWave.setVisible(visible);
		btnOptions.setVisible(visible);
		btnSpeed.setVisible(visible);
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
		lblWaveCount.setText("Wave: " + waveCount);
	}

	/**
	 * Change which speed button is showing
	 */
	@Override
	public void changeSpeed(boolean isDoubleSpeed) {
		btnSpeed.setChecked(isDoubleSpeed);
	}

	@Override
	public void standByState() {
		btnEnlist.setTouchable(Touchable.enabled);
		btnSupport.setTouchable(Touchable.enabled);
		btnWave.setTouchable(Touchable.enabled);
		btnSpeed.setTouchable(Touchable.enabled);
		btnOptions.setTouchable(Touchable.enabled);
		setButtonsVisible(true);
	}
	@Override
	public void supportState() {
		setButtonsVisible(false);
	}
	
	@Override
	public void enlistingState() {
		setButtonsVisible(false);
	}
	
	@Override
	public void inspectingState() {
		setButtonsVisible(false);
	}

	@Override
	public void optionsState() {
		btnEnlist.setTouchable(Touchable.disabled);
		btnSupport.setTouchable(Touchable.disabled);
		btnWave.setTouchable(Touchable.disabled);
		btnSpeed.setTouchable(Touchable.disabled);
		//btnOptions.setTouchable(Touchable.disabled);
	}

	@Override
	public void gameOverState() {
		optionsState();
	}

	@Override
	public void waveInProgressState() {
		btnEnlist.setTouchable(Touchable.enabled);
		btnSupport.setTouchable(Touchable.enabled);
		btnWave.setTouchable(Touchable.enabled);
		btnSpeed.setTouchable(Touchable.enabled);
		btnOptions.setTouchable(Touchable.enabled);
		btnWave.setVisible(false);
		btnEnlist.setVisible(true);
		btnSupport.setVisible(true);
		btnOptions.setVisible(true);
		btnSpeed.setVisible(true);
	}
}
