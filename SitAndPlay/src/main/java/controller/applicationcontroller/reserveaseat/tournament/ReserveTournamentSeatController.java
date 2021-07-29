package main.java.controller.applicationcontroller.reserveaseat.tournament;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.bean.tournaments.TournamentBean;
import main.java.engineering.bean.tournaments.TournamentBeanFactory;
import main.java.engineering.dao.NotificationDAO;
import main.java.engineering.dao.TournamentDAO;
import main.java.engineering.exceptions.AlreadyExistingWinnerException;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.DateParsingException;
import main.java.engineering.exceptions.DeleteSeatException;
import main.java.engineering.exceptions.MaxParticipantsException;
import main.java.engineering.exceptions.OutOfTimeException;
import main.java.engineering.exceptions.WrongUserTypeException;
import main.java.engineering.utils.CommonStrings;
import main.java.engineering.utils.DatetimeUtil;
import main.java.model.Notification;
import main.java.model.Tournament;
import main.java.model.UserType;

public class ReserveTournamentSeatController {

	public List<TournamentBean> retrieveOpenTournaments() throws DAOException, DateParsingException {
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
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
	}

	public void removeParticipant(TournamentBean tournament, BeanUser participant)
			throws DAOException, DeleteSeatException, DateParsingException {

		// tournament reservations are closed 3 hours before the beginning of the
		// tournament
		if (Boolean.FALSE.equals(DatetimeUtil.isValidDateWithMargin(tournament.getDate(), tournament.getTime(), 3))) {
			throw new DeleteSeatException(
					"Reservations for the tournament have been closed. You can not leave the tournament now");
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

	public List<TournamentBean> retrieveActiveJoinedTournaments(BeanUser usr)
			throws DAOException, DateParsingException {
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

	public List<TournamentBean> retrieveTournamentsToDeclareWinnerTo(BeanUser organizer)
			throws DAOException, DateParsingException {
		List<TournamentBean> ret = new ArrayList<>();
		try {
			var tmts = TournamentDAO.getTournamentsToDeclareWinnerByOrganizer(organizer.getUsername());
			for (Tournament t : tmts) {
				var bean = TournamentBeanFactory.createBean(t);
				// we also need the list of participants
				var participants = TournamentDAO.retrieveParticipants(t.getName());
				bean.setParticipants(participants);
				ret.add(bean);
			}
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
		return ret;
	}

	public void setWinner(TournamentBean tournament, BeanUser userOrg)
			throws DAOException, DateParsingException, AlreadyExistingWinnerException {
		try {
			var tournamentCheck = TournamentDAO.retrieveTournament(tournament.getName());
			if (tournamentCheck.getWinner() != null) {
				throw new AlreadyExistingWinnerException("ERROR! This tournament already has a winner!");
			} else {
				TournamentDAO.setWinner(tournament.getName(), tournament.getWinner());
				var message = String.format(CommonStrings.getTournamentWinnerString(), tournament.getName(),
						userOrg.getUsername());
				var notif = new Notification(-1, userOrg.getUsername(), tournament.getWinner(), message, false);
				NotificationDAO.insertNotification(notif);
			}
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}

	}

	public List<TournamentBean> getDeletableTournaments(BeanUser organizer) throws DateParsingException, DAOException {
		List<TournamentBean> retBeans = new ArrayList<>();
		try {
			var tmts = TournamentDAO.retrieveDeletableTournamentsByOrganizer(organizer.getUsername());
			for (Tournament tmt : tmts) {
				var bean = TournamentBeanFactory.createBean(tmt);
				retBeans.add(bean);
			}
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
		return retBeans;
	}

	public void deleteTournament(TournamentBean tournament)
			throws DAOException, DateParsingException, OutOfTimeException {
		try {
			var checkTournament = TournamentDAO.retrieveTournament(tournament.getName());
			// 6 hours of margin from the start
			if (Boolean.FALSE
					.equals(DatetimeUtil.isValidDateWithMargin(tournament.getDate(), tournament.getTime(), 6))) {
				throw new OutOfTimeException(
						"Impossible to delete table. It should be deleted within 6 hours from the beginning");
			} else {
				var content = String.format(CommonStrings.getDeletedTournamentNotif(), checkTournament.getName(),
						checkTournament.getOrganizer());
				if (checkTournament.getSponsor() != null) {
					var notif = new Notification(-1, checkTournament.getOrganizer(),
							checkTournament.getSponsor().getBusinessman(), content, false);
					NotificationDAO.insertNotification(notif);
				}
				
				var participants = checkTournament.getParticipants();
				for(String part : participants) {
					var not = new Notification(-1, checkTournament.getOrganizer(), part, content, false);
					NotificationDAO.insertNotification(not);
				}
				
				TournamentDAO.deleteTournament(checkTournament.getName());
			}
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
	}

}
