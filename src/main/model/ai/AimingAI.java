package main.model.ai;

import java.util.Vector;
import java.util.Comparator;
import main.enums.AimingAIStrategy;
import main.model.enemy.ShapedEnemy;
import main.model.tower.ShapedTower;
import main.util.MathUtil;
import main.util.ModelUtil;

/**
 * @author Guido Tagliavini
 */
public class AimingAI{

	private Vector<ShapedEnemy> enemies;
	private ShapedTower tower;
	private Comparator<ShapedEnemy> comparator;

	public AimingAI(Vector<ShapedEnemy> enemies, ShapedTower tower, AimingAIStrategy aimingAIStrategy){
		this.enemies = enemies;
		this.tower = tower;

		if(aimingAIStrategy == AimingAIStrategy.CLOSEST_ENEMY){
			comparator = new DistanceComparator();
		}else{
			comparator = new WeaknessComparator();
		}
	}

	public ShapedEnemy getBestOption(){
		ShapedEnemy bestOption = null;
		
		for(ShapedEnemy se : enemies){
			if(ModelUtil.isValidTarget(se, tower)){
				if(bestOption == null){
					bestOption = se;
				}else{
					if(comparator.compare(se, bestOption) == -1){
						bestOption = se;
					}
				}
			}
		}

		return bestOption;

	}

	private class DistanceComparator implements Comparator<ShapedEnemy>{

		@Override
		public int compare(ShapedEnemy se1, ShapedEnemy se2) {

			double dist1 = MathUtil.distance(
					(int) se1.getCenter().getX(),
					(int) se1.getCenter().getY(),
					(int) tower.getCenter().getX(),
					(int) tower.getCenter().getY());

			double dist2 = MathUtil.distance(
					(int) se2.getCenter().getX(),
					(int) se2.getCenter().getY(),
					(int) tower.getCenter().getX(),
					(int) tower.getCenter().getY());

			if(dist1 > dist2){
				return 1;
			}else if(dist1 < dist2){
				return -1;
			}

			return 0;
		}
	}

	private class WeaknessComparator implements Comparator<ShapedEnemy>{

		@Override
		public int compare(ShapedEnemy se1, ShapedEnemy se2){

			if(se1.getRemainingLife() > se2.getRemainingLife()){
				return 1;
			}else if(se1.getRemainingLife() < se2.getRemainingLife()){
				return -1;
			}

			return 0;
		}
	}
}
