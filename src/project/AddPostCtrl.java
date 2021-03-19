package project;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class AddPostCtrl extends DBConn {
	
	PreparedStatement postStatement; 
	public static int PID = 1;
	public static int TID = 1;
	
	public void startPost(int PID) {
		 try {
			 postStatement = conn.prepareStatement("insert into Post values"
			 		+ " ( (?), (?), (?), (?), (?), (?), (?), (?), (?), (?), (?) )");
			 	}
		 catch (Exception e) {
			 System.out.println(e);
			 }
		 }
	
	public void addHeadPost() {
		Scanner myObj = new Scanner(System.in);
	    String postText;
	    String summary;
	    String folderName;
	    String tag;
	    Boolean userAnonymity;
	    // Enter username and press Enter
	    System.out.println("Enter Summary:");
	    summary = myObj.nextLine();
	    System.out.println("Enter text:"); 
	    postText = myObj.nextLine();
	    System.out.println("Enter folder name:");
	    folderName = myObj.nextLine();
	    System.out.println("Enter tag:"); 
	    tag = myObj.nextLine();
	    try {
	    	Statement stmt = conn.createStatement();
			String query = "select anonymity from Course where Coursecode=" + '"' + coursecode + '"';
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			Boolean anonymity = rs.getBoolean("Anonymity");
			if (anonymity == true) {
				System.out.println("Do you want to be anonymous?(true/false)");
			    userAnonymity = Boolean.parseBoolean(myObj.nextLine());
			    }
			else {
				userAnonymity = false;
			}
			
			postStatement.setInt(1, PID++);
	    	postStatement.setString(2, postText);
	    	postStatement.setString(3, LocalDate.now().toString());
	    	postStatement.setBoolean(4, userAnonymity);
	    	postStatement.setString(5, summary);
	    	postStatement.setString(6, tag);
	    	postStatement.setString(7, loggedInUser);
	    	postStatement.setInt(8, TID++);
	    	postStatement.setString(9, coursecode);
	    	postStatement.setNull(10, 0);
	    	postStatement.setString(11, "Headpost");
	    	postStatement.execute();
	    	System.out.println("hei");
	    }
	    catch (Exception e){
	    	System.out.println(e);
	    }
	}
	public static void main(String[] args) {
		//LoginCtrl db = new LoginCtrl();
	    //db.connect();
	    //db.logUserIn();
		AddPostCtrl addPost = new AddPostCtrl();
		addPost.connect();
		addPost.startPost(PID);
		addPost.addPost();
		
		
	}
}
