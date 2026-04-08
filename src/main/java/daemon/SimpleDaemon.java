package daemon;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleDaemon extends AbstractDaemon {

	private int waitSeconds = 60;

	public SimpleDaemon(int waitSeconds) {
		this(waitSeconds, null);
	}

	public SimpleDaemon(int waitSeconds, Logger logger) {
		this.waitSeconds = waitSeconds;

		this.setLogger(logger);
	}

	public void start() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						callAllDataSenders();
						Thread.sleep(waitSeconds * 1000);
					} catch (InterruptedException e) {
						if (isLogging()) {
							getLogger().log(Level.SEVERE, e.getMessage());
						}
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

}
