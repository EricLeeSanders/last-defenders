package com.eric.mtd.game.ui.view.widget.inspect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.eric.mtd.game.ui.view.widget.MTDLabel;
import com.eric.mtd.util.Resources;

/**
 * A widget that represents an Upgrade Level
 * 
 * @author Eric
 *
 */
public class UpgradeLevel extends Group {
	private int maxLevel;
	private MTDLabel[] lblLevels;

	/**
	 * Constructor that constructs an array of labels to represent what level an
	 * upgrade is on.
	 * 
	 * @param layer
	 * @param objectName
	 * @param text
	 * @param visible
	 * @param maxLevel
	 * @param fontColor
	 */
	public UpgradeLevel(String layer, String objectName, String text, boolean visible, int maxLevel, Color fontColor) {
		this.maxLevel = maxLevel;
		if (maxLevel <= 0) {
			return;
		}
		lblLevels = new MTDLabel[maxLevel];
		for (int i = 0; i < maxLevel; i++) {
			lblLevels[i] = new MTDLabel(layer, objectName + (i + 1), text, visible, fontColor, Align.center, 1f);
			lblLevels[i].getStyle().background = Resources.getSkin(Resources.SKIN_JSON).newDrawable("white", 0f, 0f, 0f, 1);
			addActor(lblLevels[i]);
		}
	}

	/**
	 * Reset all the levels
	 */
	public void resetLevels() {
		for (int i = 0; i < maxLevel; i++) {
			lblLevels[i].getStyle().background = Resources.getSkin(Resources.SKIN_JSON).newDrawable("white", 0f, 0f, 0f, 1);
		}
	}

	/**
	 * Add a level
	 * 
	 * @param level
	 */
	public void setLevel(int level) {
		for (int i = 0; i < level; i++) {
			lblLevels[i].getStyle().background = Resources.getSkin(Resources.SKIN_JSON).newDrawable("white", 0f, 1f, 0f, 1);
		}
	}
}
