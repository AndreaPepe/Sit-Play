package main.java.controller.applicationcontroller.userspage;

import java.sql.SQLException;

import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.dao.TournamentDAO;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.WrongUserTypeException;
import main.java.engineering.utils.CommonStrings;
import main.java.model.UserType;

public class StatisticsController {

	public int getNumberOfSponsorizedTournaments(BeanUser user) throws WrongUserTypeException, DAOException {
		if (!user.getUserType().equals(UserType.BUSINESSMAN)) {
			throw new WrongUserTypeException("Only businessman users can retrieve their own stats");
		}else {
			try {
				return TournamentDAO.getNumberOfSponsorizedTournamentByBusinessman(user.getUsername());
			} catch (SQLException e) {
				throw new DAOException(CommonStrings.getDatabaseErrorMsg());
			}
		}
	}
}
