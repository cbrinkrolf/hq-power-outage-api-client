package main;

import java.util.logging.LogManager;
import java.util.logging.Logger;

import client.HQClient;
import daemon.Daemon;
import daemon.SimpleDaemon;
import export.JSONFileWriter;

public class Starter {

	private static String statsFolder = "HQstats";
	private boolean writeLog = true;
	private static String logFolder = "logs";

	public Starter() {

		Logger logger = Utility.getLogger("MyLog", logFolder, true);

		HQClient client = new HQClient(logger);

		JSONFileWriter writer = new JSONFileWriter(client, statsFolder, logger);

		Daemon d = new SimpleDaemon(60, logger);
		d.addDataSender(writer);
		d.start();

		System.out.println("durch");
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				System.out.println("cleanup");
				LogManager.getLogManager().reset();
			}
		}));

	}

}
