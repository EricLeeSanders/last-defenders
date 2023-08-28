package com.lastdefenders.sound;

public interface AudioPlayer<T extends LDAudio> {
    void play(T audio);
    void updateVolume(float volume);
}
