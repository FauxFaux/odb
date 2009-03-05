package uk.co.benjiweber.odb;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;

import uk.co.benjiweber.odb.mysql.MySQLDBConnection;
import uk.co.benjiweber.odb.mysql.MySQLObjectRepository;

public class MysqlTests extends TestsBase
{
	private MySQLDBConnection conn;
	@Override @Before public void setUp() throws Exception
	{
		conn = new MySQLDBConnection("localhost", "odb",
				"odb", "odb");
		or = new MySQLObjectRepository(conn);

		super.setUp();
	}


	@After public void deleteEverything() throws ODBException, SQLException
	{
		final Connection connection = conn.getConnection();
		try
		{
			connection.createStatement().execute("DROP TABLE " + TestType.class.getSimpleName());
			connection.createStatement().execute("DROP TABLE " + AnotherTestType.class.getSimpleName());
		}
		finally
		{
			connection.close();
		}
	}
}
