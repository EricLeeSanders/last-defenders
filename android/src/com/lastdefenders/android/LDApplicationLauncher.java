package com.lastdefenders.android;

import android.app.Application;

import android.content.Context;

import com.lastdefenders.config.AcraProperties;

import com.lastdefenders.game.R;
import org.acra.ACRA;
import org.acra.BuildConfig;
import org.acra.ReportField;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.HttpSenderConfigurationBuilder;
import org.acra.config.ToastConfigurationBuilder;
import org.acra.data.StringFormat;
import org.acra.sender.HttpSender;

public class LDApplicationLauncher extends Application {
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this)
			.setBuildConfigClass(BuildConfig.class)
			.setReportFormat(StringFormat.JSON)
			.setLogcatArguments( "-t", "2000", "-v", "time", "LD:D", "*:S")
			.setReportContent(
				ReportField.APP_VERSION_CODE,
				ReportField.APP_VERSION_NAME,
				ReportField.ANDROID_VERSION,
				ReportField.PACKAGE_NAME,
				ReportField.REPORT_ID,
				ReportField.BUILD,
				ReportField.STACK_TRACE,
				ReportField.LOGCAT
			);
		builder.getPluginConfigurationBuilder(HttpSenderConfigurationBuilder.class)
			.setUri(AcraProperties.URI)
			.setHttpMethod(HttpSender.Method.POST)
			.setBasicAuthLogin(AcraProperties.LOGIN)
			.setBasicAuthPassword(AcraProperties.PASSWORD)
			.setEnabled(true);
		builder.getPluginConfigurationBuilder(ToastConfigurationBuilder.class)
			.setResText(R.string.toast_crash)
			.setEnabled(false);

		ACRA.init(this, builder);
	}
}
