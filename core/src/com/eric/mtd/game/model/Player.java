package com.eric.mtd.game.model;

import com.eric.mtd.game.helper.Resources;

public class Player {
	private int money, lives;
	private int waveCount = 1;
	public Player(){

	}
	public void setWaveCount(int waveCount){
		this.waveCount = waveCount;
	}
	public int getWaveCount(){
		return waveCount;
	}
	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}
	public void spendMoney(int amount){
		money = money - amount;
	}
	public void giveMoney(int amount){
		money = money + amount;
	}

}
