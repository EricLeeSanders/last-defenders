package com.lastdefenders.ui.widget;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.lastdefenders.ui.widget.progressbar.LDProgressBar;

/**
 * Created by Eric on 2/9/2018.
 */

public class LDSlider extends WidgetGroup {

    private LDProgressBar progressBar;
    private Slider slider;

    public LDSlider(LDProgressBar progressBar, Skin skin, Vector2 knobSize){
        this.progressBar = progressBar;
        this.slider = new Slider(progressBar.getMinValue(), progressBar.getMaxValue(),
            progressBar.getStepSize(), false, skin);
        slider.getStyle().knob.setMinWidth(knobSize.x);
        slider.getStyle().knob.setMinHeight(knobSize.y);
        progressBar.getStyle().background = null;
        addActor(progressBar);
        addActor(slider);
    }

    @Override
    public void act(float delta){
        super.act(delta);
        progressBar.setValue(slider.getValue());
    }

    public void setValue(float value){
        progressBar.setValue(value);
        slider.setValue(value);
    }

    @Override
    public void setSize(float width, float height){
        progressBar.setSize(width, height);
        slider.setSize(width, height);
        slider.getStyle().background.setMinHeight(height);
        slider.getStyle().background.setMinWidth(width);
    }

    public LDProgressBar getProgressBar(){
        return progressBar;
    }

    public Slider getSlider(){
        return slider;
    }

}
