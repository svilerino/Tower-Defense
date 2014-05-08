/**
 *
 */
package main.event;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import main.consts.LookAndFeelConsts;

/**
 * @author Silvio Vileriño
 *
 */
public class ComponentGFX implements MouseListener {

	private JComponent jcComponent;
	private Color clrOriginal;

	public ComponentGFX(JComponent jbButton){
		super();
		this.jcComponent=jbButton;
		this.clrOriginal=this.jcComponent.getBackground();
	}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {
		//jcComponent.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,
		//        LookAndFeelConsts.SELECTED_BORDER_COMPONENT_HIGHLIGHT_COLOR, LookAndFeelConsts.SELECTED_BORDER_COMPONENT_SHADOW_COLOR));
		jcComponent.setBackground(LookAndFeelConsts.SELECTED_COMPONENT_COLOR);
	}

	public void mouseExited(MouseEvent e) {
		//jcComponent.setBorder(BorderFactory.createEmptyBorder());
		jcComponent.setBackground(clrOriginal);
	}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

}
