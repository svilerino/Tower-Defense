package main.util;

/**
 *
 * @author Guido Tagliavini
 *
 */

public class MathUtil {
	/**
	 * Calcula el ángulo (en grados) entre (x0, y0) e (x1, y1) tomando 0° sobre la recta y = y0
	 * @author Guido Tagliavini
	 */
	public static Double calculateAngle(Integer x0, Integer y0, Integer x1, Integer y1){
		if(x1.intValue() == x0.intValue()){
			return - (y1 - y0) * 90D;
		}else{
			if(x1.intValue() > x0.intValue()){
				return Math.toDegrees(Math.atan(- (y1 - y0) / (x1 - x0)));
			}else{
				return Math.toDegrees(Math.atan(- (y1 - y0) / (x1 - x0))) + 180;
			}
		}
	}

	public static Double distance(Integer x0, Integer y0, Integer x1, Integer y1) {
		return Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2));
	}

	public static Integer random(Integer a, Integer b){
		return (int) (Math.random() * (b - a + 1) + a);
	}
}
