package com.foxholedefense.game.ui.view.interfaces;

/**
 * Interface for Options View. Used by the Options Presenter to communicate with
 * the view
 * 
 * @author Eric
 *
 */
public interface IOptionsView {
	public void optionsState();

	public void standByState();
	
	public void setBtnShowRangesOn(boolean showRangesOn);
	
	public void setBtnSoundOn(boolean soundOn);
	
	public void setBtnMusicOn(boolean musicOn);
}
