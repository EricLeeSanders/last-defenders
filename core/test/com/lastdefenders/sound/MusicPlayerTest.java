package com.lastdefenders.sound;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.lastdefenders.util.UserPreferences;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MusicPlayerTest {

    @BeforeAll
    public static void init() {

        Gdx.app = mock(Application.class);
        Gdx.audio = mock(Audio.class);
        Gdx.files = mock(Files.class);
    }


    private static LDMusic.Type createLDMusicTypeMock(boolean immediately, boolean loop){
        LDMusic.Type musicTypeMock = mock(LDMusic.Type.class);

        LDMusic ldMusicMock = mock(LDMusic.class);
        Music musicMock = mock(Music.class);
        doReturn(immediately).when(ldMusicMock).isPlayImmediately();
        doReturn(loop).when(ldMusicMock).isLoop();
        doReturn(musicMock).when(ldMusicMock).getMusic();

        doReturn(ldMusicMock).when(musicTypeMock).getLDMusic();

        return musicTypeMock;
    }

    @Test
    public void playMenuTest_EmptyQueue(){
        UserPreferences userPreferences = mock(UserPreferences.class);
        AudioHelper audioHelperMock = mock(AudioHelper.class);
        doReturn(0.5f).when(audioHelperMock).getVolume();

        MusicPlayer musicPlayer = new MusicPlayer(userPreferences, audioHelperMock);

        musicPlayer.setEnabled(true);

        LDMusic.Type ldMusicTypeMock = createLDMusicTypeMock(false, false);

        musicPlayer.play(ldMusicTypeMock);

        Music musicMock = ldMusicTypeMock.getLDMusic().getMusic();
        verify(musicMock, times(1)).setOnCompletionListener(musicPlayer);
        verify(musicMock, times(1)).setVolume(0.5f);
        verify(musicMock, times(1)).setLooping(false);
        verify(musicMock, times(1)).play();

        reset(musicMock);
    }

    @Test
    public void playTest_QueueNotEmpty(){
        UserPreferences userPreferences = mock(UserPreferences.class);
        AudioHelper audioHelperMock = mock(AudioHelper.class);
        doReturn(0.5f).when(audioHelperMock).getVolume();

        MusicPlayer musicPlayer = new MusicPlayer(userPreferences, audioHelperMock);

        LDMusic.Type [] ldMusicTypeArr = {
            createLDMusicTypeMock(false, false),
            createLDMusicTypeMock(false, false)
        };

        musicPlayer.setEnabled(true);

        musicPlayer.play(ldMusicTypeArr[0]);
        musicPlayer.play(ldMusicTypeArr[1]);

        Music musicMock1 = ldMusicTypeArr[0].getLDMusic().getMusic();
        verify(musicMock1, times(1)).setOnCompletionListener(musicPlayer);
        verify(musicMock1, times(1)).setVolume(0.5f);
        verify(musicMock1, times(1)).setLooping(false);
        verify(musicMock1, times(1)).play();


        verify(ldMusicTypeArr[1].getLDMusic().getMusic(), never()).play();
    }

    @Test
    public void playImmediatelyTest_QueueNotEmpty(){
        UserPreferences userPreferences = mock(UserPreferences.class);
        AudioHelper audioHelperMock = mock(AudioHelper.class);
        doReturn(0.5f).when(audioHelperMock).getVolume();

        MusicPlayer musicPlayer = new MusicPlayer(userPreferences, audioHelperMock);

        LDMusic.Type [] ldMusicTypeArr = {
            createLDMusicTypeMock(false, false),
            createLDMusicTypeMock(true, false)
        };

        musicPlayer.setEnabled(true);

        musicPlayer.play(ldMusicTypeArr[0]);
        musicPlayer.play(ldMusicTypeArr[1]);

        Music musicMock1 = ldMusicTypeArr[0].getLDMusic().getMusic();
        verify(musicMock1, times(1)).setOnCompletionListener(musicPlayer);
        verify(musicMock1, times(1)).setVolume(0.5f);
        verify(musicMock1, times(1)).setLooping(false);
        verify(musicMock1, times(1)).play();
        verify(ldMusicTypeArr[0].getLDMusic(), times(1)).stop();

        Music musicMock2 = ldMusicTypeArr[1].getLDMusic().getMusic();
        verify(musicMock2, times(1)).setOnCompletionListener(musicPlayer);
        verify(musicMock2, times(1)).setVolume(0.5f);
        verify(musicMock2, times(1)).setLooping(false);
        verify(musicMock2, times(1)).play();
    }

    @Test
    public void playImmediatelyTest_CurrentMusicIsImmediate(){
        UserPreferences userPreferences = mock(UserPreferences.class);
        AudioHelper audioHelperMock = mock(AudioHelper.class);
        doReturn(0.5f).when(audioHelperMock).getVolume();

        MusicPlayer musicPlayer = new MusicPlayer(userPreferences, audioHelperMock);

        LDMusic.Type [] ldMusicTypeArr = {
            createLDMusicTypeMock(true, false),
            createLDMusicTypeMock(true, false)
        };

        musicPlayer.setEnabled(true);
        musicPlayer.play(ldMusicTypeArr[0]);
        musicPlayer.play(ldMusicTypeArr[1]);

        Music musicMock1 = ldMusicTypeArr[0].getLDMusic().getMusic();
        verify(musicMock1, times(1)).setOnCompletionListener(musicPlayer);
        verify(musicMock1, times(1)).setVolume(0.5f);
        verify(musicMock1, times(1)).setLooping(false);
        verify(musicMock1, times(1)).play();
        verify(ldMusicTypeArr[0].getLDMusic(), times(1)).stop();

        Music musicMock2 = ldMusicTypeArr[1].getLDMusic().getMusic();
        verify(musicMock2, times(1)).setOnCompletionListener(musicPlayer);
        verify(musicMock2, times(1)).setVolume(0.5f);
        verify(musicMock2, times(1)).setLooping(false);
        verify(musicMock2, times(1)).play();
    }

    @Test
    public void completeTest_QueueNotEmpty(){
        UserPreferences userPreferences = mock(UserPreferences.class);
        AudioHelper audioHelperMock = mock(AudioHelper.class);
        doReturn(0.5f).when(audioHelperMock).getVolume();

        MusicPlayer musicPlayer = new MusicPlayer(userPreferences, audioHelperMock);

        LDMusic.Type [] ldMusicTypeArr = {
            createLDMusicTypeMock(false, false),
            createLDMusicTypeMock(false, false)
        };

        musicPlayer.setEnabled(true);
        musicPlayer.play(ldMusicTypeArr[0]);
        musicPlayer.play(ldMusicTypeArr[1]);
        musicPlayer.onCompletion(ldMusicTypeArr[0].getLDMusic().getMusic());

        Music musicMock1 = ldMusicTypeArr[0].getLDMusic().getMusic();
        verify(musicMock1, times(1)).setOnCompletionListener(musicPlayer);
        verify(musicMock1, times(1)).setVolume(0.5f);
        verify(musicMock1, times(1)).setLooping(false);
        verify(musicMock1, times(1)).play();
        verify(ldMusicTypeArr[0].getLDMusic(), never()).stop();

        Music musicMock2 = ldMusicTypeArr[1].getLDMusic().getMusic();
        verify(musicMock2, times(1)).setOnCompletionListener(musicPlayer);
        verify(musicMock2, times(1)).setVolume(0.5f);
        verify(musicMock2, times(1)).setLooping(false);
        verify(musicMock2, times(1)).play();
    }

    @Test
    public void setMusicEnabledTrueTest(){
        UserPreferences userPreferences = mock(UserPreferences.class);
        AudioHelper audioHelperMock = mock(AudioHelper.class);

        MusicPlayer musicPlayer = new MusicPlayer(userPreferences, audioHelperMock);

        musicPlayer.setEnabled(true);

        verify(userPreferences, times(1)).setMusicEnabled(true);

    }

    @Test
    public void setMusicEnabledFalseTest_NoCurrentMusicOrQueue(){
        UserPreferences userPreferences = mock(UserPreferences.class);
        AudioHelper audioHelperMock = mock(AudioHelper.class);

        MusicPlayer musicPlayer = new MusicPlayer(userPreferences, audioHelperMock);

        musicPlayer.setEnabled(false);

        verify(userPreferences, times(1)).setMusicEnabled(false);

    }

    @Test
    public void setMusicEnabledFalseTest_QueueNotEmpty(){
        UserPreferences userPreferences = mock(UserPreferences.class);
        AudioHelper audioHelperMock = mock(AudioHelper.class);

        MusicPlayer musicPlayer = new MusicPlayer(userPreferences, audioHelperMock);

        LDMusic.Type [] ldMusicTypeArr = {
            createLDMusicTypeMock(false, false),
            createLDMusicTypeMock(false, false)
        };

        musicPlayer.setEnabled(true);
        musicPlayer.play(ldMusicTypeArr[0]);
        musicPlayer.play(ldMusicTypeArr[1]);

        musicPlayer.setEnabled(false);


        verify(userPreferences, times(1)).setMusicEnabled(false);
        verify(ldMusicTypeArr[0].getLDMusic(), times(1)).stop();

    }

    @Test
    public void toggleEnabled(){
        UserPreferences userPreferences = mock(UserPreferences.class);
        AudioHelper audioHelperMock = mock(AudioHelper.class);

        MusicPlayer musicPlayer = new MusicPlayer(userPreferences, audioHelperMock);
        musicPlayer.setEnabled(true);

        assertEquals(true, musicPlayer.isEnabled());
        musicPlayer.toggleEnabled();
        assertEquals(false, musicPlayer.isEnabled());
    }
}
