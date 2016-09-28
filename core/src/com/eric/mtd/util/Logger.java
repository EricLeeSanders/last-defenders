package com.eric.mtd.util;

import com.badlogic.gdx.Gdx;

public class Logger {
	public static final boolean DEBUG = false;
	public static final boolean LOG_ACTIVE = false;
	private static final String DEBUG_TAG = "MTD";
	private static final String INFO_TAG = "MTD";
	private static final String ERROR_TAG = "MTD";
	
	public static void debug(String message){
		Gdx.app.debug(DEBUG_TAG, message);
	}
	
	public static void info(String message){
		Gdx.app.log(INFO_TAG, message);
	}
	
	public static void error(String message, Throwable exception){
		Gdx.app.error(ERROR_TAG, message, exception);
	}
	
}
