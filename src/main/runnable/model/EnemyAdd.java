package main.runnable.model;

import main.model.enemy.ShapedEnemy;
import main.view.panel.GridPanel;

public class EnemyAdd implements Runnable {

	private GridPanel gridPanel;
	private Integer addingDelay;


	public EnemyAdd(GridPanel gridPanel, Integer addingDelay) {
		this.gridPanel = gridPanel;
		this.addingDelay = addingDelay;
	}

	public void run(){
		for(ShapedEnemy se : gridPanel.getEnemies()){
			EnemyMove em = new EnemyMove(se, gridPanel);
			//New thread implementation December 2010
			//new Thread(em).start();
			gridPanel.getEnemiesExecutor().submit(em);
			gridPanel.getEmEnemies().add(em);

			se.setAdded(true);

			try {
				Thread.sleep(addingDelay);
			} catch (InterruptedException e) {
				e.printStackTrace(System.err);
			}
		}
	}

	public Integer getAddingDelay() {
		return addingDelay;
	}

	public void setAddingDelay(Integer addingDelay) {
		this.addingDelay = addingDelay;
	}
}
