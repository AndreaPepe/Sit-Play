package main.java.engineering.bean.businessactivity;

import java.io.InputStream;

import javafx.scene.image.Image;
import main.java.engineering.exceptions.BeanCheckException;

public class BusinessActivityBean {

	private String name;
	private InputStream logo;
	private String owner;
	
	private Image fxLogo;
	
	public BusinessActivityBean(String name, InputStream logo, String owner) {
		super();
		this.name = name;
		this.logo = logo;
		this.owner = owner;
	}
	
	public String getName() {
		return name;
	}
	public InputStream getLogo() {
		return logo;
	}
	public String getOwner() {
		return owner;
	}
	public Image getFxLogo() {
		return fxLogo;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setLogo(InputStream logo) {
		this.logo = logo;
		// set also the javaFX image
		
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public void setFxLogo(Image fxLogo) {
		this.fxLogo = fxLogo;
	}
	
	public Boolean checkFields() throws BeanCheckException {
		if (name.isBlank()) {
			throw new BeanCheckException("The name of the activity can not be blank!");
		}
		return true;
	}
}
