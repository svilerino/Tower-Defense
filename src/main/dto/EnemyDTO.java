package main.dto;

public class EnemyDTO {

	private Integer totalLife;
	private Integer delay;
	private Integer sizeInPix;
	private Integer moneyReward;
	private Integer scoreReward;
	
	private String imageName;
	
	/**
	 * @return the totalLife
	 */
	public Integer getTotalLife() {
		return totalLife;
	}
	
	/**
	 * @param totalLife the totalLife to set
	 */
	public void setTotalLife(Integer totalLife) {
		this.totalLife = totalLife;
	}
	
	/**
	 * @return the delay
	 */
	public Integer getDelay() {
		return delay;
	}
	
	/**
	 * @param delay the delay to set
	 */
	public void setDelay(Integer delay) {
		this.delay = delay;
	}
	
	/**
	 * @return the sizeInPix
	 */
	public Integer getSizeInPix() {
		return sizeInPix;
	}
	
	/**
	 * @param sizeInPix the sizeInPix to set
	 */
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
	 * @return the moneyReward
	 */
	public Integer getMoneyReward() {
		return moneyReward;
	}
	
	/**
	 * @param moneyReward the moneyReward to set
	 */
	public void setMoneyReward(Integer moneyReward) {
		this.moneyReward = moneyReward;
	}
	
	/**
	 * @return the scoreReward
	 */
	public Integer getScoreReward() {
		return scoreReward;
	}
	
	/**
	 * @param scoreReward the scoreReward to set
	 */
	public void setScoreReward(Integer scoreReward) {
		this.scoreReward = scoreReward;
	}
}
