package main.model.ai;

import java.awt.geom.Point2D;  
import java.util.Vector;

import main.util.ConversorUtil;
import main.util.MathUtil;
import main.util.coordinate.XYCoordinate;

/**
 * @author Guido Tagliavini
 *
 */

public class MovementAI{
	
	private Vector<Point2D> path;
	private Vector<Double> angle;
	
	private static final Double SLOW_DOWN = 1D; //Math.sqrt(2D);
	
	public MovementAI() {
		this.setPath(new Vector<Point2D>());
		this.setAngle(new Vector<Double>());
	}

	/**
	 * @param pathFound path calculated from AlgorithmUtil
	 */

	public MovementAI(Vector<XYCoordinate> pathFound) {
		setPath(new Vector<Point2D>());
		setAngle(new Vector<Double>());
		
		generatePath(reversePath(pathFound));
	}
	
	public void generatePath(Vector<XYCoordinate> pathFound){
		if(path != null && angle != null){
			for (int i = 0; i < pathFound.size() - 1; i++) {
				int x0 = pathFound.get(i).getX();
				int y0 = pathFound.get(i).getY();
				int x1 = pathFound.get(i + 1).getX();
				int y1 = pathFound.get(i + 1).getY();
				
				int dy = y1 - y0;
				int dx = x1 - x0;
				
				double sd = 1;
				
				if(x0 != x1 && y0 != y1){ //if it moves diagonally
					sd = SLOW_DOWN.doubleValue();
				}
				
				/*
				double k = 0;
				
				if(i + 2 < pathFound.size()){
					int nextX = pathFound.get(i + 2).getX();
					int nextY = pathFound.get(i + 2).getY();
					
					if(dx != x0 - nextX && dy != y0 - nextY){
						//It changed orientation
						k = 4/3 * Math.sqrt(2) / sd; 
					}
				}
				*/
				
				int logicUnit = ConversorUtil.logicToPixels(1);

				for (int j = 0; j < sd * logicUnit; j++) {
										
					path.add(new Point2D.Double(ConversorUtil
									.logicToPixels(pathFound.get(i).getX()) + ((j /*+ k*/) * dx) / sd,
									ConversorUtil.logicToPixels(pathFound.get(i)
											.getY()) + ((j /*+ k*/) * dy) / sd));
					
					//We determine the rotation angle
					angle.add(90D - MathUtil.calculateAngle(x0, y0, x1, y1));
				}
			}
		}
	}
	
	public Vector<XYCoordinate> reversePath(Vector<XYCoordinate> pathFound){
		Vector<XYCoordinate> ret = new Vector<XYCoordinate>();
		
		for(int i = 0; i < pathFound.size(); i++){
			ret.add(pathFound.get(pathFound.size() - i - 1));
		}
		
		return ret;
	}
	
	public Vector<Point2D> getPath() {
		return path;
	}
	
	public void setPath(Vector<Point2D> path) {
		this.path = path;
	}
	
	public Vector<Double> getAngle() {
		return angle;
	}
	
	public void setAngle(Vector<Double> angle) {
		this.angle = angle;
	}
}
