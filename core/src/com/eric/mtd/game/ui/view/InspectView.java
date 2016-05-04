package com.eric.mtd.game.ui.view;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.eric.mtd.game.model.actor.ai.TowerTargetPriority;
import com.eric.mtd.game.model.actor.combat.tower.Tower;
import com.eric.mtd.game.ui.presenter.InspectPresenter;
import com.eric.mtd.game.ui.view.interfaces.IInspectView;
import com.eric.mtd.game.ui.view.widget.MTDImage;
import com.eric.mtd.game.ui.view.widget.MTDImageButton;
import com.eric.mtd.game.ui.view.widget.MTDLabel;
import com.eric.mtd.game.ui.view.widget.MTDTextButton;
import com.eric.mtd.game.ui.view.widget.inspect.UpgradeLevel;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * View for inspecting a tower
 * 
 * @author Eric
 *
 */
public class InspectView extends Group implements InputProcessor, IInspectView {
	private InspectPresenter presenter;
	private MTDImageButton btnArmor, btnSpeed, btnRange, btnAttack;
	private MTDLabel lblArmorCost, lblSpeedCost, lblRangeCost, lblAttackCost;
	private UpgradeLevel lvlArmor, lvlSpeed, lvlRange, lvlAttack;
	private Group grpArmor, grpSpeed, grpRange, grpAttack, grpDischarge, grpTargetPriority;
	private MTDLabel lblDischargePrice, lblTargetPriorityTitle, lblTargetPriority, lblKills;
	private MTDImage pnlInspect, pnlSideInspect, imgDischarge, iconDischarge, imgKills;
	private MTDImageButton btnDischarge, btnTargetPriority;
	private MTDTextButton btnInspectClose;

	public InspectView(InspectPresenter presenter) {
		this.presenter = presenter;
		createControls();
	}

	/**
	 * Create the controls using MTD Widgets
	 */
	public void createControls() {
		grpDischarge = new Group();
		grpTargetPriority = new Group();
		pnlInspect = new MTDImage("UI_Inspect", "bottomPanel", Resources.INSPECT_ATLAS, "inspect_bg", true, false);
		addActor(pnlInspect);
		pnlSideInspect = new MTDImage("UI_Inspect", "sidePanel", Resources.INSPECT_ATLAS, "inspect_bg", true, false);
		addActor(pnlSideInspect);

		btnDischarge = new MTDImageButton("UI_Inspect", "btnDischarge", Resources.INSPECT_ATLAS, "btnDischarge", true, false);
		setDischargeListener();
		lblDischargePrice = new MTDLabel("UI_Inspect", "lblDischargePrice", "", true, Color.WHITE, Align.left, 0.625f);
		grpDischarge.addActor(btnDischarge);
		grpDischarge.addActor(lblDischargePrice);
		addActor(grpDischarge);

		btnTargetPriority = new MTDImageButton("UI_Inspect", "btnTargetPriority", Resources.INSPECT_ATLAS, "btnTargetPriority", true, false);
		lblTargetPriority = new MTDLabel("UI_Inspect", "lblTargetPriority", TowerTargetPriority.values()[0].name(), true, Color.WHITE, Align.center, 0.5f);
		setTargetPriorityListener();
		grpTargetPriority.addActor(btnTargetPriority);
		grpTargetPriority.addActor(lblTargetPriority);
		addActor(grpTargetPriority);

		btnInspectClose = new MTDTextButton("UI_Inspect", "btnClose", "Close", Align.center, true);
		setUpgradeCloseListener();
		addActor(btnInspectClose);

		imgKills = new MTDImage("UI_Inspect", "imgKills", Resources.INSPECT_ATLAS, "kills", true, true);
		addActor(imgKills);
		lblKills = new MTDLabel("UI_Inspect", "lblKills", "0 kills", true, Color.WHITE, Align.left, 0.5f);
		addActor(lblKills);

		createUpgradeControls();
	}

	/**
	 * Create the upgrade controls that are grouped together with Groups
	 */
	private void createUpgradeControls() {
		grpArmor = new Group();
		grpSpeed = new Group();
		grpRange = new Group();
		grpAttack = new Group();

		btnArmor = new MTDImageButton("UI_Inspect", "btnArmor", Resources.INSPECT_ATLAS, "btnArmor", "btnArmorDisabled", true, false);
		lblArmorCost = new MTDLabel("UI_Inspect", "lblArmorCost", "5555", true, Color.WHITE, Align.left, 0.58f);
		lvlArmor = new UpgradeLevel("UI_Inspect", "lblArmorLevel", "", true, 1, Color.WHITE);
		setArmorListener();
		grpArmor.addActor(btnArmor);
		grpArmor.addActor(lblArmorCost);
		grpArmor.addActor(lvlArmor);

		btnSpeed = new MTDImageButton("UI_Inspect", "btnSpeed", Resources.INSPECT_ATLAS, "btnSpeed", "btnSpeedDisabled", true, false);
		lblSpeedCost = new MTDLabel("UI_Inspect", "lblSpeedCost", "6555", true, Color.WHITE, Align.left, 0.58f);
		lvlSpeed = new UpgradeLevel("UI_Inspect", "lblSpeedLevel", "", true, 2, Color.WHITE);
		setIncreaseSpeedListener();
		grpSpeed.addActor(btnSpeed);
		grpSpeed.addActor(lblSpeedCost);
		grpSpeed.addActor(lvlSpeed);

		btnRange = new MTDImageButton("UI_Inspect", "btnRange", Resources.INSPECT_ATLAS, "btnRange", "btnRangeDisabled", true, false);
		lblRangeCost = new MTDLabel("UI_Inspect", "lblRangeCost", "8888", true, Color.WHITE, Align.left, 0.58f);
		lvlRange = new UpgradeLevel("UI_Inspect", "lblRangeLevel", "", true, 2, Color.WHITE);
		setIncreaseRangeListener();
		grpRange.addActor(btnRange);
		grpRange.addActor(lblRangeCost);
		grpRange.addActor(lvlRange);

		btnAttack = new MTDImageButton("UI_Inspect", "btnAttack", Resources.INSPECT_ATLAS, "btnAttack", "btnAttackDisabled", true, false);
		lblAttackCost = new MTDLabel("UI_Inspect", "lblAttackCost", "4789", true, Color.WHITE, Align.left, 0.58f);
		lvlAttack = new UpgradeLevel("UI_Inspect", "lblAttackLevel", "", true, 2, Color.WHITE);
		setIncreaseAttackListener();
		grpAttack.addActor(btnAttack);
		grpAttack.addActor(lblAttackCost);
		grpAttack.addActor(lvlAttack);

		addActor(grpArmor);
		addActor(grpSpeed);
		addActor(grpRange);
		addActor(grpAttack);
	}

