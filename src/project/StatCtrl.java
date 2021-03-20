package project;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class StatCtrl extends DBConn {
	
	public Collection<String> getStats() {
		Collection<String> stats = new ArrayList<>();
		try {
			Statement stmt = conn.createStatement();
			String query = "Select user.fullname, count(distinct hasread.tid) as ThreadsRead,"
					+ " count(distinct post.pid) as Postscreated"
					+ " from (user left join hasread on user.email = hasread.email)"
					+ " left join post on user.email = post.email group by user.email "
					+ "order by threadsread desc; ";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				String name = rs.getNString(1);
				int postsRead = rs.getInt(2);
				int postsCreated = rs.getInt(3);
				String str = "Navn: " + name + ", Tr�der lest: " + postsRead + ", Poster opprettet: " + postsCreated;
				stats.add(str);
			}
		}
		catch (Exception e) {	
		}
		return stats;
	}
	
	public static void main(String[] args) {
		StatCtrl stats = new StatCtrl();
		stats.connect();
		stats.getStats().stream().forEach(e -> System.out.println(e));
	}

}
