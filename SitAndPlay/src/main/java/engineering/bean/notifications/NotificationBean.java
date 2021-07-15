package main.java.engineering.bean.notifications;

import main.java.model.Notification;

public class NotificationBean {
	
	private int number;
	private String generator;
	private String userToNotify;
	private String msg;
	private Boolean shown;
	
	public NotificationBean (Notification notification) {
		this.number = notification.getId();
		this.generator = notification.getReceiver();
		this.userToNotify = notification.getReceiver();
		this.msg = notification.getContent();
		this.shown = notification.getAlreadyPopupped();
	}

	public int getNumber() {
		return number;
	}

	public String getGenerator() {
		return generator;
	}

	public String getUserToNotify() {
		return userToNotify;
	}

	public String getMsg() {
		return msg;
	}

	public Boolean getShown() {
		return shown;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setGenerator(String generator) {
		this.generator = generator;
	}

	public void setUserToNotify(String userToNotify) {
		this.userToNotify = userToNotify;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setShown(Boolean shown) {
		this.shown = shown;
	}
	
	
	
}
