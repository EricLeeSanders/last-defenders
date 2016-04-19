package com.eric.mtd.game.ui.view.widget.enlist;

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
public class MTDTowerButton extends MTDImageButton {
	private String towerName;

	public MTDTowerButton(String layer, String objectName, String strAtlas, String enabledRegionName, String disabledRegionName, String towerName, boolean visible, boolean useImgAsSize) {
		super(layer, objectName, strAtlas, enabledRegionName, visible, useImgAsSize);
		super.getStyle().imageDisabled = new TextureRegionDrawable(Resources.getAtlas(strAtlas).findRegion(disabledRegionName));
		this.towerName = towerName;

	}

	public String getTowerName() {
		return towerName;
	}
}
