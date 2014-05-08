package main.util;

import java.util.Vector;

import main.enums.EnemyType;
import main.enums.TowerType;
import main.model.enemy.ShapedEnemy;
import main.model.tower.ShapedTower;

public class ModelUtil {

	public static boolean isValidTarget(ShapedEnemy enemy, ShapedTower tower){
		if(enemy.isAdded()){
			if(isValidType(enemy, tower)){
				if(enemy.getAttacker()==tower.getOwnerPlayer()){
					if(enemy.getRemainingLife() > 0){
						if(MathUtil.distance(
								(int) enemy.getCenter().getX(),
								(int) enemy.getCenter().getY(),
								(int) tower.getCenter().getX(),
								(int) tower.getCenter().getY()) <= tower.getShootRadius()){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public static boolean isValidType(ShapedEnemy enemy, ShapedTower tower){
		if(tower.getTowerType() == TowerType.AEREAL){
			if(enemy.getEnemyType() == EnemyType.AEREAL){
				return true;
			}
		}else{
			if(enemy.getEnemyType() != EnemyType.AEREAL){
				return true;
			}
		}
		
		return false;
	}

	public static int countDeadEnemies(Vector<ShapedEnemy> enemies){
		int count = 0;

		for(ShapedEnemy e : enemies){
			if(e.getRemainingLife() <= 0){
				count ++;
			}
		}
		return count;
	}
}
