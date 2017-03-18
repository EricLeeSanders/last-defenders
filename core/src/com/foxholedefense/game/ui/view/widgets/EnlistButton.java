package com.foxholedefense.game.ui.view.widgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.Resources;

/**
 * Created by Eric on 11/4/2016.
 */

public class EnlistButton extends Group {
    public ImageButton button;
    public int cost;
    public EnlistButton(Skin skin, float attack, float health, float range, float speed, String name, int cost){
        this.setTransform(false);
        this.button = new ImageButton(skin, "enlist");
        this.cost = cost;
        addActor(button);
        createBar(skin, attack, 108, "attack_icon", 18,18, 2, 3, name, cost);
        createBar(skin, health, 83, "heart", 18,18, 2, 2, name, cost);
        createBar(skin, range, 58, "range_icon", 18,19, 2, 1, name, cost);
        createBar(skin, speed, 33, "speed_icon", 18,18, 2, 1, name, cost);
    }

    private void createBar(Skin skin, float attrValue, float y, String iconName, float iconWidth, float iconHeight, float iconX, float iconY, String name, int cost){
        Image fullbar = new Image(skin.getAtlas().findRegion("tower-attr-full"));
        fullbar.setSize(64,15);
        fullbar.setPosition(36,y);
        addActor(fullbar);

        Image bg = new Image(skin.getAtlas().findRegion("slider-bg"));
        bg.setY(y);
        setBgPosition(bg, attrValue/10, 37, 62);
        addActor(bg);

        Image frame = new Image(skin.getAtlas().findRegion("tower-stat"));
        frame.setPosition(13,y-1);
        addActor(frame);

        Image icon = new Image(skin.getAtlas().findRegion(iconName));
        icon.setSize(iconWidth, iconHeight);
        icon.setPosition(iconX + frame.getX() , iconY + frame.getY());
        addActor(icon);

        Label lblCost = new Label(String.valueOf(cost), skin);
        lblCost.setFontScale(0.45f);
        lblCost.setAlignment(Align.bottomLeft);
        lblCost.pack();
        lblCost.setPosition(40,2);
        addActor(lblCost);

        Label lblTitle = new Label(name.toUpperCase().replaceAll(" ", "\n"), skin);
        lblTitle.setFontScale(0.40f);
        lblTitle.setAlignment(Align.center);
        lblTitle.pack();
        float lblTitleX = ActorUtil.calcXBotLeftFromCenter(button.getWidth() / 2, lblTitle.getWidth());
        float lblTitleY = ActorUtil.calcYBotLeftFromCenter(155, lblTitle.getHeight());
        lblTitle.setPosition(lblTitleX, lblTitleY);
        addActor(lblTitle);
    }

    private void setBgPosition(Image bg, float value, float start, float end){
        float x = start + end * value;
        bg.setX(x);
        bg.setSize(end - ( end * value), 15);
    }
}
