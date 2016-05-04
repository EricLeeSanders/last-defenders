package com.eric.mtd.game.ui.view.widget.support;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.eric.mtd.game.ui.view.widget.MTDImageButton;
import com.eric.mtd.util.Resources;

/**
 * A widget that represents a Support Button. Is associated with a Support and holds
 * a String Value of the Support Name
 * 
 * @author Eric
 *
 */
public class MTDSupportButton extends MTDImageButton {
	private String supportName;

	public MTDSupportButton(String layer, String objectName, String strAtlas, String enabledRegionName
			, String disabledRegionName, String supportName, boolean visible, boolean useImgAsSize) {
		super(layer, objectName, strAtlas, enabledRegionName, visible, useImgAsSize);
		super.getStyle().imageDisabled = new TextureRegionDrawable(Resources.getAtlas(strAtlas).findRegion(disabledRegionName));
		this.supportName = supportName;

	}

	public String getSupportName() {
		return supportName;
	}
}
