package uk.co.benjiweber.odb.oinq;

import java.util.List;

/**
 *
 * @author benji
 */
public interface Select<T> 
{
	public List<T> select() throws QueryException;
	public void select(Use<T> user) throws QueryException;
}
