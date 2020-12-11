package com.lastdefenders.ui.view.widget.progressbar;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;

/**
 * Created by Eric on 2/8/2018.
 */

public class UnfilledLoadingBar extends Actor {

    private ProgressBar progressBar;
    private TextureRegion textureRegion;

    public UnfilledLoadingBar(TextureRegion textureRegion, ProgressBar progressBar) {
        this.progressBar = progressBar;
        this.textureRegion = textureRegion;
    }

    @Override
    public void draw(Batch batch, float parentAlpha ) {
        System.out.println(textureRegion == null);
        float width = getWidth() - (getWidth() * (progressBar.getValue()/progressBar.getMaxValue()));
        float textureRegionWidth = textureRegion.getRegionWidth() -
            (textureRegion.getRegionWidth() * (progressBar.getValue()/progressBar.getMaxValue()));
        float textureX = textureRegion.getRegionX() +
            (textureRegion.getRegionWidth() * (progressBar.getValue()/progressBar.getMaxValue()));
        float x = getX() + (getWidth() * (progressBar.getValue()/progressBar.getMaxValue()));

        batch.draw(
            textureRegion.getTexture(),
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
            textureRegion.getRegionY(),
            (int)textureRegionWidth,
            textureRegion.getRegionHeight(),
            false,
            false);
    }

    protected void setTextureRegion(TextureRegion textureRegion){
        this.textureRegion = textureRegion;
    }

}
