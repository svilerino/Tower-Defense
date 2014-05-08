package sv.view.frame;

import java.awt.BorderLayout;   
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import sv.runnable.socket.ConnectionListener;


/**
 *
 * @author Guido Tagliavini
 *
 */

public class MainFrame extends JFrame{

	private static final long serialVersionUID = 7888648476343908171L;
	private final int DEFAULT_WIDTH = 500;
	private final int DEFAULT_HEIGHT = 500;

	private JTable jtServers;
	private JButton jbExit;
	private JButton jbRefresh;
	
	private Vector<Vector <String>> data;
	private Vector<String> columnNames;
	
	private ConnectionListener connections;
	

	public MainFrame(){
		super("Server [Connection Manager]");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			//TODO cambiar a HIDE_ON_CLOSE
		this.setBounds(150, 150, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setResizable(true);

		//Setting the table
		jtServers = new JTable();

		data = new Vector<Vector<String>>();
		columnNames = new Vector<String>();
		columnNames.add("Dirección host");
		columnNames.add("Port host");			//Puerto en el que espera conexión
		columnNames.add("Nombre host");
		columnNames.add("Tipo de juego");
		columnNames.add("Nombre del server");
		jtServers.setModel(new DefaultTableModel(null, columnNames));

		//Setting the buttons

		jbExit = new JButton("Salir");
		jbExit.addActionListener(new ExitAction());
		
		jbRefresh = new JButton("Actualizar");
		jbRefresh.addActionListener(new RefreshAction());

		Container cButtons = new Container();
		cButtons.setLayout(new FlowLayout());
		cButtons.add(jbRefresh);
		cButtons.add(jbExit);

		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(new JScrollPane(jtServers), BorderLayout.CENTER);
		c.add(cButtons, BorderLayout.SOUTH);

		this.setContentPane(c);
		this.setVisible(true);

		startServer();
	}

	private void startServer(){
		connections = new ConnectionListener();
		Thread t = new Thread(connections);
		t.start();
	}

	private class ExitAction implements ActionListener{
		public void actionPerformed(ActionEvent e){
			System.exit(0);
		}
	}
	
	private class RefreshAction implements ActionListener{
		public void actionPerformed(ActionEvent e){
			data = connections.getData();
			
			if(data.size() == 0){
				jtServers.setModel(new DefaultTableModel(null, columnNames));
			}else{
				jtServers.setModel(new DefaultTableModel(data, columnNames));
			}
		}
	}
}
