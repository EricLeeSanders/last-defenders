package com.lastdefenders.ui.view.widget.progressbar;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Eric on 2/15/2018.
 */

public class FilledLoadingBar extends Actor {

    private TextureRegion textureRegion;

    public FilledLoadingBar(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    @Override
    public void draw(Batch batch, float parentAlpha ) {

        batch.draw(
            textureRegion,
            getX(),
            getY(),
            getWidth() / 2,
            getHeight() / 2,
            getWidth(),
            getHeight(),
            getScaleX(),
            getScaleY(),
            getRotation());
    }

    protected void setTextureRegion(TextureRegion textureRegion){
        this.textureRegion = textureRegion;
    }
}
