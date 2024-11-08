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


import org.nerd4j.utils.lang.*;


/**
 * This class is intended to be used inside the {@link Interval} class.
 * It is an internal support class with the aim to keep the
 * {@link Interval} class clean and easy to test.
 *
 * @param <T> type of comparable elements of the interval.
 *
 * @author Massimo Coluzzi
 * @since 2.0.0
 */
class IntervalLimit<T extends Comparable<T>>
implements Comparative<IntervalLimit<T>>
{

	/** Singleton instance of an unbounded inferior limit. */
	private static final IntervalLimit<?> UNBOUNDED_INF
	= new IntervalLimit<>( null, Type.UNBOUNDED, Side.INF );

	/** Singleton instance of an unbounded inferior limit. */
	private static final IntervalLimit<?> UNBOUNDED_SUP
	= new IntervalLimit<>( null, Type.UNBOUNDED, Side.SUP );


	/** The type of interval limit. */
	private final Type type;

	/** The side of the interval limit. */
	private final Side side;

	/** Value of the interval limit, {@code null} if unbounded. */
	final T value;


	/**
	 * Default constructor.
	 * <p>
	 * This constructor is intended to be used
	 * by reflection during de-serialization.
	 * <p>
	 * To create a new interval limit
	 * use factory methods.
	 */
	protected IntervalLimit()
	{

		super();

		this.type = null;
		this.side = null;
		this.value = null;

	}


	/**
	 * Constructor with parameters.
	 * <p>
	 * This constructor is intended to be used
	 * by extending classes only.
	 * <p>
	 * To create a new interval limit
	 * use the factory methods.
	 *
	 * @param value the limit value, {@code null} if unbounded.
	 * @param type  type of the interval limit.
	 * @param side  side of the interval limit.
	 */
	protected IntervalLimit( T value, Type type, Side side )
	{

		super();

		this.type  = Require.nonNull( type, "The interval limit type is mandatory" );
		this.side  = Require.nonNull( side, "The interval limit side is mandatory" );
		this.value = value;

	}


	/* ***************** */
	/*  FACTORY METHODS  */
	/* ***************** */


	/**
	 * Returns the singleton instance of an unbounded inferior {@link IntervalLimit}.
	 *
	 * @param <T> type of the elements in the interval.
	 * @return singleton instance of an unbounded inferior {@link IntervalLimit}.
	 */
	@SuppressWarnings("unchecked")
	static <T extends Comparable<T>> IntervalLimit<T> unboundedInf()
	{

		return (IntervalLimit<T>) UNBOUNDED_INF;

	}

	/**
	 * Returns the singleton instance of an unbounded superior {@link IntervalLimit}.
	 *
	 * @param <T> type of the elements in the interval.
	 * @return singleton instance of an unbounded superior {@link IntervalLimit}.
	 */
	@SuppressWarnings("unchecked")
	static <T extends Comparable<T>> IntervalLimit<T> unboundedSup()
	{

		return (IntervalLimit<T>) UNBOUNDED_SUP;

	}

	/**
	 * Creates a new open inferior {@link IntervalLimit} with the given value.
	 *
	 * @param <T> type of the elements in the interval.
	 * @param value value of the limit.
	 * @return new instance of an open inferior {@link IntervalLimit}.
	 * @throws RequirementFailure if the value is null.
	 */
	static <T extends Comparable<T>> IntervalLimit<T> openInf( T value )
	{

		return new IntervalLimit<>(
			Require.nonNull( value, "The value of the limit is mandatory" ),
			Type.OPEN, Side.INF
		);

	}
	/**
	 * Creates a new open superior {@link IntervalLimit} with the given value.
	 *
	 * @param <T> type of the elements in the interval.
	 * @param value value of the limit.
	 * @return new instance of an open superior {@link IntervalLimit}.
	 * @throws RequirementFailure if the value is null.
	 */
	static <T extends Comparable<T>> IntervalLimit<T> openSup( T value )
	{

		return new IntervalLimit<>(
			Require.nonNull( value, "The value of the limit is mandatory" ),
			Type.OPEN, Side.SUP
		);

	}

	/**
	 * Creates a new closed inferior {@link IntervalLimit} with the given value.
	 *
	 * @param <T> type of the elements in the interval.
	 * @param value value of the limit.
	 * @return new instance of a closed inferior {@link IntervalLimit}.
	 * @throws RequirementFailure if the value is null.
	 */
	static <T extends Comparable<T>> IntervalLimit<T> closedInf( T value )
	{

		return new IntervalLimit<>(
			Require.nonNull( value, "The value of the limit is mandatory" ),
			Type.CLOSED, Side.INF
		);

	}

	/**
	 * Creates a new closed superior {@link IntervalLimit} with the given value.
	 *
	 * @param <T> type of the elements in the interval.
	 * @param value value of the limit.
	 * @return new instance of a closed superior {@link IntervalLimit}.
	 * @throws RequirementFailure if the value is null.
	 */
	static <T extends Comparable<T>> IntervalLimit<T> closedSup( T value )
	{

		return new IntervalLimit<>(
			Require.nonNull( value, "The value of the limit is mandatory" ),
			Type.CLOSED, Side.SUP
		);

	}


	/* ******************* */
	/*  INTERFACE METHODS  */
	/* ******************* */


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo( IntervalLimit<T> other )
	{

		/*
		 * If only one is unbounded than is always
		 * less than or greater than depending on
		 * the side it belongs.
		 * If both are unbounded need to be handled.
		 */
		if( isUnbounded() )
			return other.isUnbounded()
			? compareUnbounded( other )
			: (isInf() ? -1 : 1);

		if( other.isUnbounded() )
			return other.isInf() ? 1 : -1;

		/*
		 * If are both bounded we can compare
		 * the related values.
		 */
		final int valueCompare = value.compareTo( other.value );

		/*
		 * If are not equal than we are done,
		 * otherwise we need to check all the
		 * possible cases of* open and closed
		 * limits with the same value.
		 */
		return valueCompare == 0
		? compareBoundedWithSameValue( other )
		:  valueCompare;

	}


	/* ***************** */
	/*  DEFAULT METHODS  */
	/* ***************** */


	/**
	 * Tells if this is an inferior limit.
	 *
	 * @return {@code true} if this is an inferior limit.
	 */
	boolean isInf()
	{

		return side == Side.INF;

	}

	/**
	 * Tells if this is a superior limit.
	 *
	 * @return {@code true} if this is a superior limit.
	 */
	boolean isSup()
	{

		return side == Side.SUP;

	}

	/**
	 * Tells if the given limit is open.
	 *
	 * @return {@code true} if the given limit is open.
	 */
	boolean isOpen()
	{

		return value != null && type == Type.OPEN;

	}

	/**
	 * Tells if the given limit is closed.
	 *
	 * @return {@code true} if the given limit is closed.
	 */
	boolean isClosed()
	{

		return value != null && type == Type.CLOSED;

	}

	/**
	 * Tells if the given limit is unbounded.
	 *
	 * @return {@code true} if the given limit is unbounded.
	 */
	boolean isUnbounded()
	{

		return value == null && type == Type.UNBOUNDED;

	}


	/**
	 * Tells if the given value lays within this limit.
	 *
	 * @param value the value to check.
	 * @return {@code true} if the value lays within the limit.
	 */
	boolean contains( T value )
	{

		switch( type )
		{

			case OPEN:
				return isInf()
				? CU.gt( value, this.value )
				: CU.lt( value, this.value );

			case CLOSED:
				return isInf()
				? CU.ge( value, this.value )
				: CU.le( value, this.value );

			/* An unbounded limit contains every value. */
			default:
				return true;

		}

	}


	/* ***************** */
	/*  PRIVATE METHODS  */
	/* ***************** */


	/**
	 * This method assumes both limits to be unbounded
	 * and performs a comparison over unbounded limits.
	 *
	 * @param other the other limit to compare with {@code this}.
	 * @return a value as described in {@link Comparable#compareTo(Object)}.
	 */
	private int compareUnbounded( IntervalLimit<T> other )
	{

		/*
		 * If belongs both to the same side
		 * than have to be considered equal.
		 */
		if( this.side == other.side )
			return 0;

		/*
		 * Otherwise one is inf and the other sup.
		 * If this in inf than has to be considered
		 * less than other, otherwise has to be
		 * considered greater.
		 */
		return isInf() ? -1 : 1;

	}


	/**
	 * This method assumes both limits to be bounded
	 * and to have the same value.
	 *
	 * @param other the other limit to compare with {@code this}.
	 * @return a value as described in {@link Comparable#compareTo(Object)}.
	 */
	private int compareBoundedWithSameValue( IntervalLimit<T> other )
	{

		/*
		  Cases where the two limits are equal:
		  [val = [val
		  [val = val]
		  (val = (val
		  val) = val)
		  val] = [val
		  val] = val]
		 */

		/*
		 * If both belong to the same side
		 * and are of the same type than
		 * can be considered to be equal.
		 */
		if( this.type == other.type
		 && this.side == other.side )
			return 0;

		/*
		 * Considering that the value is the same
		 * if are both closed than can be considered
		 * to be equal regardless of the side each belongs.
		 */
		if( this.isClosed() && other.isClosed() )
			return 0;


		/*
		  Cases where this is less than other:
		  val) < [val
		  val) < (val
		  val) < val]
		  [val < (val
		  val] < (val
		 */

		/*
		 * If this is a superior open limit
		 * and is not equals to other than is
		 * less than other.
		 */
		if( this.isSup() && this.isOpen() )
			return -1;

		/*
		 * If other is an inferior open limit
		 * and is not equals to this than is
		 * greater than this.
		 */
		if( other.isInf() && other.isOpen() )
			return -1;


		/*
		  Cases where this is greater than other:
		  (val > [val
		  (val > val)
		  (val > val]
		  [val > val)
		  val] > val)
		 */

		/*
		 * These are all the remaining cases.
		 * If this is not equal to other and
		 * is not less then other, then must
		 * be greater. We don't need to actually
		 * perform any check.
		 */
		return 1;

	}


	/* *************** */
	/*  INNER CLASSES  */
	/* *************** */


	/**
	 * Enumerates the types of an interval limit.
	 *
	 * @since 2.0.0
	 * @author Massimo Coluzzi
	 */
	private enum Type
	{

		/** The limit is not part of the interval. */
		OPEN,

		/** The limit is part of the interval. */
		CLOSED,

		/** There is no limit, the interval includes all values. */
		UNBOUNDED

	}

	/**
	 * Enumerates the sidess of an interval limit.
	 *
	 * @since 2.0.0
	 * @author Massimo Coluzzi
	 */
	private enum Side
	{

		/** The inferior limit or lower bound. */
		INF,

		/** The superior limit or upper bound */
		SUP

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

		return Hashcode.of( value, type, side );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals( Object other )
	{

		return Equals.ifSameClass(
			this, other,
			o -> o.value,
			o -> o.type,
			o -> o.side
		);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{

		switch( type )
		{

		case OPEN:
			return isInf() ? "(" + value : value + ")";

		case CLOSED:
			return isInf() ? "[" + value : value + "]";

		default:
			return isInf() ? "(-\u221e" : "+\u221e)";

		}

	}

}
