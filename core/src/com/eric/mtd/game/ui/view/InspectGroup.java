package com.eric.mtd.game.ui.view;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.eric.mtd.game.helper.CollisionDetection;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.actor.tower.Tower;
import com.eric.mtd.game.model.ai.TowerTargetPriority;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.game.ui.controller.interfaces.IInspectController;
import com.eric.mtd.game.ui.state.IGameUIStateObserver;
import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.game.ui.view.widget.MTDImage;
import com.eric.mtd.game.ui.view.widget.MTDImageButton;
import com.eric.mtd.game.ui.view.widget.MTDLabel;
import com.eric.mtd.game.ui.view.widget.MTDTextButton;
import com.eric.mtd.game.ui.view.widget.inspect.AttributeUpgrade;
import com.eric.mtd.game.ui.view.widget.inspect.UpgradeButton;
import com.eric.mtd.game.ui.view.widget.inspect.UpgradeLevel;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;
public class InspectGroup extends Group implements IGameUIStateObserver, InputProcessor{
	private GameUIStateManager uiStateManager;
	private IInspectController controller;
	private UpgradeButton btnArmor, btnSpeed, btnRange, btnAttack;
	private MTDLabel lblArmorCost, lblSpeedCost, lblRangeCost, lblAttackCost;
	private UpgradeLevel lvlArmor, lvlSpeed, lvlRange, lvlAttack;
	private Group grpArmor, grpSpeed, grpRange, grpAttack, grpDischarge, grpTargetPriority;
	//private MTDLabel [] lblIncreaseSpeedLevel, lblIncreaseAttackLevel, lblIncreaseRangeLevel;
	private MTDLabel lblDischargePrice, lblTargetPriorityTitle, lblTargetPriority, lblKills;
	private MTDImage pnlInspect, pnlSideInspect, imgDischarge, iconDischarge, imgKills;
	private MTDImageButton btnDischarge, btnTargetPriority;
	private MTDTextButton btnInspectClose;
	public InspectGroup(GameUIStateManager uiStateManager,IInspectController controller){
		this.uiStateManager = uiStateManager;
		this.controller = controller;
		uiStateManager.attach(this);
		createControls();
	}
	
