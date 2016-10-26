package com.foxholedefense.game.ui.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.foxholedefense.game.ui.presenter.EnlistPresenter;
import com.foxholedefense.game.ui.view.interfaces.IEnlistView;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;
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

/**
 * View class for Enlisting. Shows Enlisting window as well as the options to
 * place the tower.
 * 
 * @author Eric
 *
 */
public class EnlistView extends Group implements IEnlistView, InputProcessor {
	private Map<ImageButton, String> towerButtons;
	private ImageButton btnPlacingCancel, btnPlace, btnRotate;
	private ImageButton btnCancel, btnScrollUp, btnScrollDown;
	private EnlistPresenter presenter;
	private Group choosingGroup;
	private Label lblTitle, lblMoney;
	private ScrollPane scroll;

	public EnlistView(EnlistPresenter presenter, Skin skin) {
		this.presenter = presenter;
		choosingGroup = new Group();
		choosingGroup.setTransform(false);
		this.setTransform(false);
		addActor(choosingGroup);
		createControls(skin);
	}

	/**
	 * Creates the controls
	 */
	public void createControls(Skin skin) {
		Table container = new Table();
		container.setSize(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT);
		choosingGroup.addActor(container);
		Table enlistTable = new Table();
		// table.debug();

		scroll = new ScrollPane(enlistTable, skin);
		enlistTable.padTop(10);
		enlistTable.defaults().expandX();
		
		container.add(scroll).expand().fill().colspan(1);
		container.setBackground(skin.getDrawable("main-panel"));
		
		lblTitle = new Label("ENLIST", skin);
		lblTitle.setPosition(container.getX() + (container.getWidth()/2) - (lblTitle.getWidth()/2)
					,container.getY() + container.getHeight() - lblTitle.getHeight()+1);
		lblTitle.setAlignment(Align.center);
		lblTitle.setFontScale(.9f);
		choosingGroup.addActor(lblTitle);
	
		
		LabelStyle lblMoneyStyle = new LabelStyle(skin.get("money_label", LabelStyle.class));
		lblMoneyStyle.background.setLeftWidth(60);
		lblMoneyStyle.background.setTopHeight(10);
		lblMoney = new Label("$0", lblMoneyStyle);
		lblMoney.setSize(200, 52);
		lblMoney.setPosition(100,10);
		lblMoney.setAlignment(Align.left);
		lblMoney.setFontScale(0.6f);
		choosingGroup.addActor(lblMoney);
		
		towerButtons = new HashMap<ImageButton, String>();
		createTowerButton(enlistTable, skin, "enlist_rifle", "Rifle");
		createTowerButton(enlistTable, skin, "enlist_machine_gun", "MachineGun");
		createTowerButton(enlistTable, skin, "enlist_sniper", "Sniper");
		enlistTable.row();
		createTowerButton(enlistTable, skin, "enlist_flame_thrower", "FlameThrower");
		createTowerButton(enlistTable, skin, "enlist_rocket_launcher", "RocketLauncher");
		createTowerButton(enlistTable, skin, "enlist_turret", "Turret");
		enlistTable.row();
		createTowerButton(enlistTable, skin, "enlist_tank", "Tank");
		
		
		btnCancel = new ImageButton(skin,"cancel");
		setCancelListener();
		btnCancel.setSize(64, 64);
		btnCancel.setPosition(Resources.VIRTUAL_WIDTH - 75, Resources.VIRTUAL_HEIGHT - 75);
		choosingGroup.addActor(btnCancel);
		//btnCancel.setVisible(true);
		
		btnScrollUp = new ImageButton(skin,"arrow_up");
		btnScrollUp.setSize(64, 64);
		btnScrollUp.setPosition(Resources.VIRTUAL_WIDTH-75, (Resources.VIRTUAL_HEIGHT/2) + 20);
		choosingGroup.addActor(btnScrollUp);
		
		btnScrollDown = new ImageButton(skin,"arrow_down");
		btnScrollDown.setSize(64, 64);
		btnScrollDown.setPosition(Resources.VIRTUAL_WIDTH-75, (Resources.VIRTUAL_HEIGHT/2) - 84);
		choosingGroup.addActor(btnScrollDown);
		
		btnPlace = new ImageButton(skin, "select");
		btnPlace.setSize(50, 50);
		btnPlace.setPosition(Resources.VIRTUAL_WIDTH - 60, 10);
		setPlaceListener();
		addActor(btnPlace);
		
		btnPlacingCancel = new ImageButton(skin, "cancel");
		btnPlacingCancel.setSize(50, 50);
		btnPlacingCancel.setPosition(Resources.VIRTUAL_WIDTH - 60, btnPlace.getY() + 60);
		setPlacingCancelListener();
		addActor(btnPlacingCancel);
		
		btnRotate = new ImageButton(skin, "rotate");
		btnRotate.setSize(50, 50);
		btnRotate.setPosition(Resources.VIRTUAL_WIDTH - 60, btnPlacingCancel.getY() + 60);
		setRotateListener();
		addActor(btnRotate);

	}
	/**
	 * Creates a tower button and adds it to the map
	 * @param enlistTable
	 * @param skin
	 * @param styleName
	 * @param towerName
	 */
	private void createTowerButton(Table enlistTable, Skin skin, String styleName, String towerName){
		ImageButton towerButton = new ImageButton(skin, styleName);
		enlistTable.add(towerButton).size(116,156).spaceBottom(5);
		setTowerListener(towerButton,towerName);
		towerButtons.put(towerButton,towerName);
	}

	/**
	 * Updates the tower buttons to disable/enable.
	 */
	private void updateTowerButtons() {
	    Iterator<Entry<ImageButton, String>> iter = towerButtons.entrySet().iterator();
	    while(iter.hasNext()){
	    	Map.Entry<ImageButton, String> tower = iter.next();
	    	boolean affordable = presenter.canAffordTower(tower.getValue());
	    	tower.getKey().setDisabled(!affordable);
	    	if(affordable){
	    		tower.getKey().setTouchable(Touchable.enabled);
	    	} else {
	    		tower.getKey().setTouchable(Touchable.disabled);
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
		if (btnScrollUp.isPressed()){
			scroll.setScrollY(scroll.getScrollY() - 10);
		}
		if (btnScrollDown.isPressed()){
			scroll.setScrollY(scroll.getScrollY() + 10);
		}
	}

	private void setRotateListener() {
		btnRotate.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				super.touchDown(event, x, y, pointer, button);
				return true;
			}
		});
	}
	private void setTowerListener(ImageButton button, final String tower){
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
				presenter.placeTower();
			}
		});
	}

	private void setCancelListener() {
		btnCancel.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.cancelEnlist();
				
			}
		});
	}
	
	private void setPlacingCancelListener() {
		btnPlacingCancel.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
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
