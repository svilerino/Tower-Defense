package main.runnable.gfx;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import main.consts.GameConsts;
import main.enums.GFXType;
import main.model.gfx.ShapedGFX;

/**
 *
 * @author Silvio Vileriño
 * creates a new Explosion in X,Y pixels position.
 *
 */
public class Explosion implements Runnable{

	private Graphics2D g2;
	private Point2D target;
	private ShapedGFX shapedGFX;

	public Explosion(Graphics2D g, Point2D target, ShapedGFX shapedGFX){
		this.g2=g;
		this.target=target;
		this.shapedGFX=shapedGFX;
	}

	@Override
	public void run() {
		for(int a=0;a<this.shapedGFX.getFrameCount();a++){
				g2.drawImage(this.shapedGFX.getFrame(a), null, (int) target.getX(), (int) target.getY());
			try {
				Thread.sleep(GameConsts.EXPLOSION_DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace(System.err);
			}
		}
	}

}
