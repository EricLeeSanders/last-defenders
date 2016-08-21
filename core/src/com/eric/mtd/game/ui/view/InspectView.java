package com.eric.mtd.game.ui.view;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.eric.mtd.game.model.actor.ai.TowerTargetPriority;
import com.eric.mtd.game.model.actor.combat.tower.Tower;
import com.eric.mtd.game.ui.presenter.InspectPresenter;
import com.eric.mtd.game.ui.view.interfaces.IInspectView;
import com.eric.mtd.game.ui.view.widget.MTDImage;
import com.eric.mtd.game.ui.view.widget.MTDImageButton;
import com.eric.mtd.game.ui.view.widget.MTDLabel;
import com.eric.mtd.game.ui.view.widget.MTDLabelOld;
import com.eric.mtd.game.ui.view.widget.MTDTextButton;
import com.eric.mtd.game.ui.view.widget.inspect.UpgradeLevel;
import com.eric.mtd.util.ActorUtil;
import com.eric.mtd.util.Dimension;
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
	private ImageButton btnChangeTarget, btnDischarge, btnArmor, btnSpeed, btnRange, btnAttack, btnCancel;
	private MTDLabelOld lblArmorCost, lblSpeedCost, lblRangeCost, lblAttackCost;
	private UpgradeLevel lvlArmor, lvlSpeed, lvlRange, lvlAttack;
	private Group grpArmor, grpSpeed, grpRange, grpAttack, grpDischarge;
	private Group grpTargetPriority = new Group();
	private Label lblTargetPriority, lblTitle, lblMoney_img, lblKills_img, lblDischargeMoney_img, lblDischarge;
	MTDLabel lblMoney;
	MTDLabel lblKills;
	private Image background;
	public InspectView(InspectPresenter presenter) {
		this.presenter = presenter;
		createControls();
	}

	/**
	 * Create the controls using MTD Widgets
	 */
	public void createControls() {
		
		
		Skin skin = Resources.getSkin(Resources.SKIN_JSON);
		background = new Image(skin.getDrawable("main-panel-horz-padded"));
		background.setSize(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT);
		addActor(background);
		
		LabelStyle lblTitleStyle = new LabelStyle();
		lblTitleStyle.font = Resources.getFont("default-font-22");
		lblTitle = new Label("INSPECT", lblTitleStyle);
		lblTitle.setPosition((background.getWidth()/2) - (lblTitle.getWidth()/2)
					,background.getY() + background.getHeight() - lblTitle.getHeight() - 20);
		lblTitle.setAlignment(Align.center);
		addActor(lblTitle);
		
		lblMoney = new MTDLabel("0", skin, "money_label_img", "default-font-22", Align.left);
		lblMoney.getLabel_img().setSize(160, 69);
		lblMoney.getLabel_img().setPosition(background.getX() + 208 - (lblMoney.getLabel_img().getWidth()/2)
				,background.getY() + 37 - lblMoney.getLabel_img().getHeight()/2);
		lblMoney.getLabel_text().setSize(100, 59);
		lblMoney.getLabel_text().setPosition(lblMoney.getLabel_img().getX()+60, lblMoney.getLabel_img().getY()+3);
		addActor(lblMoney);
			
		lblKills = new MTDLabel("0", skin, "kills_label_img", "default-font-22", Align.right);
		lblKills.getLabel_img().setSize(160, 69);
		lblKills.getLabel_img().setPosition(background.getX() + background.getWidth() - 208 - (lblKills.getLabel_img().getWidth()/2) 
				, background.getY() + 37 - lblKills.getLabel_img().getHeight()/2);
		lblKills.getLabel_text().setSize(100, 59);
		lblKills.getLabel_text().setPosition(lblKills.getLabel_img().getX(), lblKills.getLabel_img().getY()+3);
		addActor(lblKills);
		
		btnCancel = new ImageButton(skin,"cancel");
		setCancelListener();
		btnCancel.setSize(64, 64);
		btnCancel.setPosition(Resources.VIRTUAL_WIDTH - 120, 28);
		addActor(btnCancel);
		/*------------------------------*/
		/*grpDischarge = new Group();
		grpTargetPriority = new Group();
		pnlInspect = new MTDImage("UI_Inspect", "bottomPanel", Resources.INSPECT_ATLAS, "inspect_bg", true, false);
		addActor(pnlInspect);
		pnlSideInspect = new MTDImage("UI_Inspect", "sidePanel", Resources.INSPECT_ATLAS, "inspect_bg", true, false);
		addActor(pnlSideInspect);

		btnDischarge = new MTDImageButton("UI_Inspect", "btnDischarge", Resources.INSPECT_ATLAS, "btnDischarge", true, false);
		setDischargeListener();
		lblDischargePrice = new MTDLabel("UI_Inspect", "lblDischargePrice", "", true, Color.WHITE, Align.left, Resources.getFont("default-font-22"));
		grpDischarge.addActor(btnDischarge);
		grpDischarge.addActor(lblDischargePrice);
		addActor(grpDischarge);

		btnTargetPriority = new MTDImageButton("UI_Inspect", "btnTargetPriority", Resources.INSPECT_ATLAS, "btnTargetPriority", true, false);
		lblTargetPriority = new MTDLabel("UI_Inspect", "lblTargetPriority", TowerTargetPriority.values()[0].name(), true, Color.WHITE, Align.center, Resources.getFont("default-font-22"));
		setTargetPriorityListener();
		grpTargetPriority.addActor(btnTargetPriority);
		grpTargetPriority.addActor(lblTargetPriority);
		addActor(grpTargetPriority);

		btnInspectClose = new MTDTextButton("UI_Inspect", "btnClose", "Close", Align.center, 0.45f, true);
		setUpgradeCloseListener();
		addActor(btnInspectClose);
*/
		Table upgradeTable = new Table();
		
		
		btnArmor = new ImageButton(skin, "upgrade");
		upgradeTable.add(btnArmor).size(90,106).spaceBottom(10);
		setArmorListener();
		btnRange = new ImageButton(skin, "upgrade");
		upgradeTable.add(btnRange).size(90,106).spaceBottom(10);
		setIncreaseRangeListener();

		btnSpeed = new ImageButton(skin, "upgrade");
		upgradeTable.add(btnSpeed).size(90,106).spaceBottom(5);
		setIncreaseSpeedListener();
		btnAttack = new ImageButton(skin, "upgrade");
		upgradeTable.add(btnAttack).size(90,106).spaceBottom(5);
		setIncreaseAttackListener();
		
		
		Vector2 bgCenter = ActorUtil.calcCenterFromBotLeft(background);
		upgradeTable.setPosition(bgCenter.x, bgCenter.y+30);
		addActor(upgradeTable);
		
		LabelStyle lblTargetStyle = new LabelStyle(skin.get("label_normal", LabelStyle.class));
		lblTargetStyle.font = Resources.getFont("default-font-16");
		lblTargetPriority = new Label("First", lblTargetStyle);
		lblTargetPriority.setAlignment(Align.center);
		lblTargetPriority.setSize(140, 41);
		lblTargetPriority.setPosition(140,85);
		grpTargetPriority.addActor(lblTargetPriority);
		
		btnChangeTarget = new ImageButton(skin, "arrow_right");
		btnChangeTarget.setSize(50, 50);
		btnChangeTarget.setPosition(lblTargetPriority.getX() + lblTargetPriority.getWidth() - 20, 81);
		grpTargetPriority.addActor(btnChangeTarget);
			
		btnDischarge = new ImageButton(skin, "discharge");
		btnDischarge.setSize(90, 106);
		btnDischarge.setPosition(btnChangeTarget.getX() + btnChangeTarget.getWidth() + 40, 77);
		addActor(btnDischarge);
		
		addActor(grpTargetPriority);
		setTargetPriorityListener();
	}

	/**
	 * Create the upgrade controls that are grouped together with Groups
	 */

	private void createUpgradeControls(Skin skin) {	
		//addActor(btnArmor);
		/*grpArmor = new Group();
		grpSpeed = new Group();
		grpRange = new Group();
		grpAttack = new Group();

		btnArmor = new MTDImageButton("UI_Inspect", "btnArmor", Resources.INSPECT_ATLAS, "btnArmor", "btnArmorDisabled", true, false);
		lblArmorCost = new MTDLabel("UI_Inspect", "lblArmorCost", "5555", true, Color.WHITE, Align.left, Resources.getFont("default-font-22"));
		lvlArmor = new UpgradeLevel("UI_Inspect", "lblArmorLevel", "", true, 1, Color.WHITE);
		setArmorListener();
		grpArmor.addActor(btnArmor);
		grpArmor.addActor(lblArmorCost);
		grpArmor.addActor(lvlArmor);

		btnSpeed = new MTDImageButton("UI_Inspect", "btnSpeed", Resources.INSPECT_ATLAS, "btnSpeed", "btnSpeedDisabled", true, false);
		lblSpeedCost = new MTDLabel("UI_Inspect", "lblSpeedCost", "6555", true, Color.WHITE, Align.left, Resources.getFont("default-font-22"));
		lvlSpeed = new UpgradeLevel("UI_Inspect", "lblSpeedLevel", "", true, 2, Color.WHITE);
		setIncreaseSpeedListener();
		grpSpeed.addActor(btnSpeed);
		grpSpeed.addActor(lblSpeedCost);
		grpSpeed.addActor(lvlSpeed);

		btnRange = new MTDImageButton("UI_Inspect", "btnRange", Resources.INSPECT_ATLAS, "btnRange", "btnRangeDisabled", true, false);
		lblRangeCost = new MTDLabel("UI_Inspect", "lblRangeCost", "8888", true, Color.WHITE, Align.left, Resources.getFont("default-font-22"));
		lvlRange = new UpgradeLevel("UI_Inspect", "lblRangeLevel", "", true, 2, Color.WHITE);
		setIncreaseRangeListener();
		grpRange.addActor(btnRange);
		grpRange.addActor(lblRangeCost);
		grpRange.addActor(lvlRange);

		btnAttack = new MTDImageButton("UI_Inspect", "btnAttack", Resources.INSPECT_ATLAS, "btnAttack", "btnAttackDisabled", true, false);
		lblAttackCost = new MTDLabel("UI_Inspect", "lblAttackCost", "4789", true, Color.WHITE, Align.left, Resources.getFont("default-font-22"));
		lvlAttack = new UpgradeLevel("UI_Inspect", "lblAttackLevel", "", true, 2, Color.WHITE);
		setIncreaseAttackListener();
		grpAttack.addActor(btnAttack);
		grpAttack.addActor(lblAttackCost);
		grpAttack.addActor(lvlAttack);

		addActor(grpArmor);
		addActor(grpSpeed);
		addActor(grpRange);
		addActor(grpAttack);*/
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
			btnDischarge.setTouchable(Touchable.enabled);
			btnDischarge.setDisabled(false);
			
		} else {
			btnDischarge.setTouchable(Touchable.disabled);
			btnDischarge.setDisabled(true);
			
		}
		
	}
	/**
	 * Binds to the Inspected Tower and updates the widgets
	 */
	@Override
	public void update(Tower selectedTower) {
		lblKills.getLabel_text().setText(String.valueOf(selectedTower.getNumOfKills()));
		lblMoney.getLabel_text().setText(String.valueOf(presenter.getPlayerMoney()));		
		/*lblArmorCost.setText(String.valueOf(selectedTower.getArmorCost()));
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



		updateUpgradeControls(selectedTower.getRangeIncreaseCost(), grpRange, btnRange, lblRangeCost);
		updateUpgradeControls(selectedTower.getSpeedIncreaseCost(), grpSpeed, btnSpeed, lblSpeedCost);
		updateUpgradeControls(selectedTower.getAttackIncreaseCost(), grpAttack, btnAttack, lblAttackCost);
		updateUpgradeControls(selectedTower.getArmorCost(), grpArmor, btnArmor, lblArmorCost);
		*/
		lblTargetPriority.setText(selectedTower.getTargetPriority().name());
		
	}

	/**
	 * Updates the upgrade controlls
	 * 
	 * @param towerCost
	 * @param group
	 * @param button
	 * @param label
	 */
	private void updateUpgradeControls(int towerCost, Group group, MTDImageButton button, MTDLabelOld label) {
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

	private void setCancelListener() {
		btnCancel.addListener(new ClickListener() {
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
		btnAttack.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.increaseAttack();
			}
		});
	}

	private void setArmorListener() {
		btnArmor.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.giveArmor();
			}
		});
	}

	private void setIncreaseRangeListener() {
		btnRange.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.increaseRange();
			}
		});
	}

	private void setIncreaseSpeedListener() {
		btnSpeed.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.increaseSpeed();
			}
		});
	}

	private void setDischargeListener() {
		btnDischarge.addListener(new ClickListener() {
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
