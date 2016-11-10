package com.foxholedefense.game.ui.view;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.ui.presenter.InspectPresenter;
import com.foxholedefense.game.ui.view.interfaces.IInspectView;
import com.foxholedefense.util.Resources;

/**
 * View for inspecting a tower
 * 
 * @author Eric
 *
 */
public class InspectView extends Group implements InputProcessor, IInspectView {
	private InspectPresenter presenter;
	private ImageButton btnChangeTarget, btnCancel;
	private TextButton btnDischarge, btnArmor, btnSpeed, btnRange, btnAttack;
	private Group grpTargetPriority;
	private Label lblTargetPriority, lblTitle, lblMoney, lblKills;
	public InspectView(InspectPresenter presenter, Skin skin) {
		this.presenter = presenter;
		grpTargetPriority = new Group();
		grpTargetPriority.setTransform(false);
		this.setTransform(false);
		createControls(skin);
	}

	/**
	 * Create the controls
	 */
	public void createControls(Skin skin) {
		Table container = new Table();
		container.setSize(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT);
		addActor(container);
		container.setBackground(skin.getDrawable("main-panel"));
		
		lblTitle = new Label("Tower", skin);
		lblTitle.setPosition((container.getWidth()/2) - (lblTitle.getWidth()/2)
					,container.getY() + container.getHeight() - lblTitle.getHeight());
		lblTitle.setAlignment(Align.center);
		lblTitle.setFontScale(0.7f);
		addActor(lblTitle);
		
		
		LabelStyle lblMoneyStyle = new LabelStyle(skin.get("money_label", LabelStyle.class));
		lblMoneyStyle.background.setLeftWidth(60);
		lblMoneyStyle.background.setTopHeight(10);
		lblMoney = new Label("$0", lblMoneyStyle);
		lblMoney.setSize(200, 52);
		lblMoney.setPosition(75,5);
		lblMoney.setAlignment(Align.left);
		lblMoney.setFontScale(0.6f);
		addActor(lblMoney);

		LabelStyle lblKillsStyle = new LabelStyle(skin.get("kills_label", LabelStyle.class));
		lblKillsStyle.background.setRightWidth(60);
		lblKillsStyle.background.setTopHeight(10);
		lblKills = new Label("0", lblKillsStyle);
		lblKills.setSize(200, 52);
		lblKills.setPosition(365,5);
		lblKills.setAlignment(Align.right);
		lblKills.setFontScale(0.6f);
		addActor(lblKills);
		
		btnCancel = new ImageButton(skin,"cancel");
		setCancelListener();
		btnCancel.setSize(64, 64);
		btnCancel.getImageCell().size(35,36);
		btnCancel.getImage().setScaling(Scaling.stretch);
		btnCancel.setPosition(Resources.VIRTUAL_WIDTH - 75, Resources.VIRTUAL_HEIGHT - 75);
		addActor(btnCancel);

		Table upgradeTable = new Table();
		btnArmor = createUpgradeButton("100",skin, "upgrade_armor");
		upgradeTable.add(btnArmor).size(100,128).spaceBottom(10).spaceRight(10);
		setArmorListener();
		btnRange = createUpgradeButton("100",skin, "upgrade_range");
		upgradeTable.add(btnRange).size(100,128).spaceBottom(10).spaceRight(10);
		setIncreaseRangeListener();

		btnSpeed = createUpgradeButton("100",skin, "upgrade_speed");
		upgradeTable.add(btnSpeed).size(100,128).spaceBottom(5).spaceRight(10);
		setIncreaseSpeedListener();
		btnAttack = createUpgradeButton("100",skin, "upgrade_attack");
		upgradeTable.add(btnAttack).size(100,128).spaceBottom(5).spaceRight(10);
		setIncreaseAttackListener();

		container.add(upgradeTable).colspan(2).padTop(25);
		
		container.row();
		LabelStyle lblTargetPriorityStyle = new LabelStyle(skin.get("hollow_label", LabelStyle.class));
		lblTargetPriorityStyle.background.setLeftWidth(-2);
		lblTargetPriority = new Label("First", lblTargetPriorityStyle);
		lblTargetPriority.setAlignment(Align.center);
		lblTargetPriority.setSize(140, 41);
		lblTargetPriority.setFontScale(0.45f);
		grpTargetPriority.addActor(lblTargetPriority);
		
		
		Label lblTarget = new Label("Priority", skin);
		lblTarget.setPosition(lblTargetPriority.getX() + 20, lblTargetPriority.getY() + 25);
		lblTarget.setFontScale(0.45f);
		grpTargetPriority.addActor(lblTarget);
		
		btnChangeTarget = new ImageButton(skin, "arrow-right");
		btnChangeTarget.setSize(50, 50);
		btnChangeTarget.getImageCell().size(32,22);
		btnChangeTarget.getImage().setScaling(Scaling.stretch);
		btnChangeTarget.setPosition(lblTargetPriority.getX() + lblTargetPriority.getWidth(), lblTargetPriority.getY() - 4);
		grpTargetPriority.addActor(btnChangeTarget);
		container.add(grpTargetPriority).align(Align.left).padTop(45);
		setTargetPriorityListener();


		TextButtonStyle dischargeStyle = new TextButtonStyle(skin.get("discharge", TextButtonStyle.class));
		dischargeStyle.pressedOffsetY = -27;
		dischargeStyle.unpressedOffsetY = -27;
		dischargeStyle.checkedOffsetY = -27;
		dischargeStyle.pressedOffsetX = 40;
		dischargeStyle.unpressedOffsetX = 40;
		dischargeStyle.checkedOffsetX = 40;
		btnDischarge = new TextButton("9999",dischargeStyle);
		btnDischarge.getLabel().setAlignment(Align.left);
		btnDischarge.getLabel().setFontScale(0.45f);
		container.add(btnDischarge).align(Align.center).size(120,99).padTop(10);
		setDischargeListener();
	}

