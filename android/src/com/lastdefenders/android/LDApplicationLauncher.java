package com.lastdefenders.android;

import android.app.Application;

import com.lastdefenders.config.AcraProperties;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

@ReportsCrashes(
	    formUri = AcraProperties.URI,
	    reportType = HttpSender.Type.JSON,
	    httpMethod = HttpSender.Method.POST,
	    formUriBasicAuthLogin = AcraProperties.LOGIN,
	    formUriBasicAuthPassword = AcraProperties.PASSWORD,
		logcatArguments = { "-t", "200", "-v", "time", "LD:D", "*:S"},
	    customReportContent = {
	            ReportField.APP_VERSION_CODE,
	            ReportField.APP_VERSION_NAME,
	            ReportField.ANDROID_VERSION,
	            ReportField.PACKAGE_NAME,
	            ReportField.REPORT_ID,
	            ReportField.BUILD,
	            ReportField.STACK_TRACE,
	            ReportField.LOGCAT
	    },
	    mode = ReportingInteractionMode.TOAST,
		resToastText = R.string.toast_crash
	)

	public class LDApplicationLauncher extends Application {
	   
	   @Override
	   public void onCreate() {
	        super.onCreate();
	        ACRA.init(this);
	   }
	}
