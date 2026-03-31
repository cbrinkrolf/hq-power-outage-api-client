package daemon;

import export.DataSender;

public interface Daemon {

	public void addDataSender(DataSender dataSender);

	public void callAllDataSenders();

	public void start();

}
