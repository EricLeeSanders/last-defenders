package com.eric.mtd.menu.ui;

import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;
import com.eric.mtd.util.MTDAudio;
import com.eric.mtd.util.MTDAudio.MTDSound;

/**
 * Presenter class for the Main Menu
 * 
 * @author Eric
 *
 */
public class MenuPresenter {
	private ScreenStateManager screenStateManager;
	private IMenuView view;
	private MTDAudio audio;
	public MenuPresenter(ScreenStateManager screenStateManager, MTDAudio audio) {
		this.screenStateManager = screenStateManager;
		this.audio = audio;
	}
	public void setView(IMenuView view){
		this.view = view;
		initView();
	}
	private void initView(){
		view.setBtnMusicOn(audio.isMusicEnabled());
		view.setBtnSoundOn(audio.isSoundEnabled());
	}
	public void playGame() {
		audio.playSound(MTDSound.LARGE_CLICK);
		screenStateManager.setState(ScreenState.LEVEL_SELECTION);

	}
	
	public void soundPressed() {
		audio.playSound(MTDSound.SMALL_CLICK);
		audio.changeSoundEnabled();
		view.setBtnSoundOn(audio.isSoundEnabled());
	}
	
	public void musicPressed() {
		audio.playSound(MTDSound.SMALL_CLICK);
		audio.changeMusicEnabled();
		view.setBtnMusicOn(audio.isMusicEnabled());
	}

}
