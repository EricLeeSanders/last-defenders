package com.lastdefenders.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.audio.Ogg;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Util class for playing sounds and music
 *
 * @author Eric
 */
public class LDAudio {

    private static final String MENU_MUSIC = "audio/insurgence.ogg";
    private static final String GAME_ENDING_MUSIC = "audio/fighting_forces.ogg";

    private static final String ROCKET_EXPLOSION_SOUND = "audio/rocket_explosion.ogg";
    private static final String ROCKET_LAUNCH_SOUND = "audio/rocket_launch.ogg";
    private static final String FLAME_SOUND = "audio/flame_burst.ogg";
    private static final String RIFLE_SHOT_SOUND = "audio/rifle_shot.ogg";
    private static final String MACHINE_GUN_SHOT_SOUND = "audio/machine_gun_shot.ogg";
    private static final String SNIPER_SHOT_SOUND = "audio/sniper_shot.ogg";
    private static final String ACTOR_PLACE_SOUND = "audio/actor_place.ogg";
    private static final String SELL_SOUND = "audio/sell.ogg";
    private static final String SMALL_CLICK = "audio/button_small_click.ogg";
    private static final String LARGE_CLICK = "audio/button_large_click.ogg";
    private static final String VEHICLE_EXPLOSION_SOUND = "audio/vehicle_explosion.ogg";
    private static final String HELICOPTER_HOVER = "audio/helicopter_hover.ogg";
    private static final String AIRCRAFT_FLYOVER = "audio/aircraft_flyover.ogg";

    private Map<LDSound, Sound> sounds = new HashMap<>();
    private boolean musicEnabled, soundEnabled;
    private UserPreferences userPreferences;
    private float volume;

    private Music currentMusic;
    private String currentMusicFile = "";
    private Set<String> musicQueue = new HashSet<>();

    public LDAudio(UserPreferences userPreferences) {

        this.userPreferences = userPreferences;
    }

    /**
     * Load the sounds and music
     */
    public void load() {

        Logger.info("LDAudio: loading");

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
        Sound helicopterHover = Gdx.audio.newSound(Gdx.files.internal(HELICOPTER_HOVER));
        Sound aircraftFlyover = Gdx.audio.newSound(Gdx.files.internal(AIRCRAFT_FLYOVER));

        sounds.put(LDSound.ROCKET_EXPLOSION, rocketExplosion);
        sounds.put(LDSound.ROCKET_LAUNCH, rocketLaunch);
        sounds.put(LDSound.FLAME_BURST, flameBurst);
        sounds.put(LDSound.RIFLE, rifleShot);
        sounds.put(LDSound.SNIPER, sniperShot);
        sounds.put(LDSound.MACHINE_GUN, machineGunShot);
        sounds.put(LDSound.VEHICLE_EXPLOSION, vehicleExplosion);
        sounds.put(LDSound.ACTOR_PLACE, actorPlace);
        sounds.put(LDSound.SELL, sell);
        sounds.put(LDSound.SMALL_CLICK, smallClick);
        sounds.put(LDSound.LARGE_CLICK, largeClick);
        sounds.put(LDSound.HELICOPTER_HOVER, helicopterHover);
        sounds.put(LDSound.AIRCRAFT_FLYOVER, aircraftFlyover);

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
        helicopterHover.play(0);
        aircraftFlyover.play(0);

        setMasterVolume(userPreferences.getMasterVolume());
        setSoundEnabled(userPreferences.getSoundEnabled());
        setMusicEnabled(userPreferences.getMusicEnabled());
        Logger.info("LDAudio: loaded");
    }

    public float getMasterVolume() {

        return volume;
    }

    public void setMasterVolume(float volume) {

        this.volume = volume;

        if(currentMusic != null){
            currentMusic.setVolume(volume);
        }

    }

    public void saveMasterVolume() {

        Logger.info("Saving master volume");
        userPreferences.setMasterVolume(volume);
    }

    private void playMusic(final String m, final boolean loop, final boolean queue) {

        if (!musicEnabled || currentMusicFile.equals(m) || musicQueue.contains(m)) {
            return;
        }

        if(currentMusic != null && queue) {
            musicQueue.add(m);
            currentMusic.setOnCompletionListener(new Music.OnCompletionListener() {

                @Override
                public void onCompletion(Music music) {

                    playMusic(MENU_MUSIC, loop, false);

                }
            });

            return;
        }

        disposeMusic();
        Logger.info("Playing Music: " + m);
        Music music = Gdx.audio.newMusic(Gdx.files.internal(m));
        music.setLooping(loop);
        music.setVolume(volume);
        currentMusic = music;
        currentMusicFile = m;
        currentMusic.play();
    }

    private void disposeMusic(){

        if(currentMusic != null){
            Logger.info("Disposing Music");
            currentMusic.dispose();
        }

        currentMusic = null;
        currentMusicFile = "";
    }

    private void disposeSound() {

        Logger.info("Disposing Sounds");
        for (LDSound key : sounds.keySet()) {
            Sound sound = sounds.get(key);
            sound.dispose();
        }
    }

    public void dispose() {

        disposeSound();
    }

    public void playSound(LDSound sound) {

        if (!soundEnabled) {
            return;
        }

        Logger.info("LDAudio: playing sound: " + sound.name());

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
        userPreferences.setSoundEnabled(enabled);
    }

    public boolean isMusicEnabled() {

        return musicEnabled;
    }

    private void setMusicEnabled(boolean enabled) {

        Logger.info("Setting music enabled to " + enabled);
        musicEnabled = enabled;

        if(!enabled){
            // When we disable, clear the queue
            musicQueue.clear();
        }

        disposeMusic();

        userPreferences.setMusicEnabled(enabled);
    }

    public void playGameEndingMusic(){

        playMusic(GAME_ENDING_MUSIC, false, false);
    }

    public void playMenuMusic(){

        playMusic(MENU_MUSIC, true, true);
    }

    public void fadeOutMusic(){

        // When we fade out, we want to clear the queue
        musicQueue.clear();

        Timer.Task fadeOut = new Timer.Task() {
            private final float currentMusicVolumne = currentMusic != null ? currentMusic.getVolume() : 0;
            @Override
            public void run() {
                if(currentMusic != null){

                    float newVolume = currentMusic.getVolume() - (currentMusicVolumne / 10);
                    currentMusic.setVolume(newVolume >= 0 ? newVolume : 0);

                    if(newVolume <= 0) {
                        this.cancel();
                        disposeMusic();
                    }
                } else {
                    disposeMusic();
                }
            }


        };

        if(currentMusic != null){
            Timer.schedule(fadeOut, 0f, 0.1f);
        }
    }

    public enum LDSound {
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
        FLAME_BURST,
        HELICOPTER_HOVER,
        AIRCRAFT_FLYOVER
    }

}
