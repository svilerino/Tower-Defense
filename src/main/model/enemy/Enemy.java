package main.model.enemy;

import main.enums.EnemyType; 
import main.enums.PlayerNumber;
import main.model.ai.MovementAI;
 
/**
 * Enemies abstract class
 * @author Guido Tagliavini
 */

public abstract class Enemy {

	private Integer x;
	private Integer y;
	private Integer totalLife;
	private PlayerNumber attacker;

	private EnemyType enemyType;

	protected MovementAI ai;

	public Integer getX() {
		return x;
	}

	public Integer getY() {
		return y;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public Integer getTotalLife() {
		return totalLife;
	}

	public void setTotalLife(Integer totalLife) {
		this.totalLife = totalLife;
	}

	public MovementAI getAi() {
		return ai;
	}

	public void setAi(MovementAI ai) {
		this.ai = ai;
	}

	public EnemyType getEnemyType() {
		return enemyType;
	}

	public void setEnemyType(EnemyType enemyType) {
		this.enemyType = enemyType;
	}

	/**
	 * @return the attacker
	 */
	public PlayerNumber getAttacker() {
		return attacker;
	}

	/**
	 * @param attacker the attacker to set
	 */
	public void setAttacker(PlayerNumber attacker) {
		this.attacker = attacker;
	}
}
