/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.benjiweber.odb;


/**
 *
 * @author benji
 */
public class AnotherTestType
{
	private int number;
	private String name;

	public AnotherTestType() { }

	public AnotherTestType(int number, String name)
	{
		this.number = number;
		this.name = name;
	}

	public final Property<Integer> Number = new Property<Integer>("Number",ODBType.Integer)
	{
		@Override
		public Integer getValue()
		{
			return Integer.valueOf(number);
		}

		@Override
		public void setValue(Integer value)
		{
			number = value.intValue();
		}
	};

	public final Property<String> Name = new Property<String>("Name",ODBType.String)
	{
		@Override
		public String getValue()
		{
			return name;
		}

		@Override
		public void setValue(String value)
		{
			name = value;
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
		final AnotherTestType other = (AnotherTestType) obj;
		if (this.Number != other.Number && (this.Number == null || !this.Number.equals(other.Number)))
		{
			return false;
		}
		if (this.Name != other.Name && (this.Name == null || !this.Name.equals(other.Name)))
		{
			return false;
		}
		return true;
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 79 * hash + (this.Number != null ? this.Number.hashCode() : 0);
		hash = 79 * hash + (this.Name != null ? this.Name.hashCode() : 0);
		return hash;
	}

	@Override public String toString()
	{
		return "AnotherTestType[" + number + ":" + name + "]";
	}

}