	@Override
	public void standByState() {
		this.setVisible(false);
	}

	@Override
	public void inspectingState() {
		this.setVisible(true);
	}
	
	@Override
	public void dischargeEnabled(boolean enabled) {
		if (enabled){
			grpDischarge.setTouchable(Touchable.enabled);
			btnDischarge.setDisabled(false);
			
		} else {
			grpDischarge.setTouchable(Touchable.disabled);
			btnDischarge.setDisabled(true);
			
		}
		
	}
	/**
	 * Binds to the Inspected Tower and updates the widgets
	 */
	@Override
	public void update(Tower selectedTower) {
		lblArmorCost.setText(String.valueOf(selectedTower.getArmorCost()));
		lblSpeedCost.setText(String.valueOf(selectedTower.getSpeedIncreaseCost()));
		lblRangeCost.setText(String.valueOf(selectedTower.getRangeIncreaseCost()));
		lblAttackCost.setText(String.valueOf(selectedTower.getAttackIncreaseCost()));
		lblDischargePrice.setText(String.valueOf(selectedTower.getSellCost()));
		lblKills.setText(String.valueOf(selectedTower.getNumOfKills()));
		lvlArmor.resetLevels();
		lvlSpeed.resetLevels();
		lvlRange.resetLevels();
		lvlAttack.resetLevels();

		if (Logger.DEBUG) {
			System.out.println("Armor: " + selectedTower.hasArmor());
			System.out.println("Speed: " + selectedTower.getSpeedLevel());
			System.out.println("Range: " + selectedTower.getRangeLevel());
			System.out.println("Attack: " + selectedTower.getAttackLevel());
		}
		if (selectedTower.hasArmor()) {
			lvlArmor.setLevel(1);
		}
		lvlSpeed.setLevel(selectedTower.getSpeedLevel());
		lvlRange.setLevel(selectedTower.getRangeLevel());
		lvlAttack.setLevel(selectedTower.getAttackLevel());

		lblTargetPriority.setText(selectedTower.getTargetPriority().name());

		updateUpgradeControls(selectedTower.getRangeIncreaseCost(), grpRange, btnRange, lblRangeCost);
		updateUpgradeControls(selectedTower.getSpeedIncreaseCost(), grpSpeed, btnSpeed, lblSpeedCost);
		updateUpgradeControls(selectedTower.getAttackIncreaseCost(), grpAttack, btnAttack, lblAttackCost);
		updateUpgradeControls(selectedTower.getArmorCost(), grpArmor, btnArmor, lblArmorCost);
	}

	/**
	 * Updates the upgrade controlls
	 * 
	 * @param towerCost
	 * @param group
	 * @param button
	 * @param label
	 */
	private void updateUpgradeControls(int towerCost, Group group, MTDImageButton button, MTDLabel label) {
		if (presenter.canAffordUpgrade(towerCost)) {
			group.setTouchable(Touchable.enabled);
			button.setDisabled(false);
			label.getStyle().fontColor = Color.WHITE;
		} else {
			group.setTouchable(Touchable.disabled);
			button.setDisabled(true);
			label.getStyle().fontColor = Color.RED;
		}

	}

	private void setUpgradeCloseListener() {
		btnInspectClose.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.closeInspect();
			}
		});
	}

	private void setTargetPriorityListener() {
		grpTargetPriority.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.changeTargetPriority();
			}
		});
	}

	private void setIncreaseAttackListener() {
		grpAttack.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.increaseAttack();
			}
		});
	}

	private void setArmorListener() {
		grpArmor.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.giveArmor();
			}
		});
	}

	private void setIncreaseRangeListener() {
		grpRange.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.increaseRange();
			}
		});
	}

	private void setIncreaseSpeedListener() {
		grpSpeed.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.increaseSpeed();
			}
		});
	}

	private void setDischargeListener() {
		grpDischarge.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.dishcharge();
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
		presenter.inspectTower(coords);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
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



}
