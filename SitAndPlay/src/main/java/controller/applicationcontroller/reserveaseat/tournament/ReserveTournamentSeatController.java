package main.java.controller.applicationcontroller.reserveaseat.tournament;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.bean.tournaments.TournamentBean;
import main.java.engineering.bean.tournaments.TournamentBeanFactory;
import main.java.engineering.dao.NotificationDAO;
import main.java.engineering.dao.TournamentDAO;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.DeleteSeatException;
import main.java.engineering.exceptions.MaxParticipantsException;
import main.java.engineering.exceptions.WrongUserTypeException;
import main.java.engineering.utils.CommonStrings;
import main.java.engineering.utils.DatetimeUtil;
import main.java.model.Notification;
import main.java.model.Tournament;
import main.java.model.UserType;

public class ReserveTournamentSeatController {

	public List<TournamentBean> retrieveOpenTournaments() throws DAOException {
		List<TournamentBean> beans = new ArrayList<>();
		try {
			var tournaments = TournamentDAO.retrieveOpenTournaments();
			for (Tournament t : tournaments) {
				var bean = TournamentBeanFactory.createBean(t);
				beans.add(bean);
			}
		} catch (SQLException e) {
			// Change the exception type, so the graphic controller
			// has not to be aware of database concepts and error
			e.printStackTrace();
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
		return beans;
	}

	public void joinTournament(TournamentBean bean, BeanUser user)
			throws DAOException, MaxParticipantsException, WrongUserTypeException {

		if (user.getUserType() != UserType.PLAYER) {
			throw new WrongUserTypeException(CommonStrings.getWrongUserErrMsg());
		}
		try {
			List<String> participants = TournamentDAO.retrieveParticipants(bean.getName());

			// if the tournament has already the maximum number of participant, the seat
			// can't be reserved
			if (participants.size() >= bean.getMaxParticipants()) {
				throw new MaxParticipantsException(
						"The number of participants for this tournament has already been reached");
			} else {
				TournamentDAO.addParticipant(bean.getName(), user.getUsername());
				var notificationContent = String.format(CommonStrings.getTournamentReservedNotif(), user.getUsername(),
						bean.getName());
				var notif = new Notification(-1, user.getUsername(), bean.getOrganizer(), notificationContent, false);
				NotificationDAO.insertNotification(notif);
			}
		} catch (SQLException e) {
			// Change the exception type, so the graphic controller
			// has not to be aware of database concepts and error
			e.printStackTrace();
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
	}

	public void removeParticipant(TournamentBean tournament, BeanUser participant) throws DAOException, DeleteSeatException {
		if (Boolean.FALSE.equals(DatetimeUtil.isValidDateWithMargin(tournament.getDate(), tournament.getTime(), 3))) {
			throw new DeleteSeatException("The tournament has already started. You can not leave the tournament now");
		}
		try {
			TournamentDAO.removeParticipant(tournament.getName(), participant.getUsername());
			var notifContent = String.format(CommonStrings.getTournamentSeatLeaved(), participant.getUsername(),
					tournament.getName());
			var notif = new Notification(-1, participant.getUsername(), tournament.getOrganizer(), notifContent, false);
			NotificationDAO.insertNotification(notif);
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
	}

	public List<TournamentBean> retrieveActiveJoinedTournaments(BeanUser usr) throws DAOException {
		List<TournamentBean> list = new ArrayList<>();

		try {
			var tournaments = TournamentDAO.retrieveActiveTournamentsByParticipant(usr.getUsername());
			for (Tournament t : tournaments) {
				var bean = TournamentBeanFactory.createBean(t);
				list.add(bean);
			}

		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
		return list;
	}

}
