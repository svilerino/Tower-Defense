/**
 *
 */
package main.model.enemy;

import java.awt.Dimension; 
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.model.ai.MovementAI;
import main.consts.FilePathConsts;
import main.dto.EnemyDTO;
import main.enums.EnemyType;
import main.enums.PlayerNumber;
import main.util.ConversorUtil;
import main.util.DataLoadUtil;
import main.util.FileUtil;

/**
 * @author Silvio Vileriño
 * @author Guido Tagliavini
 *
 */
public class ShapedEnemy extends Enemy{

	private Dimension imgArea;
	private Point2D pixPosition;
	private Double angle;
	private BufferedImage staticImage;
	private BufferedImage[] animationFrames;
	private Boolean countedAsDead;
	private Boolean countedAsWinner;
	private Boolean winner;
	private Boolean added;
	private Integer scoreReward;
	private Integer moneyReward;
	private Integer lifeDamage;
	private Integer frameIndex;
	private Integer remainingLife;
	private Integer delay;


	/**
	 *
	 * @param clr
	 *            Color
	 * @param map
	 *            Map(GamePanel Grid)
	 * @param max_life
	 *            Total Life
	 * @param retardo_pasos
	 *            delay between steps in the path
	 */
	public ShapedEnemy(EnemyType enemyType, Integer x, Integer y, MovementAI ai, PlayerNumber attacker) {
		EnemyDTO props = DataLoadUtil.getEnemyProperties(enemyType);

		this.setAttacker(attacker);

		this.setX(x);
		this.setY(y);
		this.imgArea = new Dimension(props.getSizeInPix(), props.getSizeInPix());
		this.setDelay(props.getDelay());
		this.setTotalLife(props.getTotalLife());
		this.setRemainingLife(props.getTotalLife());
		this.setAi(ai);
		this.setEnemyType(enemyType);
		this.setPixPosition(new Point2D.Double(ConversorUtil.logicToPixels(x),
				ConversorUtil.logicToPixels(y)));
		this.setAngle(0D);

		this.animationFrames=FileUtil.readImages(FilePathConsts.enemyImagePath
				+ "\\" + props.getImageName());

		this.frameIndex = 0;
		this.staticImage = animationFrames[frameIndex];
		this.countedAsDead = false;
		this.scoreReward = this.getRemainingLife() / 10;
		this.moneyReward = this.getDelay() / 10;
		this.winner = false;
		this.countedAsWinner = false;
		this.added = false;
		this.lifeDamage = 1;// puntos que se restan cuando gana un
										// enemy
	}

	//TODO ver
	public void increaseLevel(Integer level){
		EnemyDTO props=DataLoadUtil.getEnemyProperties(getEnemyType());
		setTotalLife((int) (props.getTotalLife() + (0.25 * level * props.getTotalLife())) );
		setRemainingLife(getTotalLife());
		setMoneyReward((int) (props.getMoneyReward() + (0.10 * level * props.getMoneyReward())));
		setScoreReward((int) (props.getScoreReward() + (0.15 * level * props.getScoreReward())));
	}

	public void modifySpeed(Integer modifyValue){
		if(this.getDelay()+modifyValue >0){
			this.setDelay(this.getDelay()+modifyValue);
		}else{
			System.out.println("[ INFO] Cannot reduce speed");
		}

	}

	public Point2D getCenter() {
		return new Point2D.Double(this.getPixPosition().getX()
				+ this.getImgArea().getWidth() / 2, this.getPixPosition()
				.getY()
				+ this.getImgArea().getHeight() / 2);
	}

	public Double getAngle() {
		return this.angle;
	}

	public Dimension getImgArea() {
		return this.imgArea;
	}

	/**
	 * @author Silvio Vileriño
	 * @return returns the life bar of the object
	 */
	public Rectangle2D getLifeShape() {
		Rectangle2D rect = new Rectangle2D.Double();
		rect.setFrame(this.pixPosition.getX(), this.pixPosition.getY(), (this
				.getRemainingLife().intValue() / this.getTotalLife().doubleValue())
				* this.imgArea.getWidth(), 2);
		return rect;
	}

	public Point2D getPixPosition() {
		return this.pixPosition;
	}

