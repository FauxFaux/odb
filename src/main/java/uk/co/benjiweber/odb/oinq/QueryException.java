package uk.co.benjiweber.odb.oinq;

/**
 *
 * @author benji
 */
public class QueryException extends Exception
{
	public QueryException()
	{

	}

	public QueryException(String message)
	{
		super(message);
	}

	public QueryException(String message, Throwable t)
	{
		super(message,t);
	}

	public QueryException(Throwable t)
	{
		super(t);
	}
}
