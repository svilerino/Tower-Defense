package main.dto;

import java.io.Serializable; 

public class TowerDataDTO implements Serializable{

	private static final long serialVersionUID = 1576046375911594879L;
	private Integer x;
	private Integer y;
	private Integer level;
	private Integer towerType;

	public TowerDataDTO(){
		super();
	}

	public TowerDataDTO(Integer x, Integer y, Integer level, Integer towerType){
		this.x=x;
		this.y=y;
		this.level=level;
		this.towerType = towerType;
	}

	/**
	 * @return the x
	 */
	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	/**
	 * @return the level
	 */
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getTowerType() {
		return towerType;
	}

	public void setTowerType(Integer towerType) {
		this.towerType = towerType;
	}
}
