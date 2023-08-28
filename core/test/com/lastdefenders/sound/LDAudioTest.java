//package com.lastdefenders.sound;
//
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//import com.badlogic.gdx.Application;
//import com.badlogic.gdx.ApplicationListener;
//import com.badlogic.gdx.ApplicationLogger;
//import com.badlogic.gdx.Audio;
//import com.badlogic.gdx.Files;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Graphics;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.LifecycleListener;
//import com.badlogic.gdx.Net;
//import com.badlogic.gdx.Preferences;
//import com.badlogic.gdx.audio.Music;
//import com.badlogic.gdx.files.FileHandle;
//import com.badlogic.gdx.utils.Clipboard;
//import com.lastdefenders.game.model.actor.ai.EnemyAI;
//import com.lastdefenders.util.Logger;
//import com.lastdefenders.util.UserPreferences;
//
//public class LDAudioTest {
//
//    @BeforeAll
//    public void initLDAudioTest() {
//
//
//        Gdx.app = mock(Application.class);
//    }
//
//    @Test
//    public void playMenuMusicTest(){
//        UserPreferences userPreferences = mock(UserPreferences.class);
//        LDAudio audio = new LDAudio(userPreferences);
//
//        Audio gdxAudio = mock(Audio.class);
//        Gdx.audio = gdxAudio;
//        Files files = mock(Files.class);
//
//        Gdx.files = files;
//        Music music = mock(Music.class);
//        FileHandle fileHandleMock = mock(FileHandle.class);
//
//        doReturn(fileHandleMock).when(files).internal(anyString());
//
//        doReturn(music).when(gdxAudio).newMusic(fileHandleMock);
//
//        audio.setMusicEnabled(true);
//        audio.playMenuMusic();
//
//        verify(music, times(1)).play();
//
//
//    }
//
//    @Test
//    public void playMenuMusicTest_AlreadyPlaying(){
//        UserPreferences userPreferences = mock(UserPreferences.class);
//        LDAudio audio = new LDAudio(userPreferences);
//
//        Audio gdxAudio = mock(Audio.class);
//        Gdx.audio = gdxAudio;
//        Files files = mock(Files.class);
//
//        Gdx.files = files;
//        Music music = mock(Music.class);
//        FileHandle fileHandleMock = mock(FileHandle.class);
//
//        doReturn(fileHandleMock).when(files).internal(anyString());
//
//        doReturn(music).when(gdxAudio).newMusic(fileHandleMock);
//
//        audio.setMusicEnabled(true);
//        audio.playMenuMusic();
//        audio.playMenuMusic();
//
//        verify(music, times(1)).play();
//
//    }
//
//    @Test
//    public void playMusicTest_InQueue(){
//        UserPreferences userPreferences = mock(UserPreferences.class);
//        LDAudio audio = new LDAudio(userPreferences);
//
//        Audio gdxAudio = mock(Audio.class);
//        Gdx.audio = gdxAudio;
//        Files files = mock(Files.class);
//
//        Gdx.files = files;
//        Music music1 = mock(Music.class);
//        Music music2 = mock(Music.class);
//        FileHandle fileHandleMock = mock(FileHandle.class);
//
//        doReturn(fileHandleMock).when(files).internal(anyString());
//
//        doReturn(music1).doReturn(music2).when(gdxAudio).newMusic(fileHandleMock);
//
//        audio.setMusicEnabled(true);
//        audio.playMenuMusic();
//        audio.playGameEndingMusic();
//
//        verify(music1, times(1)).play();
//        verify(music2, never()).play();
//    }
//}
