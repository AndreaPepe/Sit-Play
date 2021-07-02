package main.java.model;

public enum CardGame {
	POKER("Poker"),
	BLACK_JACK("Black Jack"),
	TEXAS_HOLDEM_POKER("Texas Holdem Poker");
	
	public final String name;
	private CardGame(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public static CardGame getConstant(String name) {
		for (CardGame constant: CardGame.values()) {
			if (constant.toString().equals(name))
				return constant;
		}
		return null;
	}
}


