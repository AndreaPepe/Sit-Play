package main.java.view.listview;

import main.java.engineering.bean.login.BeanUser;

public class BeanContainer {

	private Object carriedBean;
	private BeanUser userBean;
	
	public BeanContainer(Object carriedBean, BeanUser userBean) {
		this.carriedBean = carriedBean;
		this.userBean = userBean;
	}

	public Object getCarriedBean() {
		return carriedBean;
	}

	public BeanUser getUserBean() {
		return userBean;
	}

	public void setCarriedBean(Object carriedBean) {
		this.carriedBean = carriedBean;
	}

	public void setUserBean(BeanUser userBean) {
		this.userBean = userBean;
	}
	
	
}
