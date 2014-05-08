package main.session;

import java.util.Vector;

import main.enums.EnemyType;
import main.enums.GameMode;
import main.enums.PlayerNumber;
import main.util.MathUtil;

/**
 * 
 * @author Guido Tagliavini
 *
 */

public class SessionObject {

	private static final Integer DISTINCT_ENEMIES = 5;

	private GameMode gameMode;
	private PlayerNumber playerNumber;
	private Vector<EnemyType> wave;
	private EnemyType actualEnemy;
	private EnemyType nextEnemy;
	private Integer roundNumber;
	
	private int actualIndex;

	public SessionObject(){
		super();
		roundNumber=0;
		fillWave();
	}

	public void resetRounds(){
		roundNumber=0;
	}

	public void newRound(){
		actualEnemy = nextEnemy;
		roundNumber++;
		if(actualIndex == DISTINCT_ENEMIES - 1){
			fillWave();
		}else{
			nextEnemy = wave.get(actualIndex + 1);
			++actualIndex;
		}
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	public PlayerNumber getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(PlayerNumber playerNumber) {
		this.playerNumber = playerNumber;
	}

	public EnemyType getActualEnemy() {
		return actualEnemy;
	}

	public void setActualEnemy(EnemyType actualEnemy) {
		this.actualEnemy = actualEnemy;
	}

	public EnemyType getNextEnemy() {
		return nextEnemy;
	}

	public void setNextEnemy(EnemyType nextEnemy) {
		this.nextEnemy = nextEnemy;
	}

	private boolean isSelected(EnemyType next){
		for(EnemyType e : wave){
			if(e.equals(next)){
				return true;
			}
		}

		return false;
	}

	private void fillWave(){
		wave = new Vector<EnemyType>();

		for(int i = 0; i < DISTINCT_ENEMIES; i++){
			EnemyType next;
			do{
				next = EnemyType.values()[MathUtil.random(0, DISTINCT_ENEMIES - 1)];
			}while(isSelected(next));

			wave.add(next);
		}

		nextEnemy = wave.get(0);
		actualIndex = 0;
	}

	/**
	 * @return the roundNumber
	 */
	public Integer getRoundNumber() {
		return roundNumber;
	}

}
