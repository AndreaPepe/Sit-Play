package main.java.model;

public class Player extends User{

	public Player(String username, String password) {
		super(username, password, UserType.PLAYER);
	}



}
