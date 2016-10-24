package com.eric.mtd.util;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Util class for playing sounds and music
 * 
 * @author Eric
 *
 */
public class MTDAudio {
	private Music music;
	private Map<MTDSound, Sound> sounds = new HashMap<MTDSound, Sound>();
	//private Sound rpgExplosion, rocketLaunch, flameBurst, rifleShot, sniperShot, machineGunShot,
			//vehicleExplosion, actorPlace, sell, smallClick, largeClick;
	private boolean musicEnabled, soundEnabled;
	private UserPreferences userPreferences;
	private float volume;
	
	public MTDAudio(UserPreferences userPreferences){
		this.userPreferences = userPreferences;
	}

	/**
	 * Load the sounds and music
	 */
	public void load() {
		music = Gdx.audio.newMusic(Gdx.files.internal(Resources.MENU_MUSIC));
		music.setLooping(true);

		Sound rpgExplosion = Gdx.audio.newSound(Gdx.files.internal(Resources.RPG_EXPLOSION_SOUND));
		Sound rocketLaunch = Gdx.audio.newSound(Gdx.files.internal(Resources.ROCKET_LAUNCH_SOUND));
		Sound flameBurst = Gdx.audio.newSound(Gdx.files.internal(Resources.FLAME_SOUND));
		Sound rifleShot = Gdx.audio.newSound(Gdx.files.internal(Resources.RIFLE_SHOT_SOUND));
		Sound sniperShot = Gdx.audio.newSound(Gdx.files.internal(Resources.SNIPER_SHOT_SOUND));
		Sound machineGunShot = Gdx.audio.newSound(Gdx.files.internal(Resources.MACHINE_GUN_SHOT_SOUND));
		Sound vehicleExplosion = Gdx.audio.newSound(Gdx.files.internal(Resources.VEHICLE_EXPLOSION_SOUND));
		Sound actorPlace = Gdx.audio.newSound(Gdx.files.internal(Resources.ACTOR_PLACE_SOUND));
		Sound sell = Gdx.audio.newSound(Gdx.files.internal(Resources.SELL_SOUND));
		Sound smallClick = Gdx.audio.newSound(Gdx.files.internal(Resources.SMALL_CLICK));
		Sound largeClick = Gdx.audio.newSound(Gdx.files.internal(Resources.LARGE_CLICK));
		
		sounds.put(MTDSound.RPG_EXPLOSION, rpgExplosion);
		sounds.put(MTDSound.ROCKET_LAUNCH, rocketLaunch);
		sounds.put(MTDSound.FLAME_BURST, flameBurst);
		sounds.put(MTDSound.RIFLE, rifleShot);
		sounds.put(MTDSound.SNIPER, sniperShot);
		sounds.put(MTDSound.MACHINE_GUN, machineGunShot);
		sounds.put(MTDSound.VEHICLE_EXPLOSION, vehicleExplosion);
		sounds.put(MTDSound.ACTOR_PLACE, actorPlace);
		sounds.put(MTDSound.SELL, sell);
		sounds.put(MTDSound.SMALL_CLICK, smallClick);
		sounds.put(MTDSound.LARGE_CLICK, largeClick);
		
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

		setMasterVolume(userPreferences.getPreferences().getFloat("masterVolume", 100));
		setSoundEnabled(userPreferences.getPreferences().getBoolean("soundEnabled", true));
		setMusicEnabled(userPreferences.getPreferences().getBoolean("musicEnabled", true));

	}
	
	public float getMasterVolume(){
		return volume;
	}

	public void setMasterVolume(float volume){
		this.volume = volume;
		if(musicEnabled){
			music.setVolume(volume);
		}
	}
	
	public void saveMasterVolume(){
		Logger.info("Saving master volume");
		userPreferences.getPreferences().putFloat("masterVolume", volume);
		userPreferences.getPreferences().flush();
	}
	
	public void playMusic() {
		Logger.info("Playing Music");
		if(!music.isPlaying()){
			music.play();
		}
	}
	
	public void turnOffMusic(){
		Logger.info("Turning off Music");
		music.stop();
	}
	
	public void disposeMusic() {
		Logger.info("Disposing Music");
		music.dispose();
	}
	public void disposeSound() {
		Logger.info("Disposing Sounds");
		for (MTDSound key : sounds.keySet()) {
		    Sound sound = sounds.get(key);
		    sound.dispose();
		}
	}
	public void dispose(){
		disposeMusic();
		disposeSound();
	}
	
	public void playSound(MTDSound sound){
		if(!soundEnabled){
			return;
		}
		
		Sound playSound = sounds.get(sound);
		if(playSound != null){
			playSound.play(volume);
		}
	}
	public enum MTDSound {
		ACTOR_PLACE, SELL, SMALL_CLICK, LARGE_CLICK, RIFLE, SNIPER, MACHINE_GUN, RPG_EXPLOSION, VEHICLE_EXPLOSION, ROCKET_LAUNCH, FLAME_BURST;
	}
	public void changeMusicEnabled(){
		setMusicEnabled(musicEnabled ? false : true);
	}
	public void setMusicEnabled(boolean enabled){
		Logger.info("Setting music to " + enabled);
		musicEnabled = enabled;
		music.setVolume(enabled ? volume : 0);
		
		userPreferences.getPreferences().putBoolean("musicEnabled", enabled);
		userPreferences.getPreferences().flush();
	}
	public void changeSoundEnabled(){
		setSoundEnabled(soundEnabled ? false : true);
	}
	public void setSoundEnabled(boolean enabled){
		Logger.info("Setting sound to " + enabled);
		soundEnabled = enabled;
		userPreferences.getPreferences().putBoolean("soundEnabled", enabled);
		userPreferences.getPreferences().flush();
	}
	
	public boolean isSoundEnabled(){
		return soundEnabled;
	}
	public boolean isMusicEnabled(){
		return musicEnabled;
	}

}
