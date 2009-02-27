/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.benjiweber.odb.oinq;

/**
 *
 * @author benji
 */
public interface Condition<T>
{
	public Select<T> equals(String value) throws QueryException;
	public Select<T> equals(int value) throws QueryException;

	public Select<T> like(String value) throws QueryException;
}
