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

import main.model.enemy.ShapedEnemy;

/**
 * @author Silvio Vilerino
 *
 */
public class EnemyInfo extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = -7660350268819698304L;
	private JTextField jtfName;
	private JTextField jtfDelay;
	private JTextField jtfLife;
	private String title;


	public EnemyInfo(Point2D position, String titulo) {
		super();
		this.title=titulo;
		this.setBackground(Color.LIGHT_GRAY);
		this.setBounds((int) position.getX(), (int) position.getY(), 185, 60);
		this.setLayout(new GridLayout(0, 2));
		this.addComponents();
		this.setVisible(true);
	}
	
	public void enemySelected(ShapedEnemy se) {
		this.jtfDelay.setText(se.getDelay().toString());
		this.jtfName.setText(se.getEnemyType().name());
		this.jtfLife.setText(se.getTotalLife().toString());
	}

	public void addComponents(){
		this.add(new JLabel("<html><b>" + this.title.split(";")[0] + "</b>"));
		this.add(new JLabel("<html><b>" + this.title.split(";")[1] + "</b>"));
		this.add(new JLabel("Tipo: "));
		this.jtfName=new JTextField(11);
		this.jtfName.setEnabled(false);
		this.add(jtfName);

		this.add(new JLabel("Lentitud: "));
		this.jtfDelay=new JTextField(11);
		this.jtfDelay.setEnabled(false);
		this.add(jtfDelay);
		this.add(new JLabel("Vida: "));
		this.jtfLife=new JTextField(11);
		this.jtfLife.setEnabled(false);
		this.add(jtfLife);
	}
}
