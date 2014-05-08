package main.model.tower;

import main.enums.PlayerNumber;
import main.enums.TowerType;

/**
 * Clase genérica de torres
 * @author Guido Tagliavini
 */

public abstract class Tower{

	private Integer x;
	private Integer y;
	private Integer size;
	private TowerType towerType;
	private PlayerNumber ownerPlayer;

	public Integer getSize() {
		return size;
	}

	public Integer getX() {
		return x;
	}

	public Integer getY() {
		return y;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public TowerType getTowerType() {
		return towerType;
	}

	public void setTowerType(TowerType towerType) {
		this.towerType = towerType;
	}

	/**
	 * @return the ownerPlayer
	 */
	public PlayerNumber getOwnerPlayer() {
		return ownerPlayer;
	}

	/**
	 * @param ownerPlayer the ownerPlayer to set
	 */
	public void setOwnerPlayer(PlayerNumber ownerPlayer) {
		this.ownerPlayer = ownerPlayer;
	}
}
