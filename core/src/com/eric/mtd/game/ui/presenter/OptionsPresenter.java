package com.eric.mtd.game.ui.presenter;

import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.game.ui.state.IGameUIStateObserver;
import com.eric.mtd.game.ui.view.interfaces.IOptionsView;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;
import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.state.GameStateManager.GameState;

/**
 * Presenter for the Options View
 * 
 * @author Eric
 *
 */
public class OptionsPresenter implements IGameUIStateObserver {
	private GameStateManager gameStateManager;
	private GameUIStateManager uiStateManager;
	private ScreenStateManager screenStateManager;
	private IOptionsView view;

	public OptionsPresenter(GameUIStateManager uiStateManager, GameStateManager gameStateManager, ScreenStateManager screenStateManager) {
		this.uiStateManager = uiStateManager;
		uiStateManager.attach(this);
		this.gameStateManager = gameStateManager;
		this.screenStateManager = screenStateManager;
	}

	/**
	 * Set the options view
	 * 
	 * @param view
	 */
	public void setView(IOptionsView view) {
		this.view = view;
		changeUIState(uiStateManager.getState());
	}

	/**
	 * Resume the game
	 */
	public void resumeGame() {
		gameStateManager.setState(GameState.PLAY);
		uiStateManager.setStateReturn();

	}

	/**
	 * Go to main menu
	 */
	public void mainMenu() {
		gameStateManager.setState(GameState.PLAY);
		screenStateManager.setState(ScreenState.MENU);

	}

	/**
	 * Start a new game
	 */
	public void newGame() {
		gameStateManager.setState(GameState.PLAY);
		screenStateManager.setState(ScreenState.LEVEL_SELECTION);

	}

	@Override
	public void changeUIState(GameUIState state) {
		switch (state) {
		case OPTIONS:
			view.optionsState();
			break;
		default:
			view.standByState();
			break;
		}

	}

}
