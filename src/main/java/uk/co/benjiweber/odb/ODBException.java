package uk.co.benjiweber.odb;

/**
 *
 * @author benji
 */
public class ODBException extends Exception
{
	public ODBException()
	{
		super();
	}

	public ODBException(String message)
	{
		super(message);
	}

	public ODBException(String message, Throwable t)
	{
		super(message,t);
	}

	public ODBException(Throwable t)
	{
		super(t);
	}
}
