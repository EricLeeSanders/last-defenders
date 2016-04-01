package com.eric.mtd.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Disposable;

public abstract class AudioUtil {
	private static Music music;
    public static void load() {
        music = Gdx.audio.newMusic(Gdx.files.internal(Resources.MENU_MUSIC));
        music.setLooping(true);
    }
    
    public static void playMusic(){
    	if(Logger.DEBUG)System.out.println("Playing Music");
    	music.play();
    }
    
    public static void dispose(){
    	if(Logger.DEBUG)System.out.println("Disposing Music");
    	music.dispose();
    }
    
}
