package com.foxholedefense.game.ui.view.widgets;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.foxholedefense.util.ActorUtil;

/**
 * Created by Eric on 11/4/2016.
 */

public class EnlistButton extends Group {
    public ImageButton button;
    public int cost;

    public EnlistButton(Skin skin, float attack, float health, float range, float speed, String name, int cost) {
        this.setTransform(false);
        this.button = new ImageButton(skin, "enlist");
        button.setSize(120, 195);
        this.cost = cost;
        addActor(button);


        Label lblCost = new Label(String.valueOf(cost), skin);
        //lblCost.setAlignment(Align.center);
        lblCost.setFontScale(0.45f);
        lblCost.pack();
        float lblCostX = ActorUtil.calcBotLeftPointFromCenter(button.getWidth() / 2, lblCost.getWidth());
        lblCost.setPosition(lblCostX, 10);
        addActor(lblCost);

        Label lblTitle = new Label(name.toUpperCase().replaceAll(" ", "\n"), skin);
        lblTitle.setFontScale(0.40f);
        lblTitle.setAlignment(Align.center);
        lblTitle.pack();
        float lblTitleX = ActorUtil.calcBotLeftPointFromCenter(button.getWidth() / 2, lblTitle.getWidth());
        float lblTitleY = ActorUtil.calcBotLeftPointFromCenter(155, lblTitle.getHeight());
        lblTitle.setPosition(lblTitleX, lblTitleY);
        addActor(lblTitle);

        createBar(skin, attack, 110, "attack_icon", 18, 18, 2, 3);
        createBar(skin, health, 85, "heart", 18, 18, 2, 2);
        createBar(skin, range, 60, "range_icon", 18, 19, 2, 1);
        createBar(skin, speed, 35, "speed_icon", 18, 18, 2, 1);
    }

    private void createBar(Skin skin, float attrValue, float y, String iconName, float iconWidth, float iconHeight, float iconX, float iconY) {
        Image fullbar = new Image(skin.getAtlas().findRegion("tower-attr-full"));
        fullbar.setSize(64, 15);
        fullbar.setPosition(36, y);
        addActor(fullbar);

        Image bg = new Image(skin.getAtlas().findRegion("slider-bg"));
        bg.setY(y);
        setBgPosition(bg, attrValue / 10, 37, 64);
        addActor(bg);

        Image frame = new Image(skin.getAtlas().findRegion("tower-stat"));
        frame.setPosition(14, y - 1);
        addActor(frame);

        Image icon = new Image(skin.getAtlas().findRegion(iconName));
        icon.setSize(iconWidth, iconHeight);
        icon.setPosition(iconX + frame.getX(), iconY + frame.getY());
        addActor(icon);
    }

    private void setBgPosition(Image bg, float value, float start, float end) {
        float x = start + end * value;
        bg.setX(x);
        bg.setSize(end - (end * value), 15);
    }
}
