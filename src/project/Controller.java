package project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class Controller extends DBConn {

	PreparedStatement postStatement;
	PreparedStatement threadStatement;
	PreparedStatement threadInFolderStatement;
	Scanner myObj = new Scanner(System.in);

	public void logUserIn() {

		// Scanner myObj = new Scanner(System.in);
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

	public void startPost() {
		try {
			postStatement = conn.prepareStatement(
					"insert into Post" + "(text, datecreated, anonymity, summary, tag, email, tid, paid, type) values "
							+ " ( (?), NOW(), (?), (?), (?), (?), (?), (?), (?) )");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void addHeadPost() {

		// Scanner myObj = new Scanner(System.in);
		String postText;
		String summary;
		String tag;
		String folderName;
		Boolean userAnonymity;

		System.out.println("Enter Summary:");
		summary = myObj.nextLine();

		System.out.println("Enter text:");
		postText = myObj.nextLine();

		System.out.println("Enter tag:");
		tag = myObj.nextLine();

		System.out.println("Enter folder:");
		folderName = myObj.nextLine();

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
			} else {
				userAnonymity = false;
			}
			// Creates a thread
			String sql = "Insert into Thread(coursecode, colorcode) values (?, ?)";
			threadStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			threadStatement.setString(1, coursecode);
			threadStatement.setString(2, "red"); // red represents a thread without answer
			threadStatement.execute();

			int tid = -1;

			rs = threadStatement.getGeneratedKeys();

			if (rs.next()) {
				tid = rs.getInt(1);
			} else {
				System.out.println("Funka ikke");
			}

			if (rs != null) {
				rs.close();
			}

			if (threadStatement != null) {
				threadStatement.close();
			}

			// Creates a relation between the given thread and folder
			threadInFolderStatement = conn.prepareStatement("insert into ThreadInFolder values" + "( (?), (?) )");
			threadInFolderStatement.setInt(1, FID);
			threadInFolderStatement.setInt(2, tid);
			threadInFolderStatement.execute();

			// Creates a post within a new thread
			postStatement.setString(1, postText);
			postStatement.setBoolean(2, userAnonymity);
			postStatement.setString(3, summary);
			postStatement.setString(4, tag);
			postStatement.setString(5, loggedInUser);
			postStatement.setInt(6, tid);
			postStatement.setNull(7, Types.INTEGER);
			postStatement.setString(8, "Headpost");
			postStatement.execute();
		}

		catch (Exception e) {
			System.out.println(e);
		}

		System.out.println("Post lagt til");
	}

	public void addAnswer(String folderName, int PaID, String type) {

		String postText;
		Boolean userAnonymity;
		// Scanner myObj = new Scanner(System.in);

		System.out.println("Enter text:");
		postText = myObj.nextLine();

		try {
			Statement stmt = conn.createStatement();
			String query = "select TID from Post where PID=" + '"' + PaID + '"';
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
			} else {
				userAnonymity = false;
			}

			// Creates an answer within an existing thread
			postStatement.setString(1, postText);
			postStatement.setBoolean(2, userAnonymity);
			postStatement.setString(3, null);
			postStatement.setString(4, null);
			postStatement.setString(5, loggedInUser);
			postStatement.setInt(6, TID);
			postStatement.setInt(7, PaID);
			postStatement.setString(8, type);
			postStatement.execute();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public Collection<Integer> searchKeyWord(String keyWord) {
		Collection<Integer> threads = new ArrayList<Integer>();
		try {
			Statement stmt = conn.createStatement();
			String query = "select distinct TID from Post where (text LIKE " + '"' + keyWord + "%" + '"'
					+ " or summary LIKE " + '"' + keyWord + "%" + '"' + ") and tid in ("
					+ "Select tid from thread where coursecode =" + '"' + coursecode + '"' + ")";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				threads.add(rs.getInt("TID"));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return threads;
	}

	public Collection<String> getStats() {
		Collection<String> stats = new ArrayList<>();
		try {
			Statement stmt = conn.createStatement();
			String query = "Select user.fullname, count(distinct hasread.tid) as ThreadsRead,"
					+ " count(distinct post.pid) as Postscreated"
					+ " from (user left join hasread on user.email = hasread.email)"
					+ " left join post on user.email = post.email group by user.email " + "order by threadsread desc; ";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				String name = rs.getNString(1);
				int postsRead = rs.getInt(2);
				int postsCreated = rs.getInt(3);
				String str = "Navn: " + name + ", Tråder lest: " + postsRead + ", Poster opprettet: " + postsCreated;
				stats.add(str);
			}
		} catch (Exception e) {
		}
		return stats;
	}

	public void runPiazza() {
		this.connect();
		logUserIn();
		startPost();
		// Scanner myObj = new Scanner(System.in);
		while (true) {
			System.out.println("Which usecase do you want to execute?(2-5)");
			int usecase = Integer.parseInt(myObj.nextLine());
			if (usecase == 2) {
				addHeadPost();
			} else if (usecase == 3) {
				addAnswer("Exam", 2, "Answer");
			} else if (usecase == 4) {
				System.out.println(searchKeyWord("WAL"));
			} else if (usecase == 5) {
				getStats().stream().forEach(e -> System.out.println(e));
			} else {
				System.out.println("This is not a valid usecase");
			}
			System.out.println("Do you want to continue with the same user? (y/n)");
			if (myObj.nextLine().equals("n")) {
				logUserIn();
			}
			System.out.println("Press q to quit");
			if (myObj.nextLine().equals("q")) {
				break;
			}
		}
	}

	public static void main(String[] args) {
		Controller db = new Controller();
		db.runPiazza();
	}
}
