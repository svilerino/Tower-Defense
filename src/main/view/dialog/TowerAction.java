/**
 *
 */
package main.view.dialog;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.consts.FilePathConsts;
import main.model.tower.ShapedTower;
import main.util.UserScreenUtil;
import main.view.panel.GridPanel;

/**
 * @author Silvio Vileriño Clase que permite operaciones sobre una tower
 *         determinada
 */
public class TowerAction extends JDialog {
	
	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_HEIGHT = 200;
	private static final int DEFAULT_WIDTH = 80;

	private GridPanel gamePanelReference;
	private JButton jbCancel;
	private JButton jbSell;

	private JButton jbUpgrade;

	private JPanel jpComponents;

	private ShapedTower tObject;

	public TowerAction(JFrame parent, GridPanel gamePanel, ShapedTower tObject) {
		super(parent, true);

		gamePanelReference = gamePanel;
		this.tObject = tObject;

		this.setBounds(UserScreenUtil.getWidth() / 2
				- TowerAction.DEFAULT_HEIGHT / 2, UserScreenUtil.getHeight()
				/ 2 - TowerAction.DEFAULT_HEIGHT / 2,
				TowerAction.DEFAULT_WIDTH, TowerAction.DEFAULT_HEIGHT);
		setLayout(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Tower Options");
		setUndecorated(true);
		addComponents();
		setVisible(true);
	}

	private void addComponents() {
		jpComponents = new JPanel();
		jpComponents.setBounds(0, 0, TowerAction.DEFAULT_WIDTH,
				TowerAction.DEFAULT_HEIGHT);
		jpComponents.setLayout(new GridLayout(3, 0));

		jbSell = createButton("\nSell Tower", new SellAction(), jpComponents,
				new ImageIcon(FilePathConsts.menuImagesPath + "\\sellTower.jpg"));
		jbUpgrade = createButton("\nUpgrade Tower", new UpgradeAction(),
				jpComponents, new ImageIcon(FilePathConsts.menuImagesPath
						+ "\\upgradeTower.png"));
		jbCancel = createButton(
				"Cancel",
				new CancelAction(),
				jpComponents,
				new ImageIcon(FilePathConsts.menuImagesPath + "\\cancelTower.jpg"));

		getContentPane().add(jpComponents);
	}

	private JButton createButton(String label, ActionListener listener,
			JPanel container, Icon icon) {
		JButton btn = new JButton();
		btn.addActionListener(listener);
		container.add(btn);
		if (icon != null) {
			btn.setIcon(icon);
			btn.setToolTipText(label);
		} else {
			btn.setText(label);
		}
		return btn;
	}
	
	private class CancelAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			dispose();
		}

	}

	private class SellAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			gamePanelReference.deleteTower(tObject);
			jbCancel.doClick();
		}

	}

	private class UpgradeAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			gamePanelReference.upgradeTower(tObject);
			jbCancel.doClick();
		}

	}
}
