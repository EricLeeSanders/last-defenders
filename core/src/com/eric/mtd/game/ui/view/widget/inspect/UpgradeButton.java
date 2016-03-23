package com.eric.mtd.game.ui.view.widget.inspect;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.eric.mtd.game.helper.Resources;
import com.eric.mtd.game.ui.view.widget.MTDImageButton;

public class UpgradeButton extends MTDImageButton{

	public UpgradeButton(String layer, String objectName, String strAtlas,String enabledRegionName, String disabledRegionName,
			boolean visible, boolean useImgAsSize) {
		super(layer, objectName, strAtlas, enabledRegionName, visible, useImgAsSize);
		super.getStyle().imageDisabled = new TextureRegionDrawable(Resources.getAtlas(strAtlas).findRegion(disabledRegionName));
		// TODO Auto-generated constructor stub
	}

}
