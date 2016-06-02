package com.eric.mtd.menu.ui;

import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;
import com.eric.mtd.util.AudioUtil;

/**
 * Presenter class for the Main Menu
 * 
 * @author Eric
 *
 */
public class MenuPresenter {
	private ScreenStateManager screenStateManager;
	private IMenuView view;

	public MenuPresenter(ScreenStateManager screenStateManager) {
		this.screenStateManager = screenStateManager;
	}
	public void setView(IMenuView view){
		this.view = view;
		initView();
	}
	private void initView(){
		view.setBtnMusicOn(AudioUtil.isMusicEnabled());
		view.setBtnSoundOn(AudioUtil.isSoundEnabled());
	}
	public void playGame() {
		screenStateManager.setState(ScreenState.LEVEL_SELECTION);

	}
	
	public void soundPressed() {
		AudioUtil.changeSoundEnabled();
		view.setBtnSoundOn(AudioUtil.isSoundEnabled());
	}
	
	public void musicPressed() {
		AudioUtil.changeMusicEnabled();
		view.setBtnMusicOn(AudioUtil.isMusicEnabled());
	}

}
