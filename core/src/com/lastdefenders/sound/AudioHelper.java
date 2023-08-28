package com.lastdefenders.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

public class AudioHelper {

    private float volume;

    static final String MENU_MUSIC = "audio/insurgence.ogg";
    static final String GAME_ENDING_MUSIC = "audio/fighting_forces.ogg";

    static final String ROCKET_EXPLOSION_SOUND = "audio/rocket_explosion.ogg";
    static final String ROCKET_LAUNCH_SOUND = "audio/rocket_launch.ogg";
    static final String FLAME_SOUND = "audio/flame_burst.ogg";
    static final String RIFLE_SHOT_SOUND = "audio/rifle_shot.ogg";
    static final String MACHINE_GUN_SHOT_SOUND = "audio/machine_gun_shot.ogg";
    static final String SNIPER_SHOT_SOUND = "audio/sniper_shot.ogg";
    static final String ACTOR_PLACE_SOUND = "audio/actor_place.ogg";
    static final String SELL_SOUND = "audio/sell.ogg";
    static final String SMALL_CLICK = "audio/button_small_click.ogg";
    static final String LARGE_CLICK = "audio/button_large_click.ogg";
    static final String VEHICLE_EXPLOSION_SOUND = "audio/vehicle_explosion.ogg";
    static final String HELICOPTER_HOVER = "audio/helicopter_hover.ogg";
    static final String AIRCRAFT_FLYOVER = "audio/aircraft_flyover.ogg";

    static Music loadMusic(FileHandle musicFile){
        return Gdx.audio.newMusic(musicFile);
    }

    public float getVolume() {

        return volume;
    }

    public void setVolume(float volume) {

        this.volume = volume;
    }
}
