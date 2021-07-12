package main.java.controller.applicationcontroller.reserveaseat.table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.engineering.bean.tournaments.TournamentBean;
import main.java.engineering.bean.tournaments.TournamentBeanFactory;
import main.java.engineering.dao.TournamentDAO;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.utils.CommonStrings;
import main.java.model.Tournament;

public class ReserveTournamentSeatController {

	public List<TournamentBean> retrieveOpenTournaments() throws DAOException {
		List<TournamentBean> beans = new ArrayList<>();
		try {
			var tournaments = TournamentDAO.retrieveOpenTournaments();
			for(Tournament t : tournaments) {
				TournamentBean bean = TournamentBeanFactory.createBean(t);
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
}
