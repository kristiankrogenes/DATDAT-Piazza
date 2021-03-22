package project;

public class runPiazza extends DBConn {
	
	public static void main(String[] args) {
		
		// Log in ==============================================================
		System.out.println("========= LOG IN AS STUDENT =========");
		LoginCtrl db = new LoginCtrl();
	    db.connect();
	    db.logUserIn();
	    System.out.println();
	   
	    
	    // Add post =========================================================
	    System.out.println("========= ADD POST AS STUDENT =========");
	    AddPostCtrl addPost = new AddPostCtrl();
		addPost.connect();
		addPost.startPost();
		addPost.addHeadPost();
		System.out.println();
		
		// Reply to post ======================================================
		System.out.println("========= LOG IN AS INSTRUCTOR =========");
		db.logUserIn();
		
		System.out.println();
		
		System.out.println("========= REPLY TO POST 2 AS INSTRUCTOR =========");
		replyCtrl addPost2 = new replyCtrl();
		addPost2.connect();
		addPost2.startPost();
		addPost2.addAnswer("Exam", 2, "Answer");
		System.out.println();
		
		// Search post =======================================================
		System.out.println("========= STUDENT SEARCHED FOR KEYWORD WAL =========");
		PostSearchCtrl searchPost = new PostSearchCtrl();
		searchPost.connect();
		System.out.println(searchPost.searchKeyWord("WAL"));
		System.out.println();
		System.out.println();
		
		// Check stats ========================================================
		System.out.println("========= INSTRUCTOR VIEWED STATISTICS FOR USERS =========");
		StatCtrl stats = new StatCtrl();
		stats.connect();
		stats.getStats().stream().forEach(e -> System.out.println(e));
		System.out.println();
	}
}
