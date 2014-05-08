package main.view.panel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import main.consts.LookAndFeelConsts;
/**
 *
 * @author Silvio Vilerino
 * Panel Contenedor con un fondo determinado.
 */
public class TexturedPanel extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = -4631468000287018148L;
	private BufferedImage backGroundImagePath;

	public TexturedPanel(BufferedImage backGroundImagePath){
		this.backGroundImagePath = backGroundImagePath;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if(LookAndFeelConsts.MENU_IMAGE_BACKGROUNDS){
			g2.drawImage(backGroundImagePath, 0, 0, getWidth(), getHeight(), this);
			setOpaque(false);
		}
		super.paintComponent(g);
	}

}
