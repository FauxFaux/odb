package uk.co.benjiweber.odb;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.co.benjiweber.odb.oinq.QueryException;
import uk.co.benjiweber.odb.oinq.util.ListAccum;

public class TestsBase
{
	private static final AnotherTestType AT6Dogs = new AnotherTestType(6, "Dogs");
	private static final AnotherTestType AT5Cats = new AnotherTestType(5, "Cats");
	private static final TestType TTFooBar = new TestType("Foo", "Bar");
	private static final TestType TTBazBingo = new TestType("Baz", "Bingo");
	private static final TestType TTBarSEE = new TestType("Bar", "Something Else Entirely");
	private static final TestType TTBarBaz = new TestType("Bar", "Baz");

	final static List<TestType> testTypes = Arrays.asList(new TestType("Hello", "World"), TTFooBar,
			TTBarBaz, TTBarSEE, TTBazBingo, new TestType("Spleen", "Liver"));

	protected ObjectRepository or;

	@Before public void setUp() throws Exception
	{
		for (TestType t : testTypes)
			or.save(t);

		or.saveAll(AT5Cats, AT6Dogs);
	}

	@Test public void retreevHelloEqualsBar() throws QueryException
	{
		ListAccum<TestType> la = makeListAccum();
		or.from(TestType.class).where(new TestType().Hello).equals("Bar").select(la);
		assertSetEquals(Arrays.asList(TTBarBaz, TTBarSEE), la);
	}

	@Test public void retreevLikeB() throws QueryException
	{
		ListAccum<TestType> la = makeListAccum();
		or.from(TestType.class).where(new TestType().World).like("B%").select(la);
		assertSetEquals(Arrays.asList(TTBarBaz, TTBazBingo, TTFooBar), la);
	}

	@Test public void retrieveAll() throws QueryException
	{
		ListAccum<TestType> la = makeListAccum();
		or.from(TestType.class).select(la);
		assertSetEquals(testTypes, la);
	}

	@Test public void selectAllAnother() throws QueryException
	{
		ListAccum<AnotherTestType> la = makeListAccum();
		or.from(AnotherTestType.class).select(la);
		assertSetEquals(Arrays.asList(AT5Cats, AT6Dogs), la);
	}

	@Test public void selectId5() throws QueryException
	{
		ListAccum<AnotherTestType> la = makeListAccum();
		or.from(AnotherTestType.class).where(new AnotherTestType().Number).equals(5).select(la);
		assertSetEquals(Arrays.asList(AT5Cats), la);
	}

	@Test public void addMany() throws QueryException, ODBException
	{
		final int nums = 500000;
		for (long i = 0; i < nums; ++i)
			or.save(new TestType(String.valueOf(i), ""));
		final Property<String> hello = new TestType().Hello;
		assertEquals(nums/10, or.from(TestType.class).where(hello).like("%1").select().size());
	}

	static <T> void assertSetEquals(Collection<? extends T> expected, Collection<? extends T> actual)
	{
		assertEquals(new HashSet<T>(expected), new HashSet<T>(actual));
	}

	static <T> void assertSetEquals(Collection<? extends T> expected, ListAccum<? extends T> actual)
	{
		assertEquals(new HashSet<T>(expected), new HashSet<T>(actual.l));
	}

	protected <T> ListAccum<T> makeListAccum()
	{
		return new ListAccum<T>();
	}
}
