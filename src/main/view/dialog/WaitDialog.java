package main.view.dialog;
import java.awt.Component;
import javax.swing.ProgressMonitor;


public class WaitDialog{
	public static void main(String args[]){
		if(WaitDialog.waitingDialog(null, "PL3", "Waiting for players to join...", 100L)){
			System.out.println("El usuario apreto cancelar");
		}else{
			System.out.println("timedout operation");
		}
	}

	public static boolean waitingDialog(Component parentWindow, Object message, String note, Long delayTime){
		boolean wasCancelled=false;
		ProgressMonitor monitor=new ProgressMonitor(null, message, note, 0, 100);
		for(int a=1;((a<=100) && (!monitor.isCanceled()));a++){
			monitor.setProgress(a);
			try {
				Thread.sleep(delayTime);
			} catch (InterruptedException e) {
				e.printStackTrace(System.err);
			}
			if(monitor.isCanceled()){
				wasCancelled=true;
			}
		}
		return wasCancelled;
	}
}
