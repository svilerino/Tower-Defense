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
import javax.swing.JPanel;

import main.consts.FilePathConsts;
import main.enums.BonusType;
import main.util.UserScreenUtil;
import main.view.frame.GameFrame;

/**
 * @author Silvio Vileriño
 *
 */
public class BonusUsage extends JDialog {

	private static final long serialVersionUID = -4745657947561258412L;
	private static final int DEFAULT_HEIGHT = 200;
	private static final int DEFAULT_WIDTH = 100;
	private GameFrame gameFrameReference;
	private JButton jbCancel;
	private JButton jbMoney;
	private JButton jbScore;
	private JButton jbLife;
	private JButton jbEnemySlowDown;
	private JButton jbTowerUltraQuick;
	private JPanel jpComponents;

	private class CancelAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			BonusUsage.this.setVisible(false);
		}
	}

	private class MoneyAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			gameFrameReference.applyBonus(BonusType.MONEY);
			gameFrameReference.removeBonus(1);
			BonusUsage.this.setVisible(false);
		}
	}

	private class ScoreAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			gameFrameReference.applyBonus(BonusType.SCORE);
			gameFrameReference.removeBonus(1);
			BonusUsage.this.setVisible(false);
		}
	}

	private class LifeAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			gameFrameReference.applyBonus(BonusType.LIFE);
			gameFrameReference.removeBonus(1);
			BonusUsage.this.setVisible(false);
		}
	}

	private class EnemySlowDownAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			gameFrameReference.applyBonus(BonusType.ENEMY_SLOWDOWN);
			gameFrameReference.removeBonus(1);
			BonusUsage.this.setVisible(false);
		}
	}

	private class TowerUltraQuickAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			gameFrameReference.applyBonus(BonusType.TOWER_ULTRAQUICK);
			gameFrameReference.removeBonus(1);
			BonusUsage.this.setVisible(false);
		}
	}

	public BonusUsage(GameFrame gameFrame) {
		super(gameFrame, true);
		this.gameFrameReference = gameFrame;
		this.setBounds(UserScreenUtil.getWidth() / 2
				- BonusUsage.DEFAULT_HEIGHT / 2, UserScreenUtil.getHeight() / 2
				- BonusUsage.DEFAULT_HEIGHT / 2, BonusUsage.DEFAULT_WIDTH,
				BonusUsage.DEFAULT_HEIGHT);
		setLayout(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setUndecorated(true);
		addComponents();
		setVisible(true);

	}

	private void addComponents() {
		jpComponents = new JPanel();
		jpComponents.setBounds(0, 0, BonusUsage.DEFAULT_WIDTH,
				BonusUsage.DEFAULT_HEIGHT);
		jpComponents.setLayout(new GridLayout(3, 2));

		jbMoney = createButton("Money", new MoneyAction(), jpComponents,
				new ImageIcon(FilePathConsts.menuImagesPath + "\\moneyBonus.png"));
		jbScore = createButton("Score", new ScoreAction(), jpComponents,
				new ImageIcon(FilePathConsts.menuImagesPath + "\\scoreBonus.png"));
		jbEnemySlowDown = createButton("EnemySlowDown",
				new EnemySlowDownAction(), jpComponents, new ImageIcon(
						FilePathConsts.menuImagesPath + "\\slowBonus.png"));
		jbTowerUltraQuick = createButton("TowerUltraQuick",
				new TowerUltraQuickAction(), jpComponents, new ImageIcon(
						FilePathConsts.menuImagesPath + "\\quickBonus.gif"));
		jbLife = createButton("Life", new LifeAction(), jpComponents,
				new ImageIcon(FilePathConsts.menuImagesPath + "\\lifeBonus.png"));
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
}
