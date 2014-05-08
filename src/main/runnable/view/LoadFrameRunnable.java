/**
 *
 */
package main.runnable.view;

import main.view.frame.LoadGameFrame;

/**
 * @author Silvio The Black
 *
 */
public class LoadFrameRunnable implements Runnable {

	private Integer delay;
	private LoadGameFrame frame;

	public LoadFrameRunnable(Integer delay, LoadGameFrame frame){
		this.delay = delay;
		this.frame = frame;
	}

	@Override
	public void run() {
		for(int a = 0; a <= 100; a++){
			frame.setProgress(a);
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		frame.progressCompleted();
	}

}
