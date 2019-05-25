package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.rowset.CachedRowSet;

public class DBProcess {

	private Connection conn = null;
	private String url = "jdbc:mysql://localhost:3306/";
	private String dbName = "intern-project";
	private String username = "root";
	private String password = "12342345";
	private String driver = "com.mysql.jdbc.Driver";
	
	Statement stmt;
	
	public Connection getConnection() {
		return conn;
	}
	
	public Statement openConnection() throws Exception{
		Class.forName(driver).newInstance();
		conn = DriverManager.getConnection(url + dbName + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=true", username, password);
		return conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	}
	
	public void closeConnection() throws Exception{
		conn.close();
	}
	
	public CachedRowSet createStatement() {
        Statement st1;
        CachedRowSet resWanted = null;

        try {
            st1 = conn.createStatement();
            resWanted = new com.sun.rowset.CachedRowSetImpl();
        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return resWanted;
    }
}
