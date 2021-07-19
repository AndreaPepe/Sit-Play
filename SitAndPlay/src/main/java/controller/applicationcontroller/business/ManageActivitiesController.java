package main.java.controller.applicationcontroller.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.engineering.bean.businessactivity.BusinessActivityBean;
import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.dao.BusinessActivityDAO;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.DateParsingException;
import main.java.engineering.exceptions.DeleteActivityException;
import main.java.engineering.exceptions.WrongUserTypeException;
import main.java.engineering.utils.CommonStrings;
import main.java.model.BusinessActivity;
import main.java.model.Tournament;
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

	public List<BusinessActivityBean> retrieveActivities(BeanUser user) throws WrongUserTypeException, DAOException {
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

	public void deleteBusinessActivity(BusinessActivityBean bean) throws DAOException, DeleteActivityException, DateParsingException {
		try {
			List<Tournament> sponsorizedTournaments = BusinessActivityDAO
					.retrieveOpenSponsorizedTournaments(bean.getName());

			// if the activity is sponsoring tournaments, it can't be deleted
			if (!sponsorizedTournaments.isEmpty()) {
				throw new DeleteActivityException(
						"This activity is currently used to sponsorize one or more tournaments and can't be deleted");
			}
			
			BusinessActivityDAO.deleteBusinessActivity(bean.getName());
			
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
	}
}
