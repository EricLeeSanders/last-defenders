package com.foxholedefense.game.ui.view.widgets;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.foxholedefense.util.ActorUtil;

/**
 * Created by Eric on 11/12/2016.
 */

public class SupportButton extends Group{
    public ImageButton button;
    public int cost;
    public SupportButton(Skin skin, String name, int cost){
        this.setTransform(false);
        this.button = new ImageButton(skin, "support");
        this.cost = cost;
        addActor(button);

        Label lblTitle = new Label(name.toUpperCase().replaceAll(" ", "\n"), skin);
        lblTitle.setFontScale(0.40f);
        lblTitle.setAlignment(Align.center);
        lblTitle.pack();
        float lblTitleX = ActorUtil.calcXBotLeftFromCenter(button.getWidth() / 2, lblTitle.getWidth());
        float lblTitleY = ActorUtil.calcYBotLeftFromCenter(75, lblTitle.getHeight());
        lblTitle.setPosition(lblTitleX, lblTitleY);
        addActor(lblTitle);

        Label lblCost = new Label(String.valueOf(cost), skin);
        lblCost.setFontScale(0.45f);
        lblCost.setAlignment(Align.center);
        lblCost.pack();
        lblCost.setPosition(40, 11);
        addActor(lblCost);

    }
}
