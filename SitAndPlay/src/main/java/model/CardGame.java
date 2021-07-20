package main.java.model;

public enum CardGame {
	POKER("Poker"),
	BLACK_JACK("Black Jack"),
	TEXAS_HOLDEM_POKER("Texas Holdem Poker"),
	SCOPA("Scopa"),
	SCOPONE("Scopone"),
	BRISCOLA("Briscola"),
	BURACO("Buraco"),
	RUMMY("Rummy"),
	TRESSETTE("Tressette");
	
	public final String gameName;
	private CardGame(String name) {
		gameName = name;
	}
	
	@Override
	public String toString() {
		return gameName;
	}
	
	public static CardGame getConstant(String name) {
		for (CardGame constant: CardGame.values()) {
			if (constant.toString().equals(name))
				return constant;
		}
		return null;
	}
}


