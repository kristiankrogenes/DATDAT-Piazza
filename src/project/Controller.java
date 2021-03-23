package project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

	// Takes input from user and checks if email and password matches the database,
	// and then logs the user in
	public void logUserIn() throws SQLException {
		System.out.println("Log in using your email and password");
		System.out.println("Enter email:");
		String email = myObj.nextLine();
		System.out.println("Enter password:");
		String password = myObj.nextLine();

		Statement stmt = conn.createStatement();
		String query = "select email, password from User where email=" + '"' + email + '"';
		ResultSet rs = stmt.executeQuery(query);
		// Checks if email and password matches the database
		if (rs.next()) {
			String dbEmail = rs.getString("email");
			String dbPassword = rs.getString("password");
			if (password.equals(dbPassword)) {
				loggedInUser = dbEmail;
				rs.close();
				System.out.println(userType(loggedInUser) + " with email: " + dbEmail + " is now logged in");
			} else {
				System.out.println("Password is wrong");
				rs.close();
				logUserIn();
			}

		} else {
			System.out.println("Email is wrong");
			rs.close();
			logUserIn();
		}
	}

	// Creates a preparedStatement for inserting a post to the database
	public void startPost() throws SQLException {
		postStatement = conn.prepareStatement(
				"insert into Post" + "(text, datecreated, anonymity, summary, tag, email, tid, paid, type) values "
						+ " ( (?), NOW(), (?), (?), (?), (?), (?), (?), (?) )");
	}

	// Takes the necessary input from user and inserts headpost and a new thread to
	// the database
	public void addHeadPost() throws SQLException {
		String postText;
		String summary;
		String tag;
		String folderName;
		String userAnonymity;

		System.out.println("Enter Summary:");
		summary = myObj.nextLine();

		System.out.println("Enter text:");
		postText = myObj.nextLine();

		System.out.println("Enter tag:");
		tag = myObj.nextLine();

		// Checks if the input from user is valid
		while (!(tag.equals("Question") || tag.equals("Announcement") || tag.equals("Homework")
				|| tag.equals("Homework solutions") || tag.equals("Lecture notes")
				|| tag.equals("General Announcement"))) {
			System.out.println("You have to select a valid tag");
			System.out.println("Enter tag:");
			tag = myObj.nextLine();
		}

		System.out.println("Enter one of the following foldernames below: ");
		Collection<String> folderNames = new ArrayList<>();
		Statement stmt = conn.createStatement();
		String query = "Select foldername from folder";
		ResultSet rs = stmt.executeQuery(query);
		// Extracts all folders from the database in the given course and checks if user
		// input is valid
		while (rs.next()) {
			folderNames.add(rs.getString("Foldername"));
		}
		folderNames.stream().forEach(n -> System.out.println(n + " "));
		folderName = myObj.nextLine();
		while (!folderNames.contains(folderName.toString())) {
			folderName = null;
			System.out
					.println("You have to select a folder that exists. Enter one of the following foldernames below: ");
			folderNames.stream().forEach(n -> System.out.println(n + " "));
			folderName = myObj.nextLine();
		}
		query = "select anonymity from Course where Coursecode=" + '"' + coursecode + '"';
		rs = stmt.executeQuery(query);
		rs.next();
		Boolean anonymity = rs.getBoolean("Anonymity");
		query = "select FID from Folder where Foldername=" + '"' + folderName + '"';
		rs = stmt.executeQuery(query);
		rs.next();
		int FID = rs.getInt("FID");

		// Checks if the user is allowed to be anonymous within the given course
		if (anonymity == true) {
			System.out.println("Do you want to be anonymous?(true/false)");
			userAnonymity = myObj.nextLine();
			while (!(userAnonymity.equals("true") || userAnonymity.equals("false"))) {
				System.out.println("You have to write true or false");
				System.out.println("Do you want to be anonymous?(true/false)");
				userAnonymity = myObj.nextLine();
			}

		} else {
			userAnonymity = "false";
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
		}
		rs.close();
		threadStatement.close();

		// Creates a relation between the given thread and folder
		threadInFolderStatement = conn.prepareStatement("insert into ThreadInFolder values" + "( (?), (?) )");
		threadInFolderStatement.setInt(1, FID);
		threadInFolderStatement.setInt(2, tid);
		threadInFolderStatement.execute();
		// Creates a post within a new thread
		makePost(postText, userAnonymity, summary, tag, tid, -1, "Headpost");
		System.out
				.println("Post added in folder " + folderName + " tagged as " + tag + " with this summary: " + summary);
	}

	// Takes the necessary input from user and inserts an answer to the database
	public void addAnswer(String folderName, int PaID, String type) throws SQLException {
		String postText;
		String userAnonymity;
		System.out.println("You are now replying to post with id = 1");
		System.out.println("Enter text:");
		postText = myObj.nextLine();
		Statement stmt = conn.createStatement();
		String query = "select TID from Post where PID=" + '"' + PaID + '"';
		ResultSet rs = stmt.executeQuery(query);
		rs.next();

		int TID = rs.getInt("TID");

		query = "select anonymity from Course where Coursecode=" + '"' + coursecode + '"';
		rs = stmt.executeQuery(query);
		rs.next();
		Boolean anonymity = rs.getBoolean("Anonymity");

		// Checks whether the user is allowed to be anonymous or not
		if (anonymity == true) {
			System.out.println("Do you want to be anonymous?(true/false)");
			userAnonymity = myObj.nextLine();
			while (!(userAnonymity.equals("true") || userAnonymity.equals("false"))) {
				System.out.println("You have to write true or false");
				System.out.println("Do you want to be anonymous?(true/false)");
				userAnonymity = myObj.nextLine();
			}

		} else {
			userAnonymity = "false";
		}
		// Creates an answer within an existing thread, the same thread as the post that
		// is answered to
		makePost(postText, userAnonymity, null, null, TID, PaID, type);
		System.out.println("Answer added to post with ID = " + PaID);
	}

	// Returns a list with all threadID that contains one or more posts with a given
	// keyword
	public Collection<Integer> searchKeyWord(String keyWord) throws SQLException {
		Collection<Integer> threads = new ArrayList<Integer>();
		Statement stmt = conn.createStatement();
		String query = "select distinct TID from Post where (text LIKE " + '"' + keyWord + "%" + '"'
				+ " or summary LIKE " + '"' + keyWord + "%" + '"' + ") and tid in ("
				+ "Select tid from thread where coursecode =" + '"' + coursecode + '"' + ")";
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			threads.add(rs.getInt("TID"));
		}
		return threads;
	}

	// Returns a list with all users, how many threads they have read and how many
	// posts they have created, ordered by how many threads they have read.
	public Collection<String> getStats() throws SQLException {
		if (userType(loggedInUser).equals("Student")) {
			System.out.println("Only instructors are allowed to view statistics");
			return null;
		} else {
			Collection<String> stats = new ArrayList<>();
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
				String str = "Name: " + name + ", Threads read: " + postsRead + ", Posts created: " + postsCreated;
				stats.add(str);
			}

			return stats;
		}
	}

	// Gives the user a choice of which usecase to execute, and executes the given
	// usecase from user
	public void runUseCases() throws SQLException {
		System.out.println("Which usecase do you want to execute?(2-5)");
		String usecase = myObj.nextLine();
		if (usecase.equals("2")) {
			addHeadPost();
		} else if (usecase.equals("3")) {
			addAnswer("Exam", 1, "Answer");
		} else if (usecase.equals("4")) {
			System.out.println("Posts with searchword WAL included in these thread IDs: ");
			searchKeyWord("WAL").stream().forEach(e -> System.out.println(e));
		} else if (usecase.equals("5")) {
			getStats().stream().forEach(e -> System.out.println(e));
		} else {
			System.out.println("This is not a valid usecase");
			runUseCases();
		}
	}

	// Asks the user to log in and gives option to quit and log in with another user
	// The user can go on forever or quit whenever he/she wants
	public void runPiazza() {
		this.connect();
		try {
			logUserIn();
			startPost();
			while (true) {
				runUseCases();
				System.out.println("Do you want to continue with the same user? (y/n)");
				String answer = myObj.nextLine();
				while (!(answer.equals("y") || answer.equals("n"))) {
					System.out.println("You must type a valid input");
					System.out.println("Do you want to continue with the same user? (y/n)");
					answer = myObj.nextLine();
				}
				if (answer.equals("n")) {
					logUserIn();
				}
				System.out.println("Press q to quit or press enter to continue");
				if (myObj.nextLine().equals("q")) {
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Inserts a new post to the database
	public void makePost(String postText, String userAnonymity, String summary, String tag, int TID, int PaID,
			String type) throws SQLException {
		postStatement.setString(1, postText);
		postStatement.setBoolean(2, Boolean.parseBoolean(userAnonymity));
		postStatement.setString(3, null);
		postStatement.setString(4, null);
		postStatement.setString(5, loggedInUser);
		postStatement.setInt(6, TID);
		if (PaID == -1) {
			postStatement.setNull(7, Types.INTEGER);
		} else {
			postStatement.setInt(7, PaID);
		}
		postStatement.setString(8, type);
		postStatement.execute();
	}

	// Checks if the logged in user is a student or an instructor
	public String userType(String email) throws SQLException {
		Statement stmt = conn.createStatement();
		String query = "select type from User where email=" + '"' + email + '"';
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		String type = rs.getString("Type");
		rs.close();
		return type;
	}

	public static void main(String[] args) {
		Controller db = new Controller();
		db.runPiazza();
	}
}
