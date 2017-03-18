package discordbot;

import java.sql.*;

public class SQLConnector {
	
	private static SQLConnector instance = new SQLConnector();
	public static final String URL = "jdbc:mysql://localhost/mydb"; 
	public static final String USER = "root";
	public static final String PASSWORD = "Tt2006ja";
	public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver"; 

	public SQLConnector() {
		try {
			//Step 2: Load MySQL Java driver
			Class.forName(DRIVER_CLASS);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private Connection createConnection() {

		Connection connection = null;
		try {
			//Step 3: Establish Java MySQL connection
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			System.out.println("ERROR: Unable to Connect to Database.");
		}
		return connection;
	}	
	
	public static Connection getConnection() {
		return instance.createConnection();
	}
	
}