	/**
	 * Create the upgrade controls that are grouped together with Groups
	 */

	private TextButton createUpgradeButton(String cost,Skin skin,String styleName) {	
		TextButtonStyle upgradeStyle = new TextButtonStyle(skin.get(styleName, TextButtonStyle.class));
		//upgradeStyle.font = Resources.getFont("default-font-46");
		upgradeStyle.pressedOffsetY = -49;
		upgradeStyle.unpressedOffsetY = -49;
		upgradeStyle.checkedOffsetY = -49;
		upgradeStyle.pressedOffsetX = 35;
		upgradeStyle.unpressedOffsetX = 35;
		upgradeStyle.checkedOffsetX = 35;
		
		TextButton upgradeButton = new TextButton(cost, upgradeStyle);
		upgradeButton.getLabel().setAlignment(Align.left);
		upgradeButton.getLabel().setFontScale(0.45f);
		return upgradeButton;
		
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
		btnDischarge.setDisabled(!enabled);
		if (enabled){
			btnDischarge.setTouchable(Touchable.enabled);
		} else {
			btnDischarge.setTouchable(Touchable.disabled);			
		}
		
	}
	/**
	 * Binds to the Inspected Tower and updates the widgets
	 */
	@Override
	public void update(Tower selectedTower) {
		lblMoney.setText(String.valueOf(presenter.getPlayerMoney()));
		lblKills.setText(String.valueOf(selectedTower.getNumOfKills()));
		lblTitle.setText(selectedTower.getName());
		btnArmor.getLabel().setText(String.valueOf(selectedTower.getArmorCost()));
		btnSpeed.getLabel().setText(String.valueOf(selectedTower.getSpeedIncreaseCost()));
		btnRange.getLabel().setText(String.valueOf(selectedTower.getRangeIncreaseCost()));
		btnAttack.getLabel().setText(String.valueOf(selectedTower.getAttackIncreaseCost()));
		btnDischarge.getLabel().setText(String.valueOf(selectedTower.getSellCost()));
		lblTargetPriority.setText(selectedTower.getAI().name().replace('_', ' '));
		updateUpgradeControl(btnArmor, selectedTower.hasArmor(), selectedTower.getArmorCost());
		updateUpgradeControl(btnSpeed, selectedTower.hasIncreasedSpeed(), selectedTower.getSpeedIncreaseCost());
		updateUpgradeControl(btnRange, selectedTower.hasIncreasedRange(), selectedTower.getRangeIncreaseCost());
		updateUpgradeControl(btnAttack, selectedTower.hasIncreasedAttack(), selectedTower.getAttackIncreaseCost());
	}

	/**
	 * Updates the upgrade controls
	 *
	 */
	private void updateUpgradeControl(TextButton upgradeButton, boolean towerHasUpgrade, int upgradeCost) {
		boolean affordable = presenter.canAffordUpgrade(upgradeCost);
		upgradeButton.setDisabled(!affordable);
		if (affordable && !towerHasUpgrade) {
			upgradeButton.setTouchable(Touchable.enabled);
			upgradeButton.setDisabled(false);
		} else {
			upgradeButton.setTouchable(Touchable.disabled);
			upgradeButton.setDisabled(true);
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
