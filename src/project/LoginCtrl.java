package project;

import java.sql.*;
import java.util.Scanner;



public class LoginCtrl extends DBConn {
	public void logUserIn() {
		Scanner myObj = new Scanner(System.in);
	    String email;
	    // Enter username and press Enter
	    System.out.println("Enter email:"); 
	    email = myObj.nextLine();
	    String password;
	    System.out.println(email);
	    // Enter password and press Enter
	    System.out.println("Enter password:"); 
	    password = myObj.nextLine();
	    System.out.println(password);
		 try {
			 Statement stmt = conn.createStatement();
			 String query = "select email, password from User where email=" + '"' + email + '"';
			 ResultSet rs = stmt.executeQuery(query);
			 if (rs.next()) {
				 String dbEmail = rs.getString("email");
				 String dbPassword = rs.getString("password");
				 if (password.equals(dbPassword)) {
					 System.out.println("Bruker med email = " + email + " er n� logget inn");
					 rs.close();
					 loggedInUser = email;
					 }
				 else {
					 System.out.println("Passordet er feil");
					 rs.close();
					 logUserIn();
					 }
				 //myObj.close();
				 } 
			 else {
				 System.out.println("Epost er feil");
				 rs.close();
				 logUserIn();
			 }
			 //myObj.close();
			 }
		 
		 catch (Exception e) {
			 System.out.println(e);
			 }
		 }
	public static void main(String[] args) {
	    
	    LoginCtrl db = new LoginCtrl();
	    db.connect();
	    db.logUserIn();
	}
}
