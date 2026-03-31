package daemon;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleDaemon extends AbstractDaemon {

	private boolean writeLog = true;
	private Logger logger;
	private int waitSeconds = 60;

	public SimpleDaemon(int waitSeconds) {
		this(waitSeconds, null);
	}

	public SimpleDaemon(int waitSeconds, Logger logger) {
		this.waitSeconds = waitSeconds;

		this.logger = logger;
		if (logger == null) {
			writeLog = false;
		}
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
							logger.log(Level.SEVERE, e.getMessage());
						}
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	private boolean isLogging() {
		return writeLog && logger != null;
	}
}
