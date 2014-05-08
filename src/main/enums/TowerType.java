package main.enums;

/**
 *
 * @author Silvio
 *
 */

public enum TowerType{
	CLASSIC(0), QUICK(1), CANNON(2), SLOW(3), FREEZER(4), AEREAL(5), MULTIPLE(6);

	private int towerType;

	private TowerType(int towerType){
		this.towerType = towerType;
	}

	public int getTowerType() {
		return towerType;
	}

	public void setTowerType(int towerType) {
		this.towerType = towerType;
	}

}
