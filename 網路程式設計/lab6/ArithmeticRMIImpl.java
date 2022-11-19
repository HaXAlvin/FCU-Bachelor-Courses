
import java.rmi.RemoteException;
import java.rmi.server.*;
import java.util.*;
import java.util.Date;

public class ArithmeticRMIImpl extends UnicastRemoteObject implements ArithmeticInterface {
	int accountCount = 0;
	String[][] accountData = new String[2][100];
	private final Object accountLock = new Object();
	// synchronized
	int subjectCount = 0;
	String[][] subjectData = new String[5][100];
	private final Object subjectLock = new Object();
	// creater
	// title
	// content
	// 0->exist 1->del
	// Data
	int disscussCount = 0;
	String[][] disscuss = new String[4][100];
	private final Object disscussLock = new Object();
	// user
	// index of subjectData
	// disscuss content
	// Data

	public ArithmeticRMIImpl() throws java.rmi.RemoteException {
		super();
	}

	// return if register success
	public boolean register(String account, String password) throws java.rmi.RemoteException {
		synchronized (accountLock) {
			if (Arrays.asList(accountData[0]).contains(account) == true) {
				return false;
			}
			System.out.println("New Register");
			System.out.println("Account : " + account);
			System.out.println("Password : " + password + "\n");
			accountData[0][accountCount] = account;
			accountData[1][accountCount] = password;
			System.out.println(accountData[0][accountCount]);
			accountCount += 1;

			return true;
		}
	}

	// return if login success
	public boolean login(String account, String password) throws java.rmi.RemoteException {
		synchronized (accountLock) {
			for (int i = 0; i < accountCount; i++) {
				if (accountData[0][i].equals(account) && accountData[1][i].equals(password)) {
					System.out.println("Login Success : " + account + "\n");
					return true;
				}
			}
			return false;
		}
	}

	// show all subject
	public String[] subject() throws java.rmi.RemoteException {
		synchronized (subjectLock) {
			String[] returnSubjects = new String[subjectCount];
			for (int i = 0; i < subjectCount; i++) {
				returnSubjects[i] = subjectData[3][i].equals("0") ? subjectData[1][i] : null;
			}
			return returnSubjects;
		}
	}

	// creat a subject
	public boolean creat(String user, String title, String content) throws RemoteException {
		synchronized (subjectLock) {
			String[] signData = { user, title, content, "0", new Date().toString() };
			System.out.println("New Subject Created :");
			for (int i = 0; i < 5; i++) {
				System.out.println(subjectData[i][subjectCount] = signData[i]);
			}
			System.out.println();
			subjectCount += 1;
			return true;
		}
	}

	// reply a subject
	public boolean reply(String user, String subjectIndex, String msg) throws RemoteException {
		int index = Integer.parseInt(subjectIndex);
		synchronized (subjectLock) {
			if (index < 0 || index > subjectCount - 1 || subjectData[3][index].equals("1")) {
				return false;
			}
			// if (subjectData[3][index].equals("1")) {
			// return false;
			// }
		}
		synchronized (disscussLock) {
			String[] signData = { user, subjectIndex, msg, new Date().toString() };
			System.out.println("New Reply Created :");
			for (int i = 0; i < 4; i++) {
				System.out.println(disscuss[i][disscussCount] = signData[i]);
			}
			System.out.println();
			disscussCount += 1;
			return true;
		}
	}

	// show reply in a subject
	public String[][] discussion(String subjectIndex) throws java.rmi.RemoteException {
		synchronized (subjectLock) {
			if (Integer.parseInt(subjectIndex) > subjectCount - 1) {
				return null;
			}
		}
		String[][] singleDiss = new String[3][100];
		int singleDissCounter = 0;
		synchronized (disscussLock) {
			for (int i = 0; i < disscussCount; i++) {
				if (disscuss[1][i].equals(subjectIndex)) {
					singleDiss[0][singleDissCounter] = disscuss[0][i];// user
					singleDiss[1][singleDissCounter] = disscuss[2][i];// diss content
					singleDiss[2][singleDissCounter] = disscuss[3][i];// date
					singleDissCounter++;
				}
			}
		}
		String[][] returnDiss = new String[3][singleDissCounter];
		for (int i = 0; i < 3; i++) {
			System.arraycopy(singleDiss[i], 0, returnDiss[i], 0, singleDissCounter);
		}
		return returnDiss;
	}

	// get a subject
	public String[] getInfoSubject(String subjectIndex) throws java.rmi.RemoteException {
		int index = Integer.parseInt(subjectIndex);
		synchronized (subjectLock) {
			if (index > subjectCount - 1) {
				return null;
			}
			return new String[] { subjectData[0][index], subjectData[1][index], subjectData[2][index],
					subjectData[4][index], subjectData[3][index] };
		}
	}

	// del a subject
	public boolean deleteSubject(String user, String subjectIndex) throws java.rmi.RemoteException {
		int index = Integer.parseInt(subjectIndex);
		synchronized (subjectLock) {
			if (index < 0 || index > subjectCount - 1 || !subjectData[0][index].equals(user)) {
				return false;
			}
			synchronized (disscussLock) {
				for (int i = 0; i < disscussCount; i++) {
					if (disscuss[1][i].equals(subjectIndex)) {
						return false;
					}
				}
			}
			subjectData[3][index] = "1";
			return true;
		}
	}

	// del a reply in subject if it's the last reply
	public boolean deleteReply(String user, String subjectIndex) throws java.rmi.RemoteException {
		synchronized (subjectLock) {
			if (Integer.parseInt(subjectIndex) > subjectCount - 1) {
				return false;
			}
		}
		String[][] singleDiss;
		try {
			singleDiss = discussion(subjectIndex);
		} catch (RemoteException e) {
			return false;
		}
		int len = singleDiss[0].length;
		if (!singleDiss[0][len - 1].equals(user)) {
			return false;
		}
		int delIndex = 0;
		synchronized (disscussLock) {
			for (; delIndex < disscussCount; delIndex++) {
				// if (disscuss[0][delIndex] == null)
				// continue;
				if (disscuss[0][delIndex].equals(user) && disscuss[1][delIndex].equals(subjectIndex)
						&& disscuss[2][delIndex].equals(singleDiss[1][len - 1])) {
					for (int i = 0; i < 4; i++) {
						System.arraycopy(disscuss[i], delIndex + 1, disscuss[i], delIndex,
								disscussCount - (delIndex + 1));
					}
					disscussCount--;
					break;
				}
			}
			return true;
		}
	}
}