	public void createControls(){
		grpArmor = new Group();
		grpSpeed = new Group();
		grpRange = new Group();
		grpAttack = new Group();
		grpDischarge = new Group();
		grpTargetPriority = new Group();
		pnlInspect = new MTDImage("UI_Inspect", "bottomPanel", Resources.INSPECT_ATLAS, "inspect_bg",true, false);
			addActor(pnlInspect);
		pnlSideInspect = new MTDImage("UI_Inspect", "sidePanel", Resources.INSPECT_ATLAS, "inspect_bg",true, false);
			addActor(pnlSideInspect);
		
		btnArmor = new UpgradeButton("UI_Inspect","btnArmor",Resources.INSPECT_ATLAS,"btnArmor","btnArmorDisabled",true, false);
		lblArmorCost  = new MTDLabel("UI_Inspect", "lblArmorCost", "5555", true, Color.WHITE, Align.left, 0.58f);	
		lvlArmor = new UpgradeLevel("UI_Inspect", "lblArmorLevel", "", true, 1, Color.WHITE);
			setArmorListener();
			grpArmor.addActor(btnArmor);
			grpArmor.addActor(lblArmorCost);
			grpArmor.addActor(lvlArmor);
			
		btnSpeed = new UpgradeButton("UI_Inspect","btnSpeed",Resources.INSPECT_ATLAS,"btnSpeed","btnSpeedDisabled",true, false);
		lblSpeedCost  = new MTDLabel("UI_Inspect", "lblSpeedCost", "6555", true, Color.WHITE, Align.left, 0.58f);	
		lvlSpeed = new UpgradeLevel("UI_Inspect", "lblSpeedLevel", "", true, 2, Color.WHITE);
			setIncreaseSpeedListener();
			grpSpeed.addActor(btnSpeed);
			grpSpeed.addActor(lblSpeedCost);
			grpSpeed.addActor(lvlSpeed);
			
		btnRange = new UpgradeButton("UI_Inspect","btnRange",Resources.INSPECT_ATLAS,"btnRange","btnRangeDisabled",true, false);
		lblRangeCost  = new MTDLabel("UI_Inspect", "lblRangeCost", "8888", true, Color.WHITE, Align.left, 0.58f);	
		lvlRange = new UpgradeLevel("UI_Inspect", "lblRangeLevel", "", true, 2, Color.WHITE);
			setIncreaseRangeListener();
			grpRange.addActor(btnRange);
			grpRange.addActor(lblRangeCost);
			grpRange.addActor(lvlRange);
				
		btnAttack = new UpgradeButton("UI_Inspect","btnAttack",Resources.INSPECT_ATLAS,"btnAttack","btnAttackDisabled",true, false);
		lblAttackCost  = new MTDLabel("UI_Inspect", "lblAttackCost", "4789", true, Color.WHITE, Align.left, 0.58f);	
		lvlAttack = new UpgradeLevel("UI_Inspect", "lblAttackLevel", "", true, 2, Color.WHITE);
			setIncreaseAttackListener();
			grpAttack.addActor(btnAttack);
			grpAttack.addActor(lblAttackCost);
			grpAttack.addActor(lvlAttack);
		
		addActor(grpArmor);
		addActor(grpSpeed);
		addActor(grpRange);
		addActor(grpAttack);
		

		btnDischarge = new MTDImageButton("UI_Inspect","btnDischarge",Resources.INSPECT_ATLAS,"btnDischarge",true, false);
			setDischargeListener();
			lblDischargePrice = new MTDLabel("UI_Inspect", "lblDischargePrice", "", true, Color.WHITE, Align.left, 0.625f);
			grpDischarge.addActor(btnDischarge);
			grpDischarge.addActor(lblDischargePrice);
		addActor(grpDischarge);
		
		btnTargetPriority = new MTDImageButton("UI_Inspect","btnTargetPriority",Resources.INSPECT_ATLAS,"btnTargetPriority",true, false);
			lblTargetPriority = new MTDLabel("UI_Inspect","lblTargetPriority",TowerTargetPriority.values()[0].name(),true, Color.WHITE, Align.center, 0.5f);
			setTargetPriorityListener();
			grpTargetPriority.addActor(btnTargetPriority);
			grpTargetPriority.addActor(lblTargetPriority);
		addActor(grpTargetPriority);
	
		btnInspectClose = new MTDTextButton("UI_Inspect","btnClose","Close",Align.center, true);
			setUpgradeCloseListener();
		addActor(btnInspectClose);
		
		
		imgKills = new MTDImage("UI_Inspect","imgKills",Resources.INSPECT_ATLAS,"kills",true, true);
		addActor(imgKills);
		lblKills = new MTDLabel("UI_Inspect", "lblKills","0 kills",true,Color.WHITE, Align.left, 0.5f);
		addActor(lblKills);
	}

