package com.eric.mtd.game.ui.view.widget.enlist;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.eric.mtd.game.ui.view.widget.MTDImageButton;
import com.eric.mtd.util.Resources;

/**
 * A widget that represents a Tower Button. Is associated with a Tower and holds
 * a String Value of the Tower Name
 * 
 * @author Eric
 *
 */
public class MTDTowerButton extends ImageButton{
	private String towerName;
	public MTDTowerButton(String towerName){
		super(new TextureRegionDrawable(Resources.getAtlas(Resources.ENLIST_ATLAS).findRegion("btnEnlist")));
		this.towerName = towerName;
	}

	public String getTowerName() {
		return towerName;
	}
}
