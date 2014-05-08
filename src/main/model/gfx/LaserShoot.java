package main.model.gfx;

import java.awt.Color;
import java.awt.geom.Point2D;


public class LaserShoot {
	
	private Point2D target;
	private Point2D origin;
	private Color clr;

	public LaserShoot(Point2D origin, Point2D target, Color clr){
		this.target = target;
		this.origin = origin;
		this.clr = clr;
	}

	public Point2D getTarget() {
		return target;
	}

	public void setTarget(Point2D target) {
		this.target = target;
	}

	public Point2D getOrigin() {
		return origin;
	}

	public void setOrigin(Point2D origin) {
		this.origin = origin;
	}

	public Color getClr() {
		return clr;
	}

	public void setClr(Color clr) {
		this.clr = clr;
	}
	
}
