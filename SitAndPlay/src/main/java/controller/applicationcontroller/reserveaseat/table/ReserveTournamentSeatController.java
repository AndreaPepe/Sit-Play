package main.java.controller.applicationcontroller.reserveaseat.table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.bean.tournaments.TournamentBean;
import main.java.engineering.bean.tournaments.TournamentBeanFactory;
import main.java.engineering.dao.TournamentDAO;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.MaxParticipantsException;
import main.java.engineering.exceptions.WrongUserTypeException;
import main.java.engineering.utils.CommonStrings;
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
			}
		} catch (SQLException e) {
			// Change the exception type, so the graphic controller
			// has not to be aware of database concepts and error
			e.printStackTrace();
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
	}

}
