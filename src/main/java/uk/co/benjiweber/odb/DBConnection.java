package uk.co.benjiweber.odb;

import java.sql.Connection;

/**
 *
 * @author benji
 */
public interface DBConnection
{
	public Connection getConnection() throws ODBException;
}
