package com.eric.mtd.game.ui.view.widget;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.eric.mtd.game.helper.Resources;

public class MTDImage extends Image{
	public MTDImage(String layer, String objectName, String strAtlas, String regionName, boolean visible, boolean useImgAsSize){
		super(Resources.getAtlas(strAtlas).findRegion(regionName));
	    RectangleMapObject rectangle = (RectangleMapObject)Resources.getUIMap().getLayers().get(layer).getObjects().get(objectName);
	    if(useImgAsSize){
			TextureRegion imageRegion = (Resources.getAtlas(strAtlas).findRegion(regionName));
	    	setSize(imageRegion.getRegionWidth(),imageRegion.getRegionHeight());
	    }
	    else{
	    	setSize(rectangle.getRectangle().getWidth(),rectangle.getRectangle().getHeight());
	    }
	    setPosition(rectangle.getRectangle().x,rectangle.getRectangle().y);
	    setName(layer + "_" + objectName);
	    setVisible(visible);
	}
}
