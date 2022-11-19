
//*******************************************************************
//*  Network Programming - Unit 6 Remote Method Invocation          *
//*  Program Name: CalculatorRMIClient                              *
//*  The program is a RMI client.                                   *
//*  Usage: java CalculatorRMIClient op num1 num2,                  *
//*         op = add, sub, mul, div                                 *
//*  2014.02.26                                                     *
//*******************************************************************
import java.io.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.Arrays;

/*
javac ArithmeticInterface.java
javac ArithmeticServer.java
javac ArithmeticRMIImpl.java
javac CalculatorRMIClient.java

rmic ArithmeticRMIImpl

rmiregistry
java ArithmeticServer
java CalculatorRMIClient
*/
class CalculatorRMIClient {
	public static void main(String args[]) {
		ArithmeticInterface o = null;
		int op = 0;
		Scanner input = new Scanner(System.in);
		String user = null;
		try {
			Registry registry = LocateRegistry.getRegistry(null);

			// Looking up the registry for the remote object
			o = (ArithmeticInterface) registry.lookup("arithmetic");

			// Calling the remote method using the obtained object
			// stub.printMsg();
			// ArithmeticInterface o = (ArithmeticInterface)
			// UnicastRemoteObject.exportObject(new ArithmeticRMIImpl());
			// o = (ArithmeticInterface) Naming.lookup("rmi://127.0.0.1/arithmetic");
			System.out.println("RMI server connected\n");
		} catch (Exception e) {
			System.out.println("Server lookup exception: " + e.getMessage());
		}
		while (true) {
			do {
				System.out.println("1 : Register");
				System.out.println("2 : " + (user == null ? "Login" : "Logout"));
				System.out.println("3 : Creat Subject");
				System.out.println("4 : List All Subject");
				System.out.println("5 : Reply");
				System.out.println("6 : Show A Subject");
				System.out.println("7 : Delete A Subject Or Reply");
				System.out.println("8 : Exit");
				System.out.print("What do you want to do : ");
				String methods = input.next();
				System.out.println();
				try {
					op = Integer.parseInt(methods);
				} catch (Exception e) {
					System.out.println("Error : Please Enter A Number\n");
					op = 0;
				}
			} while (op < 1 || op > 8);

			try {
				switch (op) {
					case 1: // register
						System.out.print("Account : ");
						String regAccount = input.next();
						System.out.print("Password : ");
						String regPassword = input.next();
						System.out.print("Verify Password : ");
						if (regPassword.compareTo(input.next()) != 0) {
							System.out.println("Verify Failed !\n");
							break;
						}
						Boolean regSuccess = o.register(regAccount, regPassword);
						System.out.println((regSuccess ? "" : "Error : ") + "Register "
								+ (regSuccess ? "Success" : "Failed") + "\n");
						break;
					case 2: // login and logout
						if (user != null) {
							user = null;
							System.out.println("Logout Success\n");
							break;
						}
						System.out.print("Account : ");
						String account = input.next();
						System.out.print("Password : ");
						String password = input.next();
						if (!o.login(account, password)) {
							System.out.println("Login Failed !\n");
							break;
						}
						user = account;
						System.out.println("Login Success !\n");
						break;
					case 3: // creat
						if (user == null) {
							System.out.println("Login First !\n");
							break;
						}
						System.out.print("Title : ");
						input.nextLine(); // skip \n
						String crtTitle = input.nextLine();
						System.out.print("Content : ");
						String crtContent = input.nextLine();
						o.creat(user, crtTitle, crtContent);
						System.out.println("Creat Success !\n");
						break;
					case 4: // list all
						String[] data = o.subject();
						if (data.length == 0) {
							System.out.println("Error : Nothing Here!\n");
							break;
						}
						for (int i = 0; i < data.length; i++) {
							System.out.println(i + ". " + (data[i] == null ? "--Deleted--" : data[i]));
						}
						System.out.println();
						break;
					case 5: // leave msg
						if (user == null) {
							System.out.println("Error : Login First !\n");
							break;
						}
						System.out.print("Subject Index : ");
						String title = input.next();
						System.out.print("Leave Message : ");
						input.nextLine();
						String content = input.nextLine();
						Boolean repSuccess = o.reply(user, title, content);
						System.out.println("Reply " + (repSuccess ? "success!" : "failed") + "\n");
						break;
					case 6:// one subject
						System.out.print("Subject Index :");
						String index = input.next();
						String[] subject = o.getInfoSubject(index);
						if (subject == null) {
							System.out.println("Error : Index Out of Range!\n");
							break;
						} else if (subject[4].equals("1")) {
							System.out.println("Error : This Subject Has Been Deleted!\n");
							break;
						}
						String[] format = { "Creater", "Title", "Content", "Date" };
						for (int i = 0; i < 4; i++) {
							System.out.println(format[i] + " : " + subject[i]);
						}
						String[][] discussion = o.discussion(index);
						if (discussion[0].length == 0) {
							System.out.println("This Subject have No Discussion\n");
							break;
						}
						System.out.println("\\\\\\\\Below is Discussion\\\\\\\\");
						for (int i = 0; i < discussion[0].length; i++) {
							System.out.println(discussion[2][i] + " " + discussion[0][i] + " : " + discussion[1][i]);
						}
						System.out.println();
						break;
					case 7:// del
						if (user == null) {
							System.out.println("Error : Login First !\n");
							break;
						}
						System.out.println("1.Delete The Subject 2.Delete The Last Discuss In Subject :");
						String delChos;
						do {
							System.out.print("Your Choose :");
							delChos = input.next();
						} while (!(delChos.equals("1") || delChos.equals("2")));
						String delIndex;
						while (true) {
							try {
								System.out.print("Enter Subject Index :");
								delIndex = input.next();
								Integer.parseInt(delIndex);
								break;
							} catch (Exception e) {
								System.out.println("Error : Please Enter A Number");
							}
						}

						Boolean success = delChos.equals("1") ? o.deleteSubject(user, delIndex)
								: o.deleteReply(user, delIndex);
						System.out.println(
								(success ? "" : "Error : ") + "Delete " + (success ? "success !" : "failed !") + "\n");
						break;
					case 8:
						input.close();
						System.exit(0);
						break;
					default:
						break;
				}
			} catch (Exception e) {
				System.out.println("ArithmeticServer exception: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}