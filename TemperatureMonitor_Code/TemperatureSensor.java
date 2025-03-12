// This interface defines the remote methods provided by the TemperatureSensor server.
interface TemperatureSensor extends java.rmi.Remote
{
	// Method to get the current temperature.
	public double getTemperature() throws java.rmi.RemoteException;

    // Method to add a TemperatureListener to the server's listener list.
	public void addTemperatureListener(TemperatureListener listener ) throws java.rmi.RemoteException;

    // Method to remove a TemperatureListener from the server's listener list.
	public void removeTemperatureListener(TemperatureListener listener ) throws java.rmi.RemoteException;
}
