package authentication;

import com.mysql.cj.jdbc.MysqlDataSource;
import properties.Properties;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 04/05/18 Time : 3:23 AM Project Name: inc.sarhad.CMS Class Name:
 * DatabaseConnection
 */
public class DatabaseConnection
{
	/*
	* A connection with the database is obtained through this method provided the username and password.
	* */
	public Connection getConnection(String username, String password) throws SQLException, IOException
	{
		
		Properties p = new Properties();
		//get the data source driver
		MysqlDataSource dataSource = new MysqlDataSource();
		
		//permanent settings
		dataSource.setServerTimezone("UTC");
		dataSource.setUseSSL(false);
		dataSource.setUser(username);
		dataSource.setPassword(password);
		
		//settings obtained through the properties file
		dataSource.setPort(Integer.parseInt(p.getProperty("port", Properties.PROPERTY_TYPE.env)));
		dataSource.setDatabaseName(p.getProperty("dbName", Properties.PROPERTY_TYPE.env));
		dataSource.setServerName(p.getProperty("serverName", Properties.PROPERTY_TYPE.env));
		
		//return the connection
		return dataSource.getConnection();
	}
}
