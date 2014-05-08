package main.util;

import java.util.Vector;

import main.enums.CoordinateStatus;
import main.enums.EnemyType;
import main.model.grid.Grid;
import main.util.coordinate.AStarCoordinate;
import main.util.coordinate.XYCoordinate;

/**
 * Class which implements non-trivial algorithms. 
 * 
 * Clase que implementa algoritmos no triviales.
 *
 * @author Guido Tagliavini
 * @since v1.0.0
 */


public class AlgorithmUtil {
	
	/**
	 * Calculates the shortest path using an A* pathfinder, with Manhattan
	 * heuristic. Let f = g + h, where f is the total cost, g is the movement
	 * cost, h is the Manhattan distance.
	 * 
	 * Calcula el camino más corto usando un pathfinder A*, empleando la heurística 
	 * Manhattan. Sea f = g + h, donde f es el costo total, g es el costo del 
	 * movimiento y h es la distancia Manhattan.
	 *
	 * @param grid
	 * @param enemyType
	 * @return
	 */

	public static Vector<XYCoordinate> calculatePath(Integer xStart, Integer yStart,
			Integer xTarget, Integer yTarget, Grid grid, EnemyType enemyType) {

		Vector<XYCoordinate> path = new Vector<XYCoordinate>();

		if (enemyType == EnemyType.AEREAL) {

			return calculatePath(xStart, yStart, xTarget, yTarget, new Grid(), EnemyType.CLASSIC);

		} else {
			Vector<AStarCoordinate> openSet = new Vector<AStarCoordinate>();
			Vector<AStarCoordinate> closedSet = new Vector<AStarCoordinate>();

			AStarCoordinate init = new AStarCoordinate(xStart, yStart);
			init.setG(0);
			init.setH(calculateH(init, xTarget, yTarget));
			init.setF(init.getH());

			openSet.add(init);

			while (!openSet.isEmpty()) {
				AStarCoordinate lowest = getLowestF(openSet);

				if (lowest.getX() == xTarget && lowest.getY() == yTarget) {
					AStarCoordinate c = lowest;
					do{
						path.add(new XYCoordinate(c.getX(), c.getY()));
						c = getParent(c, closedSet);
					}while(c != null);

					return path;

				} else {
					closedSet.add(lowest);
					openSet.remove(lowest);

					Vector<AStarCoordinate> neighbours = getNeighbours(
							lowest, grid, closedSet, openSet);

					for(AStarCoordinate c : neighbours){
						int g = calculateG(lowest, c);
						int h = calculateH(c, xTarget, yTarget);
						int f = g + h;
						boolean v = false;

						if(!isInSet(c.getX(), c.getY(), openSet)){
							openSet.add(c);
							v = true;
						}else{
							if(g < c.getG()){
								v = true;
							}
						}

						if(v){
							c.setParent(new XYCoordinate(lowest.getX(), lowest.getY()));
							c.setG(g);
							c.setH(h);
							c.setF(f);
						}
					}
				}
			}
		}

		return null;
	}

	/**
	 * Obtiene el nodo padre dentro del mapa.
	 * @author Guido Tagliavini
	 * @param x
	 * @param closedSet
	 * @return
	 */
	
	private static AStarCoordinate getParent(AStarCoordinate x, Vector<AStarCoordinate> closedSet){
		if(x.getParent() == null){
			return null;
		}

		for(AStarCoordinate c : closedSet){
			if(x.getParent().getX() == c.getX() && x.getParent().getY() == c.getY()){
				return c;
			}
		}

		return null;
	}

	/**
	 * Obtiene el nodo en la lista abierta, con costo f más bajo.
	 * @author Guido Tagliavini
	 * @param openSet
	 * @return the node with lowest f
	 */
	private static AStarCoordinate getLowestF(Vector<AStarCoordinate> openSet) {
		AStarCoordinate lowest = openSet.get(0);

		for (AStarCoordinate c : openSet) {
			if (c.getF() < lowest.getF()) {
				lowest = c;
			}
		}

		return lowest;
	}
	
