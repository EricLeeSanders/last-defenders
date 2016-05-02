package com.eric.mtd.game.ui.view;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
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
	private MTDSupportButton btnLandmines, btnAirstrike, btnApache;
	private MTDImageButton btnCancel, btnPlace;
	private SupportPresenter presenter;
	private Group choosingGroup;

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
		pnlSupport = new MTDImage("UI_Support", "panel", Resources.SUPPORT_UI_ATLAS, "panel", true, false);
		pnlSupport.getColor().set(1f, 1f, 1f, .75f);
		choosingGroup.addActor(pnlSupport);

		btnLandmines = new MTDSupportButton("UI_Support", "btnLandmines", Resources.SUPPORT_UI_ATLAS, "landminesEnabled"
										, "landminesDisabled", "Landmine", true, false);
		setLandminesListener();
		choosingGroup.addActor(btnLandmines);

		btnAirstrike = new MTDSupportButton("UI_Support", "btnAirstrike", Resources.SUPPORT_UI_ATLAS, "airstrikeEnabled"
										, "airstrikeDisabled", "Airstrike", true, false);
		setAirstrikeListener();
		choosingGroup.addActor(btnAirstrike);

		btnApache = new MTDSupportButton("UI_Support", "btnApache", Resources.SUPPORT_UI_ATLAS, "apacheEnabled"
										, "apacheDisabled", "Apache", true, false);
		setApacheListener();
		choosingGroup.addActor(btnApache);

		btnCancel = new MTDImageButton("UI_Support", "btnCancel", Resources.SUPPORT_UI_ATLAS, "cancel", false, false);
		setCancelListener();
		addActor(btnCancel);
		btnPlace = new MTDImageButton("UI_Support", "btnPlace", Resources.SUPPORT_UI_ATLAS, "place", false, false);
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
		btnLandmines.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.createSupportActor("LandMine");
			}
		});

	}

	private void setAirstrikeListener() {
		btnAirstrike.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.createAirStrike();
			}
		});
	}

	private void setApacheListener() {
		btnApache.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.createSupportActor("Apache");
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
		//updateSupportButtons();
		choosingGroup.setVisible(true);
		btnCancel.setVisible(true);
		this.setVisible(true);
	}

	@Override
	public void placingSupportState() {
		btnCancel.setVisible(true);
		choosingGroup.setVisible(false);
	}

	@Override
	public void standByState() {
		btnCancel.setVisible(false);
		choosingGroup.setVisible(false);
		btnPlace.setVisible(false);
		btnCancel.setVisible(false);
		this.setVisible(false);
		
	}

	@Override
	public void showBtnPlace() {
		btnPlace.setVisible(true);
	}



}
