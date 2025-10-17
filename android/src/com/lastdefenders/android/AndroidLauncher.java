package com.lastdefenders.android;

import android.content.Context;
import android.os.Bundle;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidAudio;
import com.badlogic.gdx.backends.android.AsynchronousAndroidAudio;
import com.badlogic.gdx.pay.PurchaseManager;
import com.badlogic.gdx.pay.android.googlebilling.PurchaseManagerGoogleBilling;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.lastdefenders.LDGame;
import com.lastdefenders.log.EventLogger;
import com.lastdefenders.util.Logger;

public class AndroidLauncher extends AndroidApplication {

	private GooglePlayServicesHelper googlePlayServicesHelper;
	private GoogleAdsControllerImpl adController;

	public AndroidLauncher(){
		this.googlePlayServicesHelper = new GooglePlayServicesHelper();
		this.adController = new GoogleAdsControllerImpl();
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		FirebaseAnalytics fb = FirebaseAnalytics.getInstance(this);
		EventLogger eventLogger = new FirebaseEventLogger(fb);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useWakelock = true;
		config.useImmersiveMode = true;

		// Check Google Play Services availability before initializing billing
		int gpsAvailability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
		Logger.info("AndroidLauncher: Google Play Services availability check:");
		Logger.info("  - Result code: " + gpsAvailability);
		Logger.info("  - Result name: " + getConnectionResultName(gpsAvailability));
		Logger.info("  - Is available: " + (gpsAvailability == ConnectionResult.SUCCESS));

		if (gpsAvailability != ConnectionResult.SUCCESS) {
			Logger.error("AndroidLauncher: Google Play Services not available - billing may fail");
			Logger.error("  - User recoverable: " + GoogleApiAvailability.getInstance().isUserResolvableError(gpsAvailability));
		}

		PurchaseManager purchaseManager = new PurchaseManagerGoogleBilling(this);

		View gameView = initializeForView(
			new LDGame(googlePlayServicesHelper, adController, eventLogger, purchaseManager, new ErrorReporterImpl()),
			config);
		RelativeLayout layout = createLayout(gameView);
		googlePlayServicesHelper.initialize(this, layout);
		adController.initialize(this);

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
	public AndroidAudio createAudio(Context context, AndroidApplicationConfiguration config) {
		return new AsynchronousAndroidAudio(context, config);
	}

	/**
	 * Convert Google Play Services connection result code to human-readable name
	 */
	private String getConnectionResultName(int resultCode) {
		switch (resultCode) {
			case ConnectionResult.SUCCESS:
				return "SUCCESS";
			case ConnectionResult.SERVICE_MISSING:
				return "SERVICE_MISSING - Google Play services is missing on this device";
			case ConnectionResult.SERVICE_UPDATING:
				return "SERVICE_UPDATING - Google Play services is currently being updated";
			case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
				return "SERVICE_VERSION_UPDATE_REQUIRED - The installed version of Google Play services is out of date";
			case ConnectionResult.SERVICE_DISABLED:
				return "SERVICE_DISABLED - The installed version of Google Play services has been disabled";
			case ConnectionResult.SERVICE_INVALID:
				return "SERVICE_INVALID - The version of the Google Play services installed is not authentic";
			default:
				return "UNKNOWN (" + resultCode + ")";
		}
	}
}
