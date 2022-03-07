import java.io.*;
//import java.rmi.*;
import java.rmi.RemoteException;
import java.rmi.server.*;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import com.google.gson.stream.JsonReader;

public class RMIImpl extends UnicastRemoteObject implements CloudInterface
{
	//define here
	private static final String USERJSONFILE = "user.json";
	private static final String FILEDETAILJSONFILE = "fileDetail.json";
	private static final Boolean DEFAULTACTIVE = true;
	//user list
	ArrayList<User> users = new ArrayList<>();
	ArrayList<FileDetail> fileDetails = new ArrayList<>();

	public RMIImpl() throws java.rmi.RemoteException
	{
		super();
	}
	//read user file to user list
	private void updateList(String listData, String jsonFile){
		try {
			File file = new File(jsonFile);
			Scanner reader = new Scanner(file);
			String data = reader.nextLine();
			if(listData.equals("users")){
				this.users = new Gson().fromJson(data,new TypeToken<ArrayList<User>>(){}.getType());
			}else if(listData.equals("fileDetails")) {
				this.fileDetails = new Gson().fromJson(data,new TypeToken<ArrayList<FileDetail>>(){}.getType());
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An Error Occurred.");
			e.printStackTrace();
		}
	}
	//write user list to user file
	private void updateJson(String listData, String jsonFile) {
		try{
			File file = new File(jsonFile);
			if (file.createNewFile()) {
				System.out.println("File Created.");
			} else {
				System.out.println("File Already Exists.");
			}
			FileWriter writer = new FileWriter(jsonFile);
			if(listData.equals("users")) {
				writer.write(new Gson().toJson(users));
			}else if(listData.equals("fileDetails")) {
				writer.write(new Gson().toJson(fileDetails));
			}
			writer.close();
			System.out.println("Updated File.");
		} catch (IOException e) {
			System.out.println("An Error Occurred.");
			e.printStackTrace();
		}
	}

	// register
	synchronized public boolean register(String account, String password) {
		for (User nowUser : users) {//check same account
			if(nowUser.getAccount().equals(account)){
				return false;
			}
		}
		users.add(new User(users.size(),account,password,DEFAULTACTIVE));
		updateJson("users",USERJSONFILE);
		updateList("users",USERJSONFILE);
		return true;
	}

	synchronized public boolean login(String account, String password) {
		updateList("users",USERJSONFILE);
		for (User nowUser : users) {
			if(nowUser.checkLogin(account,password)){
				return true;
			}
		}
		return false;
	}


	public boolean writeDataToServer(String fileName, byte[] bytes,int len,String account) {
		try {
			File dir = new File("./cloud/"+account);
			if (!dir.exists()){
				dir.mkdirs();
			}
			File file = new File("./cloud/"+account+"/"+fileName);
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file,true);
			out.write(bytes,0,len);
			out.flush();
			out.close();
			System.out.println("Done writing data...");
			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
			fileDetails.add(new FileDetail(account,new String[]{account},fileName,formatter.format(new Date())));
			updateJson("fileDetails",FILEDETAILJSONFILE);
			updateList("fileDetails",FILEDETAILJSONFILE);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	//file name
	//creater
	//date
	public String[][] showMyFile(String account) {

		updateList("fileDetails",FILEDETAILJSONFILE);
		List<List<String>> fileList = new ArrayList<>();
//		for(int i=0; i < 4; i++) {
//			fileList.add(new ArrayList());
//		}
		int counter=0;
		for (FileDetail file : fileDetails) {
			for(String accesser:file.getAccesser()) {
				if (accesser.equals(account)) {
					fileList.add(Arrays.asList(
							String.valueOf(counter++),
							file.getFileName(),
							file.getCreater(),
							file.getDate()
					));
					break;
				}
			}
		}
		String[][] array = new String[fileList.size()][];
		for (int i = 0; i < fileList.size(); i++) {
			List<String> row = fileList.get(i);
			array[i] = row.toArray(new String[0]);
		}
		return array;
	}

}
