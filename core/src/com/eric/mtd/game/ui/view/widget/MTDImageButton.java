package com.eric.mtd.game.ui.view.widget;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.eric.mtd.game.helper.Logger;
import com.eric.mtd.game.helper.Resources;

public class MTDImageButton extends ImageButton{
	public MTDImageButton(String layer, String objectName, String strAtlas, String regionName, boolean visible, boolean useImgAsSize){
		super(new TextureRegionDrawable(Resources.getAtlas(strAtlas).findRegion(regionName)));
	    RectangleMapObject rectangle = (RectangleMapObject)Resources.getUIMap().getLayers().get(layer).getObjects().get(objectName);
	    TextureRegion imageRegion = (Resources.getAtlas(strAtlas).findRegion(regionName));
	    if(useImgAsSize){
	    	setSize(imageRegion.getRegionWidth(),imageRegion.getRegionHeight());
	    }
	    else{
	    	setSize(rectangle.getRectangle().getWidth(),rectangle.getRectangle().getHeight());
	    	if(Logger.DEBUG)System.out.println(objectName + " x:" +rectangle.getRectangle().getWidth() + " y:" + rectangle.getRectangle().getHeight());
	    }
    	if(Logger.DEBUG)System.out.println(objectName + " img x:" +imageRegion.getRegionWidth() + " y:" + imageRegion.getRegionHeight());
	    setPosition(rectangle.getRectangle().x,rectangle.getRectangle().y);
	    setName(layer + "_" + objectName);
	    setVisible(visible);
	}

}
