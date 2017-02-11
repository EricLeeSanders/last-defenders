package com.foxholedefense.menu.ui;

import com.foxholedefense.screen.IScreenChanger;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;

/**
 * Presenter class for the Main Menu
 * 
 * @author Eric
 *
 */
public class MenuPresenter {
	private IScreenChanger screenChanger;
	private IMenuView view;
	private FHDAudio audio;
	public MenuPresenter(IScreenChanger screenChanger, FHDAudio audio) {
		this.screenChanger = screenChanger;
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
		screenChanger.changeToLevelSelect();

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
