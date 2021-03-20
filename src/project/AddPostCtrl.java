package project;

import java.sql.*;
import java.util.Scanner;

public class AddPostCtrl extends DBConn {
	
	PreparedStatement postStatement; 
	PreparedStatement threadStatement; 
	PreparedStatement threadInFolderStatement; 

	
	public void startPost() {
		 try {
			 postStatement = conn.prepareStatement("insert into Post(text, datecreated, anonymity, summary, tag, email, tid, paid, type) values "
			 		+ " ( (?), NOW(), (?), (?), (?), (?), (?), (?), (?) )");
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

			// Creates a thread 
			String sql = "Insert into Thread(coursecode, colorcode) values (?, ?)";
			threadStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			threadStatement.setString(1, coursecode);
			threadStatement.setString(2, "red"); //red represents a thread without answer
			threadStatement.execute();
			
			int tid = -1;
			rs = threadStatement.getGeneratedKeys();
			if(rs.next()) {
				tid = rs.getInt(1);
			} else {
				System.out.println("Funka ikke");
			}
			
			if(rs != null) {
					rs.close();
			}
			if(threadStatement != null) {
					threadStatement.close();
			}
			// Creates a relation between the given thread and folder
			threadInFolderStatement = conn.prepareStatement("insert into ThreadInFolder values"
			 		+ "( (?), (?) )");
			threadInFolderStatement.setInt(1, FID);
			threadInFolderStatement.setInt(2, tid);
			threadInFolderStatement.execute();
			
			//Creates a post within a new thread 
	    	postStatement.setString(1, postText);
	    	postStatement.setBoolean(2, userAnonymity);
	    	postStatement.setString(3, summary);
	    	postStatement.setString(4, tag);
	    	postStatement.setString(5, loggedInUser);
	    	postStatement.setInt(6, tid);
	    	postStatement.setNull(7, Types.INTEGER);
	    	postStatement.setString(8, "Headpost");
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
		AddPostCtrl addPost = new AddPostCtrl();
		addPost.connect();
		addPost.startPost();
		addPost.addHeadPost("Question", "Exam");
	}
}
