package com.lastdefenders.game.ui.view.widgets;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.lastdefenders.ui.widget.progressbar.LDProgressBar;
import com.lastdefenders.ui.widget.progressbar.LDProgressBar.LDProgressBarPadding;
import com.lastdefenders.ui.widget.progressbar.LDProgressBar.LDProgressBarStyle;
import com.lastdefenders.util.ActorUtil;

/**
 * Created by Eric on 11/4/2016.
 */

public class EnlistButton extends Group {

    public ImageButton button;
    public int cost;

    public EnlistButton(Skin skin, float attack, float health, float range, float speed,
        String name, int cost, float fontScale) {

        this.setTransform(false);
        this.button = new ImageButton(skin, "enlist");
        button.setSize(120, 195);
        this.cost = cost;
        addActor(button);

        Label lblCost = new Label(String.valueOf(cost), skin);
        lblCost.setFontScale(0.45f * fontScale);
        lblCost.pack();
        float lblCostX = ActorUtil
            .calcBotLeftPointFromCenter(button.getWidth() / 2, lblCost.getWidth());
        lblCost.setPosition(lblCostX, 10);
        addActor(lblCost);

        Label lblTitle = new Label(name.toUpperCase().replaceAll(" ", "\n"), skin);
        lblTitle.setFontScale(0.40f * fontScale);
        lblTitle.setAlignment(Align.center);
        lblTitle.pack();
        float lblTitleX = ActorUtil
            .calcBotLeftPointFromCenter(button.getWidth() / 2, lblTitle.getWidth());
        float lblTitleY = ActorUtil.calcBotLeftPointFromCenter(155, lblTitle.getHeight());
        lblTitle.setPosition(lblTitleX, lblTitleY);
        addActor(lblTitle);

        createBar(skin, attack, 110, "attack_icon", 18, 18, 2, 3);
        createBar(skin, health, 85, "heart", 18, 17, 2, 2);
        createBar(skin, range, 60, "range_icon", 18, 19, 2, 1);
        createBar(skin, speed, 35, "speed_icon", 18, 18, 2, 1);
    }

    private void createBar(Skin skin, float attrValue, float y, String iconName, float iconWidth,
        float iconHeight, float iconX, float iconY) {

        LDProgressBarPadding progressBarPadding = new LDProgressBarPadding(24,3f,8,1.5f);
        LDProgressBarStyle style = new LDProgressBarStyle(skin.get("default", LDProgressBarStyle.class));
        style.frame = new TextureRegionDrawable(skin.getAtlas().findRegion("tower-stat"));
        LDProgressBar progressBar = new LDProgressBar(0,10, 0.000001f, progressBarPadding, style);
        progressBar.setValue(attrValue);
        progressBar.setSize(92, 24);
        progressBar.setPosition(14,y);
        addActor(progressBar);

        Image icon = new Image(skin.getAtlas().findRegion(iconName));
        icon.setSize(iconWidth, iconHeight);
        icon.setPosition(iconX + progressBar.getX(), iconY + progressBar.getY());
        addActor(icon);

    }
}
