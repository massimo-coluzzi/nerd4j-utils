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
package org.nerd4j.utils.tuple;

import org.nerd4j.utils.lang.Emptily;
import org.nerd4j.utils.lang.Equals;
import org.nerd4j.utils.lang.Hashcode;
import org.nerd4j.utils.lang.ToString;

import java.io.Serializable;


/**
 * Represents an immutable pair of values.
 *
 * <p>
 * The values of the pair can be mutable but the association,
 * created by this class, between two instances cannot change.
 *
 * <p>
 * The values of the pair can be addressed as:
 * <ul>
 *  <li>{@code left} and {@code right} or</li>
 *  <li>{@code key} and {@code value}</li>
 * </ul>
 * depending on the intended usage.
 *
 *  <p>
 *  This implementation does't make any assumption
 *  on the nature of the values.
 *  There is an extension of {@link Pair} that
 *  requires the provided values to be {@link Comparable}
 *  and therefore is able to implement the {@link Comparable}
 *  interface for itself.
 *
 *  <p>
 *  This class implements {@link Emptily}.
 *  A pair is considered to be empty if both
 *  of its values are {@code null}.
 *
 * @param <L> type of the {@code left} element.
 * @param <R> type of the {@code right} element.
 * 
 * @author Massimo Coluzzi
 * @since 2.0.0
 */
public class Pair<L,R> implements Serializable, Emptily
{

	/** Il serial version UUID. */
	private static final long serialVersionUID = 1L;

	/** Singleton instance of an empty pair. */
	private static final Pair<?,?> EMPTY = new Pair<>();


	/** The {@code left} element  of the pair. */
	protected final L left;

	/** The {@code right} element  of the pair. */
	protected final R right;


	/**
	 * Default constructor.
	 * <p>
	 * This constructor is intended to be used
	 * by reflection during de-serialization.
	 * <p>
	 * To create a new pair use
	 * the factory methods.
	 */
	protected Pair()
	{

		super();

		this.left  = null;
		this.right = null;

	}

	/**
	 * Constructor with parameters.
	 * <p>
	 * This constructor is intended to be used
	 * by extending classes only.
	 * <p>
	 * To create a new pair use
	 * the factory methods.
	 *
	 * @param left  the {@code left} element of the pair.
	 * @param right the {@code right} element of the pair.
	 */
	protected Pair( L left, R right )
	{

		super();

		this.left  = left;
		this.right = right;

	}


	/* ***************** */
	/*  FACTORY METHODS  */
	/* ***************** */


	/**
	 * Returns the singleton instance of an empty {@link Pair}.
	 *
	 * @param <L> type of the {@code left} element.
	 * @param <R> type of the {@code right} element.
	 * @return singleton instance of an empty {@link Pair}.
	 */
	@SuppressWarnings("unchecked")
	public static <L,R> Pair<L,R> empty()
	{

		return (Pair<L,R>) EMPTY;

	}

	/**
	 * Creates a new {@link Pair} with
	 * the given values.
	 *
	 * @param <L> type of the {@code left} element.
	 * @param <R> type of the {@code right} element.
	 * @param left  the {@code left} element of the pair.
	 * @param right the {@code right} element of the pair.
	 * @return a new pair with the given values.
	 */
	public static <L,R> Pair<L,R> of( L left, R right )
	{

		return left == null && right == null
		? empty()
		: new Pair<>( left, right );

	}


	/* ********************* */
	/*   INTERFACE METHODS   */
	/* ********************* */


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty()
	{

		return left == null && right == null;

	}


	/* *********************** */
	/*  CONFIGURATION METHODS  */
	/* *********************** */


	/**
	 * Returns the {@code left} element of the pair.
	 *
	 * @return {@code left} element of the pair.
	 */
	public L getLeft()
	{
		return left;
	}

	/**
	 * Returns the {@code right} element of the pair.
	 *
	 * @return {@code right} element of the pair.
	 */
	public R getRight()
	{
		return right;
	}

	/**
	 * Returns the {@code left} element of the pair.
	 *
	 * @return {@code left} element of the pair.
	 */
	public L getKey()
	{
		return left;
	}

	/**
	 * Returns the {@code right} element of the pair.
	 *
	 * @return {@code right} element of the pair.
	 */
	public R getValue()
	{
		return right;
	}


	/* ****************** */
	/*  OBJECT OVERRIDES  */
	/* ****************** */


    /**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{

		return Hashcode.of( left, right );

	}

	/**
	 * {@inheritDoc}
	 */
    @Override
	public boolean equals( Object other )
	{

		return Equals.ifSameClass(
			this, other,
			o -> o.left,
			o -> o.right
		);

	}

	/**
	 * {@inheritDoc}
	 */
    @Override
	public String toString()
	{

		return ToString.of( this )
				 	   .print( left )
					   .print( right )
					   .withNoClassName()
					   .likeTuple();

	}

}