	public void setNextFrame(){
		this.frameIndex++;
		if(this.frameIndex>=this.animationFrames.length){
			this.frameIndex=0;
		}
	}

	/**
	 *
	 * @param angle
	 *            angle from y=0
	 * @return the rotated image to draw on the panel
	 */
	public BufferedImage getRotatedImage() {
		this.staticImage=this.animationFrames[this.frameIndex];
		BufferedImage image = this.staticImage;
		int w = image.getWidth();
		int h = image.getHeight();

		this.imgArea.setSize(image.getHeight(), image.getWidth());

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

	/**
	 * @author Silvio Vileriño
	 * @return
	 */
	public BufferedImage[] getRotatedFrames() {
		BufferedImage array[] = FileUtil.readImages(FilePathConsts.enemyImagePath
				+ "\\"
				+ DataLoadUtil.getEnemyProperties(this.getEnemyType())
						.getImageName());

		for(int a=0;a<array.length;a++){
			BufferedImage image = array[a];
			int w = image.getWidth();
			int h = image.getHeight();

			this.imgArea.setSize(image.getHeight(), image.getWidth());

			BufferedImage image2 = new BufferedImage(h, w,
					BufferedImage.TRANSLUCENT);//IMPORTANTE EL TRANSLUCENT PARA LOS GIF TRANSPARENTES

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
	 */
	public Shape getShape() {
		Rectangle2D fig = new Rectangle2D.Double();
		fig.setFrame(this.pixPosition.getX(), this.pixPosition.getY(),
				this.imgArea.getWidth(), this.imgArea.getHeight());
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
	 * sets the position of the enemy to x and y
	 *
	 * @param x
	 * @param y
	 */
	public void moveTo(Integer x, Integer y) {
		this.setX(ConversorUtil.pixelsToLogic(x));
		this.setY(ConversorUtil.pixelsToLogic(y));
		this.setPixPosition(new Point2D.Double(x, y));
	}

	/**
	 * reduces life in n points
	 */
	public void reduceLife(Integer n) {
		this.setRemainingLife(new Integer(this.getRemainingLife().intValue() - n.intValue()));
	}

	public void setAngle(Double angle) {
		this.angle = angle;
	}

	public void setImgArea(Dimension imgArea) {
		this.imgArea = imgArea;
	}

	public void setPixPosition(Point2D pixPosition) {
		this.pixPosition = pixPosition;
	}

	/**
	 * @return the countedAsDead
	 */
	public boolean isCountedAsDead() {
		return countedAsDead;
	}

	/**
	 * @param countedAsDead
	 *            the countedAsDead to set
	 */
	public void setCountedAsDead(boolean countedAsDead) {
		this.countedAsDead = countedAsDead;
	}

	/**
	 * @return the scoreForKill
	 */
	public Integer getScoreReward() {
		return scoreReward;
	}

	/**
	 * @return the MoneyForKill
	 */
	public Integer getMoneyReward() {
		return moneyReward;
	}

	/**
	 * @return the hasWon
	 */
	public boolean isWinner() {
		return winner;
	}

	/**
	 * @param winner
	 *            the hasWon to set
	 */
	public void setWinner(boolean winner) {
		this.winner = winner;
	}

	/**
	 * @return the punishmentForEnemyWon
	 */
	public Integer getLifeDamage() {
		return lifeDamage;
	}

	/**
	 * @return the countedAsWinner
	 */
	public boolean isCountedAsWinner() {
		return countedAsWinner;
	}

	/**
	 * @param countedAsWinner
	 *            the countedAsWinner to set
	 */
	public void setCountedAsWinner(boolean countedAsWinner) {
		this.countedAsWinner = countedAsWinner;
	}

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public Integer getRemainingLife() {
		return remainingLife;
	}

	public void setRemainingLife(Integer remainingLife) {
		this.remainingLife = remainingLife;
	}

	public Boolean isAdded() {
		return added;
	}

	public void setAdded(Boolean added) {
		this.added = added;
	}

	/**
	 * @param scoreReward the scoreReward to set
	 */
	public void setScoreReward(Integer scoreReward) {
		this.scoreReward = scoreReward;
	}

	/**
	 * @param moneyReward the moneyReward to set
	 */
	public void setMoneyReward(Integer moneyReward) {
		this.moneyReward = moneyReward;
	}
}
