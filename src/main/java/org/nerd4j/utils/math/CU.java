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
package org.nerd4j.utils.math;

import org.nerd4j.utils.lang.Comparative;

import java.util.Comparator;

/**
 * This utility class provides some convenience methods
 * do deal with existing legacy classes that implement
 * {@link java.lang.Comparable}.
 * 
 * <p>
 * The name of this class is an abbreviation of
 * Comparable Utilities. We have intentionally used
 * a short name for readability because this class
 * is intended to be used in comparison statements
 * so that you can write something like
 * <pre>
 * if( CU.lt(a,b) )
 *   // Do something...
 * </pre>
 * instead of
 * <pre>
 * if( a.compareTo(b) &lt; 0 )
 *   // Do something...
 * </pre>
 * 
 * If you are dealing with existing {@code comparable}
 * classes like {@link java.lang.String} or {@link java.lang.Integer}
 * this class will facilitate the writing of comparison statements.
 * 
 * <p>
 * If you are writing a new {@code comparable} class we
 * suggest to implement {@link Comparative} instead.
 * This will allow you to implement {@link Comparable}
 * and to have the convenience methods provided by this
 * class directly on your {@code comparable} object.
 *
 * 
 * @see org.nerd4j.utils.lang.Comparative
 * @author Massimo Coluzzi
 * @since 2.0.0
 */
public class CU
{
	
	/** Natural order comparator that considers {@code null} to be less than {@code non-null}. */
	private static final Comparator<?> NULLS_FIRST_NATURAL_ORDER
	= Comparator.nullsFirst( Comparator.naturalOrder() );

	/** Natural order comparator that considers {@code null} to be greater than {@code non-null}. */
	private static final Comparator<?> NULLS_LAST_NATURAL_ORDER
	= Comparator.nullsLast( Comparator.naturalOrder() );

	
	/**
	 * This class is intended to be static
	 * so there is no public constructor.
	 */
	CU() {}


	/* **************** */
	/*  PUBLIC METHODS  */
	/* **************** */

	
	/**
	 * Returns a {@code null-safe} natural ordered comparator
	 * considering {@code null} to be less than {@code non-null}.
	 *
	 * @param <T> type of object to compare.
	 * @return a {@code null-safe} natural ordered comparator.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Comparator<T> nullFirstNaturalOrderComparator()
	{

		return (Comparator<T>) NULLS_FIRST_NATURAL_ORDER;

	}

	/**
	 * Returns a {@code null-safe} natural ordered comparator
	 * considering {@code null} to be greater than {@code non-null}.
	 *
	 * @param <T> type of object to compare.
	 * @return a {@code null-safe} natural ordered comparator.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Comparator<T> nullLastNaturalOrderComparator()
	{
		
		return (Comparator<T>) NULLS_LAST_NATURAL_ORDER;
		
	}
	
	/**
	 * Returns a {@code null-safe} natural ordered comparator
	 * considering {@code null} to be less than or greater than
	 * {@code non-null} based on the value of the parameter.
	 *
	 * @param <T> type of object to compare.
	 * @param nullLast tells to consider {@code null} to be greater than {@code non-null}.
	 * @return a {@code null-safe} natural ordered comparator.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Comparator<T> nullSafeNaturalOrderComparator( boolean nullLast )
	{

		return nullLast
		? (Comparator<T>) NULLS_LAST_NATURAL_ORDER
		: (Comparator<T>) NULLS_FIRST_NATURAL_ORDER;

	}


	/*  LESS THAN  */

    /**
     * This is a {@code null-safe} method that, given two {@code comparable}
	 * values {@code a} and {@code b}, tells if {@code a < b}.
	 * <p>
	 * This method considers {@code null} to be less than {@code non-null}.
	 * <p>
	 * This method is the same as {@code CU.lt(a,b,false)}.
     *
     * @param <C> type of the {@code comparable} values.
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code true} if {@code a < b}.
     */
    public static <C extends Comparable<C>> boolean lt( C a, C b )
    {

        return nullFirstNaturalOrderComparator().compare( a, b ) < 0;

    }

    /**
     * This is a {@code null-safe} method that, given two {@code comparable}
	 * values {@code a} and {@code b}, tells if {@code a < b}.
	 * <p>
	 * This method considers {@code null} to be less than or greater
	 * than {@code non-null} based on the value of {@code nullLast}.
	 *
	 * @param <C> type of the {@code comparable} values.
	 * @param a value to compare.
	 * @param b value to compare.
	 * @param nullLast tells if {@code null} is greater than {@code non-null}.
	 * @return {@code true} if {@code a < b}.
     */
    public static <C extends Comparable<C>> boolean lt( C a, C b, boolean nullLast )
    {

    	final Comparator<C> comparator = nullSafeNaturalOrderComparator( nullLast );
        return comparator.compare( a, b ) < 0;

    }


	/*  LESS OR EQUAL  */

