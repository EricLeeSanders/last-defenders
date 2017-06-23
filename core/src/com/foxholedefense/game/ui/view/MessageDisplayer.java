package com.foxholedefense.game.ui.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 3/31/2017.
 */

public class MessageDisplayer extends Group implements com.foxholedefense.game.ui.view.interfaces.MessageDisplayer {
    private static final float MESSAGE_DURATION = 2;
    private static final float DEFAULT_FONT_SCALE = 0.35f;
    private static final Color DEFAULT_FONT_COLOR = Color.RED;
    private Map<Color, LabelStyle> labelStyleMap = new HashMap<>();
    private Label messageLabel;
    private Skin skin;

    public MessageDisplayer(Skin skin){
        this.skin = skin;
        messageLabel = new Label("", skin);
    }

    @Override
    public void displayMessage(String message) {
        displayMessage(message, DEFAULT_FONT_SCALE, DEFAULT_FONT_COLOR );
    }

    @Override
    public void displayMessage(String message, Color color) {
        displayMessage(message, DEFAULT_FONT_SCALE, color);
    }

    @Override
    public void displayMessage(String message, float fontScale) {
        displayMessage(message, fontScale, DEFAULT_FONT_COLOR);
    }

    @Override
    public void displayMessage(String message, float fontScale, Color color) {
        Logger.info("HUDView: displaying message: " + message);
        LabelStyle style = getLabelStyleByColor(color);
        messageLabel.setStyle(style);
        messageLabel.clearActions();
        messageLabel.setText(message.toUpperCase());
        messageLabel.setFontScale(fontScale);
        messageLabel.pack();
        messageLabel.setPosition((Resources.VIRTUAL_WIDTH / 2) - (messageLabel.getWidth() / 2), (Resources.VIRTUAL_HEIGHT / 2) + 50);
        messageLabel.addAction(Actions.moveTo(messageLabel.getX(), messageLabel.getY() + 50, MESSAGE_DURATION));

        messageLabel.addAction(Actions.sequence(
                Actions.moveTo(messageLabel.getX(), messageLabel.getY() + 100, MESSAGE_DURATION),
                Actions.removeActor()));
        addActor(messageLabel);

    }

    private LabelStyle getLabelStyleByColor(Color color){
        LabelStyle style = labelStyleMap.get(color);
        if(style == null){
            style = new Label.LabelStyle(skin.get(LabelStyle.class));
            style.fontColor = color;
            labelStyleMap.put(color, style);
        }

        return style;
    }

}
