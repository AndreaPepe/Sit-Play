package main.java.controller.applicationcontroller.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.engineering.bean.businessactivity.BusinessActivityBean;
import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.dao.BusinessActivityDAO;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.WrongUserTypeException;
import main.java.engineering.utils.CommonStrings;
import main.java.model.BusinessActivity;
import main.java.model.UserType;

public class ManageActivitiesController {

	public Boolean modifyLogo(BusinessActivityBean bean) throws DAOException {
		try {
			BusinessActivityDAO.updateLogo(bean.getName(), bean.getLogo());
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
		return true;
	}
	
	public List<BusinessActivityBean> retrieveActivities(BeanUser user) throws WrongUserTypeException, DAOException{
		if (user.getUserType() != UserType.BUSINESSMAN) {
			throw new WrongUserTypeException(CommonStrings.getNotBusinessmanErrMsg());
		}
		
		List<BusinessActivityBean> ret = new ArrayList<>();
		try {
			List<BusinessActivity> activities = BusinessActivityDAO.retrieveActivitiesByOwner(user.getUsername());
			for (BusinessActivity a : activities) {
				var bean = new BusinessActivityBean(a.getActivityName(), a.getLogo(), a.getBusinessman());
				ret.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
		return ret;
	}
	
	public void deleteBusinessActivity(BusinessActivityBean bean) {
		// TODO: controllare se non sta sponsorizzando prima di rimuovere
	}
}
