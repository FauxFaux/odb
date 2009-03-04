package uk.co.benjiweber.odb;

import org.junit.Before;

import uk.co.benjiweber.odb.hm.HashMapObjectRepository;

public class HashMapTests extends TestsBase
{
	@Override @Before public void setUp() throws Exception
	{
		or = new HashMapObjectRepository();

		super.setUp();
	}
}
