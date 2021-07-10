package main.java.controller.applicationcontroller.createtournament;

import java.sql.SQLException;
import main.java.engineering.bean.tournaments.TournamentBean;
import main.java.engineering.dao.TournamentDAO;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.utils.CommonStrings;
import main.java.engineering.utils.DatetimeUtil;
import main.java.model.CardGame;
import main.java.model.ParticipationInfo;
import main.java.model.Place;
import main.java.model.Tournament;

public class CreateTournamentController {

	public Boolean createTournament(TournamentBean bean) throws DAOException {
		var tournament = tournamentFromBean(bean);
		try {
			TournamentDAO.insertTournament(tournament);
			return true;
		} catch (SQLException e) {
			// Change the exception type, so the graphic controller
			// has not to be aware of database concepts and error
			e.printStackTrace();
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
	}

	private Tournament tournamentFromBean(TournamentBean bean) {
		String name = bean.getName();
		var place = new Place(bean.getAddress(), bean.getLatitude(), bean.getLongitude());
		var datetime = DatetimeUtil.stringToDate(bean.getDate(), bean.getTime());
		String organizer = bean.getOrganizer();
		var cardGame = CardGame.getConstant(bean.getCardGame());
		var pInfo = new ParticipationInfo(bean.getMaxParticipants(), bean.getPrice(), bean.getAward());
		Boolean sponsorRequested = bean.getInRequestForSponsor();

		return new Tournament(name, place, cardGame, datetime, organizer, sponsorRequested, pInfo);
	}
}
