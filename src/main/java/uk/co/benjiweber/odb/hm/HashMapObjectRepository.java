package uk.co.benjiweber.odb.hm;

import java.lang.reflect.Field;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import uk.co.benjiweber.odb.ODBException;
import uk.co.benjiweber.odb.ObjectRepository;
import uk.co.benjiweber.odb.Property;
import uk.co.benjiweber.odb.oinq.Condition;
import uk.co.benjiweber.odb.oinq.QueryException;
import uk.co.benjiweber.odb.oinq.Select;
import uk.co.benjiweber.odb.oinq.SelectOrWhere;
import uk.co.benjiweber.odb.oinq.Use;

public class HashMapObjectRepository implements ObjectRepository
{
	private static interface Inclusor
	{
		boolean isIncluded(Object ob);
	}

	protected Map<Class<?>, Set<Object>> a = Collections
			.synchronizedMap(new HashMap<Class<?>, Set<Object>>());

	@Override public <T> SelectOrWhere<T> from(final Class<T> type) throws QueryException
	{
		return new SelectOrWhere<T>()
		{
			@Override public List<T> select() throws QueryException
			{
				return asList(getVals());
			}

			@SuppressWarnings("unchecked") private Collection<T> getVals()
			{
				final Collection<T> vals = (Collection<T>) a.get(type);
				if (null != vals)
					return vals;
				return makeEmpty();
			}

			private Collection<T> makeEmpty()
			{
				return new AbstractCollection<T>()
				{
					private Iterator<T> it = new Iterator<T>()
					{
						@Override public boolean hasNext()
						{
							return false;
						}

						@Override public T next()
						{
							throw new IllegalStateException();
						}

						@Override public void remove()
						{
							throw new UnsupportedOperationException();
						}
					};

					@Override public Iterator<T> iterator()
					{
						return it;
					}

					@Override public int size()
					{
						return 0;
					}
				};
			}

			private List<T> asList(final Collection<? extends T> vals)
			{
				return Collections.unmodifiableList(new ArrayList<T>(vals));
			}

			@Override public void select(Use<T> user) throws QueryException
			{
				for (T t : select())
					user.use(t);
			}

			@Override public Condition<T> where(final Property<?> prop) throws QueryException
			{
				return new Condition<T>()
				{
					private Collection<T> filter(Collection<T> source, Inclusor inc) throws QueryException
					{
						Field f;
						try
						{
							f = type.getField(prop.Name);
						}
						catch (SecurityException e)
						{
							throw new QueryException(e);

						}
						catch (NoSuchFieldException e)
						{
							throw new QueryException(e);
						}
						final Collection<T> c = new ArrayList<T>();
						for (T t : getVals())
							try
							{
								if (inc.isIncluded(f.get(t)))
									c.add(t);
							}
							catch (IllegalArgumentException e)
							{
								throw new QueryException(e);
							}
							catch (IllegalAccessException e)
							{
								throw new QueryException(e);
							}
						return c;
					}

					private Select<T> selectFor(final Collection<T> coll)
					{
						return new Select<T>()
						{
							@Override public List<T> select() throws QueryException
							{
								return asList(coll);
							}

							@Override public void select(Use<T> user) throws QueryException
							{
								for (T t : select())
									user.use(t);
							}
						};
					}

					@Override public Select<T> equals(final String value) throws QueryException
					{
						return selectFor(filter(getVals(), new Inclusor()
						{

							@Override public boolean isIncluded(Object ob)
							{
								return value.equals(ob);
							}
						}));
					}

					@Override public Select<T> equals(int value) throws QueryException
					{
						final Integer i = Integer.valueOf(value);
						return selectFor(filter(getVals(), new Inclusor()
						{

							@Override public boolean isIncluded(Object ob)
							{
								return i.equals(ob);
							}

						}));
					}

					@Override public Select<T> like(String value) throws QueryException
					{
						final Pattern reg = Pattern.compile(value.replaceAll("([^a-zA-Z0-9])", "\\\\$1")
								.replaceAll("\\%", ".*").replaceAll("\\_", "."));

						return selectFor(filter(getVals(), new Inclusor()
						{
							@Override public boolean isIncluded(Object ob)
							{
								return reg.matcher(String.valueOf(ob)).matches();
							}
						}));
					}

				};
			}
		};

	}

	@Override public void save(Object o) throws ODBException
	{
		getSet(o).add(o);
	}

	private synchronized Set<Object> getSet(Object o)
	{
		Set<Object> w;
		final Class<? extends Object> cla££ = o.getClass();
		w = a.get(cla££);

		if (w == null)
			a.put(cla££, w = new HashSet<Object>());

		return w;
	}

	@Override public void saveAll(Object... os) throws ODBException
	{
		for (Object o : os)
			save(o);
	}

	@Override public void saveAll(Collection<Object> os) throws ODBException
	{
		for (Object o : os)
			save(o);
	}

}
