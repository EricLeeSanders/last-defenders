package com.eric.mtd.game.ui.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.eric.mtd.game.ui.presenter.HUDPresenter;
import com.eric.mtd.game.ui.view.interfaces.IHUDView;
import com.eric.mtd.game.ui.view.widget.*;
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
	
	private MTDImageButton btnEnlist, btnSupport, btnQuit;//, btnOptions;
	private ImageButton btnSpeed, btnWave;
	private MTDLabelOld lblMoney, lblLives, lblWaveCount;
	private HUDPresenter presenter;
	private ImageButton btnOptions;

	public HUDView(HUDPresenter presenter) {
		this.presenter = presenter;
		createControls();
	}

	/**
	 * Create the controls using MTD Widgets.
	 */
	public void createControls() {
		Skin skin = Resources.getSkin(Resources.SKIN_JSON);
		btnWave = new MTDImageButton("UI_HUD", "btnWave", skin, "wave", true, false);
		setBtnWaveListener();
		addActor(btnWave);

		btnSpeed = new MTDImageButton("UI_HUD", "btnSpeed", skin, "speed", true, false);
		setBtnSpeedListener();
		btnSpeed.setChecked(false);
		addActor(btnSpeed);
		
		btnEnlist = new MTDImageButton("UI_HUD", "btnEnlist", skin, "enlist_hud", true, false);
		setBtnEnlistListener();
		addActor(btnEnlist);

		btnSupport = new MTDImageButton("UI_HUD", "btnSupport", skin, "support_hud", true, false);
		setBtnSupportListener();
		addActor(btnSupport);
		
		//btnOptions = new MTDImageButton("UI_HUD", "btnOptions", skin, "options", true, false);
		btnOptions = new ImageButton(skin, "options");
		btnOptions.setSize(50, 50);
		btnOptions.setPosition(Resources.VIRTUAL_WIDTH - 60, Resources.VIRTUAL_HEIGHT - 60);
		setBtnOptionsListener();
		addActor(btnOptions);
		imgMoney = new MTDImage("UI_HUD", "imgMoney", skin, "money", true, false);
		addActor(imgMoney);
		lblMoney = new MTDLabelOld("UI_HUD", "lblMoney", "0", true, Color.WHITE, Align.left, Resources.getFont("default-font-22"));
		addActor(lblMoney);
		imgLife = new MTDImage("UI_HUD", "imgLife", skin, "lives", true, false);
		addActor(imgLife);
		lblLives = new MTDLabelOld("UI_HUD", "lblLife", "0", true, Color.WHITE, Align.left, Resources.getFont("default-font-22"));
		addActor(lblLives);
		lblWaveCount = new MTDLabelOld("UI_HUD", "lblWaveCount", "0", true, Color.WHITE, Align.left, Resources.getFont("default-font-22"));
		addActor(lblWaveCount);
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
		btnWave.setVisible(true);
		btnEnlist.setVisible(true);
		btnSupport.setVisible(true);
		btnOptions.setVisible(true);
		btnSpeed.setVisible(true);
	}
	@Override
	public void supportState() {
		btnEnlist.setVisible(false);
		btnSupport.setVisible(false);
		btnWave.setVisible(false);
		btnOptions.setVisible(false);
		btnSpeed.setVisible(false);
	}
	
	@Override
	public void enlistingState() {
		btnEnlist.setVisible(false);
		btnSupport.setVisible(false);
		btnWave.setVisible(false);
		btnOptions.setVisible(false);
		btnSpeed.setVisible(false);
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
