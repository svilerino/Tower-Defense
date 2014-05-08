/**
 *
 */
package main.enums;

/**
 * @author Silvio Vilerino
 *
 */
public enum BonusType {
	MONEY(0), SCORE(1), LIFE(2), ENEMY_SLOWDOWN(3), TOWER_ULTRAQUICK(4);
	private int bonusType;

	private BonusType(int bonusType){
		this.bonusType = bonusType;
	}

	/**
	 * @return the bonusType
	 */
	public int getBonusType() {
		return bonusType;
	}

	/**
	 * @param bonusType the bonusType to set
	 */
	public void setBonusType(int bonusType) {
		this.bonusType = bonusType;
	}
}
