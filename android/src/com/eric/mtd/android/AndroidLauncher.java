package com.eric.mtd.android;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.eric.mtd.MTDGame;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;


public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Drive.API)
				.addScope(Drive.SCOPE_FILE)
				.build();
		initialize(new MTDGame(), config);
	}
}