    /**
     * This is a {@code null-safe} method that, given two {@code comparable}
	 * values {@code a} and {@code b}, tells if {@code a <= b}.
	 * <p>
	 * This method considers {@code null} to be less than {@code non-null}.
	 * <p>
	 * This method is the same as {@code CU.le(a,b,false)}.
     *
     * @param <C> type of the {@code comparable} values.
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code true} if {@code a <= b}.
     */
    public static <C extends Comparable<C>> boolean le( C a, C b )
    {

        return nullFirstNaturalOrderComparator().compare( a, b ) <= 0;

    }

    /**
     * This is a {@code null-safe} method that, given two {@code comparable}
	 * values {@code a} and {@code b}, tells if {@code a <= b}.
	 * <p>
	 * This method considers {@code null} to be less than or greater
	 * than {@code non-null} based on the value of {@code nullLast}.
	 *
	 * @param <C> type of the {@code comparable} values.
	 * @param a value to compare.
	 * @param b value to compare.
	 * @param nullLast tells if {@code null} is greater than {@code non-null}.
	 * @return {@code true} if {@code a <= b}.
     */
    public static <C extends Comparable<C>> boolean le( C a, C b, boolean nullLast )
    {

    	final Comparator<C> comparator = nullSafeNaturalOrderComparator( nullLast );
        return comparator.compare( a, b ) <= 0;

    }


	/*  EQUAL  */

    /**
     * This is a {@code null-safe} method that, given two {@code comparable}
	 * values {@code a} and {@code b}, tells if {@code a == b}.
	 * <p>
	 * This method considers {@code null} to be less than {@code non-null}.
	 * <p>
	 * This method is the same as {@code CU.eq(a,b,false)}.
     *
     * @param <C> type of the {@code comparable} values.
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code true} if {@code a == b}.
     */
    public static <C extends Comparable<C>> boolean eq( C a, C b )
    {

        return nullFirstNaturalOrderComparator().compare( a, b ) == 0;

    }

    /**
     * This is a {@code null-safe} method that, given two {@code comparable}
	 * values {@code a} and {@code b}, tells if {@code a == b}.
	 * <p>
	 * This method considers {@code null} to be less than or greater
	 * than {@code non-null} based on the value of {@code nullLast}.
	 *
	 * @param <C> type of the {@code comparable} values.
	 * @param a value to compare.
	 * @param b value to compare.
	 * @param nullLast tells if {@code null} is greater than {@code non-null}.
	 * @return {@code true} if {@code a == b}.
     */
    public static <C extends Comparable<C>> boolean eq( C a, C b, boolean nullLast )
    {

    	final Comparator<C> comparator = nullSafeNaturalOrderComparator( nullLast );
        return comparator.compare( a, b ) == 0;

    }


	/*  NOT EQUAL  */

    /**
     * This is a {@code null-safe} method that, given two {@code comparable}
	 * values {@code a} and {@code b}, tells if {@code a != b}.
	 * <p>
	 * This method considers {@code null} to be less than {@code non-null}.
	 * <p>
	 * This method is the same as {@code CU.ne(a,b,false)}.
     *
     * @param <C> type of the {@code comparable} values.
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code true} if {@code a != b}.
     */
    public static <C extends Comparable<C>> boolean ne( C a, C b )
    {

        return nullFirstNaturalOrderComparator().compare( a, b ) != 0;

    }

    /**
     * This is a {@code null-safe} method that, given two {@code comparable}
	 * values {@code a} and {@code b}, tells if {@code a != b}.
	 * <p>
	 * This method considers {@code null} to be less than or greater
	 * than {@code non-null} based on the value of {@code nullLast}.
	 *
	 * @param <C> type of the {@code comparable} values.
	 * @param a value to compare.
	 * @param b value to compare.
	 * @param nullLast tells if {@code null} is greater than {@code non-null}.
	 * @return {@code true} if {@code a != b}.
     */
    public static <C extends Comparable<C>> boolean ne( C a, C b, boolean nullLast )
    {

    	final Comparator<C> comparator = nullSafeNaturalOrderComparator( nullLast );
        return comparator.compare( a, b ) != 0;

    }


	/*  GREATER OR EQUAL  */

    /**
     * This is a {@code null-safe} method that, given two {@code comparable}
	 * values {@code a} and {@code b}, tells if {@code a >= b}.
	 * <p>
	 * This method considers {@code null} to be less than {@code non-null}.
	 * <p>
	 * This method is the same as {@code CU.ge(a,b,false)}.
     *
     * @param <C> type of the {@code comparable} values.
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code true} if {@code a >= b}.
     */
    public static <C extends Comparable<C>> boolean ge( C a, C b )
    {

        return nullFirstNaturalOrderComparator().compare( a, b ) >= 0;

    }

