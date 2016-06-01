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
public abstract class AudioUtil {
	private static Music music;
	private static Sound rpgExplosion, rocketLaunch, flameBurst, rifleShot, sniperShot, machineGunShot,
			vehicleExplosion;

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
		
		rpgExplosion.play(0);
		rocketLaunch.play(0);
		flameBurst.play(0);
		rifleShot.play(0);
		sniperShot.play(0);
		machineGunShot.play(0);
		vehicleExplosion.play(0);
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
	}
	public static void dispose(){
		disposeMusic();
		disposeSound();
	}
	public static void playProjectileSound(ProjectileSound sound) {
		if (Logger.DEBUG)
			System.out.println("Playing + " + sound.name());
		switch (sound) {
		case RIFLE:
			rifleShot.play();
			break;
		case SNIPER:
			sniperShot.play();
			break;
		case MACHINE:
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

	public enum ProjectileSound {
		RIFLE, SNIPER, MACHINE, RPG_EXPLOSION, ROCKET_LAUNCH, FLAME_BURST;
	}

}
