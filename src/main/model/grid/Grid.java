package main.model.grid;

import java.io.Serializable;

import main.dto.GameDataDTO;
import main.dto.TowerDataDTO;
import main.enums.CoordinateStatus;
import main.enums.PlayerNumber;
import main.enums.TowerType;
import main.util.ConversorUtil;
import main.util.DataLoadUtil;
import main.util.coordinate.GridCoordinate;

/**
 *
 * @author Guido Tagliavini
 *
 */

public class Grid implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6915667337549401400L;
	public static final Integer GRID_WIDTH = 25;
	public static final Integer GRID_HEIGHT = 43;

	private GridCoordinate[][] grid;

	public Grid() {
		super();
		this.grid = new GridCoordinate[Grid.GRID_HEIGHT][Grid.GRID_WIDTH];

		for (int i = 0; i < Grid.GRID_HEIGHT; i++) {
			for (int j = 0; j < Grid.GRID_WIDTH; j++) {
				this.grid[i][j] = new GridCoordinate();
			}
		}
	}

	public GridCoordinate getFieldStatus(Integer x, Integer y) {
		return this.grid[x][y];
	}

	public GridCoordinate[][] getFieldStatus(Integer x, Integer y, Integer size){
		GridCoordinate[][] result = new GridCoordinate[size][size];

		for(int i = x; i < x + size; i++){
			for(int j = y; j < y + size; j++){
				result[i - x][j - y] = grid[i][j];
			}
		}

		return result;
	}

	public boolean isFree(GridCoordinate[][] grid){
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j <grid[i].length; j++){
				if(grid[i][j].getCoordinateStatus() == CoordinateStatus.OCCUPIED ){
					return false;
				}
			}
		}

		return true;
	}

	public GridCoordinate[][] getGrid() {
		return this.grid;
	}

	public void setFieldFree(Integer x, Integer y, Integer size) {
		this.setFieldStatus(x, y, size, null, CoordinateStatus.FREE);
	}

	public void setFieldOccupied(Integer x, Integer y, Integer size,
			TowerType towerType) {
		this.setFieldStatus(x, y, size, towerType, CoordinateStatus.OCCUPIED);
	}

	public void setFieldStatus(Integer x, Integer y, Integer size,
			TowerType towerType, CoordinateStatus coordinateStatus) {
		for (int i = x.intValue(); i < x.intValue() + size; i++) {
			for (int j = y.intValue(); j < y.intValue() + size; j++) {
				this.grid[i][j].setCoordinateStatus(coordinateStatus);
				this.grid[i][j].setTowerType(towerType);
			}
		}
	}
	
	public void setFieldStatus(Integer x, Integer y, GridCoordinate gridCoordinate){
		this.grid[x][y] = gridCoordinate;
	}
	
	public void setFieldFree(Integer x, Integer y, Integer sizeX, Integer sizeY){
		for (int i = x.intValue(); i < x.intValue() + sizeX; i++) {
			for (int j = y.intValue(); j < y.intValue() + sizeY; j++) {
				this.grid[i][j] = new GridCoordinate();
			}
		}
	}

	public void setGrid(GridCoordinate[][] grid) {
		this.grid = grid;
	}
	
	public void updateGrid(GameDataDTO gameDataDTO, PlayerNumber player){
		int y;
		
		if(player == PlayerNumber.PLAYER_1){
			y = 22;
		}else{
			y = 0;
		}
		
		this.setFieldFree(y, 0, GRID_HEIGHT/2, GRID_WIDTH);
		
		for(TowerDataDTO tp : gameDataDTO.getTowerDataDTO()){
			this.setFieldOccupied(tp.getY(), tp.getX(), 
					ConversorUtil.pixelsToLogic(DataLoadUtil.getTowerProperties(TowerType.values()[tp.getTowerType()]).getSizeInPix()),
					TowerType.values()[tp.getTowerType()]);
		}
	}
	
	@Deprecated
	public void mergeGrid(Grid otherGrid, PlayerNumber player){
		int y0, y1;
		
		if(player == PlayerNumber.PLAYER_1){
			y0 = 22;
			y1 = 42;
		}else{
			y0 = 0;
			y1 = 20;
		}
		
		for(int y = y0; y <= y1; y++){
			for(int x = 0; x <= Grid.GRID_WIDTH - 1; x++){
				this.setFieldStatus(y, x, otherGrid.getFieldStatus(y, x));
			}
		}
	}

	public String toString(){
		return String.valueOf(hashCode());
	}

}
