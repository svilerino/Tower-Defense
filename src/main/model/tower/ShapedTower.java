/**
 *
 */
package main.model.tower;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import main.consts.FilePathConsts;
import main.consts.GameConsts;
import main.dto.TowerDTO;
import main.enums.PlayerNumber;
import main.enums.ShootStyle;
import main.enums.TowerType;
import main.util.ConversorUtil;
import main.util.DataLoadUtil;
import main.util.FileUtil;

/**
 * @author Silvio Vileriño
 */

public class ShapedTower extends Tower implements Serializable{

	private static final long serialVersionUID = 6818806766087058558L;
	private BufferedImage[] animationFrames;
	private BufferedImage baseImage;

	private Boolean isSelected;
	private Integer frameIndex;

	private Dimension imgArea;
	private Point2D pixPosition;
	private Color towerClr;
	private Color baseClr;

	private Integer shootRadius;
	private Integer shootDelay;
	private Integer shootDamage;
	private ShootStyle shootStyle;

	private Integer towerCost;
	private Integer upgradeCost;

	private Double angle;
	private Integer level;


	public ShapedTower(TowerType towerType, Integer x, Integer y, PlayerNumber owner) {
		this.setX(x);
		this.setY(y);
		this.level=1;
		this.baseClr=Color.LIGHT_GRAY;
		TowerDTO props = DataLoadUtil.getTowerProperties(towerType);
		this.towerClr = props.getColor();
		shootDelay = props.getShootDelay();
		shootRadius = props.getShootRadius();
		shootDamage = props.getShootDamage();
		shootStyle = props.getShootStyle();
		towerCost = props.getTowerCost();
		upgradeCost = props.getUpgradeCost();
		this.baseImage = FileUtil.readImage(FilePathConsts.towersSpritesPath
				+ "\\"
				+ props.getBaseImageName());
		this.imgArea = new Dimension(props.getSizeInPix(), props.getSizeInPix());
		this.setSize(new Integer(ConversorUtil.pixelsToLogic(props
				.getSizeInPix())));
		this.setTowerType(towerType);
		this.setOwnerPlayer(owner);
		this.setPixPosition(new Point2D.Double(ConversorUtil.logicToPixels(x),
				ConversorUtil.logicToPixels(y)));
		this.setAngle(0D);
		this.frameIndex=0;
		this.animationFrames=this.getRotatedFrames();
		this.isSelected=false;
	}

	public void upgrade(Integer level){
		TowerDTO props = DataLoadUtil.getTowerProperties(getTowerType());
		this.setShootDamage(props.getShootDamage() + GameConsts.UPGRADE_TOWER_DAMAGE * (level-1));
		this.setShootRadius(props.getShootRadius() + GameConsts.UPGRADE_TOWER_RADIO * (level-1));
		if (props.getShootDelay() > ((GameConsts.UPGRADE_TOWER_DELAY * (level-1)) + 25)){
			this.setShootDelay(props.getShootDelay() - GameConsts.UPGRADE_TOWER_DELAY * (level-1));
		}else{
			System.out.println(this + " - Can't speed up anymore[Upgrade]");
		}
		this.level++;
	}

	/**
	 *
	 * @return the Radio Ellipse Shape of the tower
	 */
	public Ellipse2D getRadioShape() {
		Ellipse2D eli = new Ellipse2D.Double();
		Point2D towerCenter = new Point2D.Double();
		Point2D areaCorner = new Point2D.Double();
		towerCenter.setLocation(this.getShape().getBounds2D().getCenterX(),
				this.getShape().getBounds2D().getCenterY());
		areaCorner.setLocation(this.getShape().getBounds2D().getX()
				+ shootRadius, this.getShape().getBounds2D().getY()
				+ shootRadius);
		eli.setFrameFromCenter(towerCenter, areaCorner);
		return eli;
	}

	public Rectangle2D getBaseShape(){
		Rectangle2D rect = new Rectangle2D.Double();
		rect.setFrame(getPixPosition().getX(), getPixPosition().getY(), ConversorUtil.logicToPixels(getSize()), ConversorUtil.logicToPixels(getSize()));
		return rect;
	}

	public void setNextFrame(){
		this.frameIndex++;
		if(this.frameIndex>=this.animationFrames.length){
			this.frameIndex=0;
		}
	}

	public BufferedImage getRotatedImage() {

		BufferedImage image = this.animationFrames[frameIndex];
		int w = image.getWidth();
		int h = image.getHeight();

		imgArea.setSize(image.getHeight(), image.getWidth());

		int imageType=BufferedImage.OPAQUE;
		if(image.getColorModel().hasAlpha()){
			imageType=BufferedImage.TRANSLUCENT;
		}
		BufferedImage image2 = new BufferedImage(h, w,
			imageType);

		Graphics2D g2d = image2.createGraphics();
		double x = (h - w) / 2.0;
		double y = (w - h) / 2.0;

		AffineTransform at = AffineTransform.getTranslateInstance(x, y);
		at.rotate(Math.toRadians(this.angle), w / 2.0, h / 2.0);

		g2d.drawRenderedImage(image, at);
		return image2;
	}

