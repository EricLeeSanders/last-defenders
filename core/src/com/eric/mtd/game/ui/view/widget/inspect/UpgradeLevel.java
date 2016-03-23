package com.eric.mtd.game.ui.view.widget.inspect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.eric.mtd.game.helper.Resources;
import com.eric.mtd.game.ui.view.widget.MTDLabel;

public class UpgradeLevel extends Group{// extends Label{
	private int maxLevel;
	private MTDLabel [] lblLevels;
	public UpgradeLevel(String layer,String objectName, String text, boolean visible, int maxLevel, Color fontColor) {
		this.maxLevel = maxLevel;
		if(maxLevel <= 0){
			return;
		}
		lblLevels = new MTDLabel[maxLevel];
		for(int i = 0; i < maxLevel; i++){
			lblLevels[i] = new MTDLabel(layer,objectName + (i+1),text,visible, fontColor, Align.center,1f);
			lblLevels[i].getStyle().background = Resources.getSkin(Resources.SKIN_JSON).newDrawable("white", 0f,0f,0f,1);
			addActor(lblLevels[i]);
		}
	}
	public void resetLevels(){
		for(int i = 0; i < maxLevel; i++){
			lblLevels[i].getStyle().background = Resources.getSkin(Resources.SKIN_JSON).newDrawable("white", 0f, 0f, 0f, 1);
		}
	}
	public void setLevel(int level){
		for(int i = 0; i < level; i++){
			lblLevels[i].getStyle().background = Resources.getSkin(Resources.SKIN_JSON).newDrawable("white", 0f, 1f, 0f, 1);
		}
	}
}
