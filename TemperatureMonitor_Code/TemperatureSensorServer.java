import java.util.*;
import java.rmi.*;
import java.rmi.server.*;

// This class represents the server that simulates a temperature sensor.
// It implements TemperatureSensor to provide remote methods for clients.
public class TemperatureSensorServer extends UnicastRemoteObject implements TemperatureSensor, Runnable 
{
	private volatile double temp; // Current temperature.
	private ArrayList<TemperatureListener> list = new ArrayList<TemperatureListener>(); // List of listeners.

    // Constructor for the TemperatureSensorServer class.
	public TemperatureSensorServer() throws java.rmi.RemoteException {
		temp = 98.0; // Initialize temperature.
	}

    // Method to get the current temperature.
	public double getTemperature() throws java.rmi.RemoteException {
		return temp;
	}

    // Method to add a TemperatureListener to the listener list.
	public void addTemperatureListener(TemperatureListener listener) throws java.rmi.RemoteException {
		System.out.println("adding listener -" + listener);
		list.add(listener);
	}

    // Method to remove a TemperatureListener from the listener list.
	public void removeTemperatureListener(TemperatureListener listener) throws java.rmi.RemoteException {
		System.out.println("removing listener -" + listener);
		list.remove(listener);
	}

    // Run method to simulate temperature changes and notify listeners.
	public void run() {
		Random r = new Random();
		for (;;) {
			try {
				// Sleep for a random amount of time
				int duration = r.nextInt() % 10000 + 200;
				// Check to see if negative, if so, reverse
				if (duration < 0) {
					duration = duration * -1;
					Thread.sleep(duration);
				}
			} catch (InterruptedException ie) {
				System.err.println("Interrupted: " + ie.getMessage());
			}

			// Get a number, to see if temp goes up or down
			int num = r.nextInt();
			if (num < 0) {
				temp += 0.5;
			} else {
				temp -= 0.5;
			}

			// Notify registered listeners
			notifyListeners();
		}
	}

    // Method to notify all registered listeners of a temperature change.
	private void notifyListeners() {
		for (TemperatureListener listener : list) {
            try {
                listener.temperatureChanged(temp);
            } catch (RemoteException re) {
                System.err.println("Error notifying listener: " + re.getMessage());
            }
        }
	}

	    // Main method to start the server.
	public static void main(String[] args) {
        // Set the security policy to allow all permissions.
	   System.setProperty("java.security.policy", "file:allowall.policy");
 
		System.out.println("Loading temperature service");

		try {
			// Create a TemperatureSensorServer object.
			TemperatureSensorServer sensor = new TemperatureSensorServer();
			String registry = "localhost";

            // Register the server object with the RMI registry.
			String registration = "rmi://" + registry + "/TemperatureSensor";
			Naming.rebind(registration, sensor);

            // Start the server's run method in a separate thread.
			Thread thread = new Thread(sensor);
			thread.start();
        } catch (RemoteException re) {
            System.err.println("Remote Error: " + re.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
	}
}