/*
 * #%L
 * Nerd4j Core
 * %%
 * Copyright (C) 2011 - 2013 Nerd4j
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
package org.nerd4j.utils.math;


/**
 * This exception is thrown by the method {@link Interval#unify(Interval)}
 * when trying to unify two {@link Interval}s that cannot be unified.
 * <p>
 * For example the union of {@code [0,1]} and {@code [2,3]} is not an interval.
 *
 * @author Massimo Coluzzi
 * @since 2.0.0
 */
public class NoSuchIntervalException extends RuntimeException
{

	/** Generated Serial Version UID. */
	private static final long serialVersionUID = 1L;


	/**
	 * Constructs a new exception to report a failed union between
	 * the two provided intervals.
	 *
	 * @param a interval to unify.
	 * @param b interval to unify.
	 * @param op symbol of the operation involved, can be one between unify and subtract.
	 */
    public NoSuchIntervalException( Interval<?> a, Interval<?> b, String op )
    {

        super( a + op + b + " is not an interval" );

    }

}
