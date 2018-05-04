package authentication;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 04/05/18 Time : 3:23 AM Project Name: inc.sarhad.CMS Class Name:
 * DatabaseConnection
 */
public class DatabaseConnection
{
	
	public static Connection getConnection(String username, String password) throws SQLException
	{
		
		MysqlDataSource dataSource = new MysqlDataSource();
		
		dataSource.setServerTimezone("UTC");
		dataSource.setUseSSL(false);
		dataSource.setUser(username);
		dataSource.setPassword(password);
		
		dataSource.setPort(3306);
		dataSource.setDatabaseName("hijama_cms");
		dataSource.setServerName("localhost");
		
		return dataSource.getConnection();
	}
	
	public static void main(String[] args)
	{
		try
		{
			Connection c = getConnection("sarhad", "4791");
		} catch( SQLException e )
		{
			System.out.println("Connection failed, err: ");
			e.printStackTrace();
		}
	}
}
