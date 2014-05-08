/**
 *
 */
package main.view.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Formatter;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import main.consts.FilePathConsts;
import main.consts.LookAndFeelConsts;
import main.dto.RoundResultsDTO;
import main.util.FileUtil;
import main.util.UserScreenUtil;
import main.view.frame.GameFrame;
import main.view.panel.TexturedPanel;

/**
 * @author Silvio Vilerino
 *
 */
public class FinishedRoundDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_HEIGHT = 200;
	private static final int DEFAULT_WIDTH = 300;

	private JPanel jpInfo;

	private JTextField jtfPoints;
	private JTextField jtfLifes;
	private JTextField jtfMoney;
	private JTextField jtfBonus;
	private JTextField jtfTotalTowers;
	private JTextField jtfTotalEnemies;
	private JTextField jtfKilledEnemies;
	private JTextField jtfKillRatio;
	private JTextField jtfRoundTime;
	private GameFrame gameFrameReference;
	private RoundResultsDTO resultInfo;

	public FinishedRoundDialog(GameFrame gameFrame, String message,
			RoundResultsDTO resultInfo) {
		super(gameFrame, true);
		this.gameFrameReference = gameFrame;
		this.resultInfo = resultInfo;
		this.setBounds(UserScreenUtil.getWidth() / 2
				- FinishedRoundDialog.DEFAULT_WIDTH / 2, UserScreenUtil
				.getHeight()
				/ 2 - FinishedRoundDialog.DEFAULT_HEIGHT / 2,
				FinishedRoundDialog.DEFAULT_WIDTH,
				FinishedRoundDialog.DEFAULT_HEIGHT);
		setLayout(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle(message);
		createInfoPanel();
		setVisible(true);
	}

	@Override
	protected JRootPane createRootPane() {
		JRootPane rootPane = new JRootPane();
		rootPane.registerKeyboardAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}

		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}

	private void createInfoPanel() {
		jpInfo = new TexturedPanel(FileUtil
				.readImage(FilePathConsts.menuImagesPath
						+ "/RoundDialog_Background.jpg"));
		jpInfo.setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT - 25);
		jpInfo.setBackground(Color.DARK_GRAY);
		jpInfo.setLayout(new GridLayout(9, 2));

		jpInfo.add(new JLabel("Round Time (secs):"));
		jtfRoundTime = new JTextField(resultInfo.getRoundTime().toString());
		jtfRoundTime.setEditable(false);
		jtfRoundTime.setOpaque(false);
		jtfRoundTime.setBorder(null);
		jtfRoundTime.setFont(new Font(LookAndFeelConsts.FONT,
				LookAndFeelConsts.FONT_STYLE, 14));
		jpInfo.add(jtfRoundTime);

		jpInfo.add(new JLabel("Puntos:"));
		jtfPoints = new JTextField(gameFrameReference.getCurrentScore()
				.toString());
		jtfPoints.setEditable(false);
		jtfPoints.setOpaque(false);
		jtfPoints.setBorder(null);
		jtfPoints.setFont(new Font(LookAndFeelConsts.FONT,
				LookAndFeelConsts.FONT_STYLE, 14));
		jpInfo.add(jtfPoints);

		jpInfo.add(new JLabel("Vidas:"));
		jtfLifes = new JTextField(gameFrameReference.getCurrentLifesRemaining()
				.toString());
		jtfLifes.setEditable(false);
		jtfLifes.setOpaque(false);
		jtfLifes.setBorder(null);
		jtfLifes.setFont(new Font(LookAndFeelConsts.FONT,
				LookAndFeelConsts.FONT_STYLE, 14));
		jpInfo.add(jtfLifes);

		jpInfo.add(new JLabel("Dinero:"));
		jtfMoney = new JTextField(gameFrameReference.getCurrentMoneyRemaining()
				.toString());
		jtfMoney.setEditable(false);
		jtfMoney.setOpaque(false);
		jtfMoney.setBorder(null);
		jtfMoney.setFont(new Font(LookAndFeelConsts.FONT,
				LookAndFeelConsts.FONT_STYLE, 14));
		jpInfo.add(jtfMoney);

		jpInfo.add(new JLabel("Bonus:"));
		jtfBonus = new JTextField(gameFrameReference.getCurrentBonus()
				.toString());
		jtfBonus.setEditable(false);
		jtfBonus.setOpaque(false);
		jtfBonus.setBorder(null);
		jtfBonus.setFont(new Font(LookAndFeelConsts.FONT,
				LookAndFeelConsts.FONT_STYLE, 14));
		jpInfo.add(jtfBonus);

		jpInfo.add(new JLabel("Total Towers:"));
		jtfTotalTowers = new JTextField(resultInfo.getTowersCount().toString());
		jtfTotalTowers.setEditable(false);
		jtfTotalTowers.setOpaque(false);
		jtfTotalTowers.setBorder(null);
		jtfTotalTowers.setFont(new Font(LookAndFeelConsts.FONT,
				LookAndFeelConsts.FONT_STYLE, 14));
		jpInfo.add(jtfTotalTowers);

		jpInfo.add(new JLabel("Total Enemies:"));
		jtfTotalEnemies = new JTextField(resultInfo.getTotalEnemies()
				.toString());
		jtfTotalEnemies.setEditable(false);
		jtfTotalEnemies.setOpaque(false);
		jtfTotalEnemies.setBorder(null);
		jtfTotalEnemies.setFont(new Font(LookAndFeelConsts.FONT,
				LookAndFeelConsts.FONT_STYLE, 14));
		jpInfo.add(jtfTotalEnemies);

		jpInfo.add(new JLabel("Killed Enemies:"));
		jtfKilledEnemies = new JTextField(resultInfo.getKilledEnemiesCount()
				.toString());
		jtfKilledEnemies.setEditable(false);
		jtfKilledEnemies.setOpaque(false);
		jtfKilledEnemies.setBorder(null);
		jtfKilledEnemies.setFont(new Font(LookAndFeelConsts.FONT,
				LookAndFeelConsts.FONT_STYLE, 14));
		jpInfo.add(jtfKilledEnemies);

		jpInfo.add(new JLabel("Kill Ratio:"));
		jtfKillRatio = new JTextField(new Formatter().format("%.2f",
				resultInfo.getRatioKilledEnemies()).toString());
		jtfKillRatio.setEditable(false);
		jtfKillRatio.setOpaque(false);
		jtfKillRatio.setBorder(null);
		jtfKillRatio.setFont(new Font(LookAndFeelConsts.FONT,
				LookAndFeelConsts.FONT_STYLE, 14));
		jpInfo.add(jtfKillRatio);

		jpInfo.setVisible(true);
		this.getContentPane().add(jpInfo);
	}
}
