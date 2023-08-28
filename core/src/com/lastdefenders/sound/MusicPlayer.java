package com.lastdefenders.sound;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.Timer;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.UserPreferences;

public class MusicPlayer implements Music.OnCompletionListener {

    private Queue<LDMusic> musicQueue = new Queue<>();
    private UserPreferences userPreferences;
    private boolean enabled;
    private LDMusic currentMusic;

    private AudioHelper audioHelper;

    public MusicPlayer(UserPreferences userPreferences, AudioHelper audioHelper){
        this.userPreferences = userPreferences;
        this.audioHelper = audioHelper;
    }

    public void play(LDMusic.Type type){
        if(!enabled){
            return;
        }
        LDMusic music = type.getLDMusic();

        if(music.isPlayImmediately()){
            playImmediately(music);
        } else {
            boolean isQueueEmpty = musicQueue.isEmpty();
            musicQueue.addLast(music);
            if(isQueueEmpty){
                playNext();
            }
        }
    }

    private void playImmediately(LDMusic newMusic){
        if(currentMusic != null) {
            currentMusic.stop();
            musicQueue.addFirst(currentMusic);
            currentMusic = null;
        }
        musicQueue.addFirst(newMusic);
        playNext();
    }

    private void playNext(){
        if(musicQueue.size > 0 && (currentMusic == null || musicQueue.first().isPlayImmediately())){
            LDMusic ldMusic = musicQueue.removeFirst();
            Music music = ldMusic.getMusic();
            music.setOnCompletionListener(this);
            music.setVolume(audioHelper.getVolume());
            music.setLooping(ldMusic.isLoop());
            music.play();
            currentMusic = ldMusic;
        }
    }

    public void updateVolume(float volume){
        if(currentMusic != null){
            currentMusic.getMusic().setVolume(volume);
        }
    }

    public void setEnabled(boolean enabled) {

        Logger.info("Setting music enabled to " + enabled);
        this.enabled = enabled;

        if(!enabled){
            // When we disable, clear the queue
            stopAndClear();
        }

        userPreferences.setMusicEnabled(enabled);
    }

    public boolean isEnabled(){
        return this.enabled;
    }

    public void toggleEnabled(){
        setEnabled(!enabled);
    }

    private void stopAndClear(){
        if(currentMusic != null) {
            currentMusic.stop();
            currentMusic = null;
        }
        musicQueue.clear();
    }

    public void disposeAll(){

        musicQueue.clear();
        for(LDMusic.Type music : LDMusic.Type.values()){
            music.getLDMusic().dispose();
        }
    }

    @Override
    public void onCompletion(Music music) {

        currentMusic = null;
        if(musicQueue.size > 0){
            playNext();
        }
    }

    public void fadeOutCurrentMusic(){

        if(currentMusic == null){
            stopAndClear();
        }
        Timer.Task fadeOut = new Timer.Task() {

            private final float startMusicVolume = currentMusic != null ? currentMusic.getMusic().getVolume() : 0;
            @Override
            public void run() {
                if(currentMusic != null){

                    float newVolume = currentMusic.getMusic().getVolume() - (startMusicVolume / 10);

                    if(newVolume > 0) {
                        currentMusic.getMusic().setVolume(newVolume);
                    } else {
                        stopAndClear();
                        this.cancel();
                    }
                }
            }


        };

        if(currentMusic != null){
            Timer.schedule(fadeOut, 0f, 0.1f);
        }
    }

}
