package com.foxholedefense.game.ui.view.widgets;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.foxholedefense.util.ActorUtil;

/**
 * Created by Eric on 11/12/2016.
 */

public class UpgradeButton extends Group {
    public ImageButton button;
    private Label lblCost;
    public UpgradeButton(Skin skin, String name, String iconName, int cost, int iconWidth, int iconHeight, int xOffset, int yOffset ){
        this.setTransform(false);
        this.button = new ImageButton(skin, "upgrade");
        addActor(button);

        lblCost = new Label(String.valueOf(cost), skin);
        lblCost.setFontScale(0.45f);
        lblCost.setAlignment(Align.bottomLeft);
        lblCost.pack();
        lblCost.setPosition(38,2);
        addActor(lblCost);

        Label lblTitle = new Label(name.toUpperCase().replaceAll(" ", "\n"), skin);
        lblTitle.setFontScale(0.4f);
        lblTitle.setAlignment(Align.center);
        lblTitle.pack();
        float lblTitleX = ActorUtil.calcXBotLeftFromCenter(button.getWidth() / 2, lblTitle.getWidth());
        float lblTitleY = ActorUtil.calcYBotLeftFromCenter(button.getHeight() / 2, lblTitle.getHeight());
        lblTitle.setPosition(lblTitleX, lblTitleY-5);
        addActor(lblTitle);

        Image icon = new Image(skin.getAtlas().findRegion(iconName));
        icon.setSize(iconWidth, iconHeight);
        icon.setPosition(10 + xOffset, button.getHeight() - icon.getHeight() - 5 + yOffset);
        addActor(icon);
    }

    public void updateCost(int cost){
        lblCost.setText(String.valueOf(cost));
    }

    public void setPurchased(boolean purchased){
        button.setChecked(purchased);
    }

}

