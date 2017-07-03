package com.foxholedefense.game.model;

import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.util.Logger;

/**
 * Represents the player
 *
 * @author Eric
 */
public class Player {
    private int money = 20000;
    private int lives = 15;
    private int waveCount = 1;
    private SnapshotArray<PlayerObserver> observers = new SnapshotArray<>();

    public void attachObserver(PlayerObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        Logger.info("Player: notifying observers");
        Object[] objects = observers.begin();
        for (int i = observers.size - 1; i >= 0; i--) {
            PlayerObserver observer = (PlayerObserver) objects[i];
            observer.playerAttributeChange();
        }
        observers.end();
    }

    public int getWavesCompleted() {
        return waveCount - 1;
    }

    public int getWaveCount() {
        return waveCount;
    }

    public void setWaveCount(int waveCount) {
        this.waveCount = waveCount;
        notifyObservers();
    }

    public int getMoney() {
        return money;
    }

    private void setMoney(int money) {
        Logger.info("Player: set money: " + money);
        this.money = money;
        notifyObservers();
    }

    public int getLives() {
        return lives;
    }

    public void enemyReachedEnd() {
        if (lives > 0) {
            lives--;
        }
        notifyObservers();
    }


    public void spendMoney(int amount) {
        setMoney(getMoney() - amount);
    }

    public void giveMoney(int amount) {
        setMoney(getMoney() + amount);
    }

}
