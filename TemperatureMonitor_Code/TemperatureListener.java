// This interface defines the remote callback method that will be invoked by the server
// whenever there is a change in temperature. It extends Remote to indicate it is a remote interface.
interface TemperatureListener extends java.rmi.Remote
{
    // This method is called by the server to notify the client of a temperature change.
	public void temperatureChanged(double temperature) throws 	java.rmi.RemoteException;
}
