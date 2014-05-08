package main.util.validator;

import main.model.grid.Grid;

public class GridValidator {
	
	public static boolean validateCoordinate(Integer x, Integer y, Integer size, Grid gridMap){
		if (x >= 0
				&& (x + size) <= Grid.GRID_WIDTH
				&& y >= 0
				&& (y + size) <= Grid.GRID_HEIGHT
				&& gridMap.isFree(
						gridMap.getFieldStatus(
								y, x, size))) {
			return true;
		}
		
		return false;
	}
}
