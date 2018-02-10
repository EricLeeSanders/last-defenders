package com.lastdefenders.ui.widget.progressbar;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Eric on 2/8/2018.
 */

public class UnfilledLoadingBar extends Image {

    private ProgressBar progressBar;
    private TextureRegion texture;

    public UnfilledLoadingBar(Drawable image, ProgressBar progressBar) {
        super(image);
        this.progressBar = progressBar;
        texture = ((TextureRegionDrawable) getDrawable()).getRegion();
    }

    @Override
    public void draw(Batch batch, float parentAlpha ) {

        float width = getWidth() - (getWidth() * progressBar.getValue());
        float textureRegionWidth = texture.getRegionWidth() - (texture.getRegionWidth() * progressBar.getValue());
        float textureX = texture.getRegionX() + (texture.getRegionWidth() * progressBar.getValue());
        float x = getX() + (getWidth() * progressBar.getValue());


        batch.draw(
            texture.getTexture(),
            x,
            getY(),
            getWidth() / 2,
            getHeight() / 2,
            width,
            getHeight(),
            getScaleX(),
            getScaleY(),
            getRotation(),
            (int)textureX,
            texture.getRegionY(),
            (int)textureRegionWidth,
            texture.getRegionHeight(),
            false,
            false);
    }
}
