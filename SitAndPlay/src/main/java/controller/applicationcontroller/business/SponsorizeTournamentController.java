package main.java.controller.applicationcontroller.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.engineering.bean.businessactivity.BusinessActivityBean;
import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.bean.tournaments.TournamentBean;
import main.java.engineering.bean.tournaments.TournamentBeanFactory;
import main.java.engineering.dao.BusinessActivityDAO;
import main.java.engineering.dao.NotificationDAO;
import main.java.engineering.dao.TournamentDAO;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.DateParsingException;
import main.java.engineering.utils.CommonStrings;
import main.java.model.BusinessActivity;
import main.java.model.Notification;
import main.java.model.Tournament;

public class SponsorizeTournamentController {

	public List<TournamentBean> getTournamentsToSponsorize() throws DAOException, DateParsingException {
		List<TournamentBean> beans = new ArrayList<>();
		try {
			var list = TournamentDAO.retrieveSponsorizableTournaments();
			for (Tournament t : list) {
				var bean = TournamentBeanFactory.createBean(t);
				beans.add(bean);
			}
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
		return beans;
	}

	public List<BusinessActivityBean> getBusinessmanActivities(BeanUser user) throws DAOException {
		List<BusinessActivityBean> beans = new ArrayList<>();
		try {
			var activities = BusinessActivityDAO.retrieveActivitiesByOwner(user.getUsername());
			for (BusinessActivity ba : activities) {
				var bean = new BusinessActivityBean(ba.getActivityName(), ba.getLogo(), ba.getBusinessman());
				beans.add(bean);
			}
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
		return beans;
	}

	public void confirmSponsorization(TournamentBean bean, BusinessActivityBean sponsor) throws DAOException {
		try {
			TournamentDAO.updateSponsor(bean.getName(), sponsor.getName());
			var notif = String.format(CommonStrings.getTournamentSponsorizationNotif(), bean.getName(),
					sponsor.getOwner(), sponsor.getName());
			var notification = new Notification(-1, sponsor.getOwner(), bean.getOrganizer(), notif, false);
			NotificationDAO.insertNotification(notification);
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
	}
}
