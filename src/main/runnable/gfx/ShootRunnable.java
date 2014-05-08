/**
 *
 */
package main.runnable.gfx;

import main.consts.GameConsts;
import main.model.gfx.Shoot;

/**
 * @author Silvio
 *
 */
public class ShootRunnable implements Runnable {

	private Shoot shootObj;
	private Boolean finalized;

	/**
	 * @return the finalized
	 */
	public Boolean isFinalized() {
		return finalized;
	}

	/**
	 * @param finalized the finalized to set
	 */
	public void setFinalized(Boolean finalized) {
		this.finalized = finalized;
	}

	public ShootRunnable(Shoot shootObj){
		this.shootObj=shootObj;
		this.finalized = false;
	}


	public void run() {
		for (int a = 0; a < this.shootObj.getPath().size(); a++) {
			this.shootObj.setPosition(this.shootObj.getPath().get(a));
			try {
				Thread.sleep(GameConsts.SHOOT_MOVE_DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace(System.err);
			}
		}
		finalized = true;
	}

	public Shoot getShootObj() {
		return shootObj;
	}
}
