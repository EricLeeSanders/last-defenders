package com.lastdefenders.ui.widget.progressbar;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Progress Bar that supports a frame drawable. Currently only supports horizontal.
 */

public class LDProgressBar extends WidgetGroup {

    private FilledLoadingBar loadingBarFilled;
    private UnfilledLoadingBar loadingBarUnfilled;
    private LDProgressBarPadding padding;
    private ProgressBar progressBar;

    public LDProgressBar (float min, float max, float stepSize, LDProgressBarPadding padding, Skin skin) {
        this(min, max, stepSize, padding, new LDProgressBarStyle(skin.get("default", LDProgressBarStyle.class)));
    }

    public LDProgressBar (float min, float max, float stepSize, LDProgressBarPadding padding, LDProgressBarStyle style) {
        setTransform(false);
        style.background = style.frame;
        progressBar = new ProgressBar(min, max, stepSize, false, style);
        this.padding = padding;
        createProgressBarGroup();
    }

    public void createProgressBarGroup(){
        LDProgressBarStyle style = getStyle();
        loadingBarFilled = new FilledLoadingBar(((TextureRegionDrawable) style.filled).getRegion());
        loadingBarUnfilled = new UnfilledLoadingBar(((TextureRegionDrawable) style.unfilled).getRegion(), progressBar);

        // Add padding
        loadingBarFilled.setPosition(padding.leftPad, padding.bottomPad);
        loadingBarUnfilled.setPosition(padding.leftPad, padding.bottomPad);

        addActor(loadingBarFilled);
        addActor(loadingBarUnfilled);
        addActor(progressBar);

    }

    @Override
    public void setSize(float width, float height){
        // Add padding
        // Multiply by two to offset the x/y movement
        if(loadingBarFilled != null && loadingBarUnfilled != null) {
            float w = width - (padding.rightPad + padding.leftPad);
            float h = height - (padding.topPad + padding.bottomPad);
            loadingBarFilled.setSize(w, h);
            loadingBarUnfilled.setSize(w, h);
        }
        if(getStyle().background != null) {
            getStyle().background.setMinWidth(width);
            getStyle().background.setMinHeight(height);
        }
        progressBar.setSize(width, height);
        super.setSize(width, height);
    }

    public void setValue(float value){
        progressBar.setValue(value);
    }

    public ProgressBar getProgressBar(){
        return progressBar;
    }

    public LDProgressBarStyle getStyle () {
        return (LDProgressBarStyle) progressBar.getStyle();
    }

    public void setFilledTextureRegion(TextureRegion textureRegion){
        loadingBarFilled.setTextureRegion(textureRegion);
    }

    public void setUnfilledTextureRegion(TextureRegion textureRegion){
        loadingBarUnfilled.setTextureRegion(textureRegion);
    }

    static public class LDProgressBarStyle extends ProgressBarStyle {
        public Drawable frame;
        public Drawable filled;
        public Drawable unfilled;

        public LDProgressBarStyle () {
        }

        public LDProgressBarStyle (Drawable frame, Drawable filled, Drawable unfilled) {

            this.frame = frame;
            this.filled = filled;
            this.unfilled = unfilled;
        }

        public LDProgressBarStyle (LDProgressBarStyle style) {

            this.frame = style.frame;
            this.filled = style.filled;
            this.unfilled = style.unfilled;
        }
    }

    static public class LDProgressBarPadding {

        private float leftPad;
        private float rightPad;
        private float topPad;
        private float bottomPad;

        public LDProgressBarPadding(float leftPad, float rightPad,
            float topPad, float bottomPad) {

            this.leftPad = leftPad;
            this.rightPad = rightPad;
            this.topPad = topPad;
            this.bottomPad = bottomPad;
        }

        public LDProgressBarPadding(float pad){
            this(pad, pad, pad, pad);
        }

        public LDProgressBarPadding(float widthPadding, float heightPadding){
            this(widthPadding, widthPadding, heightPadding, heightPadding);
        }
    }

}
