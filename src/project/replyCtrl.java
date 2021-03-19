package project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
			postStatement.setInt(1, AddPostCtrl.PID);
	    	postStatement.setString(2, postText);
	    	postStatement.setBoolean(3, userAnonymity);
	    	postStatement.setString(4, null);
	    	postStatement.setString(5, null);
	    	postStatement.setString(6, loggedInUser);
	    	postStatement.setInt(7, TID);
	    	postStatement.setInt(8, PID);
	    	postStatement.setString(9, type);
	    	postStatement.execute();
	    	System.out.println("hei");
	    	TID++;
	    	AddPostCtrl.PID++;
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

