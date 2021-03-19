package project;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class AddPostCtrl extends DBConn {
	
	PreparedStatement postStatement; 
	static int PID = 43;
	static int TID = 43;
	public static int FID = 1;
	
	public void startPost() {
		 try {
			 postStatement = conn.prepareStatement("insert into Post values"
			 		+ " ( (?), (?), NOW(), (?), (?), (?), (?), (?), (?), (?) )");
			 	}
		 catch (Exception e) {
			 System.out.println(e);
			 }
		 }
	
	public void addHeadPost(String tag, String folderName) {
		String postText;
	    String summary;
	    Boolean userAnonymity;
		Scanner myObj = new Scanner(System.in);
	    // Enter username and press Enter
	    System.out.println("Enter Summary:");
	    summary = myObj.nextLine();
	    System.out.println("Enter text:"); 
	    postText = myObj.nextLine();
	    try {
	    	Statement stmt = conn.createStatement();
			String query = "select anonymity from Course where Coursecode=" + '"' + coursecode + '"';
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			Boolean anonymity = rs.getBoolean("Anonymity");
			query = "select FID from Folder where Foldername=" + '"' + folderName + '"';
			rs = stmt.executeQuery(query);
			rs.next();
			int FID = rs.getInt("FID");
			if (anonymity == true) {
				System.out.println("Do you want to be anonymous?(true/false)");
			    userAnonymity = Boolean.parseBoolean(myObj.nextLine());
			    }
			else {
				userAnonymity = false;
			}
			PreparedStatement threadStatement = conn.prepareStatement("insert into Thread values"
			 		+ " ( (?), (?), (?) )");
			threadStatement.setInt(1, TID);
			threadStatement.setString(2, coursecode);
			threadStatement.setString(3, "red"); //red represents a thread without answer
			threadStatement.execute();
			PreparedStatement threadInFolderStatement = conn.prepareStatement("insert into ThreadInFolder values"
			 		+ " ( (?), (?) )");
			threadInFolderStatement.setInt(1, FID);
			threadInFolderStatement.setInt(2, TID);
			threadInFolderStatement.execute();
			postStatement.setInt(1, PID);
	    	postStatement.setString(2, postText);
	    	postStatement.setBoolean(3, userAnonymity);
	    	postStatement.setString(4, summary);
	    	postStatement.setString(5, tag);
	    	postStatement.setString(6, loggedInUser);
	    	postStatement.setInt(7, TID);
	    	postStatement.setNull(8, Types.INTEGER);
	    	postStatement.setString(9, "Headpost");
	    	postStatement.execute();
	    	System.out.println("hei");
	    	TID++;
	    	PID++;
	    	myObj.close();
	    }
	    catch (Exception e){
	    	System.out.println(e);
	    }
	}
	public static void main(String[] args) {
		LoginCtrl db = new LoginCtrl();
	    db.connect();
	    db.logUserIn();
		AddPostCtrl addPost = new AddPostCtrl();
		addPost.connect();
		addPost.startPost();
		addPost.addHeadPost("Question", "Exam");
		AddPostCtrl addPost2 = new AddPostCtrl();

	}
}
