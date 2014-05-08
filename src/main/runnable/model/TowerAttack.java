package main.runnable.model;

import java.awt.geom.Point2D; 
import java.util.Vector;

import main.consts.FilePathConsts;
import main.consts.GameConsts;
import main.enums.ShootStyle;
import main.model.enemy.ShapedEnemy;
import main.model.tower.ShapedTower;
import main.runnable.sound.MP3SoundPlay;
import main.util.ConversorUtil;
import main.util.MathUtil;
import main.util.ModelUtil;
import main.view.panel.GridPanel;
import main.model.ai.AimingAI;

public class TowerAttack implements Runnable {

	private static final Long INITIAL_DELAY = 250L;

	private GridPanel gridPanel;
	private ShapedTower tower;
	private Vector<ShapedEnemy> enemies;

	private boolean attacking;
	private boolean aiming;
	private boolean bonusAffected;


	/**
	 * @return the bonusAffected
	 */
	public boolean isBonusAffected() {
		return bonusAffected;
	}

	public TowerAttack(ShapedTower tower, Vector<ShapedEnemy> enemies,
			GridPanel gridPanel) {
		this.tower = tower;
		this.gridPanel = gridPanel;
		this.enemies = enemies;
		this.attacking = true;
		this.aiming = false;
		this.bonusAffected=false;
	}

	public boolean modifySpeed(Integer modifyValue){
		boolean applied=false;
		if( (tower.getShootDelay()+modifyValue)>0 ){
			tower.setShootDelay(tower.getShootDelay()+modifyValue);
			applied=true;
		}else{
			System.out.println("[ INFO] Can't slow down tower by " + modifyValue);
		}
		return applied;
	}

	/**
	 * Tower's AI, each tower shoots inside its shoot radius to every enemy in a
	 * IA setted way with a n ms delay determined by tower parameters
	 */
	@Override
	public void run() {

		try {
			Thread.sleep(INITIAL_DELAY);
		} catch (InterruptedException e) {
			e.printStackTrace(System.err);
		}

		ShapedEnemy aimedEnemy = null;

		while (attacking) {
			if(!aiming){
				aimedEnemy = new AimingAI(enemies, tower, GameConsts.PREFERRED_AIMING_STRATEGY).getBestOption();
				if(aimedEnemy != null){
					aiming = true;
				}else{
					try {
						Thread.sleep(15);
					} catch (InterruptedException e) {
						e.printStackTrace(System.err);
					}
				}
			}


			if(aimedEnemy != null){
				this.tower.setAngle(270D - MathUtil.calculateAngle(
						(int) tower.getCenter().getX(), (int) tower.getCenter().getY(),
						(int) aimedEnemy.getCenter().getX(), (int) aimedEnemy.getCenter().getY()));

				for (int i = 0; i < tower.getRotatedFrames().length; i++) {
					this.tower.setNextFrame();// animacion de disparo

					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace(System.err);
					}
				}

				gridPanel.shootEnemy(new Point2D.Double(tower
						.getShape().getBounds2D().getCenterX(), tower
						.getShape().getBounds2D().getCenterY()),
						new Point2D.Double(ConversorUtil
								.logicToPixels(aimedEnemy.getX()) + ConversorUtil.logicToPixels(1) / 2,
								ConversorUtil.logicToPixels(aimedEnemy.getY()) + ConversorUtil.logicToPixels(1) / 2), tower
								.getTowerClr(), tower.getShootStyle());

				playAttackSound();

				aimedEnemy.reduceLife(tower.getShootDamage());

				if(!ModelUtil.isValidTarget(aimedEnemy, tower)){
					aiming = false;

					if(aimedEnemy.getRemainingLife() <= 0){
						if(ModelUtil.countDeadEnemies(enemies) == enemies.size()){
							attacking = false;
						}
					}
				}

				try {
					Thread.sleep(tower.getShootDelay().longValue());
				} catch (InterruptedException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		tower.setAngle(0.0D);
		System.out.println("[ INFO] TowerAttack Finished - "
				+ Thread.currentThread().toString());
	}

	public boolean isAttacking() {
		return attacking;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	private void playAttackSound(){
		if(tower.getShootStyle()==ShootStyle.LASER){
			new Thread(new MP3SoundPlay(FilePathConsts.towersSoundPath + "/laser1.mp3", false)).start();
		}else{
			new Thread(new MP3SoundPlay(FilePathConsts.towersSoundPath + "/missile3.mp3", false)).start();
		}
	}

	/**
	 * @param bonusAffected the bonusAffected to set
	 */
	public void setBonusAffected(boolean bonusAffected) {
		this.bonusAffected = bonusAffected;
	}

}