	public BufferedImage[] getRotatedFrames() {
		BufferedImage array[] = FileUtil.readImages(FilePathConsts.towersSpritesPath
				+ "\\"
				+ DataLoadUtil.getTowerProperties(this.getTowerType())
						.getImageName());

		for(int a=0;a<array.length;a++){
			BufferedImage image = array[a];
			int w = image.getWidth();
			int h = image.getHeight();

			this.imgArea.setSize(image.getHeight(), image.getWidth());

			BufferedImage image2 = new BufferedImage(h, w,
					BufferedImage.TRANSLUCENT);

			Graphics2D g2d = image2.createGraphics();
			double x = (h - w) / 2.0;
			double y = (w - h) / 2.0;

			AffineTransform at = AffineTransform.getTranslateInstance(x, y);
			at.rotate(Math.toRadians(this.angle), w / 2.0, h / 2.0);

			g2d.drawRenderedImage(image, at);
			array[a]=image2;
		}
		return array;
	}

	/**
	 * @author Silvio Vileriño
	 * @return returns a Drawable Shape to represent the Object Border
	 *
	 */
	public Shape getShape() {
		Rectangle2D fig = new Rectangle2D.Double();
		fig.setFrame(pixPosition.getX(), pixPosition.getY(), this.imgArea
				.getWidth(), this.imgArea.getHeight());
		return fig;
	}

	/**
	 * @author Silvio Vileriño
	 * @param x
	 *            moves x pixels from its original position
	 * @param y
	 *            moves y pixels from its original position
	 */
	public void move(Integer x, Integer y) {
		this.setX(this.getX() + ConversorUtil.pixelsToLogic(x));
		this.setY(this.getY() + ConversorUtil.pixelsToLogic(y));
		this.setPixPosition(new Point2D.Double(ConversorUtil.logicToPixels(this
				.getX())
				+ x, ConversorUtil.logicToPixels(this.getY()) + y));

	}

	/**
	 * Sets the position of the enemy to x and y
	 *
	 * @param x
	 * @param y
	 */
	public void moveTo(Integer x, Integer y) {
		this.setX(ConversorUtil.pixelsToLogic(x));
		this.setY(ConversorUtil.pixelsToLogic(y));
		this.setPixPosition(new Point2D.Double(x, y));
	}

	public Point2D getCenter() {
		return new Point2D.Double(ConversorUtil.logicToPixels(this.getX())
				+ this.getImgArea().getWidth() / 2, ConversorUtil
				.logicToPixels(this.getY())
				+ this.getImgArea().getHeight() / 2);
	}

	public Dimension getImgArea() {
		return imgArea;
	}

	public void setImgArea(Dimension imgArea) {
		this.imgArea = imgArea;
	}

	public Color getTowerClr() {
		return towerClr;
	}

	public void setTowerClr(Color clr) {
		this.towerClr = clr;
	}

	public Point2D getPixPosition() {
		return pixPosition;
	}

	public void setPixPosition(Point2D pixPosition) {
		this.pixPosition = pixPosition;
	}

	public Double getAngle() {
		return angle;
	}

	public void setAngle(Double angle) {
		this.angle = angle;
	}

	public BufferedImage getBaseImage() {
		return baseImage;
	}

	public Integer getShootDelay() {
		return shootDelay;
	}

	public void setShootDelay(Integer shootDelay) {
		this.shootDelay = shootDelay;
	}

	public Integer getShootDamage() {
		return shootDamage;
	}

	public void setShootDamage(Integer shootDamage) {
		this.shootDamage = shootDamage;
	}

	public Integer getShootRadius() {
		return shootRadius;
	}

	public void setShootRadius(Integer shootRadius) {
		this.shootRadius = shootRadius;
	}

	/**
	 * @return the shootType
	 */
	public ShootStyle getShootStyle() {
		return shootStyle;
	}

	/**
	 * @return the isSelected
	 */
	public Boolean isSelected() {
		return isSelected;
	}

	/**
	 * @param isSelected the isSelected to set
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	/**
	 * @return the baseClr
	 */
	public Color getBaseClr() {
		return baseClr;
	}

	/**
	 * @return the towerCost
	 */
	public Integer getTowerCost() {
		return towerCost;
	}

	/**
	 * @return the upgradeCost
	 */
	public Integer getUpgradeCost() {
		return upgradeCost;
	}

	/**
	 * @return the level
	 */
	public Integer getLevel() {
		return level;
	}
}
