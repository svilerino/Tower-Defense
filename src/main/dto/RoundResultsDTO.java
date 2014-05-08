package main.dto;

import main.consts.GameConsts;

public class RoundResultsDTO {

	private Integer towersCount;
	private Integer killedEnemiesCount;
	private Long roundTime;
	private final Integer totalEnemies=GameConsts.TOTAL_ENEMIES_PER_ROUND;

	public RoundResultsDTO(Integer towersCount){
		this.towersCount=towersCount;
		this.killedEnemiesCount=0;
	}

	public void addDeadEnemy(){
		killedEnemiesCount++;
	}

	/**
	 * @return the towersCount
	 */
	public Integer getTowersCount() {
		return towersCount;
	}

	/**
	 * @return the killedEnemiesCount
	 */
	public Integer getKilledEnemiesCount() {
		return killedEnemiesCount;
	}

	/**
	 * @return the ratioKilledEnemies
	 */
	public Double getRatioKilledEnemies() {
		return killedEnemiesCount/(double)totalEnemies;
	}

	/**
	 * @return the totalEnemies
	 */
	public Integer getTotalEnemies() {
		return totalEnemies;
	}

	/**
	 * @return the roundTime
	 */
	public Long getRoundTime() {
		return roundTime;
	}

	/**
	 * @param roundTime the roundTime to set
	 */
	public void setRoundTime(Long roundTime) {
		this.roundTime = roundTime;
	}
}
