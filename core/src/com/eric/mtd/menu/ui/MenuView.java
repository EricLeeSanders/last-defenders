package com.eric.mtd.menu.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * View for the Main Menu
 * 
 * @author Eric
 *
 */
public class MenuView extends Group {
	private static final float PLAY_MOVE_DURATION = 0.5f;
	private MenuPresenter presenter;
	private TextButton btnPlay;

	public MenuView(MenuPresenter presenter) {
		this.presenter = presenter;
		createControls();
	}

	public void createControls() {

		btnPlay = new TextButton("Play", Resources.getSkin(Resources.SKIN_JSON));
		btnPlay.setSize(200, 75);
		btnPlay.setPosition(0 - btnPlay.getWidth(), 100);
		btnPlay.addAction(Actions.moveTo(225, 100, PLAY_MOVE_DURATION));
		this.addActor(btnPlay);
		setBtnPlayListener();
	}

	private void setBtnPlayListener() {
		btnPlay.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.playGame();
				if (Logger.DEBUG)
					System.out.println("Play Pressed");
			}
		});

	}
}