	public void updateInspect(){
		lblArmorCost.setText(String.valueOf(controller.getArmorCost()));
		lblSpeedCost.setText(String.valueOf(controller.getSpeedCost()));
		lblRangeCost.setText(String.valueOf(controller.getRangeCost()));
		lblAttackCost.setText(String.valueOf(controller.getAttackCost()));
		lblDischargePrice.setText(String.valueOf(controller.getSellPrice()));
		lblKills.setText(String.valueOf(controller.getKills()+ " kills"));
		lvlArmor.resetLevels();
		lvlSpeed.resetLevels();
		lvlRange.resetLevels();
		lvlAttack.resetLevels();
		
		if(Logger.DEBUG){
			System.out.println("Armor: " + controller.hasArmor());
			System.out.println("Speed: " + controller.getSpeedLevel());
			System.out.println("Range: " + controller.getRangeLevel());
			System.out.println("Attack: " + controller.getAttackLevel());
		}
		if(controller.hasArmor()){
			lvlArmor.setLevel(1);
		}
		lvlSpeed.setLevel(controller.getSpeedLevel());
		lvlRange.setLevel(controller.getRangeLevel());
		lvlAttack.setLevel(controller.getAttackLevel());
		
		lblTargetPriority.setText(controller.getTowerTargetPriority());
		
		//TODO: Question: Could probably find a better way to do this
		if(controller.canAffordUpgrade(controller.getArmorCost())){
			grpArmor.setTouchable(Touchable.enabled);
			btnArmor.setDisabled(false);
			lblArmorCost.getStyle().fontColor = Color.WHITE;
		}
		else{
			grpArmor.setTouchable(Touchable.disabled);
			btnArmor.setDisabled(true);
			lblArmorCost.getStyle().fontColor = Color.RED;
		}
		
		if(controller.canAffordUpgrade(controller.getAttackCost())){
			grpAttack.setTouchable(Touchable.enabled);
			btnAttack.setDisabled(false);
			lblAttackCost.getStyle().fontColor = Color.WHITE;
		}
		else{
			grpAttack.setTouchable(Touchable.disabled);
			btnAttack.setDisabled(true);
			lblAttackCost.getStyle().fontColor = Color.RED;
		}
		
		if(controller.canAffordUpgrade(controller.getSpeedCost())){
			grpSpeed.setTouchable(Touchable.enabled);
			btnSpeed.setDisabled(false);
			lblSpeedCost.getStyle().fontColor = Color.WHITE;
		}
		else{
			grpSpeed.setTouchable(Touchable.disabled);
			btnSpeed.setDisabled(true);
			lblSpeedCost.getStyle().fontColor = Color.RED;
		}
		
		if(controller.canAffordUpgrade(controller.getRangeCost())){
			grpRange.setTouchable(Touchable.enabled);
			btnRange.setDisabled(false);
			lblRangeCost.getStyle().fontColor = Color.WHITE;
		}
		else{
			grpRange.setTouchable(Touchable.disabled);
			btnRange.setDisabled(true);
			lblRangeCost.getStyle().fontColor = Color.RED;
		}
	}
	
	
	private void setUpgradeCloseListener(){
		btnInspectClose.addListener(new ClickListener() {
	    	@Override
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
	        {
	    		super.touchUp( event, x, y, pointer, button );
	    		controller.closeInspect();
	    		//showUpgrade(false);
	    		//showTowerRanges(false);
	        }
	    } );
	}

	private void setTargetPriorityListener(){
		grpTargetPriority.addListener(new ClickListener() {
	    	@Override
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
	        {
	    		super.touchUp( event, x, y, pointer, button );
	    		controller.changeTargetPriority();
	    		updateInspect();
	        }
	    } );
	}
	private void setIncreaseAttackListener(){
	    grpAttack.addListener(new ClickListener() {
	    	@Override
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
	        {
	    		super.touchUp( event, x, y, pointer, button );
	    		controller.increaseAttack();
    			updateInspect();
	        }
	    } );
	}
	private void setArmorListener(){
	    grpArmor.addListener(new ClickListener() {
	    	@Override
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
	        {
	    		super.touchUp( event, x, y, pointer, button );
	    		controller.giveArmor();
    			updateInspect();
	        }
	    } );
	}

	private void setIncreaseRangeListener(){
	    grpRange.addListener(new ClickListener() {
	    	@Override
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
	        {
	    		super.touchUp( event, x, y, pointer, button );
	    		controller.increaseRange();
    			updateInspect();
	        }
	    } );
	}
	
	private void setIncreaseSpeedListener(){
	    grpSpeed.addListener(new ClickListener() {
	    	@Override
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
	        {
	    		super.touchUp( event, x, y, pointer, button );
	    		controller.increaseSpeed();
    			updateInspect();
	        }
	    } );
	}
	private void setDischargeListener(){
	    grpDischarge.addListener(new ClickListener() {
	    	@Override
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
	        {
	    		super.touchUp( event, x, y, pointer, button );
	    		controller.dishcharge();
	    		
	        }
	    } );
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
		Vector2 coords = this.getStage().screenToStageCoordinates(new Vector2((float)screenX,(float)screenY));
		controller.inspectTower(coords);
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

	@Override
	public void changeUIState(GameUIState state) {
		// TODO Auto-generated method stub
		
	}


}

