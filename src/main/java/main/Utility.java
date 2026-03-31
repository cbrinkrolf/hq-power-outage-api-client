package main;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public abstract class Utility {

	private Utility() {

	}

	public static Logger getLogger(String loggerName, String logFolder, boolean useParentHandlers) {
		File folder = new File(logFolder);
		if (!folder.exists() || !folder.isDirectory()) {
			folder.mkdir();
		}

		Date d = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
		String startTime = df.format(d);

		String fileName = startTime + ".log";

		Logger logger = Logger.getLogger(loggerName);
		logger.setUseParentHandlers(useParentHandlers);
		FileHandler fh;
		try {
			fh = new FileHandler(logFolder + File.separator + fileName, true);
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			return logger;
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Logger getLogger(String loggerName, String logFolder) {

		return Utility.getLogger(loggerName, logFolder, false);
	}

}
