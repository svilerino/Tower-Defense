/**
 *
 */
package main.model.gfx;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import main.enums.EnemyType;
import main.enums.GFXType;
import main.model.ai.MovementAI;
import main.model.grid.Grid;
import main.util.AlgorithmUtil;
import main.util.ConversorUtil;
import main.util.coordinate.XYCoordinate;

/**
 * @author Silvio
 *
 */
public class Shoot extends ShapedGFX{

	private Point2D target;
	private Point2D position;
	private Point2D origin;
	private Grid map;
	private Vector<Point2D> path;
	private Color clr;

	public Shoot(Point2D origin, Point2D target, Grid map, Color clr) {
		super(GFXType.SHOOT_BULLET);
		this.target = target;
		this.origin = origin;
		this.map = map;
		this.path = buildPath();
		this.clr=clr;
	}

	public BufferedImage getRotatedRenderedBullet(Shape shape, Color clr, double degrees){
		BufferedImage img=new BufferedImage(4, 4,
				BufferedImage.TRANSLUCENT);;
		Graphics2D g2d=img.createGraphics();
		if(clr!=null){
			g2d.setColor(clr);
		}else{
			g2d.setColor(Color.MAGENTA);
		}
		if(shape == null){
			g2d.fillArc(0, 0, 8, 8, 0, 360);
		}else{
			g2d.fill(shape);
		}
		return getRotatedFrame(0, degrees, img);
	}

	/**
	 * @return the path
	 */
	public Vector<Point2D> getPath() {
		return path;
	}

	/**
	 * @return the position
	 */
	public Point2D getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Point2D position) {
		this.position = position;
	}


	/**
	 * @return the clr
	 */
	public Color getClr() {
		return clr;
	}

	private Vector<Point2D> buildPath() {
		Vector<XYCoordinate> pathFound = AlgorithmUtil.calculatePath(
				ConversorUtil.pixelsToLogic((int) origin.getX()), ConversorUtil
						.pixelsToLogic((int) origin.getY()), ConversorUtil
						.pixelsToLogic((int) target.getX()), ConversorUtil
						.pixelsToLogic((int) target.getY()), this.map,
				EnemyType.AEREAL);

		return new MovementAI(pathFound).getPath();
	}

	private BufferedImage getRotatedFrame(int index, double degrees, BufferedImage imageOrigin) {
		BufferedImage image=null;
		if(imageOrigin==null){
			image = this.getFrame(index);
		}else{
			image=imageOrigin;
		}

		int w = image.getWidth();
		int h = image.getHeight();

		BufferedImage image2 = new BufferedImage(h, w,
				BufferedImage.TRANSLUCENT);

		Graphics2D g2d = image2.createGraphics();
		double x = (h - w) / 2.0;
		double y = (w - h) / 2.0;

		AffineTransform at = AffineTransform.getTranslateInstance(x, y);
		at.rotate(Math.toRadians(degrees), w / 2.0, h / 2.0);

		g2d.drawRenderedImage(image, at);
		return image2;
	}

}
