package main.runnable.model;

import java.awt.geom.Point2D; 
import main.model.ai.MovementAI;
import main.model.enemy.ShapedEnemy;

import main.runnable.sound.MP3SoundPlay;
import main.sound.RandomSoundSelector;
import main.util.ConversorUtil;
import main.view.panel.GridPanel;

public class EnemyMove implements Runnable {

	private ShapedEnemy enemy;
	private MovementAI ai;
	private GridPanel gridPanel;

	private boolean moving;

	public ShapedEnemy getEnemy() {
		return enemy;
	}

	public EnemyMove(ShapedEnemy enemy, GridPanel gridPanel) {
		this.enemy = enemy;
		this.ai = enemy.getAi();
		this.gridPanel = gridPanel;
		this.moving = true;
	}

	/**
	 * @deprecated . Use same method in ShapedEnemy
	 * @param modifyValue
	 */
	public void modifySpeed(Integer modifyValue){
		enemy.setDelay(enemy.getDelay() + modifyValue);
	}

	@Override
	/**
	 * Enemy's AI, each enemy 'walks' into a previously set path with n
	 * milliseconds of delay
	 */
	public void run() {
		for (int a = 0; moving && a < ai.getPath().size() &&
								enemy.getRemainingLife() > 0; a++) {
			enemy.moveTo(new Integer((int) ai.getPath().get(a).getX()),
					new Integer((int) ai.getPath().get(a).getY()));
			enemy.setAngle(ai.getAngle().get(a));

			try {
				Thread.sleep(enemy.getDelay());
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		if (moving) {
			RandomSoundSelector rss = new RandomSoundSelector();
			if (enemy.getRemainingLife() <= 0) {
				new Thread(new MP3SoundPlay(rss.getRandomEnemyDownSound().getAbsolutePath(), false)).start();

				System.out.println("[ INFO] Enemy down (" + this + ") - "
						+ Thread.currentThread().toString());

				gridPanel
						.enemyDeadEffect(new Point2D.Double(ConversorUtil
								.logicToPixels(this.getEnemy().getX()),
								ConversorUtil.logicToPixels(this.getEnemy()
										.getY())));
				this.enemy.setWinner(false);
			} else {

				new Thread(new MP3SoundPlay(rss.getRandomEnemyWonSound().getAbsolutePath(), false)).start();

				this.enemy.setWinner(true);

				System.out.println("[ INFO] Life lost (" + this + ") - "
						+ Thread.currentThread().toString());
			}
		} else {
			System.out.println("[ INFO] Enemy Stopped (" + this + ") - "
					+ Thread.currentThread().toString());
		}

		moving = false;
		enemy.setRemainingLife(new Integer(0));
	}

	public boolean isMoving() {
		return this.moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}
}
