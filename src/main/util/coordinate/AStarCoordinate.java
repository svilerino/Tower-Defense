package main.util.coordinate;

public class AStarCoordinate extends XYCoordinate{

	private XYCoordinate parent;
	private Integer f;
	private Integer g;
	private Integer h;
	
	public AStarCoordinate(Integer x, Integer y){
		super(x,y);
	}
	
	public XYCoordinate getParent() {
		return parent;
	}

	public void setParent(XYCoordinate parent) {
		this.parent = parent;
	}

	public Integer getF() {
		return f;
	}

	public void setF(Integer f) {
		this.f = f;
	}

	public Integer getG() {
		return g;
	}

	public void setG(Integer g) {
		this.g = g;
	}

	public Integer getH() {
		return h;
	}

	public void setH(Integer h) {
		this.h = h;
	}
}
