package com.lastdefenders.android;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.lastdefenders.config.AcraProperties;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.annotation.AcraCore;
import org.acra.annotation.AcraHttpSender;
import org.acra.annotation.AcraToast;
import org.acra.data.StringFormat;
import org.acra.sender.HttpSender;

@AcraCore(
	reportFormat = StringFormat.JSON,
	logcatArguments = { "-t", "2000", "-v", "time", "LD:D", "*:S"},
	reportContent = {
		ReportField.APP_VERSION_CODE,
		ReportField.APP_VERSION_NAME,
		ReportField.ANDROID_VERSION,
		ReportField.PACKAGE_NAME,
		ReportField.REPORT_ID,
		ReportField.BUILD,
		ReportField.STACK_TRACE,
		ReportField.LOGCAT
	}
)
@AcraHttpSender(
	    uri = AcraProperties.URI,
	    httpMethod = HttpSender.Method.POST,
	    basicAuthLogin = AcraProperties.LOGIN,
	    basicAuthPassword = AcraProperties.PASSWORD
)
@AcraToast(
	resText = R.string.toast_crash
)

	public class LDApplicationLauncher extends Application {

	   @Override
	   public void onCreate() {
			super.onCreate();
			FirebaseAnalytics.getInstance(this);
			ACRA.init(this);
	   }
	}
