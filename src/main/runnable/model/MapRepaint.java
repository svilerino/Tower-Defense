package main.runnable.model;

import main.view.panel.GridPanel;

/**
 * @author Silvio Vileriño
 */

public class MapRepaint implements Runnable {
	private GridPanel gridPanel;
	private boolean painting;

	private static final Integer REPAINTING_DELAY = 25;
	private static final Integer REPAINTING_FINISH_DELAY = 5555;
	public MapRepaint(GridPanel gamePanel){
		this.gridPanel = gamePanel;
		this.painting=true;
	}

	public void run() {
		while(painting){
			gridPanel.repaint();

			try {
				Thread.sleep(REPAINTING_DELAY);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		System.out.println("[ INFO] - MapRepaint is waiting for all threads to stop..." + Thread.currentThread().toString());

		try {
			Thread.sleep(REPAINTING_FINISH_DELAY);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		gridPanel.repaint();
		System.out.println("[ INFO] MapRepaint Thread Finished");
	}

	public void setPainting(boolean painting){
		this.painting = painting;
	}

	public boolean isPainting(){
		return this.painting;
	}
}
