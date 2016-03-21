package com.eric.mtd.ui.view.widget;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.eric.mtd.helper.Resources;

public class MTDTextButton extends TextButton{
	public MTDTextButton(String layer, String objectName, String buttonText,boolean visible) {
		this(layer,objectName,buttonText,Align.center,visible);
	}
	public MTDTextButton(String layer, String objectName, String buttonText, int labelAlign, boolean visible) {
		super(buttonText, Resources.getSkin(Resources.SKIN_JSON));
		this.getLabel().setAlignment(labelAlign);
		this.getLabel().setFontScale(0.45f);
		RectangleMapObject rectangle = (RectangleMapObject)Resources.getUIMap().getLayers().get(layer).getObjects().get(objectName);
		setSize(rectangle.getRectangle().getWidth(),rectangle.getRectangle().getHeight());
		setPosition(rectangle.getRectangle().x,rectangle.getRectangle().y);
		setName(layer + "_" + objectName);
		setVisible(visible);
	}

}
