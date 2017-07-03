package com.foxholedefense.game.ui.view.widgets;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.foxholedefense.util.ActorUtil;

/**
 * Created by Eric on 11/13/2016.
 */

public class DischargeButton extends Group {

    public ImageButton button;
    private Label lblCost;

    public DischargeButton(Skin skin) {

        this.setTransform(false);
        this.button = new ImageButton(skin, "discharge");
        button.setSize(133, 83);
        addActor(button);

        lblCost = new Label("", skin);
        lblCost.setFontScale(0.45f);
        lblCost.setAlignment(Align.center);
        lblCost.pack();
        float lblCostX = ActorUtil
            .calcBotLeftPointFromCenter(button.getWidth() / 2, lblCost.getWidth());
        lblCost.setPosition(lblCostX, 7);
        addActor(lblCost);

        Label lblTitle = new Label("DISCHARGE", skin);
        lblTitle.setFontScale(0.4f);
        lblTitle.setAlignment(Align.center);
        lblTitle.pack();
        float lblTitleX = ActorUtil
            .calcBotLeftPointFromCenter(button.getWidth() / 2, lblTitle.getWidth());
        lblTitle.setPosition(lblTitleX, 37);
        addActor(lblTitle);
    }

    public void updateCost(int cost) {

        lblCost.setText(String.valueOf(cost));
    }
}
