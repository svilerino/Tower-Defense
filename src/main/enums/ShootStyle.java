/**
 *
 */
package main.enums;

/**
 * @author Silvio Vileriño
 *
 */
public enum ShootStyle {
	LASER(0), BALL(1);

	private int shootType;

	private ShootStyle(int shootType){
		this.shootType = shootType;
	}

	public int getShootType() {
		return shootType;
	}

	public void getShootType(int shootType) {
		this.shootType = shootType;
	}
}
