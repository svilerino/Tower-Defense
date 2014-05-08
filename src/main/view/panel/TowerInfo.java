/**
 *
 */
package main.view.panel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.geom.Point2D;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.model.tower.ShapedTower;

/**
 * @author Silvio
 *
 */
public class TowerInfo extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = -7660350268819698304L;
	private JTextField jtfName;
	private JTextField jtfLevel;
	private JTextField jtfColor;
	private JTextField jtfDamage;
	private JTextField jtfDelay;
	private JTextField jtfRadius;


	public TowerInfo(Point2D position) {
		super();
		this.setBackground(Color.LIGHT_GRAY);
		this.setBounds((int) position.getX(), (int) position.getY(), 185, 90);
		this.setLayout(new GridLayout(0, 2));
		this.addComponents();
		this.setVisible(true);
	}

	public void towerPicked(ShapedTower st) {
		this.jtfColor.setBackground(st.getTowerClr());
		this.jtfDamage.setText(st.getShootDamage().toString());
		this.jtfDelay.setText(st.getShootDelay().toString());
		this.jtfRadius.setText(st.getShootRadius().toString());
		this.jtfName.setText(st.getTowerType().toString());
		this.jtfLevel.setText(st.getLevel().toString());
	}

	public void addComponents(){
		this.add(new JLabel("Tipo: "));
		this.jtfName=new JTextField(11);
		this.jtfName.setEnabled(false);
		this.add(jtfName);
		this.add(new JLabel("Level: "));
		this.jtfLevel=new JTextField(11);
		this.jtfLevel.setEnabled(false);
		this.add(jtfLevel);
		this.add(new JLabel("Color: "));
		this.jtfColor=new JTextField(11);
		this.jtfColor.setEnabled(false);
		this.add(jtfColor);
		this.add(new JLabel("Daño: "));
		this.jtfDamage=new JTextField(11);
		this.jtfDamage.setEnabled(false);
		this.add(jtfDamage);
		this.add(new JLabel("Lentitud: "));
		this.jtfDelay=new JTextField(11);
		this.jtfDelay.setEnabled(false);
		this.add(jtfDelay);
		this.add(new JLabel("Radio: "));
		this.jtfRadius=new JTextField(11);
		this.jtfRadius.setEnabled(false);
		this.add(jtfRadius);
	}
}
