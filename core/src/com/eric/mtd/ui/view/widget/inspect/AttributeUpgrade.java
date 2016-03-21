package com.eric.mtd.ui.view.widget.inspect;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.eric.mtd.ui.view.widget.MTDImage;
import com.eric.mtd.ui.view.widget.MTDImageButton;
import com.eric.mtd.ui.view.widget.MTDLabel;

public class AttributeUpgrade extends Group{
	private MTDImage image, icon; 
	private MTDImage button;
	private UpgradeLevel upgradeLevel;
	public MTDImage getImage() {
		return image;
	}
	public void setImage(MTDImage image) {
		if(this.image != null){
			this.removeActor(this.image);
		}
		this.image = image;
		addActor(image);
	}
	public MTDImage getIcon() {
		return icon;
	}
	public void setIcon(MTDImage icon) {
		if(this.icon != null){
			this.removeActor(this.icon);
		}
		this.icon = icon;
		addActor(icon);
	}
	public MTDImage getButton() {
		return button;
	}
	public void setButton(MTDImage button) {
		if(this.button != null){
			this.removeActor(this.button);
		}
		this.button = button;
		addActor(button);
	}
	public UpgradeLevel getUpgradeLevel() {
		return upgradeLevel;
	}
	public void setUpgradeLevel(UpgradeLevel upgradeLevel) {
		if(this.upgradeLevel != null){
			this.removeActor(this.upgradeLevel);
		}
		this.upgradeLevel = upgradeLevel;
		addActor(upgradeLevel);
	}
	
	
}
