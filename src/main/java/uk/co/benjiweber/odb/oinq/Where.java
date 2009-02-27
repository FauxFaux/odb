/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.benjiweber.odb.oinq;

import uk.co.benjiweber.odb.Property;

/**
 *
 * @author benji
 */
public interface Where<T>
{
	public Condition<T> where(Property<?> property) throws QueryException;
}
