package uk.co.benjiweber.odb;

import org.junit.Before;

import uk.co.benjiweber.odb.mysql.MySQLDBConnection;
import uk.co.benjiweber.odb.mysql.MySQLObjectRepository;

public class MysqlTests extends TestsBase
{
	@Override @Before public void setUp() throws Exception
	{
		or = new MySQLObjectRepository(new MySQLDBConnection("localhost", "odb",
				"odb", "odb"));

		super.setUp();
	}
}
