package com.lastdefenders.game.ui.view.widgets;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.lastdefenders.util.ActorUtil;

/**
 * Created by Eric on 11/12/2016.
 */

public class SupportButton extends Group {

    public ImageButton button;
    public int cost;

    public SupportButton(Skin skin, String name, int cost, float fontScale) {

        this.setTransform(false);
        this.button = new ImageButton(skin, "support");
        button.setSize(133, 100);
        this.cost = cost;
        addActor(button);

        Label lblTitle = new Label(name.replaceAll(" ", "\n"), skin);
        lblTitle.setFontScale(0.5f * fontScale);
        lblTitle.setAlignment(Align.center);
        lblTitle.pack();
        float lblTitleX = ActorUtil
            .calcBotLeftPointFromCenter(button.getWidth() / 2, lblTitle.getWidth());
        float lblTitleY = ActorUtil
            .calcBotLeftPointFromCenter(button.getHeight() / 2, lblTitle.getHeight());
        lblTitle.setPosition(lblTitleX, lblTitleY + 12);
        addActor(lblTitle);

        Label lblCost = new Label(String.valueOf(cost), skin);
        lblCost.setFontScale(0.45f * fontScale);
        lblCost.setAlignment(Align.center);
        lblCost.pack();
        float lblCostX = ActorUtil
            .calcBotLeftPointFromCenter(button.getWidth() / 2, lblCost.getWidth());
        lblCost.setPosition(lblCostX, 10);
        addActor(lblCost);

    }
}
