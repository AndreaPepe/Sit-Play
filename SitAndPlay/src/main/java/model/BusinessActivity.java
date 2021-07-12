package main.java.model;

import java.io.InputStream;

public class BusinessActivity {
	private String activityName;
	//TODO: change with image
	private InputStream logo;
	private String businessman;
	

	public BusinessActivity(String activityName, InputStream logo, String businessman) {
		this.activityName = activityName;
		this.logo = logo;
		this.businessman = businessman;
	}
	
	public String getActivityName() {
		return activityName;
	}
	public InputStream getLogo() {
		return logo;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public void setLogo(InputStream logo) {
		this.logo = logo;
	}
	public String getBusinessman() {
		return businessman;
	}

	public void setBusinessman(String businessman) {
		this.businessman = businessman;
	}
	
}
