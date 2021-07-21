package main.java.controller.applicationcontroller.userspage;

import java.sql.SQLException;

import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.bean.stats.StatsBean;
import main.java.engineering.dao.TableDAO;
import main.java.engineering.dao.TournamentDAO;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.WrongUserTypeException;
import main.java.engineering.utils.CommonStrings;
import main.java.model.UserType;

public class StatisticsController {

	public int getNumberOfSponsorizedTournaments(BeanUser user) throws WrongUserTypeException, DAOException {
		if (!user.getUserType().equals(UserType.BUSINESSMAN)) {
			throw new WrongUserTypeException("Only businessman users can retrieve their own stats");
		} else {
			try {
				return TournamentDAO.getNumberOfSponsorizedTournamentByBusinessman(user.getUsername());
			} catch (SQLException e) {
				throw new DAOException(CommonStrings.getDatabaseErrorMsg());
			}
		}
	}

	private int getNumberOfOrganizedTables(BeanUser user) throws DAOException {
		try {
			return TableDAO.getNumberOfOrganizedTables(user.getUsername());
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
	}

	private int getNumberOfJoinedTables(BeanUser player) throws DAOException {
		try {
			return TableDAO.getNumberOfJoinedTables(player.getUsername());
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
	}

	private int getNumberOfOrganizedTournaments(BeanUser org) throws DAOException {
		try {
			return TournamentDAO.getNumberOfOrganizedTournaments(org.getUsername());
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
	}

	private int getNumberOfJoinedTournaments(BeanUser player) throws DAOException {
		try {
			return TournamentDAO.getNumberOfJoinedTournaments(player.getUsername());
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
	}

	private int getNumberOfWonTables(BeanUser playerUser) throws DAOException {
		try {
			return TableDAO.getNumberOfWonTables(playerUser.getUsername());
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
	}

	private int getNumberOfWonTournaments(BeanUser playerUser) throws DAOException {
		try {
			return TournamentDAO.getNumberOfWonTournaments(playerUser.getUsername());
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
	}

	private float getTotalAmountOfWonMoney(BeanUser user) throws DAOException {
		try {
			return TournamentDAO.getAmountOfWonTournamentAwards(user.getUsername());
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
	}

	public StatsBean getStats(BeanUser user) throws DAOException {
		var stats = new StatsBean();
		stats.setOrgTables(getNumberOfOrganizedTables(user));
		if (user.getUserType().equals(UserType.ORGANIZER)) {
			stats.setOrgTournaments(getNumberOfOrganizedTournaments(user));
		} else if (user.getUserType().equals(UserType.PLAYER)) {
			var joinedTables = getNumberOfJoinedTables(user);
			var wonTables = getNumberOfWonTables(user);
			stats.setJoinedTables(joinedTables);
			stats.setWonTables(wonTables);
			stats.setTablesWinningPercentage(calculateWinningPercentage(joinedTables, wonTables));

			var joinedTournaments = getNumberOfJoinedTournaments(user);
			var wonTournaments = getNumberOfWonTournaments(user);
			stats.setJoinedTournaments(joinedTournaments);
			stats.setWonTournaments(wonTournaments);
			stats.setTournamentWinningPercentage(calculateWinningPercentage(joinedTournaments, wonTournaments));
			stats.setTotalMoney(getTotalAmountOfWonMoney(user));
		}

		return stats;
	}

	private float calculateWinningPercentage(int played, int won) {
		// division per 0 is safe
		if (played <= 0) {
			return 0f;
		} else if (won >= played) {
			return 100f;
		} else {
			return (float) won / played;
		}
	}
}