	/**
	 * Obtiene la AStarCoordinate de una lista, dadas las coordenadas x e y.
	 * @author Guido Tagliavini
	 * @param x
	 * @param y
	 * @param set
	 * @return the coordinate
	 */

	private static AStarCoordinate getCoordinate(Integer x, Integer y, Vector<AStarCoordinate> set){
		for(AStarCoordinate c : set){
			if(c.getX() == x && c.getY() == y){
				return c;
			}
		}

		return null;
	}

	
	/**
	 * Obtiene los nodos vecinos válidos dentro de la grilla, dada una coordenada.
	 * @author Guido Tagliavini
	 * @param c
	 * @param grid
	 * @param closedSet
	 * @param openSet
	 * @return 
	 */
	private static Vector<AStarCoordinate> getNeighbours (AStarCoordinate c,
			Grid grid, Vector<AStarCoordinate> closedSet, Vector<AStarCoordinate> openSet){

		Vector<AStarCoordinate> list = new Vector<AStarCoordinate>();

		int cx = c.getX();
		int cy = c.getY();

		for(int y = cy - 1; y <= cy + 1; y++){
			for(int x = cx - 1; x <= cx + 1; x++){
				if(y >= 0 && y < Grid.GRID_HEIGHT && x >= 0 && x < Grid.GRID_WIDTH){
					if(!(y == cy && x == cx)){
						if(grid.getFieldStatus(y, x).getCoordinateStatus() == CoordinateStatus.FREE){
							if(!isInSet(x, y, closedSet)){
								if(y - cy != 0 && x - cx != 0){
									if(grid.getFieldStatus(cy, x).getCoordinateStatus() == CoordinateStatus.FREE &&
											grid.getFieldStatus(y, cx).getCoordinateStatus() == CoordinateStatus.FREE){
										addNeighbour(list, x, y, openSet);
									}
								}else{
									addNeighbour(list, x, y, openSet);
								}
							}
						}
					}
				}
			}
		}
		
		return list;
	}
	
	private static void addNeighbour(Vector<AStarCoordinate> neighbours, Integer x, Integer y, Vector<AStarCoordinate> openSet){
		if(isInSet(x, y, openSet)){
			neighbours.add(getCoordinate(x, y, openSet));
		}else{
			neighbours.add(new AStarCoordinate(x, y));
		}
	}

	/**
	 * Verifica si el nodo de coordenadas x e y se encuentra en la lista dada.
	 * @author Guido Tagliavini
	 * @param x
	 * @param y
	 * @param set
	 * @return
	 */
	private static boolean isInSet(Integer x, Integer y,
			Vector<AStarCoordinate> set) {

		for(AStarCoordinate c : set){
			if(c.getX() == x && c.getY() == y){
				return true;
			}
		}

		return false;
	}

	/**
	 * Calcula el costo de movimiento g, dadas dos coordenadas.
	 * @author Guido Tagliavini
	 * @param c1
	 * @param c2
	 * @return
	 */
	private static int calculateG(AStarCoordinate c1, AStarCoordinate c2){
		if(Math.abs(c1.getX() - c2.getX()) + Math.abs(c1.getY() - c2.getY()) == 2){
			//Diagonal
			return c1.getG() + 14;
		}else{
			//Orthogonal
			return c1.getG() + 10;
		}
	}

	/**
	 * Calcula la distancia Manhattan h, dadas dos coordenadas.
	 * @author Guido Tagliavini
	 * @param x
	 * @param xTarget
	 * @param yTarget
	 * @return
	 */
	private static int calculateH(AStarCoordinate x, Integer xTarget, Integer yTarget){
		return (Math.abs(xTarget - x.getX()) + Math.abs(yTarget - x.getY())) * 10;
	}
}
