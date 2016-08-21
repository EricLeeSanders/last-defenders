package com.eric.mtd.game.ui.view;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.eric.mtd.game.ui.presenter.EnlistPresenter;
import com.eric.mtd.game.ui.presenter.SupportPresenter;
import com.eric.mtd.game.ui.view.interfaces.IEnlistView;
import com.eric.mtd.game.ui.view.interfaces.ISupportView;
import com.eric.mtd.game.ui.view.widget.MTDImage;
import com.eric.mtd.game.ui.view.widget.MTDImageButton;
import com.eric.mtd.game.ui.view.widget.enlist.MTDTowerButton;
import com.eric.mtd.game.ui.view.widget.support.MTDSupportButton;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * View class for Support. Shows Support window as well as the options to
 * place the various Support actors.
 * 
 * @author Eric
 *
 */
public class SupportView extends Group implements ISupportView, InputProcessor {
	private MTDImage pnlSupport;
	private ImageButton btnLandmines, btnAirstrike, btnApache;
	private ImageButton btnPlacingCancel, btnCancel;
	private MTDImageButton btnPlace;
	private SupportPresenter presenter;
	private Group choosingGroup;
	private Table container, supportTable;
	private Label lblTitle, lblMoney, lblMoney_img;
	
	public SupportView(SupportPresenter presenter) {
		this.presenter = presenter;
		choosingGroup = new Group();
		addActor(choosingGroup);
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
		supportTable = new Table();
		// table.debug();

		final ScrollPane scroll = new ScrollPane(supportTable, skin);
		//scroll.setVariableSizeKnobs(false);
		supportTable.defaults().expandX();
		
		container.add(scroll).expand().fill().colspan(1);
		container.setBackground(skin.getDrawable("main-panel-horz-padded"));
		
		LabelStyle lblTitleStyle = new LabelStyle();
		lblTitleStyle.font = Resources.getFont("default-font-22");
		lblTitle = new Label("SUPPORT", lblTitleStyle);
		lblTitle.setPosition(container.getX() + (container.getWidth()/2) - (lblTitle.getWidth()/2)
					,container.getY() + container.getHeight() - lblTitle.getHeight() - 20);
		lblTitle.setAlignment(Align.center);
		choosingGroup.addActor(lblTitle);
		
		//lblMoney_img creates the image for the label, while lblMoney has a transparent bg and is 
		//placed on the lblMoney_img. Had to separate the image and the text to allow for better alignment
		LabelStyle lblMoneyStyle_img = new LabelStyle(skin.get("money_label_img", LabelStyle.class));
		lblMoneyStyle_img.font = Resources.getFont("default-font-22");
		lblMoney_img = new Label("", lblMoneyStyle_img);
		lblMoney_img.setSize(160, 59);
		lblMoney_img.setPosition(container.getX() + 208 - (lblMoney_img.getWidth()/2)
					,container.getY() + 37 - lblMoney_img.getHeight()/2);
		lblMoney_img.setAlignment(Align.center);
		choosingGroup.addActor(lblMoney_img);
		
		LabelStyle lblMoneyStyle = new LabelStyle(skin.get("default", LabelStyle.class));
		lblMoneyStyle.font = Resources.getFont("default-font-22");
		lblMoney = new Label("0", lblMoneyStyle);
		lblMoney.setSize(160, 59);
		System.out.println(lblMoney_img.getX());
		lblMoney.setPosition(lblMoney_img.getX()+60, lblMoney_img.getY()-1);
		lblMoney.setAlignment(Align.left);
		choosingGroup.addActor(lblMoney);
		

		btnApache = new ImageButton(skin, "support");
		setApacheListener();
		supportTable.add(btnApache).size(109,128).spaceBottom(5);
		
		btnAirstrike = new ImageButton(skin, "support");
		setAirstrikeListener();
		supportTable.add(btnAirstrike).size(109,128).spaceBottom(5);
		
		btnLandmines = new ImageButton(skin, "support");
		setLandminesListener();
		supportTable.add(btnLandmines).size(109,128).spaceBottom(5);

		btnCancel = new ImageButton(skin,"cancel");
		setCancelListener();
		btnCancel.setSize(64, 64);
		btnCancel.setPosition(Resources.VIRTUAL_WIDTH - 120, 28);
		choosingGroup.addActor(btnCancel);
		btnCancel.setVisible(true);
		btnPlacingCancel = new MTDImageButton("UI_Support", "btnCancel", skin, "cancel", false, false);
		setPlacingCancelListener();
		addActor(btnPlacingCancel);
		
		btnPlace = new MTDImageButton("UI_Support", "btnPlace", skin, "select", false, false);
		setPlaceListener();
		addActor(btnPlace);

	}

	/**
	 * Updates the Support buttons to disable/enable.
	 */
	private void updateSupportButtons() {
		for (Actor button : choosingGroup.getChildren()) {
			if (button instanceof MTDSupportButton) {
				if (presenter.canAffordTower(((MTDSupportButton) button).getSupportName())) {
					if (Logger.DEBUG)
						System.out.println("Setting " + ((MTDSupportButton) button).getSupportName() + " to Enabled");
					((MTDSupportButton) button).setDisabled(false);
					button.setTouchable(Touchable.enabled);
				} else {
					if (Logger.DEBUG)
						System.out.println("Setting " + ((MTDSupportButton) button).getSupportName() + " to Disabled");
					((MTDSupportButton) button).setDisabled(true);
					button.setTouchable(Touchable.disabled);
				}
			}
		}

	}

	private void setLandminesListener() {
		btnLandmines.addListener(new ActorGestureListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.createSupportActor("LandMine");
			}
		});

	}

	private void setAirstrikeListener() {
		btnAirstrike.addListener(new ActorGestureListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.createAirStrike();
			}
		});
	}

	private void setApacheListener() {
		btnApache.addListener(new ActorGestureListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.createSupportActor("Apache");
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
				presenter.cancelSupport();
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
				presenter.placeSupportActor();
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
				presenter.cancelSupport();
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
		presenter.screenTouch(coords, "TouchDown");
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
		presenter.screenTouch(coords, "Dragged");
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
	public void supportState() {
		updateSupportButtons();
		lblMoney.setText(String.valueOf(presenter.getPlayerMoney()));
		choosingGroup.setVisible(true);
		btnPlacingCancel.setVisible(false);
		this.setVisible(true);
	}

	@Override
	public void placingSupportState() {
		btnPlacingCancel.setVisible(true);
		choosingGroup.setVisible(false);
	}

	@Override
	public void standByState() {
		btnPlacingCancel.setVisible(false);
		choosingGroup.setVisible(false);
		btnPlace.setVisible(false);
		this.setVisible(false);
		
	}

	@Override
	public void showBtnPlace() {
		btnPlace.setVisible(true);
	}



}
