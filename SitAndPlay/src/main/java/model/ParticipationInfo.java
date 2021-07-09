package main.java.model;

public class ParticipationInfo {
	private int maxParticipants;
	private float price;
	private float award;
	
	public ParticipationInfo(int maxParticipants, float price, float award) {
		super();
		this.maxParticipants = maxParticipants;
		this.price = price;
		this.award = award;
	}

	public int getMaxParticipants() {
		return maxParticipants;
	}

	public float getPrice() {
		return price;
	}

	public float getAward() {
		return award;
	}

	public void setMaxParticipants(int maxParticipants) {
		this.maxParticipants = maxParticipants;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setAward(float award) {
		this.award = award;
	}
	
	
	
}
