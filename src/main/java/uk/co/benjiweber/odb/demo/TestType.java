package uk.co.benjiweber.odb.demo;

import uk.co.benjiweber.odb.Property;
import uk.co.benjiweber.odb.ODBType;

/**
 *
 * @author benji
 */
public class TestType
{
	private String hello;

	private String world;

	public TestType(String hello, String world)
	{
		this.hello = hello;
		this.world = world;
	}

	public TestType()
	{
		//Required by odb for now.
	}

	public final Property<String> Hello = new Property<String>("Hello", ODBType.String)
	{
		@Override public String getValue()
		{
			return hello;
		}

		@Override public void setValue(String value)
		{
			hello = value;
		}
	};

	public final Property<String> World = new Property<String>("World", ODBType.String)
	{
		@Override public String getValue()
		{
			return world;
		}

		@Override public void setValue(String value)
		{
			world = value;
		}
	};

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final TestType other = (TestType) obj;
		if (this.Hello != other.Hello && (this.Hello == null || !this.Hello.equals(other.Hello)))
		{
			return false;
		}
		if (this.World != other.World && (this.World == null || !this.World.equals(other.World)))
		{
			return false;
		}
		return true;
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 79 * hash + (this.Hello != null ? this.Hello.hashCode() : 0);
		hash = 79 * hash + (this.World != null ? this.World.hashCode() : 0);
		return hash;
	}


}
