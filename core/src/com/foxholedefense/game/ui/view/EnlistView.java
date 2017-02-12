package com.foxholedefense.game.ui.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.foxholedefense.game.model.actor.combat.tower.TowerFlameThrower;
import com.foxholedefense.game.model.actor.combat.tower.TowerMachineGun;
import com.foxholedefense.game.model.actor.combat.tower.TowerRifle;
import com.foxholedefense.game.model.actor.combat.tower.TowerRocketLauncher;
import com.foxholedefense.game.model.actor.combat.tower.TowerSniper;
import com.foxholedefense.game.model.actor.combat.tower.TowerTank;
import com.foxholedefense.game.model.actor.combat.tower.TowerTurret;
import com.foxholedefense.game.ui.presenter.EnlistPresenter;
import com.foxholedefense.game.ui.view.interfaces.IEnlistView;
import com.foxholedefense.game.ui.view.widgets.EnlistButton;
import com.foxholedefense.util.Resources;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

/**
 * View class for Enlisting. Shows Enlisting window as well as the options to
 * place the tower.
 * 
 * @author Eric
 *
 */
public class EnlistView extends Group implements IEnlistView, InputProcessor {
	private Map<EnlistButton, Integer> towerCosts;
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
		container.setTransform(false);
		container.setSize(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT);
		choosingGroup.addActor(container);
		Table enlistTable = new Table();
		enlistTable.setTransform(false);
		// table.debug();

		scroll = new ScrollPane(enlistTable, skin);
		//enlistTable.padTop(10);
		enlistTable.defaults().expand().fill();
		
		container.add(scroll).expand().fill();
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
		
		towerCosts = new HashMap<EnlistButton, Integer>();
		createTowerButton(enlistTable, skin, "Rifle", TowerRifle.COST, 4, 4, 5, 3);
		createTowerButton(enlistTable, skin, "Machine Gun", TowerMachineGun.COST, 1, 4, 4, 8);
		createTowerButton(enlistTable, skin, "Sniper", TowerSniper.COST, 7, 8, 10, 1);
		enlistTable.row();
		createTowerButton(enlistTable, skin, "Flame Thrower", TowerFlameThrower.COST, 7,4,6,2);
		createTowerButton(enlistTable, skin, "Rocket Launcher", TowerRocketLauncher.COST, 10, 4, 6, 1);
		createTowerButton(enlistTable, skin, "Turret", TowerTurret.COST, 3, 7, 7, 8);
		enlistTable.row();
		createTowerButton(enlistTable, skin, "Tank", TowerTank.COST, 10, 10, 8, 10);
		
		
		btnCancel = new ImageButton(skin,"cancel");
		setCancelListener();
		btnCancel.setSize(64, 64);
		btnCancel.getImageCell().size(35,36);
		btnCancel.getImage().setScaling(Scaling.stretch);
		btnCancel.setPosition(Resources.VIRTUAL_WIDTH - 75, Resources.VIRTUAL_HEIGHT - 75);
		choosingGroup.addActor(btnCancel);
		//btnCancel.setVisible(true);
		
		btnScrollUp = new ImageButton(skin,"arrow-small-up");
		btnScrollUp.setSize(64, 64);
		btnScrollUp.getImageCell().size(35,30);
		btnScrollUp.getImage().setScaling(Scaling.stretch);
		btnScrollUp.setPosition(Resources.VIRTUAL_WIDTH-75, (Resources.VIRTUAL_HEIGHT/2) + 20);
		choosingGroup.addActor(btnScrollUp);
		
		btnScrollDown = new ImageButton(skin,"arrow-small-down");
		btnScrollDown.setSize(64, 64);
		btnScrollDown.getImageCell().size(35,30);
		btnScrollDown.getImage().setScaling(Scaling.stretch);
		btnScrollDown.setPosition(Resources.VIRTUAL_WIDTH-75, (Resources.VIRTUAL_HEIGHT/2) - 84);
		choosingGroup.addActor(btnScrollDown);
		
		btnPlace = new ImageButton(skin, "select");
		btnPlace.setSize(50, 50);
		btnPlace.getImageCell().size(30,23);
		btnPlace.getImage().setScaling(Scaling.stretch);
		btnPlace.setPosition(Resources.VIRTUAL_WIDTH - 60, 10);
		setPlaceListener();
		addActor(btnPlace);
		
		btnPlacingCancel = new ImageButton(skin, "cancel");
		btnPlacingCancel.setSize(50, 50);
		btnPlacingCancel.getImageCell().size(25,25);
		btnPlacingCancel.getImage().setScaling(Scaling.stretch);
		btnPlacingCancel.setPosition(Resources.VIRTUAL_WIDTH - 60, btnPlace.getY() + 60);
		setPlacingCancelListener();
		addActor(btnPlacingCancel);
		
		btnRotate = new ImageButton(skin, "rotate");
		btnRotate.setSize(50, 50);
		btnRotate.getImageCell().size(34,32);
		btnRotate.getImage().setScaling(Scaling.stretch);
		btnRotate.setPosition(Resources.VIRTUAL_WIDTH - 60, btnPlacingCancel.getY() + 60);
		setRotateListener();
		addActor(btnRotate);

	}
	/**
	 * Creates a tower button and adds it to the map
	 * @param enlistTable
	 * @param skin
	 * @param towerName
	 */
	private void createTowerButton(Table enlistTable, Skin skin, String towerName, Integer towerCost, int attack, int health, int range, int speed){
		EnlistButton towerButton = new EnlistButton(skin, attack, health, range, speed, towerName, towerCost);
		enlistTable.add(towerButton).size(116,178).spaceBottom(5);
		setTowerListener(towerButton,towerName);
		towerCosts.put(towerButton,towerCost);
	}

	/**
	 * Updates the tower buttons to disable/enable.
	 */
	private void updateTowerButtons() {
	    Iterator<Entry<EnlistButton, Integer>> iter = towerCosts.entrySet().iterator();
	    while(iter.hasNext()){
	    	Map.Entry<EnlistButton, Integer> tower = iter.next();
	    	boolean affordable = presenter.canAffordTower(tower.getValue());
	    	tower.getKey().button.setDisabled(!affordable);
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
			presenter.rotateTower(delta);
		} 
		if (btnScrollUp.isPressed()){
			scroll.setScrollY(scroll.getScrollY() - (scroll.getHeight() * delta));
		}
		if (btnScrollDown.isPressed()){
			scroll.setScrollY(scroll.getScrollY() + (scroll.getHeight() * delta));
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
	private void setTowerListener(EnlistButton button, final String tower){
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
				presenter.cancelEnlist(true);
				
			}
		});
	}
	
	private void setPlacingCancelListener() {
		btnPlacingCancel.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.cancelEnlist(true);
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
