package project;

import java.sql.*;
import java.util.Scanner;

public class LoginCtrl extends DBConn {
	
	public void logUserIn() {
		
		Scanner myObj = new Scanner(System.in);
	    String email;
	    String password;
	    
	    System.out.println("Enter email:"); 
	    
	    email = myObj.nextLine();
	    
	    System.out.println("Enter password:"); 
	    
	    password = myObj.nextLine();
	    
		try {
			Statement stmt = conn.createStatement();
			String query = "select email, password from User where email=" + '"' + email + '"';
			ResultSet rs = stmt.executeQuery(query);
			
			if (rs.next()) {
				String dbEmail = rs.getString("email");
				String dbPassword = rs.getString("password");
				
				if (password.equals(dbPassword)) {
					System.out.println("Bruker med email: " + dbEmail + " er nå logget inn");
					rs.close();
					loggedInUser = dbEmail;
				} else {
					System.out.println("Passordet er feil");
					rs.close();
					logUserIn();
				}
				
			} else {
				System.out.println("Epost er feil");
				rs.close();
				logUserIn();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[] args) {
	    LoginCtrl db = new LoginCtrl();
	    db.connect();
	    db.logUserIn();
	}
}
