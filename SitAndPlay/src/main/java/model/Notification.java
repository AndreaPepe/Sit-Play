package main.java.model;

public class Notification {
	private int id;
	private String sender;
	private String receiver;
	private String content;
	private Boolean alreadyPopupped;
	
	public Notification(int id, String sender, String receiver, String content, Boolean alreadyPopupped) {
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
		this.alreadyPopupped = alreadyPopupped;
	}

	public int getId() {
		return id;
	}

	public String getSender() {
		return sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public String getContent() {
		return content;
	}

	public Boolean getAlreadyPopupped() {
		return alreadyPopupped;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setAlreadyPopupped(Boolean alreadyPopupped) {
		this.alreadyPopupped = alreadyPopupped;
	}
	
	
}
