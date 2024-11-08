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

import org.nerd4j.utils.lang.Comparative;
import org.nerd4j.utils.lang.Emptily;
import org.nerd4j.utils.lang.Require;
import org.nerd4j.utils.math.CU;

import java.util.Comparator;


/**
 * Represents an immutable and comparable pair of values.
 *
 * <p>
 * The values of the pair can be mutable but the relation,
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
 * <p>
 * This class implements {@link Comparable}
 * so either the values are {@link Comparable} or a
 * suitable {@link Comparator} should be provided.
 *  
 * <p>
 * This class implements {@link Emptily}.
 * A pair is considered to be empty if both
 * of its values are {@code null}.
 *
 * <p>
 * There are several factory methods that cover all the
 * most common use cases but, if you need to se up a custom
 * combination of {@code comparable} and {@code non comparable}
 * values, you can use the {@link ComparablePair.Builder}.
 *
 * @param <L> type of the {@code left} element.
 * @param <R> type of the {@code right} element.
 * 
 * @author Massimo Coluzzi
 * @since 2.0.0
 */
public class ComparablePair<L,R>
       extends Pair<L,R>
       implements Comparative<ComparablePair<L,R>>
{

	/** Il serial version UUID. */
	private static final long serialVersionUID = 1L;

	/** Singleton instance of an empty pair. */
	private static final ComparablePair<?,?> EMPTY
	= new ComparablePair<>(
		null, CU.nullFirstNaturalOrderComparator(),
		null, CU.nullFirstNaturalOrderComparator()
	);

	
	/** The comparator for the left value. */
	private Comparator<L> leftComparator;
	
	/** The comparator for the right value. */
	private Comparator<R> rightComparator;
	
	
	/*
	 * IMPORTANT NOTE
	 * 
	 * There is no default constructor for this
	 * class because this class cannot be serialized!
	 * 
	 * When serialized and deserialized again, in the
	 * best case a missing-no-arg constructor exception
	 * will be thrown for the Comparators.NullComparator,
	 * in the worst case the original pair and the
	 * deserialized one will be similar but slightly
	 * different and this may generate sneaky bugs.
	 * 
	 * If you need to use pairs in objects intended to
	 * be serialized use the parent class Pair.
	 * To compare those classes you can write your own
	 * comparator or you can use the factory methods
	 * to convert them into ComparablePairs when needed.
	 */


	/**
	 * Constructor with parameters.
	 * <p>
	 * This constructor is intended to be used
	 * by extending classes only.
	 * <p>
	 * To create a new pair use the factory methods.
	 *
	 * @param left            the {@code left} element of the pair.
	 * @param leftComparator  the {@link Comparator} for the left element.
	 * @param right           the {@code right} element of the pair.
	 * @param rightComparator the {@link Comparator} for the right element.
	 */
	protected ComparablePair( L left,  Comparator<L> leftComparator,
			                  R right, Comparator<R> rightComparator )
	{

		super( left, right );
		
		this.leftComparator  = Require.nonNull( leftComparator,  "The comparator for the left value is mandatory" );
		this.rightComparator = Require.nonNull( rightComparator, "The comparator for the right value is mandatory" );

	}


	/* ***************** */
	/*  FACTORY METHODS  */
	/* ***************** */


	/**
	 * Returns the singleton instance of an empty {@link ComparablePair}.
	 *
	 * @param <L> type of the element {@code left}.
	 * @param <R> type of the element {@code right}.
	 * @return singleton instance of an empty {@link ComparablePair}.
	 */
	@SuppressWarnings("unchecked")
	public static <L,R> ComparablePair<L,R> empty()
	{

		return (ComparablePair<L,R>) EMPTY;

	}

	/**
	 * Creates a new {@link ComparablePair} with
	 * the given {@link Comparable} values.
	 * <p>
	 * This implementation considers {@code null} to be
     * less than {@code non-null}. Using this method is
     * the same as invoking
     * {@code ComparablePair.of(left,false,right,false)}.
	 *
	 * @param <L> type of the {@code left} element.
	 * @param <R> type of the {@code right} element.
	 * @param left  the {@code left} element of the pair.
	 * @param right the {@code right} element of the pair.
	 * @return a new comparable pair with the given values.
	 */
	public static <L extends Comparable<L>,R extends Comparable<R>>
	ComparablePair<L,R> of( L left, R right	)
	{

		return of( left, false, right, false );
		
	}
	
	/**
	 * Creates a new {@link ComparablePair} starting
	 * from the given {@link Pair} of {@link Comparable}
	 * values.
	 * <p>
	 * This implementation considers {@code null} to be
	 * less than {@code non-null}. Using this method is
	 * the same as invoking
	 * {@code ComparablePair.of(pair,false,false)}.
	 * <p>
	 * This factory method is {@code null-safe}, if the
	 * given pair is {@code null}, then this method will
	 * return {@code null}. 
	 *
	 * @param <L> type of the {@code left} element.
	 * @param <R> type of the {@code right} element.
	 * @param pair the source pair to make {@link Comparable}.
	 * @return a new comparable pair with the given values.
	 */
	public static <L extends Comparable<L>,R extends Comparable<R>>
	ComparablePair<L,R> of( Pair<L,R> pair	)
	{
		
		return pair != null
		? of( pair.left, false, pair.right, false )
		: null;
		
	}
	
	/**
	 * Creates a new {@link ComparablePair} with
	 * the given values.
	 *
	 * @param <L> type of the {@code left} element.
	 * @param <R> type of the {@code right} element.
	 * @param left           the {@code left} element of the pair.
	 * @param leftNullLast  tells to consider {@code null} to be greater than {@code non-null}.
	 * @param right          the {@code right} element of the pair.
	 * @param rightNullLast tells to consider {@code null} to be greater than {@code non-null}.
	 * @return a new comparable pair with the given values.
	 */
	public static <L extends Comparable<L>,R extends Comparable<R>>
	ComparablePair<L,R> of( L left,  boolean leftNullLast,
							R right, boolean rightNullLast	)
	{
		
		return of(
			left,  CU.nullSafeNaturalOrderComparator(leftNullLast),
			right, CU.nullSafeNaturalOrderComparator(rightNullLast)
		);
		
	}
	
	/**
	 * Creates a new {@link ComparablePair} starting
	 * from the given {@link Pair} of {@link Comparable}
	 * values and applies the given behavior for {@code null}
	 * values.
	 * <p>
	 * This factory method is {@code null-safe}, if the
	 * given pair is {@code null}, then this method will
	 * return {@code null}. 
	 *
	 * @param <L> type of the {@code left} element.
	 * @param <R> type of the {@code right} element.
	 * @param pair the source pair to make {@link Comparable}.
	 * @param leftNullLast  tells to consider {@code null} to be greater than {@code non-null}.
	 * @param rightNullLast tells to consider {@code null} to be greater than {@code non-null}.
	 * @return a new comparable pair with the given values.
	 */
	public static <L extends Comparable<L>,R extends Comparable<R>>
	ComparablePair<L,R> of( Pair<L,R> pair, boolean leftNullLast, boolean rightNullLast )
	{
		
		return pair != null
		? of( pair.left,  CU.nullSafeNaturalOrderComparator(leftNullLast),
			  pair.right, CU.nullSafeNaturalOrderComparator(rightNullLast) )
		: null;
		
	}
	
	/**
	 * Creates a new {@link ComparablePair} with
	 * the given values.
	 *
	 * @param <L> type of the {@code left} element.
	 * @param <R> type of the {@code right} element.
	 * @param left            the {@code left} element of the pair.
	 * @param leftComparator  comparator for values of type {@code <L>}.
	 * @param right           the {@code right} element of the pair.
	 * @param rightComparator comparator for values of type {@code <R>}.
	 * @return a new comparable pair with the given values.
	 */
	public static <L,R>	ComparablePair<L,R> of( L left,  Comparator<L> leftComparator,
												R right, Comparator<R> rightComparator )
	{
		
		return left == null && right == null
		? empty()
		: new ComparablePair<>(
			left,  leftComparator,
			right, rightComparator
		);
				
	}
	
	/**
	 * Creates a new {@link ComparablePair} starting
	 * from the given {@link Pair} of {@link Comparable}
	 * values and applies the given value comparators.
	 * <p>
	 * This factory method is {@code null-safe}, if the
	 * given pair is {@code null}, then this method will
	 * return {@code null}.
	 *
	 * @param <L> type of the {@code left} element.
	 * @param <R> type of the {@code right} element.
	 * @param pair the source pair to make {@link Comparable}.
	 * @param leftComparator  comparator for values of type {@code <L>}.
	 * @param rightComparator comparator for values of type {@code <R>}.
	 * @return a new comparable pair with the given values.
	 */
	public static <L,R>	ComparablePair<L,R> of( Pair<L,R> pair,
			                                    Comparator<L> leftComparator,
			                                    Comparator<R> rightComparator )
	{
		
		return pair != null
		? of( pair.left,  leftComparator,
			  pair.right, rightComparator )
		: null;
				
	}


	/**
	 * Creates a new {@link ComparablePair.Builder} with
	 * the given {@code left} value.
	 * <p>
	 * This implementation considers {@code null} to be
	 * less than {@code non-null}. Using this method is
	 * the same as invoking
	 * {@code ComparablePair.left(value,false)}.
	 *
	 * @param <L> type of the {@code left} element.
	 * @param value the {@code left} element of the pair.
	 * @return a new {@link ComparablePair.Builder}
	 */
	public static <L extends Comparable<L>> ComparablePair.Builder<L> withLeft( L value	)
	{

		return withLeft( value, false );

	}

	/**
	 * Creates a new {@link ComparablePair.Builder} with
	 * the given {@code left} values.
	 *
	 * @param <L> type of the {@code left} element.
	 * @param value     the {@code left} element of the pair.
	 * @param nullLast tells to consider {@code null} to be greater than {@code non-null}.
	 * @return a new {@link ComparablePair.Builder}
	 */
	public static <L extends Comparable<L>> ComparablePair.Builder<L> withLeft( L value, boolean nullLast )
	{

		return withLeft( value, CU.nullSafeNaturalOrderComparator(nullLast) );

	}


	/**
	 * Creates a new {@link ComparablePair.Builder} with
	 * the given {@code left} values.
	 *
	 * @param <L> type of the {@code left} element.
	 * @param value      the {@code left} element of the pair.
	 * @param comparator comparator for values of type {@code <L>}.
	 * @return a new {@link ComparablePair.Builder}
	 */
	public static <L>	ComparablePair.Builder<L> withLeft( L value, Comparator<L> comparator )
	{

		return new Builder<>( value, comparator );

	}


	/* ********************* */
	/*   INTERFACE METHODS   */
	/* ********************* */


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo( ComparablePair<L,R> other )
	{

		final int value = leftComparator.compare( this.left, other.left );
		return value != 0 ? value
		: rightComparator.compare( this.right, other.right );

	}


	/* *********************** */
	/*  CONFIGURATION METHODS  */
	/* *********************** */

	
	/**
	 * Returns the comparator for values of type {@code <L>}.
	 * 
	 * @return comparator for values of type {@code <L>}.
	 */
	public Comparator<L> getLeftComparator()
	{
		return leftComparator;
	}

	/**
	 * Returns the comparator for values of type {@code <R>}.
	 * 
	 * @return comparator for values of type {@code <R>}.
	 */
	public Comparator<R> getRightComparator()
	{
		return rightComparator;
	}


	/* ***************** */
	/*   INNER CLASSES   */
	/* ***************** */


	/**
	 * Implementation of the {@code pattern builder} intended to be used
	 * to create {@link ComparablePair}s with custom combination of
	 * {@code comparable} and {@code non comparable} values.
	 *
	 * @param <L> type of the {@code left} element.
	 *
	 * @author Massimo Coluzzi
	 * @since 2.0.0
	 */
	public static class Builder<L>
	{

		/** The {@code left} value of the pair. */
		private L left;

		/** Comparator for the {@code left} value of the pair. */
		private Comparator<L> leftComparator;

		/**
		 * Constructor with parameters.
		 * <p>
		 * This constructor is intended to be used
		 * internaly of by extending classes.
		 *
		 * @param left            the {@code left} element of the pair.
		 * @param leftComparator  the {@link Comparator} for the left element.
		 */
		protected Builder( L left, Comparator<L> leftComparator )
		{

			super();

			this.left = left;
			this.leftComparator = leftComparator;

		}


		/* **************** */
		/*  PUBLIC METHODS  */
		/* **************** */


		/**
		 * Creates a new {@link ComparablePair} with
		 * the given {@code right} value.
		 * <p>
		 * This implementation considers {@code null} to be
		 * less than {@code non-null}. Using this method is
		 * the same as invoking
		 * {@code Builder.right(value,false)}.
		 *
		 * @param <R> type of the {@code right} element.
		 * @param value the {@code right} element of the pair.
		 * @return a new comparable pair with the given values.
		 */
		public <R extends Comparable<R>> ComparablePair<L,R> andRight( R value	)
		{

			return andRight( value, false );

		}

		/**
		 * Creates a new {@link ComparablePair} with
		 * the given {@code right} values.
		 *
		 * @param <R> type of the {@code right} element.
		 * @param value     the {@code right} element of the pair.
		 * @param nullLast tells to consider {@code null} to be greater than {@code non-null}.
		 * @return a new comparable pair with the given values.
		 */
		public <R extends Comparable<R>> ComparablePair<L,R> andRight( R value, boolean nullLast )
		{

			return andRight( value, CU.nullSafeNaturalOrderComparator(nullLast) );

		}


		/**
		 * Creates a new {@link ComparablePair} with
		 * the given {@code right} values.
		 *
		 * @param <R> type of the {@code right} element.
		 * @param value      the {@code right} element of the pair.
		 * @param comparator comparator for values of type {@code <R>}.
		 * @return a new comparable pair with the given values.
		 */
		public <R> ComparablePair<L,R> andRight( R value, Comparator<R> comparator )
		{

			return of( left, leftComparator, value, comparator );

		}

	}

}
