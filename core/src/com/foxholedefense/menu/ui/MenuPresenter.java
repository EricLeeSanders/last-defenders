package com.foxholedefense.menu.ui;

import com.foxholedefense.screen.state.ScreenStateManager;
import com.foxholedefense.screen.state.ScreenStateManager.ScreenState;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;

/**
 * Presenter class for the Main Menu
 * 
 * @author Eric
 *
 */
public class MenuPresenter {
	private ScreenStateManager screenStateManager;
	private IMenuView view;
	private FHDAudio audio;
	public MenuPresenter(ScreenStateManager screenStateManager, FHDAudio audio) {
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
		audio.playSound(FHDSound.LARGE_CLICK);
		screenStateManager.setState(ScreenState.LEVEL_SELECTION);

	}
	
	public void soundPressed() {
		audio.playSound(FHDSound.SMALL_CLICK);
		audio.changeSoundEnabled();
		view.setBtnSoundOn(audio.isSoundEnabled());
	}
	
	public void musicPressed() {
		audio.playSound(FHDSound.SMALL_CLICK);
		audio.changeMusicEnabled();
		view.setBtnMusicOn(audio.isMusicEnabled());
	}
	public void volumeChanged(float vol){
		audio.setMasterVolume(vol);
	}
	
	public float getMasterVolume(){
		return audio.getMasterVolume();
	}

}
