package main.enums;

import java.io.Serializable;

/**
 * 
 * @author Guido Tagliavini
 *
 */

public enum EnemyType implements Serializable {
	CLASSIC(0), QUICK(1), FROST_RESISTANT(2), AEREAL(3), ULTRAQUICK(4);
	
	private int enemyType;
	
	private EnemyType(int enemyType){
		this.enemyType = enemyType;
	}

	public int getEnemyType() {
		return enemyType;
	}

	public void setEnemyType(int enemyType) {
		this.enemyType = enemyType;
	}
}
