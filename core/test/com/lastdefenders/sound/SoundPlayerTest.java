package com.lastdefenders.sound;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private static LDSound.Type createLDSoundTypeMock(){
        LDSound.Type soundTypeMock = mock(LDSound.Type.class);
        LDSound ldSoundMock = mock(LDSound.class);
        Sound soundMock = mock(Sound.class);

        doReturn(soundMock).when(ldSoundMock).getSound();
        doReturn(ldSoundMock).when(soundTypeMock).getLDSound();

        return soundTypeMock;
    }

    @Test
    public void playTest_Enabled(){

        UserPreferences userPreferencesMock = mock(UserPreferences.class);
        AudioHelper audioHelperMock = mock(AudioHelper.class);

        doReturn(.5f).when(audioHelperMock).getVolume();

        SoundPlayer soundPlayer = new SoundPlayer(userPreferencesMock, audioHelperMock);
        LDSound.Type soundTypeMock = createLDSoundTypeMock();

        soundPlayer.setEnabled(true);
        soundPlayer.play(soundTypeMock);

        verify(soundTypeMock.getLDSound().getSound(), times(1)).play(.5f);
        verify(userPreferencesMock, times(1)).setSoundEnabled(true);
    }

    @Test
    public void playTest_NotEnabled(){

        UserPreferences userPreferencesMock = mock(UserPreferences.class);
        AudioHelper audioHelperMock = mock(AudioHelper.class);

        doReturn(.5f).when(audioHelperMock).getVolume();

        SoundPlayer soundPlayer = new SoundPlayer(userPreferencesMock, audioHelperMock);
        LDSound.Type soundTypeMock = createLDSoundTypeMock();

        soundPlayer.setEnabled(false);
        soundPlayer.play(soundTypeMock);

        verify(soundTypeMock.getLDSound().getSound(), never()).play(.5f);
        verify(userPreferencesMock, times(1)).setSoundEnabled(false);

    }

    @Test
    public void toggleEnabled(){
        UserPreferences userPreferencesMock = mock(UserPreferences.class);
        AudioHelper audioHelperMock = mock(AudioHelper.class);

        SoundPlayer soundPlayer = new SoundPlayer(userPreferencesMock, audioHelperMock);
        soundPlayer.setEnabled(true);

        assertEquals(true, soundPlayer.isEnabled());
        soundPlayer.toggleEnabled();
        assertEquals(false, soundPlayer.isEnabled());
    }
}