    /**
     * This is a {@code null-safe} method that, given two {@code comparable}
	 * values {@code a} and {@code b}, tells if {@code a >= b}.
	 * <p>
	 * This method considers {@code null} to be less than or greater
	 * than {@code non-null} based on the value of {@code nullLast}.
	 *
	 * @param <C> type of the {@code comparable} values.
	 * @param a value to compare.
	 * @param b value to compare.
	 * @param nullLast tells if {@code null} is greater than {@code non-null}.
	 * @return {@code true} if {@code a >= b}.
     */
    public static <C extends Comparable<C>> boolean ge( C a, C b, boolean nullLast )
    {

    	final Comparator<C> comparator = nullSafeNaturalOrderComparator( nullLast );
        return comparator.compare( a, b ) >= 0;

    }


	/*  GREATER THAN  */

    /**
     * This is a {@code null-safe} method that, given two {@code comparable}
	 * values {@code a} and {@code b}, tells if {@code a > b}.
	 * <p>
	 * This method considers {@code null} to be less than {@code non-null}.
	 * <p>
	 * This method is the same as {@code CU.gt(a,b,false)}.
     *
     * @param <C> type of the {@code comparable} values.
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code true} if {@code a > b}.
     */
    public static <C extends Comparable<C>> boolean gt( C a, C b )
    {

        return nullFirstNaturalOrderComparator().compare( a, b ) > 0;

    }

    /**
     * This is a {@code null-safe} method that, given two {@code comparable}
	 * values {@code a} and {@code b}, tells if {@code a > b}.
	 * <p>
	 * This method considers {@code null} to be less than or greater
	 * than {@code non-null} based on the value of {@code nullLast}.
	 *
	 * @param <C> type of the {@code comparable} values.
	 * @param a value to compare.
	 * @param b value to compare.
	 * @param nullLast tells if {@code null} is greater than {@code non-null}.
	 * @return {@code true} if {@code a > b}.
     */
    public static <C extends Comparable<C>> boolean gt( C a, C b, boolean nullLast )
    {

    	final Comparator<C> comparator = nullSafeNaturalOrderComparator( nullLast );
        return comparator.compare( a, b ) > 0;

    }


    /*  MIN  */

	/**
	 * This is a {@code null-safe} method that, given two {@code comparable}
	 * values {@code a} and {@code b}, returns the minimum value between them.
	 * If the values are equal returns {@code a}.
	 * <p>
	 * This method considers {@code null} to be less than {@code non-null}.
	 * <p>
	 * This method is the same as {@code CU.min(a,b,false)}.
	 *
	 * @param <C> type of the {@code comparable} values.
	 * @param a value to compare.
	 * @param b value to compare.
	 * @return the minimum between {@code a} and {@code b}.
	 */
	public static <C extends Comparable<C>> C min( C a,  C b )
	{

		return le( a, b ) ? a : b;

	}

	/**
	 * This is a {@code null-safe} method that, given two {@code comparable}
	 * values {@code a} and {@code b}, returns the minimum value between them.
	 * If the values are equal returns {@code a}.
	 * <p>
	 * This method considers {@code null} to be less than or greater
	 * than {@code non-null} based on the value of {@code nullLast}.
	 *
	 * @param <C> type of the {@code comparable} values.
	 * @param a value to compare.
	 * @param b value to compare.
	 * @param nullLast tells if {@code null} is greater than {@code non-null}.
	 * @return the minimum between {@code a} and {@code b}.
	 */
	public static <C extends Comparable<C>> C min( C a,  C b, boolean nullLast )
	{

		return le( a, b, nullLast ) ? a : b;

	}


    /*  MAX  */

	/**
	 * This is a {@code null-safe} method that, given two {@code comparable}
	 * values {@code a} and {@code b}, returns the maximum value between them.
	 * If the values are equal returns {@code a}.
	 * <p>
	 * This method considers {@code null} to be less than {@code non-null}.
	 * <p>
	 * This method is the same as {@code CU.max(a,b,false)}.
	 *
	 * @param <C> type of the {@code comparable} values.
	 * @param a value to compare.
	 * @param b value to compare.
	 * @return the maximum between {@code a} and {@code b}.
	 */
	public static <C extends Comparable<C>> C max( C a,  C b )
	{

		return ge( a, b ) ? a : b;

	}

	/**
	 * This is a {@code null-safe} method that, given two {@code comparable}
	 * values {@code a} and {@code b}, returns the maximum value between them.
	 * If the values are equal returns {@code a}.
	 * <p>
	 * This method considers {@code null} to be less than or greater
	 * than {@code non-null} based on the value of {@code nullLast}.
	 *
	 * @param <C> type of the {@code comparable} values.
	 * @param a value to compare.
	 * @param b value to compare.
	 * @param nullLast tells if {@code null} is greater than {@code non-null}.
	 * @return the maximum between {@code a} and {@code b}.
	 */
	public static <C extends Comparable<C>> C max( C a,  C b, boolean nullLast )
	{

		return ge( a, b, nullLast ) ? a : b;

	}

}
