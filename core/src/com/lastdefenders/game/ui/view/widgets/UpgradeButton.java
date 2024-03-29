package com.lastdefenders.game.ui.view.widgets;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.Resources;

/**
 * Created by Eric on 11/12/2016.
 */

public class UpgradeButton extends Group {

    public ImageButton button;
    private Label lblCost;

    public UpgradeButton(Skin skin, String name, String iconName, int iconWidth, int iconHeight, float fontScale) {

        this.setTransform(false);
        createControls(skin, name, iconName, iconWidth, iconHeight, fontScale);
    }

    private void createControls(Skin skin, String name, String iconName, int iconWidth, int iconHeight, float fontScale){

        this.button = new ImageButton(skin, "upgrade");
        button.setSize(110, 115);
        addActor(button);

        lblCost = new Label(String.valueOf(0), skin);
        lblCost.setFontScale(0.45f * fontScale);
        lblCost.setAlignment(Align.center);
        lblCost.pack();
        float lblCostX = ActorUtil
            .calcBotLeftPointFromCenter(button.getWidth() / 2, lblCost.getWidth());
        lblCost.setPosition(lblCostX, 5);
        addActor(lblCost);

        Label lblTitle = new Label(name.replaceAll(" ", "\n"), skin);
        lblTitle.setFontScale(0.4f * fontScale);
        lblTitle.setAlignment(Align.center);
        lblTitle.pack();
        float lblTitleX = ActorUtil
            .calcBotLeftPointFromCenter(button.getWidth() / 2, lblTitle.getWidth());
        float lblTitleY = ActorUtil
            .calcBotLeftPointFromCenter(button.getHeight() / 2, lblTitle.getHeight());
        lblTitle.setPosition(lblTitleX, lblTitleY - 5);
        addActor(lblTitle);

        Image icon = new Image(skin.getAtlas().findRegion(iconName));
        icon.setSize(iconWidth, iconHeight);
        float iconX = ActorUtil.calcBotLeftPointFromCenter(25, icon.getWidth());
        float iconY = ActorUtil.calcBotLeftPointFromCenter(90, icon.getHeight());
        icon.setPosition(this.button.getWidth() / 2, 90, Align.center);
        addActor(icon);
    }

    public void updateCost(int cost) {

        lblCost.setText(String.valueOf(cost));
    }

    public void setPurchased(boolean purchased) {

        button.setChecked(purchased);
    }

}

