package model;

public abstract class User {
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	
	public User(String firstName, String lastName, String username, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
	}
	
	public boolean checkPassword(String password) {
		return this.password.equals(password);
	}
	
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	public String getUsername() {
		return username;
	}
	
	
}
