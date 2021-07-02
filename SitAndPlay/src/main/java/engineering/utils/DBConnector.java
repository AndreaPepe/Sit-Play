package main.java.engineering.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;


/**
 * This class follows the GoF pattern Singleton.
 * 
 * It is used to guarantee a unique connection
 * to a relational DBMS.
 * @author Andrea Pepe
 *
 */
public class DBConnector {
	
	private static DBConnector instance = null;
	private Connection conn = null;
	
	private final String CONFIG_FILE = "./src/main/java/engineering/utils/configDB.xml";
	private final String tagDatabase = "db";
	private final String tagDriver = "driver";
	
	protected DBConnector() {
		// Singleton: protected constructor
	}
	
	/**
	 * This method is "synchronized"
	 * to avoid the existence of multiple instances
	 * of DBConnector, due to concurrency.
	 */
	public static synchronized DBConnector getInstance() {
		if (instance == null) {
			instance = new DBConnector();
		}
		return instance;
	}
	
	/**
	 * The method getConnection() is "synchronized"
	 * to avoid the existence of multiple instances
	 * of connection, due to concurrency.
	 * 
	 * It loads JDBC driver with dynamic loading,
	 * reading configuration from a file.
	 */
	public synchronized Connection getConnection() {
		
		if (this.conn == null) {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
				factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
				
				DocumentBuilder docBuilder = factory.newDocumentBuilder();
				// parse the XML file
				Document document = docBuilder.parse(new File(CONFIG_FILE));
				String connectionInfo = document.getElementsByTagName(tagDatabase).item(0).getTextContent();
				String driver = document.getElementsByTagName(tagDriver).item(0).getTextContent();
				
				// dynamic loading of drivers
				Class.forName(driver);
				this.conn = DriverManager.getConnection(connectionInfo);								
			} catch (ClassNotFoundException e) {
				Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "[Class: DBConnector] Unable to load DB Driver dynamically");
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}						
		}
		return this.conn;
	}
	
}
