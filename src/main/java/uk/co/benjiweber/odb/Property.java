package uk.co.benjiweber.odb;

public abstract class Property<T>
{
	public final String Name;
	public final ODBType Type;

	public Property(String name, ODBType type)
	{
		this.Name = name;
		this.Type = type;
	}

	public abstract T getValue();
	
	public abstract void setValue(T value);

	@Override
	public String toString()
	{
		return getValue().toString();
	}

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
		final Property<T> other = (Property<T>) obj;
		if ((this.getValue() == null) ? (other.getValue() != null) : !this.getValue().equals(other.getValue()))
		{
			return false;
		}
		return true;
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 17 * hash + (this.getValue() != null ? this.getValue().hashCode() : 0);
		return hash;
	}
}
