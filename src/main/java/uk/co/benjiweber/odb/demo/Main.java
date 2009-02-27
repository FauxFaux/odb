package uk.co.benjiweber.odb.demo;

import uk.co.benjiweber.odb.mysql.MySQLObjectRepository;
import uk.co.benjiweber.odb.mysql.MySQLDBConnection;
import java.util.Arrays;
import java.util.List;
import uk.co.benjiweber.odb.ODBException;
import uk.co.benjiweber.odb.ObjectRepository;
import uk.co.benjiweber.odb.oinq.QueryException;
import uk.co.benjiweber.odb.oinq.Use;

/**
 *
 * @author benji
 */
public class Main
{

    public static void main(String[] args) throws QueryException, ODBException
	{
		if (args.length != 4)
		{
			System.out.println("Usage: <database server> <database name> <username> <password>");
			System.exit(-1);
		}

		String server = args[0];
		String database = args[1];
		String username = args[2];
		String password = args[3];

		TestType a = new TestType("Hello", "World");
		TestType b = new TestType("Foo","Bar");
		TestType c = new TestType("Bar","Baz");
		TestType d = new TestType("Bar","Something Else Entirely");
		TestType e = new TestType("Baz","Bingo");
		TestType f = new TestType("Spleen","Liver");

		List<TestType> testTypes = Arrays.asList(a,b,c,d,e,f);

        ObjectRepository or = new MySQLObjectRepository(new MySQLDBConnection(server, database, username, password));

		System.out.println("**** Saving values ****");

		for(TestType t : testTypes)
			or.save(t);

		System.out.println("**** Retriving all saved values ****");
		
		or.from(TestType.class).select(new Use<TestType>()
		{
			public void use(TestType t)
			{
				System.out.println(t.Hello + " : " + t.World);
			}
		});

		System.out.println("**** Retriving all with Hello=Bar ****");

		or.from(TestType.class).where(new TestType().Hello).equals("Bar").select(new Use<TestType>()
		{
			public void use(TestType t)
			{
				System.out.println(t.Hello + " : " + t.World);
			}
		});


		System.out.println("**** Retriving all with World LIKE B% ****");

		or.from(TestType.class).where(new TestType().World).like("B%").select(new Use<TestType>()
		{
			public void use(TestType t)
			{
				System.out.println(t.Hello + " : " + t.World);
			}
		});

		System.out.println("**** Testing another test type ****");
		AnotherTestType q = new AnotherTestType(5,"Cats");
		AnotherTestType r = new AnotherTestType(6,"Dogs");

		or.saveAll(q,r);

		System.out.println("**** Selecting All ****");

		or.from(AnotherTestType.class).select(new Use<AnotherTestType>()
		{
			public void use(AnotherTestType t)
			{
				System.out.println(t.Number + " : " + t.Name);
			}
		});

		System.out.println("**** Selecting With ID 5 ****");

		or.from(AnotherTestType.class).where(new AnotherTestType().Number).equals(5).select(new Use<AnotherTestType>()
		{
			public void use(AnotherTestType t)
			{
				System.out.println(t.Number + " : " + t.Name);
			}
		});
	}

}
