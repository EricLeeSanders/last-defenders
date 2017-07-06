package com.foxholedefense.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;
import java.util.Map;

/**
 * Util class for playing sounds and music
 *
 * @author Eric
 */
public class FHDAudio {

    public static final String MENU_MUSIC = "audio/big_action_trailer.mp3";
    public static final String ROCKET_EXPLOSION_SOUND = "audio/rocket_explosion.mp3";
    public static final String ROCKET_LAUNCH_SOUND = "audio/rocket_launch.mp3";
    public static final String FLAME_SOUND = "audio/flame_burst.mp3";
    public static final String RIFLE_SHOT_SOUND = "audio/rifle_shot.mp3";
    public static final String MACHINE_GUN_SHOT_SOUND = "audio/machine_gun_shot.mp3";
    public static final String SNIPER_SHOT_SOUND = "audio/sniper_shot.mp3";
    public static final String ACTOR_PLACE_SOUND = "audio/actor_place.mp3";
    public static final String SELL_SOUND = "audio/sell.mp3";
    public static final String SMALL_CLICK = "audio/button_small_click.mp3";
    public static final String LARGE_CLICK = "audio/button_large_click.mp3";
    public static final String VEHICLE_EXPLOSION_SOUND = "audio/vehicle_explosion.mp3";

    private Music music;
    private Map<FHDSound, Sound> sounds = new HashMap<>();
    private boolean musicEnabled, soundEnabled;
    private UserPreferences userPreferences;
    private float volume;

    public FHDAudio(UserPreferences userPreferences) {

        this.userPreferences = userPreferences;
    }

    /**
     * Load the sounds and music
     */
    public void load() {

        Logger.info("FHDAudio: loading");
        music = Gdx.audio.newMusic(Gdx.files.internal(MENU_MUSIC));
        music.setLooping(true);

        Sound rocketExplosion = Gdx.audio
            .newSound(Gdx.files.internal(ROCKET_EXPLOSION_SOUND));
        Sound rocketLaunch = Gdx.audio.newSound(Gdx.files.internal(ROCKET_LAUNCH_SOUND));
        Sound flameBurst = Gdx.audio.newSound(Gdx.files.internal(FLAME_SOUND));
        Sound rifleShot = Gdx.audio.newSound(Gdx.files.internal(RIFLE_SHOT_SOUND));
        Sound sniperShot = Gdx.audio.newSound(Gdx.files.internal(SNIPER_SHOT_SOUND));
        Sound machineGunShot = Gdx.audio
            .newSound(Gdx.files.internal(MACHINE_GUN_SHOT_SOUND));
        Sound vehicleExplosion = Gdx.audio
            .newSound(Gdx.files.internal(VEHICLE_EXPLOSION_SOUND));
        Sound actorPlace = Gdx.audio.newSound(Gdx.files.internal(ACTOR_PLACE_SOUND));
        Sound sell = Gdx.audio.newSound(Gdx.files.internal(SELL_SOUND));
        Sound smallClick = Gdx.audio.newSound(Gdx.files.internal(SMALL_CLICK));
        Sound largeClick = Gdx.audio.newSound(Gdx.files.internal(LARGE_CLICK));

        sounds.put(FHDSound.ROCKET_EXPLOSION, rocketExplosion);
        sounds.put(FHDSound.ROCKET_LAUNCH, rocketLaunch);
        sounds.put(FHDSound.FLAME_BURST, flameBurst);
        sounds.put(FHDSound.RIFLE, rifleShot);
        sounds.put(FHDSound.SNIPER, sniperShot);
        sounds.put(FHDSound.MACHINE_GUN, machineGunShot);
        sounds.put(FHDSound.VEHICLE_EXPLOSION, vehicleExplosion);
        sounds.put(FHDSound.ACTOR_PLACE, actorPlace);
        sounds.put(FHDSound.SELL, sell);
        sounds.put(FHDSound.SMALL_CLICK, smallClick);
        sounds.put(FHDSound.LARGE_CLICK, largeClick);

        rocketExplosion.play(0);
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

        setMasterVolume(userPreferences.getPreferences().getFloat("masterVolume", 1));
        setSoundEnabled(userPreferences.getPreferences().getBoolean("soundEnabled", true));
        setMusicEnabled(userPreferences.getPreferences().getBoolean("musicEnabled", true));
        Logger.info("FHDAudio: loaded");
    }

    public float getMasterVolume() {

        return volume;
    }

    public void setMasterVolume(float volume) {

        this.volume = volume;
        if (musicEnabled) {
            music.setVolume(volume);
        }
    }

    public void saveMasterVolume() {

        Logger.info("Saving master volume");
        userPreferences.getPreferences().putFloat("masterVolume", volume);
        userPreferences.getPreferences().flush();
    }

    public void playMusic() {

        Logger.info("Playing Music");
        if (!music.isPlaying()) {
            music.play();
        }
    }

    public void turnOffMusic() {

        Logger.info("Turning off Music");
        music.stop();
    }

    private void disposeMusic() {

        Logger.info("Disposing Music");
        music.dispose();
    }

    private void disposeSound() {

        Logger.info("Disposing Sounds");
        for (FHDSound key : sounds.keySet()) {
            Sound sound = sounds.get(key);
            sound.dispose();
        }
    }

    public void dispose() {

        disposeMusic();
        disposeSound();
    }

    public void playSound(FHDSound sound) {

        Logger.info("FHDAudio: playing sound: " + sound.name());
        if (!soundEnabled) {
            return;
        }

        Sound playSound = sounds.get(sound);
        if (playSound != null) {
            playSound.play(volume);
        }
    }

    public void changeMusicEnabled() {

        setMusicEnabled(!musicEnabled);
    }

    public void changeSoundEnabled() {

        setSoundEnabled(!soundEnabled);
    }

    public boolean isSoundEnabled() {

        return soundEnabled;
    }

    private void setSoundEnabled(boolean enabled) {

        Logger.info("Setting sound to " + enabled);
        soundEnabled = enabled;
        userPreferences.getPreferences().putBoolean("soundEnabled", enabled);
        userPreferences.getPreferences().flush();
    }

    public boolean isMusicEnabled() {

        return musicEnabled;
    }

    private void setMusicEnabled(boolean enabled) {

        Logger.info("Setting music to " + enabled);
        musicEnabled = enabled;
        music.setVolume(enabled ? volume : 0);

        userPreferences.getPreferences().putBoolean("musicEnabled", enabled);
        userPreferences.getPreferences().flush();
    }

    public enum FHDSound {
        ACTOR_PLACE,
        SELL,
        SMALL_CLICK,
        LARGE_CLICK,
        RIFLE,
        SNIPER,
        MACHINE_GUN,
        ROCKET_EXPLOSION,
        VEHICLE_EXPLOSION,
        ROCKET_LAUNCH,
        FLAME_BURST
    }

}
