package main.java.engineering.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.WrongCredentialsExceptions;
import main.java.engineering.utils.DBConnector;
import main.java.engineering.utils.query.QueryLogin;
import main.java.model.Businessman;
import main.java.model.Organizer;
import main.java.model.Player;
import main.java.model.User;

public class LoginDAO {
	
	private LoginDAO() {
		
	}
	
	public static User login(String username, String password) throws SQLException, DAOException, WrongCredentialsExceptions {
		Connection conn = null;
		User user;
		
		conn = DBConnector.getInstance().getConnection();
		String sql = QueryLogin.getLoginQuery();
		var pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		try {
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			
			ResultSet rs = pstmt.executeQuery();
			if (!rs.first()) {
				throw new WrongCredentialsExceptions("Username or password are wrong");
			}
			// move the cursor on the first record 
			rs.first();
			var uName = rs.getString("username");
			var pass = rs.getString("password");
			var uType = rs.getString("userType");
			
			switch (uType) {
			case "Player": 
				user = new Player(uName, pass); break;			
			case "Organizer":
				user = new Organizer(uName, pass); break;
			case "Businessman":
				user = new Businessman(uName, pass); break;			
			default:
				throw new DAOException("Unexpected value: " + uType);
			}
				
			rs.close();		
		} finally {
			pstmt.close();
		}
			
		
		return user;		
	}
	
	
	public static void registration(BeanUser user) throws SQLException, DAOException {
		Connection conn = null;
		
		
		try {
			conn = DBConnector.getInstance().getConnection();
			
			QueryLogin.registration(conn, user);
			
		}catch (SQLIntegrityConstraintViolationException e) {
			// duplicate entry for PK username
			throw new DAOException("Username already in use!");
		}
	}

}
