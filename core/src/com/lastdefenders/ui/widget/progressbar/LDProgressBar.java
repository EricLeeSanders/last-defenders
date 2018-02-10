package com.lastdefenders.ui.widget.progressbar;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Progress Bar that supports a frame drawable. Currently only supports horizontal.
 */

public class LDProgressBar extends ProgressBar {

    public Image loadingBarFilled;
    public UnfilledLoadingBar loadingBarUnfilled;
    private LDProgressBarStyle style;
    private LDProgressBarPadding padding;

    public LDProgressBar (float min, float max, float stepSize, LDProgressBarPadding padding, Skin skin) {
        this(min, max, stepSize, padding, skin.get("default", LDProgressBarStyle.class), skin);
    }

    public LDProgressBar (float min, float max, float stepSize, LDProgressBarPadding padding, Skin skin, String styleName) {
        this(min, max, stepSize, padding, skin.get(styleName, LDProgressBarStyle.class), skin);
    }

    public LDProgressBar (float min, float max, float stepSize, LDProgressBarPadding padding, LDProgressBarStyle style, Skin skin) {
        super(min, max, stepSize, false, style);
        this.style = style;
        style.background = style.frame;
        setStyle(style);
        this.padding = padding;
        createProgressBarGroup();
    }

    public void createProgressBarGroup(){
        loadingBarFilled = new Image(style.filled);
        loadingBarUnfilled = new UnfilledLoadingBar(style.unfilled, this);
        // Add padding
        loadingBarFilled.setPosition(padding.filledPadWidth,padding.filledPadHeight);
        loadingBarUnfilled.setPosition(padding.unfilledPadWidth,padding.unfilledPadHeight);

        debug();
    }

    @Override
    public void draw(Batch batch, float parentAlpha ) {

        loadingBarFilled.draw(batch, parentAlpha);
        loadingBarUnfilled.draw(batch, parentAlpha);

        super.draw(batch, parentAlpha);
    }

    @Override
    public void setPosition(float x, float y){
        super.setPosition(x, y);
        loadingBarFilled.setPosition(x + padding.filledPadWidth, y + padding.filledPadHeight);
        loadingBarUnfilled.setPosition(x + padding.unfilledPadWidth, y + padding.unfilledPadHeight);
    }

    @Override
    public void setSize(float width, float height){
        // Add padding
        // Multiply by two to offset the x/y movement
        if(loadingBarFilled != null && loadingBarUnfilled != null) {
            loadingBarFilled.setSize(width - (padding.filledPadWidth * 2),
                height - (padding.filledPadHeight * 2));
            loadingBarUnfilled.setSize(width - (padding.unfilledPadWidth * 2),
                height - (padding.unfilledPadHeight * 2));
        }
        super.setSize(width, height);
    }

    @Override
    public LDProgressBarStyle getStyle () {
        return style;
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

        private float filledPadWidth;
        private float filledPadHeight;
        private float unfilledPadWidth;
        private float unfilledPadHeight;

        public LDProgressBarPadding(float filledPadWidth, float filledPadHeight,
            float unfilledPadWidth, float unfilledPadHeight) {

            this.filledPadWidth = filledPadWidth;
            this.filledPadHeight = filledPadHeight;
            this.unfilledPadWidth = unfilledPadWidth;
            this.unfilledPadHeight = unfilledPadHeight;
        }
    }

}
