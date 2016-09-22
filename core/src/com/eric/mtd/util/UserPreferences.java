package com.eric.mtd.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class UserPreferences {
	private Preferences prefs = Gdx.app.getPreferences("MTD_Preferences");
	
	public Preferences getPreferences(){
		return prefs;
	}
}
