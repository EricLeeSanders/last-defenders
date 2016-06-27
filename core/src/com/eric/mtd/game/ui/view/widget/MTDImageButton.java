package com.eric.mtd.game.ui.view.widget;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.eric.mtd.util.Resources;

public class MTDImageButton extends ImageButton {
	public MTDImageButton(String layer, String objectName, String strAtlas, String regionName, boolean visible, boolean useImgAsSize) {
		super(new TextureRegionDrawable(Resources.getAtlas(strAtlas).findRegion(regionName)));
		RectangleMapObject rectangle = (RectangleMapObject) Resources.getUIMap().getLayers().get(layer).getObjects().get(objectName);
		TextureRegion imageRegion = (Resources.getAtlas(strAtlas).findRegion(regionName));
		if (useImgAsSize) {
			setSize(imageRegion.getRegionWidth(), imageRegion.getRegionHeight());
			setBounds(rectangle.getRectangle().x, rectangle.getRectangle().y, imageRegion.getRegionWidth(), imageRegion.getRegionHeight());
		} else {
			setSize(rectangle.getRectangle().getWidth(), rectangle.getRectangle().getHeight());
			setBounds(rectangle.getRectangle().x, rectangle.getRectangle().y, rectangle.getRectangle().getWidth(), rectangle.getRectangle().getHeight());
		}
		setPosition(rectangle.getRectangle().x, rectangle.getRectangle().y);
		setName(layer + "_" + objectName);
		setVisible(visible);
	}

	public MTDImageButton(String layer, String objectName, String strAtlas, String enabledRegionName, String disabledRegionName, boolean visible, boolean useImgAsSize) {
		this(layer, objectName, strAtlas, enabledRegionName, visible, useImgAsSize);
		ImageButtonStyle style =  getStyle();
		style.imageDisabled = new TextureRegionDrawable(Resources.getAtlas(strAtlas).findRegion(disabledRegionName));
		setStyle(style);
	}
	public MTDImageButton(String layer, String objectName, Skin skin, String drawableName, boolean visible, boolean useImgAsSize) {
		super(skin, drawableName);
		RectangleMapObject rectangle = (RectangleMapObject) Resources.getUIMap().getLayers().get(layer).getObjects().get(objectName);
		if (useImgAsSize) {
			setSize(getWidth(), getHeight());
			setBounds(rectangle.getRectangle().x, rectangle.getRectangle().y, getWidth(), getHeight());
		} else {
			setSize(rectangle.getRectangle().getWidth(), rectangle.getRectangle().getHeight());
			setBounds(rectangle.getRectangle().x, rectangle.getRectangle().y, rectangle.getRectangle().getWidth(), rectangle.getRectangle().getHeight());
		}
		setPosition(rectangle.getRectangle().x, rectangle.getRectangle().y);
		setName(layer + "_" + objectName);
		setVisible(visible);
	}
}
