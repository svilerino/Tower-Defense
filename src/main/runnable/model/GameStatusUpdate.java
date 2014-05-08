/**
 *
 */
package main.runnable.model;

import java.util.Vector;

import main.consts.GameConsts;
import main.dto.RoundResultsDTO;
import main.model.enemy.ShapedEnemy;
import main.util.ModelUtil;
import main.view.frame.GameFrame;

/**
 * @author Silvio Vileriño
 * Suma puntos y dinero a medida que van matando enemigos,
 *  y resta cuando enemigos llegan a la meta
 */
public class GameStatusUpdate implements Runnable {

	private GameFrame mainFrame;
	private boolean roundFinished;
	private RoundResultsDTO resultInfo;

	public GameStatusUpdate(GameFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.roundFinished = false;
		this.resultInfo=new RoundResultsDTO(mainFrame.getGridPanel().getTowers().size());
	}

	@Override
	public void run() {
		//map Repaint controls lifes > 0 and stops round if loose.
		Vector<ShapedEnemy> enemies = mainFrame.getGridPanel().getEnemies();
		int bonusChance=0;

		while(! roundFinished){
			for(ShapedEnemy se : enemies){
				if(se.getRemainingLife() <= 0 && !se.isCountedAsDead() && !se.isCountedAsWinner() && !se.isWinner()){
					se.setCountedAsDead(true);
					bonusChance++;
					resultInfo.addDeadEnemy();
					//otorgar 1 bonus por matar n Enemigos
					if(bonusChance == GameConsts.BONUS_KILLING_TARGET){
						System.out.println("Bonus Win!");
						this.mainFrame.addBonus(1);
						bonusChance=0;
					}
					//agregar puntos por enemigo down
					this.mainFrame.modifyScore(se.getScoreReward(), se
							.getMoneyReward());
				}else if( (se.isWinner()) && (!se.isCountedAsWinner()) ){
					se.setCountedAsWinner(true);
					this.mainFrame.reduceLife(se.getLifeDamage());
				}
			}

			if(ModelUtil.countDeadEnemies(mainFrame.getGridPanel().getEnemies()) == mainFrame.getGridPanel().getEnemies().size()){
				roundFinished = true;
				new Thread(new Runnable(){
					public void run() {
						mainFrame.finishAndDecideRoundResult(resultInfo);
					}
				}).start();
			}/*else{
				if(mainFrame.getGridPanel().getAncestorContainer().getLifesRemaining() <= 0){
					roundFinished = true;
					System.out.println("[ INFO] - GameStatusUpdate - Lost Round!");
					new Thread(new Runnable(){

						public void run() {
							mainFrame.finishLostRound();
						}
					}).start();
				}
			}*/

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace(System.err);
			}
		}

		for(ShapedEnemy se : enemies){
			se.setCountedAsDead(false);
			se.setCountedAsWinner(false);
		}

		mainFrame.getGridPanel().finalizeRound();
		System.out.println("[ INFO] GameStatusUpdate Thread Finished");
	}

}
