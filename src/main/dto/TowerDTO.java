package main.dto;

import java.awt.Color;

import main.enums.ShootStyle;

public class TowerDTO {

	private Integer shootRadius;
	private Integer shootDelay;
	private Integer shootDamage;
	private Integer sizeInPix;
	private Integer towerCost;
	private Integer upgradeCost;
	
	private Color color;
	
	private String imageName;
	private String baseImageName;
	
	private ShootStyle shootStyle;

	/**
	 * @param shootType the shootType to set
	 */
	public void setShootStyle(ShootStyle shootType) {
		this.shootStyle = shootType;
	}

	/**
	 * @return the shootType
	 */
	public ShootStyle getShootStyle() {
		return shootStyle;
	}

	public Integer getShootRadius() {
		return shootRadius;
	}

	public void setShootRadius(Integer shootRadius) {
		this.shootRadius = shootRadius;
	}

	public Integer getShootDelay() {
		return shootDelay;
	}

	public void setShootDelay(Integer shootDelay) {
		this.shootDelay = shootDelay;
	}

	public Integer getShootDamage() {
		return shootDamage;
	}

	public void setShootDamage(Integer shootDamage) {
		this.shootDamage = shootDamage;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Integer getSizeInPix() {
		return sizeInPix;
	}

	public void setSizeInPix(Integer sizeInPix) {
		this.sizeInPix = sizeInPix;
	}

	/**
	 * @return the imageName
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * @param imageName the imageName to set
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	/**
	 * @return the baseImageName
	 */
	public String getBaseImageName() {
		return baseImageName;
	}

	/**
	 * @param baseImageName the baseImageName to set
	 */
	public void setBaseImageName(String baseImageName) {
		this.baseImageName = baseImageName;
	}

	/**
	 * @return the towerCost
	 */
	public Integer getTowerCost() {
		return towerCost;
	}

	/**
	 * @param towerCost the towerCost to set
	 */
	public void setTowerCost(Integer towerCost) {
		this.towerCost = towerCost;
	}

	/**
	 * @return the upgradeCost
	 */
	public Integer getUpgradeCost() {
		return upgradeCost;
	}

	/**
	 * @param upgradeCost the upgradeCost to set
	 */
	public void setUpgradeCost(Integer upgradeCost) {
		this.upgradeCost = upgradeCost;
	}

}
