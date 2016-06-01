package com.eric.mtd.game.ui.view;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.eric.mtd.game.ui.presenter.OptionsPresenter;
import com.eric.mtd.game.ui.view.interfaces.IOptionsView;
import com.eric.mtd.game.ui.view.widget.MTDImage;
import com.eric.mtd.game.ui.view.widget.MTDTextButton;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * View for the options window
 * 
 * @author Eric
 *
 */
public class OptionsView extends Group implements IOptionsView {
	private OptionsPresenter presenter;
	private MTDTextButton btnResume, btnNewGame, btnMainMenu;
	private MTDImage panel;

	public OptionsView(OptionsPresenter presenter) {
		this.presenter = presenter;
		createControls();
	}

	/**
	 * Create controls with MTD Widgets
	 */
	public void createControls() {
		panel = new MTDImage("UI_Options", "panel", Resources.OPTIONS_ATLAS, "panel", true, false);
		panel.getColor().set(1f, 1f, 1f, .75f);
		addActor(panel);

		btnResume = new MTDTextButton("UI_Options", "btnResume", "Resume", Align.center, 0.45f, true);
		setBtnResumeListener();
		addActor(btnResume);

		btnNewGame = new MTDTextButton("UI_Options", "btnNewGame", "New Game", Align.center, 0.45f, true);
		setBtnNewGameListener();
		addActor(btnNewGame);

		btnMainMenu = new MTDTextButton("UI_Options", "btnMainMenu", "Main Menu", Align.center, 0.45f, true);
		setBtnMainMenuListener();
		addActor(btnMainMenu);
	}

	private void setBtnResumeListener() {
		btnResume.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("Resume Button Pressed");
				presenter.resumeGame();
			}
		});

	}

	private void setBtnNewGameListener() {
		btnNewGame.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("New game Pressed");
				presenter.newGame();
			}
		});

	}

	private void setBtnMainMenuListener() {
		btnMainMenu.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("Main Menu Button Pressed");
				presenter.mainMenu();
			}
		});

	}

	@Override
	public void optionsState() {
		this.setVisible(true);

	}

	@Override
	public void standByState() {
		this.setVisible(false);

	}
}
