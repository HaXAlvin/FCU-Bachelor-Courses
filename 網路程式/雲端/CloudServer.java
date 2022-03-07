import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

//javac -cp ".:./libs/gson-2.2.2.jar" *.java
//java -cp ".:./libs/gson-2.2.2.jar" CloudServer
//java -cp ".:./libs/gson-2.2.2.jar" Client
public class CloudServer
{
	// Bind ArithmeticServer and Registry
	public static void main(String args[])
	{
		try
		{
			CloudInterface name = new RMIImpl();
			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.createRegistry(1099);
			System.out.println("Registering ...");
			registry.bind("CloudServer", name);
			System.err.println("Register success");
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}