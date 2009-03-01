package uk.co.benjiweber.odb.demo;

import uk.co.benjiweber.odb.TestsBase;
import uk.co.benjiweber.odb.mysql.MySQLDBConnection;
import uk.co.benjiweber.odb.mysql.MySQLObjectRepository;
import uk.co.benjiweber.odb.oinq.util.ListAccum;

/**
 *
 * @author benji
 */
public class Main
{
	public static void main(String[] args) throws Exception
	{
		if (args.length != 4)
		{
			System.out.println("Usage: <database server> <database name> <username> <password>");
			System.exit(-1);
		}

		final String server = args[0];
		final String database = args[1];
		final String username = args[2];
		final String password = args[3];

		final TestsBase tb = new TestsBase()
		{
			@Override public void setUp() throws Exception
			{
				or = new MySQLObjectRepository(new MySQLDBConnection(server, database, username,
						password));
				super.setUp();
			}

			@Override protected <T> ListAccum<T> makeListAccum()
			{
				return new ListAccum<T>() {
					@Override public void use(T t)
					{
						super.use(t);
						System.out.println(t);
					}
				};
			}
		};
		tb.setUp();

		System.out.println("**** Retriving all saved values ****");

		tb.retrieveAll();

		System.out.println("**** Retriving all with Hello=Bar ****");

		tb.retreevHelloEqualsBar();

		System.out.println("**** Retriving all with World LIKE B% ****");

		tb.retreevLikeB();

		System.out.println("**** Selecting All ****");

		tb.selectAllAnother();

		System.out.println("**** Selecting With ID 5 ****");

		tb.selectId5();
	}

}
