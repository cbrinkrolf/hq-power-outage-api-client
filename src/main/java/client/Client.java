package client;

public interface Client {

	public long getLatestBisVersion();

	public String getRecord(long version);

}
