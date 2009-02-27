package uk.co.benjiweber.odb.mysql;

import java.sql.ResultSet;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uk.co.benjiweber.odb.DBConnection;
import uk.co.benjiweber.odb.ODBException;
import uk.co.benjiweber.odb.ODBType;
import uk.co.benjiweber.odb.ObjectRepository;
import uk.co.benjiweber.odb.Property;
import uk.co.benjiweber.odb.oinq.Condition;
import uk.co.benjiweber.odb.oinq.QueryException;
import uk.co.benjiweber.odb.oinq.Select;
import uk.co.benjiweber.odb.oinq.SelectOrWhere;
import uk.co.benjiweber.odb.oinq.Use;
/**
 *
 * @author benji
 */
public class MySQLObjectRepository implements ObjectRepository
{
	private final DBConnection conn;

	private static final Map<ODBType,String> sqlTypes = new HashMap<ODBType,String>()
	{{
		put(ODBType.Integer,"INT");
		put(ODBType.String,"VARCHAR(1024)");
		put(ODBType.Other,"BLOB"); //xxx
	}};

	public MySQLObjectRepository(DBConnection conn)
	{
		this.conn = conn;
	}

	public void save(Object o) throws ODBException
	{
		try
		{
			createTableIfRequired(o);
			upsert(o);

		} catch (SQLException ex)
		{
			throw new ODBException(ex);
		}
	}

	public <T> SelectOrWhere<T> from(final Class<T> type) throws QueryException
	{
		return new SelectOrWhere<T>()
		{
			public List<T> select() throws QueryException
			{
				return selectAll(type);
			}

			public Condition<T> where(final Property<?> property) throws QueryException
			{
				return new Condition<T>()
				{
					public Select<T> equals(final String value) throws QueryException
					{
						return selectAllProperty(type,property,value," = ");
					}

					public Select<T> equals(int value) throws QueryException
					{
						return selectAllProperty(type,property,value," = ");
					}

					public Select<T> like(String value) throws QueryException
					{
						return selectAllProperty(type,property,value," LIKE ");
					}
				};
			}

			public void select(Use<T> user) throws QueryException
			{
				for (T t : select())
					user.use(t);
			}
		};
	}

	private void upsert(Object o) throws SQLException, ODBException
	{
		final String tableName = getTableName(o);

		StringBuilder sql = new StringBuilder("INSERT INTO ")
			.append(tableName)
			.append("(id,");

		List<Property> properties = getProperties(o);

		for (Property p : properties)
			sql.append(p.Name).append(",");

		sql.deleteCharAt(sql.length() -1);
		
		sql.append(") VALUES (").append(o.hashCode()).append(",");

		for (Property p : properties)
			sql.append("?").append(",");

		sql.deleteCharAt(sql.length() -1);

		sql.append(") ON DUPLICATE KEY UPDATE  ");

		for (Property p : properties)
			sql.append(p.Name).append("=?").append(",");

		sql.deleteCharAt(sql.length() -1);

		PreparedStatement stmt = conn.getConnection().prepareStatement(sql.toString());
		for (int i = 1; i <= properties.size(); i++)
		{
			stmt.setObject(i, properties.get(i-1).getValue());
			stmt.setObject(i + properties.size(), properties.get(i-1).getValue());
		}
		stmt.executeUpdate();
	}

	private void createTableIfRequired(Object o) throws SQLException, ODBException
	{
		final String tableName = getTableName(o);
		if (tableExists(tableName))
			return;

				StringBuilder sql = new StringBuilder()
			.append("CREATE TABLE ")
			.append(o.getClass().getSimpleName())
			.append("(id INT NOT NULL PRIMARY KEY,");
		for (Property prop : getProperties(o))
		{
			sql.append(prop.Name);
			sql.append(" ");
			sql.append(sqlTypes.get(prop.Type));
			sql.append(" ");
			sql.append("NOT NULL,");
		}
		sql.deleteCharAt(sql.length() -1);
		sql.append(")");

		conn.getConnection().prepareStatement(sql.toString()).execute();

	}


	private String getTableName(Object o)
	{
		return o.getClass().getSimpleName();
	}

	private boolean tableExists(String tableName) throws SQLException, ODBException
	{
		PreparedStatement stmt = conn.getConnection().prepareStatement("SELECT * FROM Information_schema.tables WHERE table_schema = ? AND table_name = ?");
		stmt.setString(1,"odb");
		stmt.setString(2, tableName);
		return stmt.executeQuery().next();
	}

	private List<Property> getProperties(Object o)
	{
		List<Property> properties = new ArrayList<Property>();
		for (Field field : o.getClass().getFields())
		{
			if (Property.class.isAssignableFrom(field.getType()))
			{
				try
				{
					properties.add((Property) field.get(o));
				} catch (IllegalArgumentException ex)
				{
					throw new RuntimeException(ex);
				} catch (IllegalAccessException ex)
				{
					throw new RuntimeException(ex);
				}
			}
		}
		return properties;
	}


	@SuppressWarnings("unchecked")
	private <T> T map(ResultSet results, Class<T> type) throws QueryException
	{
		try
		{
			T t = type.newInstance();
			for (Property p : getProperties(t))
				p.setValue(results.getObject(p.Name));
			return t;
		} catch (Exception ex)
		{
			throw new QueryException(ex);
		} 

	}

	private <T> List<T> selectAll(final Class<T> type) throws QueryException
	{
		try
		{
			List<T> all = new ArrayList<T>();
			PreparedStatement stmt = conn.getConnection().prepareStatement("SELECT * FROM " + type.getSimpleName());
			ResultSet results = stmt.executeQuery();
			while (results.next())
				all.add(map(results,type));

			return all;
		} catch (Exception e)
		{
			throw new QueryException(e);
		}
	}

	private <T> List<T> selectAllPropertyEqual(final Class<T> type, final Property prop, final Object value, final String operator) throws QueryException
	{
		try
		{
			List<T> all = new ArrayList<T>();
			PreparedStatement stmt = conn.getConnection().prepareStatement(new StringBuilder("SELECT * FROM ").append(type.getSimpleName()).append(" WHERE ").append(prop.Name).append(operator).append("?").toString());

			stmt.setObject(1, value);
			ResultSet results = stmt.executeQuery();
			while (results.next())
				all.add(map(results,type));

			return all;
		} catch (Exception e)
		{
			throw new QueryException(e);
		}
	}

	private <T> Select<T> selectAllProperty(final Class<T> type, final Property property, final Object value,final String operator) throws QueryException
	{
		return new Select<T>()
		{
			public List<T> select() throws QueryException
			{
				return selectAllPropertyEqual(type, property, value,operator);
			}

			public void select(Use<T> user) throws QueryException
			{
				for (T t : select())
					user.use(t);
			}
		};
	}

	public void saveAll(Object... os) throws ODBException
	{
		for(Object o : os)
			save(o);
	}

	public void saveAll(List<Object> os) throws ODBException
	{
		for(Object o : os)
			save(o);
	}


	
}
