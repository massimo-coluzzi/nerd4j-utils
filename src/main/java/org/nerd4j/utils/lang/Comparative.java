/*-
 * #%L
 * Nerd4j Utils
 * %%
 * Copyright (C) 2011 - 2020 Nerd4j
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
package org.nerd4j.utils.lang;


/**
 * The aim of this interface is to add
 * some convenience method to the
 * {@link java.lang.Comparable} interface.
 *
 * <p>
 * To check if a {@link Comparable} value
 * is greater than another you need to write:
 * <pre>
 * if( a.compareTo(b) &gt; 0 )
 *   // Do something...
 * </pre>
 *
 * <p>
 * This type of statements is long to write,
 * can make the code difficult to understand
 * and is prone to errors.
 *
 * <p>
 * This interface adds the following default
 * methods:
 * <ul>
 *  <li>lt: stands for "less than"</li>
 *  <li>le: stands for "less or equal"</li>
 *  <li>eq: stands for "is equal to"</li>
 *  <li>ne: stands for "is not equal to"</li>
 *  <li>ge: stands for "greater or equal"</li>
 *  <li>gt: stands for "greater than"</li>
 * </ul>
 *
 * <p>
 * This way the same statement will become
 * <pre>
 * if( a.gt(b) )
 *   // Do something...
 * </pre>
 *
 * @param <T> the type of objects that this object may be compared to.
 *
 * @author Massimo Coluzzi
 * @since 2.0.0
 */
public interface Comparative<T> extends Comparable<T>
{


    /**
     * Tells if this object is less than the given object.
     *
     * @param other  the object to be compared.
     * @return {@code true} it {@code this} is less than {@code other}.
     * @throws NullPointerException if {@code other} is {@code null}.
     */

    default boolean lt( T other )
    {

        return compareTo( other ) < 0;

    }

    /**
     * Tells if this object is less or equal than the given object.
     *
     * @param other  the object to be compared.
     * @return {@code true} it {@code this} is less or equal than {@code other}.
     * @throws NullPointerException if {@code other} is {@code null}.
     */

    default boolean le( T other )
    {

        return compareTo( other ) <= 0;

    }

    /**
     * Tells if this object is equal to the given object.
     *
     * @param other  the object to be compared.
     * @return {@code true} it {@code this} is equal to {@code other}.
     * @throws NullPointerException if {@code other} is {@code null}.
     */

    default boolean eq( T other )
    {

        return compareTo( other ) == 0;

    }

    /**
     * Tells if this object is not equal to the given object.
     *
     * @param other  the object to be compared.
     * @return {@code true} it {@code this} is not equal to {@code other}.
     * @throws NullPointerException if {@code other} is {@code null}.
     */

    default boolean ne( T other )
    {

        return compareTo( other ) != 0;

    }

    /**
     * Tells if this object is greater or equal than the given object.
     *
     * @param other  the object to be compared.
     * @return {@code true} it {@code this} is greater or equal than {@code other}.
     * @throws NullPointerException if {@code other} is {@code null}.
     */

    default boolean ge( T other )
    {

        return compareTo( other ) >= 0;

    }

    /**
     * Tells if this object is greater than the given object.
     *
     * @param other  the object to be compared.
     * @return {@code true} it {@code this} is greater than {@code other}.
     * @throws NullPointerException if {@code other} is {@code null}.
     */

    default boolean gt( T other )
    {

        return compareTo( other ) > 0;

    }

}
