package com.eric.mtd.game.ui.view;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.eric.mtd.game.ui.presenter.EnlistPresenter;
import com.eric.mtd.game.ui.view.interfaces.IEnlistView;
import com.eric.mtd.game.ui.view.widget.MTDImage;
import com.eric.mtd.game.ui.view.widget.MTDImageButton;
import com.eric.mtd.game.ui.view.widget.enlist.MTDTowerButton;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * View class for Enlisting. Shows Enlisting window as well as the options to
 * place the tower.
 * 
 * @author Eric
 *
 */
public class EnlistView extends Group implements IEnlistView, InputProcessor {
	private MTDImage pnlEnlist;
	private MTDTowerButton btnTank, btnFlameThrower, btnTurret, btnSniper, btnMachine, btnRocketLauncher, btnRifle;
	private MTDImageButton btnCancel, btnPlace, btnRotate;
	private EnlistPresenter presenter;
	private Group choosingGroup;

	public EnlistView(EnlistPresenter presenter) {
		this.presenter = presenter;
		choosingGroup = new Group();
		addActor(choosingGroup);
		createControls();
	}

	/**
	 * Creates the controls with the MTD widgets
	 */
	public void createControls() {
		pnlEnlist = new MTDImage("UI_Enlist", "panel", Resources.ENLIST_ATLAS, "background", true, false);
		pnlEnlist.getColor().set(1f, 1f, 1f, 1f);
		choosingGroup.addActor(pnlEnlist);

		btnRifle = new MTDTowerButton("UI_Enlist", "btnRifle", Resources.ENLIST_ATLAS, "rifleEnabled", "rifleDisabled", "Rifle", true, false);
		setRifleListener();
		choosingGroup.addActor(btnRifle);

		btnTank = new MTDTowerButton("UI_Enlist", "btnTank", Resources.ENLIST_ATLAS, "tankEnabled", "tankDisabled", "Tank", true, false);
		setTankListener();
		choosingGroup.addActor(btnTank);

		btnFlameThrower = new MTDTowerButton("UI_Enlist", "btnFlameThrower", Resources.ENLIST_ATLAS, "flamethrowerEnabled", "flamethrowerDisabled", "FlameThrower", true, false);
		setFlameThrowerListener();
		choosingGroup.addActor(btnFlameThrower);

		btnTurret = new MTDTowerButton("UI_Enlist", "btnTurret", Resources.ENLIST_ATLAS, "turretEnabled", "turretDisabled", "Turret", true, false);
		setTurretListener();
		choosingGroup.addActor(btnTurret);

		btnSniper = new MTDTowerButton("UI_Enlist", "btnSniper", Resources.ENLIST_ATLAS, "sniperEnabled", "sniperDisabled", "Sniper", true, false);
		setSniperListener();
		choosingGroup.addActor(btnSniper);

		btnMachine = new MTDTowerButton("UI_Enlist", "btnMachine", Resources.ENLIST_ATLAS, "machineEnabled", "machineDisabled", "MachineGun", true, false);
		setMachineListener();
		choosingGroup.addActor(btnMachine);

		btnRocketLauncher = new MTDTowerButton("UI_Enlist", "btnRocketLauncher", Resources.ENLIST_ATLAS, "rocketlauncherEnabled", "rocketlauncherDisabled", "RocketLauncher", true, false);
		setRocketLauncherListener();
		choosingGroup.addActor(btnRocketLauncher);

		btnCancel = new MTDImageButton("UI_Enlist", "btnCancel", Resources.ENLIST_ATLAS, "cancel", false, false);
		setCancelListener();
		addActor(btnCancel);
		btnPlace = new MTDImageButton("UI_Enlist", "btnPlace", Resources.ENLIST_ATLAS, "place", false, false);
		setPlaceListener();
		addActor(btnPlace);
		btnRotate = new MTDImageButton("UI_Enlist", "btnRotate", Resources.ENLIST_ATLAS, "rotate", false, true);
		setRotateListener();
		addActor(btnRotate);

	}

	/**
	 * Updates the tower buttons to disable/enable.
	 */
	private void updateTowerButtons() {
		for (Actor button : choosingGroup.getChildren()) {
			if (button instanceof MTDTowerButton) {
				if (presenter.canAffordTower(((MTDTowerButton) button).getTowerName())) {
					if (Logger.DEBUG)
						System.out.println("Setting " + ((MTDTowerButton) button).getTowerName() + " to Enabled");
					((MTDTowerButton) button).setDisabled(false);
					button.setTouchable(Touchable.enabled);
				} else {
					if (Logger.DEBUG)
						System.out.println("Setting " + ((MTDTowerButton) button).getTowerName() + " to Disabled");
					((MTDTowerButton) button).setDisabled(true);
					button.setTouchable(Touchable.disabled);
				}
			}
		}

	}

	/**
	 * Checks if the rotate button is pressed and calls to rotate the tower
	 */
	@Override
	public void act(float delta) {
		super.act(delta);
		if (btnRotate.isPressed()) {
			presenter.rotateTower();
		}
	}

	private void setRotateListener() {
		btnRotate.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				super.touchDown(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("Rotate Pressed");
				return true;
			}
		});
	}

	private void setRifleListener() {
		if (Logger.DEBUG)
			System.out.println("creating rifle listener");
		btnRifle.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.createTower("Rifle");
			}
		});

	}

	private void setTankListener() {
		btnTank.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.createTower("Tank");
			}
		});
	}

	private void setFlameThrowerListener() {
		btnFlameThrower.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("Flame Thrower Button Pressed");
				presenter.createTower("FlameThrower");
			}
		});
	}

	private void setSniperListener() {
		btnSniper.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("Sniper Button Pressed");
				presenter.createTower("Sniper");
			}
		});
	}

	private void setMachineListener() {
		btnMachine.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("Machine Button Pressed");
				presenter.createTower("MachineGun");
			}
		});
	}

	private void setRocketLauncherListener() {
		btnRocketLauncher.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("RocketLauncher Button Pressed");
				presenter.createTower("RocketLauncher");
			}
		});
	}

	private void setTurretListener() {
		btnTurret.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("Turret Button Pressed");
				presenter.createTower("Turret");
			}
		});
	}

	private void setPlaceListener() {
		btnPlace.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("Place Pressed");
				presenter.placeTower();
			}
		});
	}

	private void setCancelListener() {
		btnCancel.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("Cancel Pressed");
				presenter.cancelEnlist();
			}
		});
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector2 coords = this.getStage().screenToStageCoordinates(new Vector2(screenX, screenY));
		presenter.moveTower(coords);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector2 coords = this.getStage().screenToStageCoordinates(new Vector2(screenX, screenY));
		presenter.moveTower(coords);
		return false;

	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void enlistingState() {
		updateTowerButtons();
		choosingGroup.setVisible(true);
		btnCancel.setVisible(true);
		this.setVisible(true);
	}

	@Override
	public void placingTowerState() {
		btnCancel.setVisible(true);
		choosingGroup.setVisible(false);

	}

	@Override
	public void standByState() {
		btnCancel.setVisible(false);
		choosingGroup.setVisible(false);
		btnRotate.setVisible(false);
		btnPlace.setVisible(false);
		btnCancel.setVisible(false);
		this.setVisible(false);
	}

	@Override
	public void showBtnPlace() {
		btnPlace.setVisible(true);

	}
	@Override
	public void showBtnRotate() {
		btnRotate.setVisible(true);

	}
}
