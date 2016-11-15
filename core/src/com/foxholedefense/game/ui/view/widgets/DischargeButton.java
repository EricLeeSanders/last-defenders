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
    public DischargeButton(Skin skin, int cost){
        this.setTransform(false);
        this.button = new ImageButton(skin, "discharge");
        addActor(button);

        lblCost = new Label(String.valueOf(cost), skin);
        lblCost.setFontScale(0.45f);
        lblCost.setAlignment(Align.bottomLeft);
        lblCost.pack();
        lblCost.setPosition(38,10);
        addActor(lblCost);

        Label lblTitle = new Label("DISCHARGE", skin);
        lblTitle.setFontScale(0.4f);
        lblTitle.setAlignment(Align.center);
        lblTitle.pack();
        float lblTitleX = ActorUtil.calcXBotLeftFromCenter(button.getWidth() / 2, lblTitle.getWidth());
        lblTitle.setPosition(lblTitleX, 62);
        addActor(lblTitle);
    }

    public void updateCost(int cost){
        lblCost.setText(String.valueOf(cost));
    }
}
