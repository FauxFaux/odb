/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.benjiweber.odb.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import uk.co.benjiweber.odb.DBConnection;
import uk.co.benjiweber.odb.ODBException;
/**
 *
 * @author benji
 */
public class MySQLDBConnection implements DBConnection
{
	private String server = "localhost";
	private String database = "odb";
	private String username = "odb";
	private String password = "odb";

	public MySQLDBConnection()
	{

	}

	public MySQLDBConnection(String server, String database, String username, String password)
	{
		this.server = server;
		this.database = database;
		this.password = password;
		this.username = username;
	}

	private Connection connect(String uname, String pword,
			String dbase, String svr) throws ClassNotFoundException,
			InstantiationException, SQLException, IllegalAccessException {
		String url = "jdbc:mysql://" + svr + "/" + dbase;
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		return DriverManager.getConnection(url, uname, pword);
	}


	public synchronized Connection getConnection() throws ODBException
	{
		try
		{
			return connect(username,password,database,server);
		} catch (Exception e)
		{
			throw new ODBException("Could not connect to the database",e);
		}
	}

	@Override
	public String getDatabaseName()
	{
		return database;
	}
}
