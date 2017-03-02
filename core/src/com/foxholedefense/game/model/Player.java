package com.foxholedefense.game.model;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.util.Logger;

/**
 * Represents the player
 * 
 * @author Eric
 *
 */
public class Player {
	private int money = 20000;
	private int lives = 15;
	private int waveCount = 1;
	private SnapshotArray<IPlayerObserver> observers = new SnapshotArray<IPlayerObserver>();

	public void attachObserver(IPlayerObserver observer) {
		observers.add(observer);
	}

	public void notifyObservers() {
		Logger.info("Player: notifying observers");
		for (IPlayerObserver observer : observers) {
			observer.playerAttributeChange();
		}
	}

	public void setWaveCount(int waveCount) {
		this.waveCount = waveCount;
		notifyObservers();
	}

	public int getWavesCompleted() {
		return waveCount - 1;
	}

	public int getWaveCount() {
		return waveCount;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		Logger.info("TowerPlacement: set money: " + money);
		this.money = money;
		notifyObservers();
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		Logger.info("TowerPlacement: set lives: " + lives);
		this.lives = lives;
		notifyObservers();
	}

	public void spendMoney(int amount) {
		setMoney(getMoney() - amount);
	}

	public void giveMoney(int amount) {
		setMoney(getMoney() + amount);
	}

}
