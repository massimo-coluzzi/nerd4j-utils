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
 * Represents an immutable triple of values.
 *
 * <p>
 * The values of the triple can be mutable but the association,
 * created by this class, among three instances cannot change.
 *
 * <p>
 * The values of the triple are addressed as:
 * {@code left}, {@code middle} and {@code right}.
 *
 *  <p>
 *  This implementation does't make any assumption
 *  on the nature of the values.
 *  There is an extension of {@link Triple} that
 *  requires the provided values to be {@link Comparable}
 *  and therefore is able to implement the {@link Comparable}
 *  interface for itself.
 *
 *  <p>
 *  This class implements {@link Emptily}.
 *  A triple is considered to be empty if all
 *  of its values are {@code null}.
 *
 * @param <L> type of the {@code left} element.
 * @param <M> type of the {@code middle} element.
 * @param <R> type of the {@code right} element.
 * 
 * @author Massimo Coluzzi
 * @since 2.0.0
 */
public class Triple<L,M,R> implements Serializable, Emptily
{

	/** Il serial version UUID. */
	private static final long serialVersionUID = 1L;

	/** Singleton instance of an empty triple. */
	private static final Triple<?,?,?> EMPTY = new Triple<>();


	/** The {@code left} element  of the triple. */
	protected final L left;

	/** The {@code middle} element  of the triple. */
	protected final M middle;

	/** The {@code right} element  of the triple. */
	protected final R right;


	/**
	 * Default constructor.
	 * <p>
	 * This constructor is intended to be used
	 * by reflection during deserialization.
	 * <p>
	 * To create a new triple use
	 * the factory method.
	 */
	protected Triple()
	{

		super();

		this.left = null;
		this.right = null;
		this.middle = null;

	}

	/**
	 * Constructor with parameters.
	 * <p>
	 * This constructor is intended to be used
	 * by extending classes only.
	 * <p>
	 * To create a new triple use
	 * the factory methods.
	 *
	 * @param left    the {@code left}   element of the triple.
	 * @param middle  the {@code middle} element of the triple.
	 * @param right   thr {@code right}  element of the triple.
	 */
	protected Triple( L left, M middle, R right )
	{

		super();

		this.left = left;
		this.right = right;
		this.middle = middle;

	}


	/* ***************** */
	/*  FACTORY METHODS  */
	/* ***************** */


	/**
	 * Returns the singleton instance of an empty {@link Triple}.
	 *
	 * @param <L> type of the {@code left}   element.
	 * @param <M> type of the {@code middle} element.
	 * @param <R> type of the {@code right}  element.
	 * @return singleton instance of an empty {@link Triple}.
	 */
	@SuppressWarnings("unchecked")
	public static <L,M,R> Triple<L,M,R> empty()
	{

		return (Triple<L,M,R>) EMPTY;

	}

	/**
	 * Creates a new {@link Triple} with
	 * the given values.
	 *
	 * @param <L> type of the {@code left}   element.
	 * @param <M> type of the {@code middle} element.
	 * @param <R> type of the {@code right}  element.
	 * @param left    the {@code left}   element of the triple.
	 * @param middle  the {@code middle} element of the triple.
	 * @param right   thr {@code right}  element of the triple.
	 * @return a new triple with the given values.
	 */
	public static <L,M,R> Triple<L,M,R> of(L left, M middle, R right )
	{

		return left == null && middle == null && right == null
		? empty()
		: new Triple<>( left, middle, right );

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

		return left == null && middle == null && right == null;

	}


	/* *********************** */
	/*  CONFIGURATION METHODS  */
	/* *********************** */


	/**
	 * Returns the {@code left} element of the triple.
	 *
	 * @return {@code left} element of the triple.
	 */
	public L getLeft()
	{
		return left;
	}

	/**
	 * Returns the {@code middle} element of the triple.
	 *
	 * @return {@code middle} element of the triple.
	 */
	public M getMiddle()
	{
		return middle;
	}

	/**
	 * Returns the {@code right} element of the triple.
	 *
	 * @return {@code right} element of the triple.
	 */
	public R getRight()
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

		return Hashcode.of( left, middle, right );

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
			o -> o.middle,
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
					   .print( middle )
					   .print( right )
					   .withNoClassName()
					   .likeTuple();

	}

}
