package com.eric.mtd.ui.controller.interfaces;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.eric.mtd.model.actor.tower.Tower;

public interface IInspectController{
	public void closeInspect();
	public void changeTargetPriority();
	public void increaseAttack();
	public void giveArmor();
	public void increaseRange();
	public void increaseSpeed();
	public void dishcharge();
	public boolean hasArmor();
	public int	getAttackLevel();
	public int getRangeLevel();
	public int getSpeedLevel();
	public int getArmorCost();
	public int getAttackCost();
	public int getRangeCost();
	public int getSpeedCost();
	public int getSellPrice();
	public int getKills();
	public void setSelectedTower(Tower selectedTower);
	public String getTowerTargetPriority();
	public void inspectTower(Vector2 coords);
	public boolean canAffordUpgrade(int upgradeCost);
}
