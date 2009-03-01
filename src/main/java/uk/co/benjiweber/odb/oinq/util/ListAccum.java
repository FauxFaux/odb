package uk.co.benjiweber.odb.oinq.util;

import java.util.ArrayList;
import java.util.List;

import uk.co.benjiweber.odb.oinq.Use;

public class ListAccum<T> implements Use<T>
{
	public final List<T> l = new ArrayList<T>();
	@Override public void use(T t)
	{
		l.add(t);
	}
}
