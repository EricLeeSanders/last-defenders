package com.lastdefenders.sound;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.lastdefenders.util.UserPreferences;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SoundPlayerTest {
    @BeforeAll
    public static void init() {

        Gdx.app = mock(Application.class);
        Gdx.audio = mock(Audio.class);
        Gdx.files = mock(Files.class);
    }

    private static LDSound.Type createLDSoundTypeMock(boolean ready){
        LDSound.Type soundTypeMock = mock(LDSound.Type.class);
        LDSound ldSoundMock = mock(LDSound.class);
        Sound soundMock = mock(Sound.class);

        doReturn(soundMock).when(ldSoundMock).getSound();
        doReturn(ldSoundMock).when(soundTypeMock).getLDSound();
        doReturn(ready).when(ldSoundMock).isReady();

        return soundTypeMock;
    }

    @Test
    public void playTest_Enabled(){

        UserPreferences userPreferencesMock = mock(UserPreferences.class);
        AudioHelper audioHelperMock = mock(AudioHelper.class);

        doReturn(.5f).when(audioHelperMock).getVolume();

        SoundPlayer soundPlayer = new SoundPlayer(userPreferencesMock, audioHelperMock);
        LDSound.Type soundTypeMock = createLDSoundTypeMock(true);

        soundPlayer.setEnabled(true);
        soundPlayer.play(soundTypeMock);

        verify(soundTypeMock.getLDSound(), times(1)).play(.5f);
        verify(userPreferencesMock, times(1)).setSoundEnabled(true);
    }

    @Test
    public void playTest_Ready(){

        UserPreferences userPreferencesMock = mock(UserPreferences.class);
        AudioHelper audioHelperMock = mock(AudioHelper.class);

        doReturn(.5f).when(audioHelperMock).getVolume();

        SoundPlayer soundPlayer = new SoundPlayer(userPreferencesMock, audioHelperMock);
        LDSound.Type soundTypeMock = createLDSoundTypeMock(true);

        soundPlayer.setEnabled(true);
        soundPlayer.play(soundTypeMock);

        verify(soundTypeMock.getLDSound(), times(1)).play(.5f);
        verify(userPreferencesMock, times(1)).setSoundEnabled(true);
    }

    @Test
    public void playTest_NotEnabled(){

        UserPreferences userPreferencesMock = mock(UserPreferences.class);
        AudioHelper audioHelperMock = mock(AudioHelper.class);

        doReturn(.5f).when(audioHelperMock).getVolume();

        SoundPlayer soundPlayer = new SoundPlayer(userPreferencesMock, audioHelperMock);
        LDSound.Type soundTypeMock = createLDSoundTypeMock(true);

        soundPlayer.setEnabled(false);
        soundPlayer.play(soundTypeMock);

        verify(soundTypeMock.getLDSound(), never()).play(.5f);
        verify(userPreferencesMock, times(1)).setSoundEnabled(false);

    }

    @Test
    public void playTest_NotReady(){

        UserPreferences userPreferencesMock = mock(UserPreferences.class);
        AudioHelper audioHelperMock = mock(AudioHelper.class);

        doReturn(.5f).when(audioHelperMock).getVolume();

        SoundPlayer soundPlayer = new SoundPlayer(userPreferencesMock, audioHelperMock);
        LDSound.Type soundTypeMock = createLDSoundTypeMock(false);

        soundPlayer.setEnabled(true);
        soundPlayer.play(soundTypeMock);

        verify(soundTypeMock.getLDSound(), never()).play(.5f);
        verify(userPreferencesMock, times(1)).setSoundEnabled(true);

    }

    @Test
    public void toggleEnabled(){
        UserPreferences userPreferencesMock = mock(UserPreferences.class);
        AudioHelper audioHelperMock = mock(AudioHelper.class);

        SoundPlayer soundPlayer = new SoundPlayer(userPreferencesMock, audioHelperMock);
        soundPlayer.setEnabled(true);

        assertTrue(soundPlayer.isEnabled());
        soundPlayer.toggleEnabled();
        assertFalse(soundPlayer.isEnabled());
    }
}
