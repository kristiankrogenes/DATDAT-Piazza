package project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.Scanner;

public class replyCtrl extends DBConn {
	PreparedStatement postStatement; 
	public void startPost() {
		 try {
			 postStatement = conn.prepareStatement("insert into Post values"
			 		+ " ( (?), (?), NOW(), (?), (?), (?), (?), (?), (?), (?) )");
			 	}
		 catch (Exception e) {
			 System.out.println(e);
			 }
		 }
	
	public void addAnswer(String folderName, int PID, String type) {
		String postText;
	    String summary;
	    Boolean userAnonymity;
		Scanner myObj = new Scanner(System.in);
	    // Enter username and press Enter
	    System.out.println("Enter text:"); 
	    postText = myObj.nextLine();
	    try {
	    	Statement stmt = conn.createStatement();
	    	String query = "select TID from Post where PID=" + '"' + PID + '"';
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
	    	int TID = rs.getInt("TID");
			query = "select anonymity from Course where Coursecode=" + '"' + coursecode + '"';
			rs = stmt.executeQuery(query);
			rs.next();
			Boolean anonymity = rs.getBoolean("Anonymity");
			if (anonymity == true) {
				System.out.println("Do you want to be anonymous?(true/false)");
			    userAnonymity = Boolean.parseBoolean(myObj.nextLine());
			    }
			else {
				userAnonymity = false;
			}
			
			
			//Creates a post within a new thread 
	    	postStatement.setString(1, postText);
	    	postStatement.setBoolean(2, userAnonymity);
	    	postStatement.setString(3, null);
	    	postStatement.setString(4, null);
	    	postStatement.setString(5, loggedInUser);
	    	postStatement.setInt(6, tid);
	    	postStatement.setInt(7, paid);
	    	postStatement.setString(8, type);
	    	postStatement.execute();
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
		replyCtrl addPost = new replyCtrl();
		addPost.connect();
		addPost.startPost();
		addPost.addAnswer("Exam", 2, "Answer");
	}
}

