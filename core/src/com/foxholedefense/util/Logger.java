package com.foxholedefense.util;

import com.badlogic.gdx.Gdx;

public class Logger {
	private static final String DEBUG_TAG = "FHD";
	private static final String INFO_TAG = "FHD";
	private static final String ERROR_TAG = "FHD";
	
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
