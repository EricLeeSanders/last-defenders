package com.eric.mtd.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Util class for playing sounds and music
 * 
 * @author Eric
 *
 */
public class AudioUtil {
	private static Music music;
	private static Sound rpgExplosion, rocketLaunch, flameBurst, rifleShot, sniperShot, machineGunShot,
			vehicleExplosion, actorPlace, sell, smallClick, largeClick;
	private static boolean musicEnabled, soundEnabled;

	/**
	 * Load the sounds and music
	 */
	public static void load() {
		music = Gdx.audio.newMusic(Gdx.files.internal(Resources.MENU_MUSIC));
		music.setLooping(true);

		rpgExplosion = Gdx.audio.newSound(Gdx.files.internal(Resources.RPG_EXPLOSION_SOUND));
		rocketLaunch = Gdx.audio.newSound(Gdx.files.internal(Resources.ROCKET_LAUNCH_SOUND));
		flameBurst = Gdx.audio.newSound(Gdx.files.internal(Resources.FLAME_SOUND));
		rifleShot = Gdx.audio.newSound(Gdx.files.internal(Resources.RIFLE_SHOT_SOUND));
		sniperShot = Gdx.audio.newSound(Gdx.files.internal(Resources.SNIPER_SHOT_SOUND));
		machineGunShot = Gdx.audio.newSound(Gdx.files.internal(Resources.MACHINE_GUN_SHOT_SOUND));
		vehicleExplosion = Gdx.audio.newSound(Gdx.files.internal(Resources.VEHICLE_EXPLOSION_SOUND));
		actorPlace = Gdx.audio.newSound(Gdx.files.internal(Resources.ACTOR_PLACE_SOUND));
		sell = Gdx.audio.newSound(Gdx.files.internal(Resources.SELL_SOUND));
		smallClick = Gdx.audio.newSound(Gdx.files.internal(Resources.SMALL_CLICK));
		largeClick = Gdx.audio.newSound(Gdx.files.internal(Resources.LARGE_CLICK));
		
		rpgExplosion.play(0);
		rocketLaunch.play(0);
		flameBurst.play(0);
		rifleShot.play(0);
		sniperShot.play(0);
		machineGunShot.play(0);
		vehicleExplosion.play(0);
		actorPlace.play(0);
		sell.play(0);
		smallClick.play(0);
		largeClick.play(0);
				
		setSoundEnabled(Resources.getPreferences().getBoolean("soundEnabled", true));
		setMusicEnabled(Resources.getPreferences().getBoolean("musicEnabled", true));
	}

	public static void playVehicleExplosion() {
		vehicleExplosion.play();
	}

	public static void playMusic() {
		if (Logger.DEBUG)
			System.out.println("Playing Music");
		music.play();
	}
	public static void disposeMusic() {
		if (Logger.DEBUG)
			System.out.println("Disposing Music");
		music.dispose();
	}
	public static void disposeSound() {
		if (Logger.DEBUG)
			System.out.println("Disposing Sounds");
		rpgExplosion.dispose();
		rocketLaunch.dispose();
		flameBurst.dispose();
		rifleShot.dispose();
		sniperShot.dispose();
		machineGunShot.dispose();
		vehicleExplosion.dispose();
		actorPlace.dispose();
		sell.dispose();
		smallClick.dispose();
		largeClick.dispose();
	}
	public static void dispose(){
		disposeMusic();
		disposeSound();
	}
	
	public static void playSound(MTDSound sound){
		if(soundEnabled) {
			if (Logger.DEBUG)
				System.out.println("Playing + " + sound.name());
			switch (sound) {
			case ACTOR_PLACE:
				actorPlace.play();
				break;
			case SELL:
				sell.play();
				break;
			case SMALL_CLICK:
				smallClick.play();
				break;
			case LARGE_CLICK:
				largeClick.play();
				break;
			}
		}
	}
	
	public static void playProjectileSound(ProjectileSound sound) {
		if(soundEnabled){
			if (Logger.DEBUG)
				System.out.println("Playing + " + sound.name());
			switch (sound) {
			case RIFLE:
				rifleShot.play();
				break;
			case SNIPER:
				sniperShot.play();
				break;
			case MACHINE_GUN:
				machineGunShot.play();
				break;
			case RPG_EXPLOSION:
				rpgExplosion.play();
				break;
			case ROCKET_LAUNCH:
				rocketLaunch.play();
				break;
			case FLAME_BURST:
				flameBurst.play();
				break;
			}
		}
	}
	public enum MTDSound {
		ACTOR_PLACE, SELL, SMALL_CLICK, LARGE_CLICK;
	}
	public enum ProjectileSound {
		RIFLE, SNIPER, MACHINE_GUN, RPG_EXPLOSION, ROCKET_LAUNCH, FLAME_BURST;
	}
	public static void changeMusicEnabled(){
		setMusicEnabled(musicEnabled ? false : true);
	}
	public static void setMusicEnabled(boolean enabled){
		if(Logger.DEBUG)System.out.println("Setting music to " + enabled);
		musicEnabled = enabled;
		
		if(enabled){
			music.setVolume(100);
		} else {
			music.setVolume(0);
		}
		
		Resources.getPreferences().putBoolean("musicEnabled", enabled);
		Resources.getPreferences().flush();
	}
	public static void changeSoundEnabled(){
		setSoundEnabled(soundEnabled ? false : true);
	}
	public static void setSoundEnabled(boolean enabled){
		if(Logger.DEBUG)System.out.println("Setting sound to " + enabled);
		soundEnabled = enabled;
		Resources.getPreferences().putBoolean("soundEnabled", enabled);
		Resources.getPreferences().flush();
	}
	
	public static boolean isSoundEnabled(){
		return soundEnabled;
	}
	public static boolean isMusicEnabled(){
		return musicEnabled;
	}

}
