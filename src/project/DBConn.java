package project;

import java.sql.*;
import java.util.Properties;

public abstract class DBConn {
	 protected Connection conn;
	 public static String loggedInUser;
	 public static int TID = 1;
	 public final static String coursecode = "TDT4145";
	 public DBConn () {
	 }
	 public void connect() {
		 try {
			 Class.forName("com.mysql.cj.jdbc.Driver");
			 Properties p = new Properties ();
			 p.put("user","root");
			 p.put("password","Kaffekoker123");
			 conn = DriverManager.getConnection(
			 "jdbc:mysql://127.0.0.1/prosjekt?"
			 + "allowPublicKeyRetrieval=true&autoReconnect=true&useSSL=false",p);
			 } catch (Exception e) {
			 throw new RuntimeException("Unable to connect", e);
			 }
	 }
	 public static void main(String[] args) {
	}
}
