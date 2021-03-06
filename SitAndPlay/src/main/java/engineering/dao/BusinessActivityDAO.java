package main.java.engineering.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.utils.DBConnector;
import main.java.engineering.utils.query.QueryBusinessActivity;
import main.java.model.BusinessActivity;

public class BusinessActivityDAO {

	private BusinessActivityDAO() {
		// no instance needed
	}

	public static void insertActivity(String name, InputStream logo, String businessman)
			throws SQLException, DAOException {
		var conn = DBConnector.getInstance().getConnection();

		try {
			QueryBusinessActivity.insertActivity(conn, name, logo, businessman);
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new DAOException(String.format("An activity with the name '%s' already exists", name));
		}

	}

	public static void updateLogo(String activity, InputStream newLogo) throws SQLException {
		var conn = DBConnector.getInstance().getConnection();

		QueryBusinessActivity.updateLogo(conn, activity, newLogo);
	}

	public static List<BusinessActivity> retrieveActivitiesByOwner(String businessman) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		List<BusinessActivity> list = new ArrayList<>();

		conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = QueryBusinessActivity.retrieveActivities(stmt, businessman);
			if (!rs.first()) {
				return list;
			}
			rs.beforeFirst();

			while (rs.next()) {
				var name = rs.getString("activity");
				var owner = rs.getString(2);
				var imageBlob = rs.getBlob("logo");
				InputStream logo;
				if (imageBlob != null) {
					logo = imageBlob.getBinaryStream(1, imageBlob.length());
				} else {
					logo = null;
				}

				var activity = new BusinessActivity(name, logo, owner);
				list.add(activity);
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		return list;
	}

	public static void deleteBusinessActivity(String name) throws SQLException {
		Connection conn = null;
		Statement stmt = null;

		conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement();
			QueryBusinessActivity.deleteActivity(stmt, name);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

	}

	public static BusinessActivity retrieveActivityByName(String name) throws SQLException, DAOException {
		Statement stmt = null;
		BusinessActivity ret = null;
		var conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = QueryBusinessActivity.retrieveActivityByName(stmt, name);
			if (!rs.first()) {
				throw new DAOException("No business activity found");
			}
			var actName = rs.getString("activity");
			var businessman = rs.getString(2);
			var blob = rs.getBlob("logo");
			InputStream logo;
			if (blob != null) {
				logo = blob.getBinaryStream(1, blob.length());
			} else {
				logo = null;
			}

			ret = new BusinessActivity(actName, logo, businessman);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return ret;
	}

	
}
