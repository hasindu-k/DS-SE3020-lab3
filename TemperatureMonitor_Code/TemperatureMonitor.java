import java.rmi.*;
import java.rmi.server.*;
import java.net.*;

// This class represents the client that listens for temperature changes.
// It implements TemperatureListener to receive callbacks from the server.
public class TemperatureMonitor extends UnicastRemoteObject implements TemperatureListener, Runnable 
{
	private int count = 0;

    // Constructor for the TemperatureMonitor class.
	public TemperatureMonitor() throws RemoteException {}

    // Main method to start the client.
	public static void main(String[] args) {

        // Set the security policy to allow all permissions.
	   System.setProperty("java.security.policy", "file:allowall.policy");
 

		try {
            // Lookup the remote TemperatureSensor object from the RMI registry.
			String registration = "//localhost/TemperatureSensor";

			Remote remoteService = Naming.lookup(registration);
			TemperatureSensor sensor = (TemperatureSensor) remoteService;

            // Get the initial temperature reading.
			double reading = sensor.getTemperature();
			System.out.println("Original temp : " + reading);

            // Create a TemperatureMonitor object.
			TemperatureMonitor monitor = new TemperatureMonitor();

			// Add method call to register the listener in the server object
            sensor.addTemperatureListener(monitor);

            // Start the monitor's run method to keep the client alive.
			monitor.run();
		} catch (MalformedURLException mue) {
            System.err.println("Malformed URL: " + mue.getMessage());
        } catch (RemoteException re) {
            System.err.println("Remote Error: " + re.getMessage());
        } catch (NotBoundException nbe) {
            System.err.println("Not Bound Error: " + nbe.getMessage());
        }
	}

    // Callback method invoked by the server when the temperature changes.
	public void temperatureChanged(double temperature)
			throws java.rmi.RemoteException {
		System.out.println("\nTemperature change event : " + temperature);
		count = 0;
	}

    // Run method to keep the client alive and display a counter.
	public void run() {
		for (;;) {
			count++;

			System.out.print("\r" + count);
			try {
				Thread.sleep(100); // Sleep for 100ms.
			} catch (InterruptedException ie) {
				System.err.println("Interrupted: " + ie.getMessage());
			}
		}
	}
}