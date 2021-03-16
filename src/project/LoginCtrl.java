package project;

import java.sql.*;
import java.util.Scanner;



public class LoginCtrl extends DBConn {
	String LoggedIn;
	
	
	public void logUserIn () {
		Scanner myObj = new Scanner(System.in);
	    String email;
	    
	    // Enter username and press Enter
	    System.out.println("Enter email:"); 
	    email = myObj.nextLine();
	    String password;
	    
	    // Enter username and press Enter
	    System.out.println("Enter password:"); 
	    password = myObj.nextLine();
	    myObj.close();
		 try {
			 Statement stmt = conn.createStatement();
			 String query = "select email, password from User where email='"+email+"'";
			 ResultSet rs = stmt.executeQuery(query);
			 String dbPassword = rs.getString("password");
			 if (password.equals(dbPassword)) {
				 System.out.println("Bruker med email = " + email + " er nå logget inn");
				 }
			 else {
				 System.out.println("Passordet er feil");
				 logUserIn();
				 }
			 } 
		 catch (Exception e) {
			 System.out.println("db error during select of loper = "+ e);
			 }
		 }
	
	public static void main(String[] args) {
		Scanner myObj = new Scanner(System.in);
	    String email;
	    
	    // Enter username and press Enter
	    System.out.println("Enter email:"); 
	    email = myObj.nextLine();
	    String password;
	    
	    // Enter username and press Enter
	    System.out.println("Enter password:"); 
	    password = myObj.nextLine();
	    System.out.println(password);
	}

}
