package project;

import java.sql.*;
import java.util.Properties;

public class DBConn {
	 protected Connection conn;
	 public DBConn () {
	 }
	 public void connect() {
		 try {
			 Class.forName("com.mysql.cj.jdbc.Driver");
			 Properties p = new Properties ();
			 p.put("user","root");
			 p.put("password","MyPassword");
			 conn = DriverManager.getConnection(
			 "jdbc:mysql://127.0.0.1/project?"
			 + "allowPublicKeyRetrieval=true&autoReconnect=true&useSSL=false",p);
			 } catch (Exception e) {
			 throw new RuntimeException("Unable to connect", e);
			 }
	 }
	 public static void main(String[] args) {
		DBConn conn = new DBConn();
		conn.connect();
	}
}
