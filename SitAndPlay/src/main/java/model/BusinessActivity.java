package main.java.model;

public class BusinessActivity {
	private String activityName;
	//TODO: change with image
	private String logo;
	private String businessman;
	

	public BusinessActivity(String activityName, String logo, String businessman) {
		this.activityName = activityName;
		this.logo = logo;
		this.businessman = businessman;
	}
	
	public String getActivityName() {
		return activityName;
	}
	public String getLogo() {
		return logo;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getBusinessman() {
		return businessman;
	}

	public void setBusinessman(String businessman) {
		this.businessman = businessman;
	}
	
}
