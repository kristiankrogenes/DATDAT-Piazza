package project;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class StatCtrl extends DBConn {
	
	static int test = 0;
	public Collection<String> getStats() {
		Collection<String> stats = new ArrayList<>();
		try {
			Statement stmt = conn.createStatement();
			String query = "Select user.fullname, count(distinct hasread.tid) as ThreadsRead,"
					+ " count(distinct post.pid) as Postscreated"
					+ " from (user left join hasread on user.email = hasread.email)"
					+ " left join post on user.email = post.email group by user.email "
					+ "order by threadsread desc; ";
			System.out.println(query);
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				System.out.println("hei");
				test++;
				String name = rs.getNString(1);
				int postsRead = rs.getInt(2);
				System.out.println(postsRead);
				int postsCreated = rs.getInt(3);
				String str = "Navn: " + name + ", Tråder lest: " + postsRead + ", Poster opprettet: " + postsCreated;
				stats.add(str);
				test++;
			}
		}
		catch (Exception e) {	
		}
		return stats;
	}
	
	public static void main(String[] args) {
		StatCtrl stats = new StatCtrl();
		stats.connect();
		System.out.println(stats.getStats());
		System.out.println(test);
		
	}

}
