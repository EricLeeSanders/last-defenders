package com.eric.mtd.game.ui.view.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.eric.mtd.util.Resources;

public class MTDLabel extends Label {

	public MTDLabel(String layer, String objectName, String text, boolean visible, Color fontColor, int align, float fontSizeScale) {
		super(text, new LabelStyle(Resources.getSkin(Resources.SKIN_JSON).get(LabelStyle.class)));
		this.getStyle().fontColor = fontColor;
		this.setAlignment(align);
		this.setFontScale(fontSizeScale);
		// this.setFontScale(3);
		RectangleMapObject rectangle = (RectangleMapObject) Resources.getUIMap().getLayers().get(layer).getObjects().get(objectName);
		setSize(rectangle.getRectangle().getWidth(), rectangle.getRectangle().getHeight());
		setPosition(rectangle.getRectangle().x, rectangle.getRectangle().y);
		setName(objectName);
		setVisible(visible);

	}

}
