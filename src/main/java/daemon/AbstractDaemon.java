package daemon;

import java.util.ArrayList;
import java.util.Collection;

import Utility.MyBaseLogger;
import export.DataSender;

public abstract class AbstractDaemon extends MyBaseLogger implements Daemon {

	Collection<DataSender> dataSenders = new ArrayList<>();

	@Override
	public void addDataSender(DataSender dataSender) {
		dataSenders.add(dataSender);
	}

	@Override
	public void callAllDataSenders() {
		for (DataSender ds : dataSenders) {
			ds.sendData();
		}
	}

}
