import java.rmi.Remote;

public interface CloudInterface extends Remote
{
	boolean register(String account, String password) throws java.rmi.RemoteException;
	boolean login(String account, String password) throws java.rmi.RemoteException;
	boolean writeDataToServer(String fileName,byte[] bytes,int len,String account) throws java.rmi.RemoteException;
	String[][] showMyFile(String account) throws java.rmi.RemoteException;
	//showAccessFile
}