package project;

public class Course {
	String courseCode;
	String courseName;
	String term;
	Boolean anonymity;
	public Course(String courseCode, String courseName, String term,
			Boolean anonymity) {
		this.courseCode = courseCode;
		this.courseName = courseName;
		this.term = term;
		this.anonymity = anonymity;
	}
	public String getCourseCode() {
		return courseCode;
	}
	public String getCourseName() {
		return courseName;
	}
	public String getTerm() {
		return term;
	}
	public Boolean getAnonymity() {
		return anonymity;
	}
	public void setAnonymity(Boolean anonymity) {
		this.anonymity = anonymity;
	}
	
	
}
