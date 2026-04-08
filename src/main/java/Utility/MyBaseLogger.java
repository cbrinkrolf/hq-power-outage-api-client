package Utility;

import java.util.logging.Logger;

public class MyBaseLogger implements MyLogger {

	private Logger logger = null;
	private boolean logging = false;

	@Override
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public Logger getLogger() {
		return this.logger;
	}

	@Override
	public void setLogging(boolean logging) {
		this.logging = logger != null && logging;
	}

	@Override
	public boolean isLogging() {
		return this.logging;
	}

}
