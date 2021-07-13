package main.java.controller.applicationcontroller.business;

import java.sql.SQLException;

import main.java.engineering.bean.businessactivity.BusinessActivityBean;
import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.dao.BusinessActivityDAO;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.WrongUserTypeException;
import main.java.engineering.utils.CommonStrings;
import main.java.model.UserType;

public class CreateBusinessActivityController {

	public void createActivity(BusinessActivityBean bean, BeanUser user) throws WrongUserTypeException, DAOException {
		if (user.getUserType() != UserType.BUSINESSMAN) {
			throw new WrongUserTypeException(CommonStrings.getNotBusinessmanErrMsg());
		}
		try {
			BusinessActivityDAO.insertActivity(bean.getName(), bean.getLogo(), bean.getOwner());
		} catch (SQLException e) {
			// tranform in DAOException 
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
	}
}
