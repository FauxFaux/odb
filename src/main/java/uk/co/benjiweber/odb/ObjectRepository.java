package uk.co.benjiweber.odb;

import java.util.Collection;

import uk.co.benjiweber.odb.oinq.QueryException;
import uk.co.benjiweber.odb.oinq.SelectOrWhere;

/**
 *
 * @author benji
 */
public interface ObjectRepository
{
	public void save(Object o) throws ODBException;
	public void saveAll(Object... os) throws ODBException;
	public void saveAll(Collection<Object> os) throws ODBException;
	public <T> SelectOrWhere<T> from(final Class<T> type) throws QueryException;
}
