package com.eric.mtd.game.ui.view;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.eric.mtd.game.ui.presenter.EnlistPresenter;
import com.eric.mtd.game.ui.view.interfaces.IEnlistView;
import com.eric.mtd.game.ui.view.widget.MTDImage;
import com.eric.mtd.game.ui.view.widget.MTDImageButton;
import com.eric.mtd.game.ui.view.widget.MTDLabel;
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
	private ImageButton btnTank, btnFlameThrower, btnTurret, btnSniper, btnMachineGun, btnRocketLauncher, btnRifle;
	private MTDImageButton btnPlacingCancel, btnPlace, btnRotate;
	private ImageButton btnCancel;
	private EnlistPresenter presenter;
	private Group choosingGroup;
	private Table container, enlistTable;
	private Label lblTitle, lblMoney;
	//private MTDLabel lblMoney;

	private Stage stage;
	public EnlistView(EnlistPresenter presenter, Stage stage) {
		this.presenter = presenter;
		choosingGroup = new Group();
		addActor(choosingGroup);
		this.stage = stage;
		createControls();
	}

	/**
	 * Creates the controls with the MTD widgets
	 */
	public void createControls() {
		
		Skin skin = Resources.getSkin(Resources.SKIN_JSON);
		container = new Table();
		container.setSize(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT);
		choosingGroup.addActor(container);
		enlistTable = new Table();
		// table.debug();

		final ScrollPane scroll = new ScrollPane(enlistTable, skin);
		//scroll.setVariableSizeKnobs(false);
		enlistTable.defaults().expandX();
		
		container.add(scroll).expand().fill().colspan(1);
		container.setBackground(skin.getDrawable("main-panel-horz-padded"));
		
//		LabelStyle lblTitleStyle = new LabelStyle();
//		lblTitleStyle.font = Resources.getFont("default-font-22");
//		lblTitle = new Label("ENLIST", lblTitleStyle);
//		lblTitle.setPosition(container.getX() + (container.getWidth()/2) - (lblTitle.getWidth()/2)
//					,container.getY() + container.getHeight() - lblTitle.getHeight() - 20);
//		lblTitle.setAlignment(Align.center);
//		choosingGroup.addActor(lblTitle);
//		
//		lblMoney = new MTDLabel("0", skin, "money_label_img", "default-font-22", Align.left);
//		lblMoney.getLabel_img().setSize(160, 69);
//		lblMoney.getLabel_img().setPosition(208 - (lblMoney.getLabel_img().getWidth()/2)
//				,37 - lblMoney.getLabel_img().getHeight()/2);
//		lblMoney.getLabel_text().setSize(100, 59);
//		lblMoney.getLabel_text().setPosition(lblMoney.getLabel_img().getX()+60, lblMoney.getLabel_img().getY()+3);
//		choosingGroup.addActor(lblMoney);
		
		
		LabelStyle lblTitleStyle = new LabelStyle(skin.get("base_label", LabelStyle.class));
		lblTitleStyle.font = Resources.getFont("default-font-22");
		lblMoney = new Label("", lblTitleStyle);
		lblMoney.setPosition(100,100);
		lblMoney.setAlignment(Align.center);
		choosingGroup.addActor(lblMoney);
		
		
		btnRifle = new ImageButton(skin, "enlist");
		enlistTable.add(btnRifle).size(116,156).spaceBottom(5);
		setTowerListener(btnRifle,"Rifle");
		
		btnSniper = new ImageButton(skin, "enlist");
		enlistTable.add(btnSniper).size(116,156).spaceBottom(5);
		setTowerListener(btnSniper,"Sniper");
		
		btnMachineGun = new ImageButton(skin, "enlist");
		enlistTable.add(btnMachineGun).size(116,156).spaceBottom(5);
		setTowerListener(btnMachineGun,"MachineGun");
		
		enlistTable.row();
		btnFlameThrower = new ImageButton(skin, "enlist");
		enlistTable.add(btnFlameThrower).size(116,156).spaceBottom(5);
		setTowerListener(btnFlameThrower,"FlameThrower");
		
		btnRocketLauncher =  new ImageButton(skin, "enlist");
		enlistTable.add(btnRocketLauncher).size(116,156).spaceBottom(5);
		setTowerListener(btnRocketLauncher,"RocketLauncher");
		
		btnTurret = new ImageButton(skin, "enlist");
		enlistTable.add(btnTurret).size(116,156).spaceBottom(5);
		setTowerListener(btnTurret,"Turret");
		
		enlistTable.row();
		btnTank = new ImageButton(skin, "enlist");
		enlistTable.add(btnTank).size(116,156).spaceBottom(5);
		setTowerListener(btnTank,"Tank");
		
		
		btnCancel = new ImageButton(skin,"cancel");
		setCancelListener();
		btnCancel.setSize(64, 64);
		btnCancel.setPosition(Resources.VIRTUAL_WIDTH - 120, 28);
		choosingGroup.addActor(btnCancel);
		btnCancel.setVisible(true);
		btnPlacingCancel = new MTDImageButton("UI_Enlist", "btnCancel", skin, "cancel", false, false);
		setPlacingCancelListener();
		addActor(btnPlacingCancel);
		btnPlace = new MTDImageButton("UI_Enlist", "btnPlace", skin, "select", false, false);
		setPlaceListener();
		addActor(btnPlace);
		btnRotate = new MTDImageButton("UI_Enlist", "btnRotate", skin, "rotate", false, false);
		setRotateListener();
		addActor(btnRotate);

	}

	/**
	 * Updates the tower buttons to disable/enable.
	 */
	private void updateTowerButtons() {
		for (Actor button : enlistTable.getChildren()) {
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
	private void setTowerListener(ImageButton button, final String tower){
		if (Logger.DEBUG)
			System.out.println("creating " + tower + " listener");
		button.addListener(new ActorGestureListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.createTower(tower);
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
	
	private void setPlacingCancelListener() {
		btnPlacingCancel.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("Placing Cancel Pressed");
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
		lblMoney.setText(String.valueOf(presenter.getPlayerMoney()));
		choosingGroup.setVisible(true);
		btnPlacingCancel.setVisible(false);
		this.setVisible(true);
	}

	@Override
	public void placingTowerState() {
		btnPlacingCancel.setVisible(true);
		choosingGroup.setVisible(false);

	}

	@Override
	public void standByState() {
		btnPlacingCancel.setVisible(false);
		choosingGroup.setVisible(false);
		btnRotate.setVisible(false);
		btnPlace.setVisible(false);
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
