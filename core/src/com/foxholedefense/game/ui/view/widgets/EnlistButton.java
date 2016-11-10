package com.foxholedefense.game.ui.view.widgets;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.foxholedefense.util.Resources;

/**
 * Created by Eric on 11/4/2016.
 */

public class EnlistButton extends Group {
    public ImageButton button;
    public EnlistButton(Skin skin, float attack, float health, float range, float speed){
        this.setTransform(false);
        this.button = new ImageButton(skin, "enlist");
        addActor(button);
        createBar(skin, attack, 108, "attack_icon", 18,18, 2, 3);
        createBar(skin, health, 83, "heart", 18,18, 2, 2);
        createBar(skin, range, 58, "range_icon", 18,19, 2, 1);
        createBar(skin, speed, 33, "speed_icon", 18,18, 2, 1);
    }

    private void createBar(Skin skin, float attrValue, float y, String iconName, float iconHeight, float iconWidth, float iconX, float iconY){
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
        icon.setSize(iconHeight, iconWidth);
        icon.setPosition(iconX + frame.getX() , iconY + frame.getY());
        addActor(icon);
    }

    private void setBgPosition(Image bg, float value, float start, float end){
        float x = start + end * value;
        bg.setX(x);
        bg.setSize(end - ( end * value), 15);
    }
}
