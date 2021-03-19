package project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class PostSearchCtrl extends DBConn {
	
	PreparedStatement searchStatement; 
	
	int test = 0;
	public Collection<Integer> searchKeyWord(String keyWord) {
		Collection<Integer> threads = new ArrayList<Integer>();
		try {
			Statement stmt = conn.createStatement();
			String query = "select distinct TID from Post where (text LIKE " + '"' + keyWord + "%" + '"' + " or summary LIKE " + '"' + keyWord +  "%" + '"' + 
					") and tid in ("
					+ "Select tid from thread where coursecode =" + '"' + coursecode + '"' + ")";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				 threads.add(rs.getInt("TID"));
				 test++;
				 
			}
		}
		catch (Exception e) {	
		}
		return threads;
	}
	
	public static void main(String[] args) {
		//LoginCtrl db = new LoginCtrl();
	    //db.connect();
	    //db.logUserIn();
		PostSearchCtrl addPost = new PostSearchCtrl();
		addPost.connect();
		System.out.println(addPost.searchKeyWord("WAL"));
		System.out.println(addPost.test);
	}
	
}
