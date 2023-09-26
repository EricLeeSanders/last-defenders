package com.lastdefenders.game.ui.view;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.lastdefenders.ads.AdController;
import com.lastdefenders.ads.AdControllerHelper;
import com.lastdefenders.game.ui.presenter.AdPresenter;
import com.lastdefenders.game.ui.view.interfaces.IAdView;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

public class AdView extends Group implements IAdView {

    private Resources resources;
    private AdPresenter adPresenter;

    public AdView(AdPresenter adPresenter, Resources resources){
        this.adPresenter = adPresenter;
        this.resources = resources;
    }

    public void init(){
        createControls();
    }

    @Override
    public void showPreAd() {
        this.setVisible(true);
    }

    @Override
    public void close() {
        this.setVisible(false);
    }

    private void createControls() {

        Logger.info("Game Over View: creating controls");

        Skin skin = resources.getSkin();

        Table table = new Table();
        table.setTransform(false);
        table.setBackground(skin.getDrawable("main-panel"));
        table.setSize(325, 300);
        table.setPosition(getStage().getViewport().getWorldWidth() / 2, getStage().getViewport().getWorldHeight() / 2, Align.center );
        this.addActor(table);

        Label lblTitle = new Label("Ads", skin);
        lblTitle.setHeight(50);
        float lblTitleX = table.getX(Align.center);
        float lblTitleY = table.getY(Align.top) - (lblTitle.getHeight()/2);
        lblTitle.setPosition(lblTitleX, lblTitleY, Align.center);
        lblTitle.setFontScale(0.7f * resources.getFontScale());
        lblTitle.setAlignment(Align.center);
        this.addActor(lblTitle);
        //table.add(lblTitle).width(275).height(40).padTop(35);

        table.row();
        Label description = new Label("Please support the developers\nby briefly viewing an ad or\nmake a purchase to remove ads!", skin);
        description.setFontScale(0.45f * resources.getFontScale());
        description.setAlignment(Align.center);
        table.add(description).width(275).height(40).colspan(2);

        table.row();
        ImageButton btnOk = new ImageButton(skin, "select");
        btnOk.getImageCell().size(30, 23);
        table.add(btnOk).width(50).height(50).spaceTop(60);
        setBtnOkListener(btnOk);

        ImageButton btnRemoveAds = new ImageButton(skin, "remove_ads_red");
        btnRemoveAds.getImageCell().size(40, 40);
        table.add(btnRemoveAds).width(50).height(50).spaceTop(60);
        setBtnRemoveAdsListener(btnRemoveAds);

        this.setVisible(false);

        Logger.info("Game Over View: controls created");
    }

    private void setBtnRemoveAdsListener(Button btnRemoveAds) {

        btnRemoveAds.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                adPresenter.removeAds();
            }
        });

    }

    private void setBtnOkListener(Button btnOk) {

        btnOk.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                adPresenter.showAd();
            }
        });

    }
}
