
//*******************************************************************
//*  Network Programming - Unit 6 Remote Method Invocation          *
//*  Program Name: ArithmeticInterface                              *
//*  The program defines the interface for Java RMI.                *
//*  2014.02.26                                                     *
//*******************************************************************
import java.rmi.Remote;

public interface ArithmeticInterface extends Remote {
	public boolean register(String account, String password) throws java.rmi.RemoteException;

	public boolean login(String account, String password) throws java.rmi.RemoteException;

	public boolean creat(String user, String title, String content) throws java.rmi.RemoteException;

	public String[] subject() throws java.rmi.RemoteException;

	public boolean reply(String user, String subjectIndex, String msg) throws java.rmi.RemoteException;

	public String[][] discussion(String subjectIndex) throws java.rmi.RemoteException;

	public String[] getInfoSubject(String subjectIndex) throws java.rmi.RemoteException;

	public boolean deleteSubject(String user, String subjectIndex) throws java.rmi.RemoteException;

	public boolean deleteReply(String user, String subjectIndex) throws java.rmi.RemoteException;
}
