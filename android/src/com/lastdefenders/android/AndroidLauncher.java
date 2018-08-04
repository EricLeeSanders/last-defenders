package com.lastdefenders.android;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.lastdefenders.LDGame;

public class AndroidLauncher extends AndroidApplication {

	private GooglePlayServicesHelper googlePlayServicesHelper;

	public AndroidLauncher(){
		this.googlePlayServicesHelper = new GooglePlayServicesHelper();
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useWakelock = true;
		View gameView = initializeForView(new LDGame(googlePlayServicesHelper), config);
		RelativeLayout layout = createLayout(gameView);
		googlePlayServicesHelper.initialize(this, layout, gameView);
	}

	private RelativeLayout createLayout(View gameView) {

		RelativeLayout relativeLayout = new RelativeLayout(this);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
			LayoutParams.MATCH_PARENT,
			LayoutParams.MATCH_PARENT);

		relativeLayout.addView(gameView);
		setContentView(relativeLayout, layoutParams);

		return relativeLayout;
	}

	@Override
	public void onBackPressed(){
		googlePlayServicesHelper.backButtonPressed();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == GooglePlayServicesHelper.RC_SIGN_IN) {
			googlePlayServicesHelper.handleGooglePlaySignInRequest(data);
		}
	}

	@Override
	protected  void onResume(){
		super.onResume();
		googlePlayServicesHelper.onResume();
	}
}
