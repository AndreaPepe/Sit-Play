package main.java.engineering.bean.businessactivity;

public class BusinessActivityBean {

	private String name;
	//TODO: change with Image
	private String logo;
	private String owner;
	
	
	public BusinessActivityBean(String name, String logo, String owner) {
		super();
		this.name = name;
		this.logo = logo;
		this.owner = owner;
	}
	
	public String getName() {
		return name;
	}
	public String getLogo() {
		return logo;
	}
	public String getOwner() {
		return owner;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
}
