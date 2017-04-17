package com.foxholedefense.game.ui.view;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.ui.presenter.InspectPresenter;
import com.foxholedefense.game.ui.view.interfaces.IInspectView;
import com.foxholedefense.game.ui.view.widgets.DischargeButton;
import com.foxholedefense.game.ui.view.widgets.UpgradeButton;
import com.foxholedefense.util.datastructures.pool.FHDVector2;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.datastructures.pool.UtilPool;

/**
 * View for inspecting a tower
 * 
 * @author Eric
 *
 */
public class InspectView extends Group implements InputProcessor, IInspectView {
	private InspectPresenter presenter;
	private DischargeButton btnDischarge;
	private UpgradeButton btnArmor, btnSpeed, btnRange, btnAttack;
	private Label lblTargetPriority, lblTitle, lblMoney, lblKills;
	public InspectView(InspectPresenter presenter, Skin skin) {
		this.presenter = presenter;
		this.setTransform(false);
		createControls(skin);
	}

	/**
	 * Create the controls
	 */
	public void createControls(Skin skin) {

		Logger.info("Inspect View: creating controls");

		Table container = new Table();
		container.setTransform(false);
		container.setSize(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT);
		addActor(container);
		container.setBackground(skin.getDrawable("main-panel"));

		Table inspectTable = new Table();
		inspectTable.setTransform(false);
		inspectTable.setBackground(skin.getDrawable("hollow"));

		inspectTable.defaults().expandX();
		
		container.add(inspectTable).expand().fill();

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
		
		ImageButton btnCancel = new ImageButton(skin,"cancel");
		setCancelListener(btnCancel);
		btnCancel.setSize(64, 64);
		btnCancel.getImageCell().size(35,36);
		btnCancel.getImage().setScaling(Scaling.stretch);
		btnCancel.setPosition(Resources.VIRTUAL_WIDTH - 75, Resources.VIRTUAL_HEIGHT - 75);
		addActor(btnCancel);

		btnArmor = new UpgradeButton(skin, "Armor", "shield", 100, 28, 30);
		inspectTable.add(btnArmor).size(110,115);//.spaceBottom(10).spaceRight(10);
		setArmorListener();

		btnRange = new UpgradeButton(skin, "Increase Range", "range_icon", 100, 28, 30);
		inspectTable.add(btnRange).size(110,115);//.spaceBottom(10).spaceRight(10);
		setIncreaseRangeListener();

		btnSpeed = new UpgradeButton(skin, "Increase Speed", "speed_icon", 100,30,30);
		inspectTable.add(btnSpeed).size(110,115);//.spaceBottom(5).spaceRight(10);
		setIncreaseSpeedListener();

		btnAttack = new UpgradeButton(skin, "Increase Attack", "attack_icon", 100, 26,26);
		inspectTable.add(btnAttack).size(110,115);//.spaceBottom(5).spaceRight(10);
		setIncreaseAttackListener();

		Group grpTargetPriority = new Group();
		grpTargetPriority.setTransform(false);

		LabelStyle lblTargetPriorityStyle = new LabelStyle(skin.get("hollow_label", LabelStyle.class));
		lblTargetPriorityStyle.background.setLeftWidth(-2);
		lblTargetPriority = new Label("First", lblTargetPriorityStyle);
		lblTargetPriority.setAlignment(Align.center);
		lblTargetPriority.setSize(140, 41);
		lblTargetPriority.setFontScale(0.45f);
		grpTargetPriority.addActor(lblTargetPriority);
		
		
		Label lblTarget = new Label("PRIORITY", skin);
		lblTarget.setPosition(lblTargetPriority.getX() + 20, lblTargetPriority.getY() + 25);
		lblTarget.setFontScale(0.45f);
		grpTargetPriority.addActor(lblTarget);
		
		Button btnChangeTarget = new Button(skin, "arrow-right");
		btnChangeTarget.setSize(32,22);
		btnChangeTarget.setPosition(lblTargetPriority.getX() + lblTargetPriority.getWidth(), lblTargetPriority.getY() + 7);
		grpTargetPriority.addActor(btnChangeTarget);
		setTargetPriorityListener(grpTargetPriority);

		inspectTable.row();

		inspectTable.add(grpTargetPriority).colspan(2).height(45).width(150).padTop(15).center();

		btnDischarge = new DischargeButton(skin);
		inspectTable.add(btnDischarge).colspan(2).size(133,83).padTop(15).center();
		setDischargeListener();

		Logger.info("Inspect View: controls created");
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
	public void dischargeDisabled(boolean disabled) {
		btnDischarge.button.setDisabled(disabled);
	}
	/**
	 * Binds to the Inspected Tower and updates the widgets
	 */
	@Override
	public void update(Tower selectedTower) {
		lblMoney.setText(String.valueOf(presenter.getPlayerMoney()));
		lblKills.setText(String.valueOf(selectedTower.getNumOfKills()));
		lblTitle.setText(selectedTower.getName().toUpperCase());
		btnArmor.updateCost(selectedTower.getArmorCost());
		btnSpeed.updateCost(selectedTower.getSpeedIncreaseCost());
		btnRange.updateCost(selectedTower.getRangeIncreaseCost());
		btnAttack.updateCost(selectedTower.getAttackIncreaseCost());
		btnDischarge.updateCost(selectedTower.getSellCost());
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
	private void updateUpgradeControl(UpgradeButton upgradeButton, boolean towerHasUpgrade, int upgradeCost) {
		boolean affordable = presenter.canAffordUpgrade(upgradeCost);
		upgradeButton.button.setDisabled(!affordable && !towerHasUpgrade);
		upgradeButton.setPurchased(towerHasUpgrade);
	}

	private void setCancelListener(Button btnCancel) {
		btnCancel.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.closeInspect();
			}
		});
	}

	private void setTargetPriorityListener(Group grpTargetPriority) {
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
		FHDVector2 coords = (FHDVector2)this.getStage().screenToStageCoordinates(UtilPool.getVector2(screenX, screenY));
		presenter.inspectTower(coords);
		coords.free();
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
