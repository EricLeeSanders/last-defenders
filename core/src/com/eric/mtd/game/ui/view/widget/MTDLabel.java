package com.eric.mtd.game.ui.view.widget;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.eric.mtd.util.Dimension;
import com.eric.mtd.util.Resources;
/**
 * This classes creates a group of two labels. One has the image and the other has just a transparent bg and is placed
 * on top of the image. It's necessary to separate these two labels because I cannot pad the text and position the text.
 * @author Eric
 *
 */
public class MTDLabel extends Group{
	private Label label_img, label_text;
	public MTDLabel(String text, Skin skin, String lbl_img_name, String font_name, int align ){
		
		LabelStyle style_img = new LabelStyle(skin.get(lbl_img_name, LabelStyle.class));
		style_img.font = Resources.getFont(font_name);
		label_img = new Label("", style_img);
		addActor(label_img);
		
		LabelStyle style_text = new LabelStyle(skin.get("default", LabelStyle.class));
		style_text.font = Resources.getFont(font_name);
		label_text = new Label(text, style_text);
		label_text.setAlignment(align);
		addActor(label_text);
		
	}
	public Label getLabel_img() {
		return label_img;
	}
	public void setLabel_img(Label label_img) {
		this.label_img = label_img;
	}
	public Label getLabel_text() {
		return label_text;
	}
	public void setLabel_text(Label label_text) {
		this.label_text = label_text;
	}

}
