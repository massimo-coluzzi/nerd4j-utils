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
 * Represents an immutable and comparable triple of values.
 *
 * <p>
 * The values of the triple can be mutable but the relation,
 * created by this class, among three instances cannot change.
 *
 * <p>
 * The values of the triple are addressed as:
 * {@code left}, {@code middle} and {@code right}.
 *
 * <p>
 * This class implements {@link Comparable}
 * so either the values are {@link Comparable} or a
 * suitable {@link Comparator} should be provided.
 *  
 * <p>
 * This class implements {@link Emptily}.
 * A triple is considered to be empty if both
 * of its values are {@code null}.
 *
 * <p>
 * There are several factory methods that cover all the
 * most common use cases but, if you need to se up a custom
 * combination of {@code comparable} and {@code non comparable}
 * values, you can use the {@link ComparableTriple.MiddleBuilder}
 * amd {@link ComparableTriple.FinalBuilder}.
 *
 * @param <L> type of the {@code left}   element.
 * @param <M> type of the {@code middle} element.
 * @param <R> type of the {@code right}  element.
 * 
 * @author Massimo Coluzzi
 * @since 2.0.0
 */
public class ComparableTriple<L,M,R>
       extends Triple<L,M,R>
       implements Comparative<ComparableTriple<L,M,R>>
{

	/** Il serial version UUID. */
	private static final long serialVersionUID = 1L;
	
	/** Singleton instance of an empty triple. */
	private static final ComparableTriple<?,?,?> EMPTY
	= new ComparableTriple<>(
		null, CU.nullFirstNaturalOrderComparator(),
		null, CU.nullFirstNaturalOrderComparator(),
		null, CU.nullFirstNaturalOrderComparator()
	);

	
	/** The comparator for the left value. */
	private Comparator<L> leftComparator;
	
	/** The comparator for the middle value. */
	private Comparator<M> middleComparator;
	
	/** The comparator for the right value. */
	private Comparator<R> rightComparator;


	/*
	 * IMPORTANT NOTE
	 *
	 * There is no default constructor for this
	 * class because this class cannot be serialized!
	 *
	 * When serialized and de-serialized again, in the
	 * best case a missing-no-arg constructor exception
	 * will be thrown for the Comparators.NullComparator,
	 * in the worst case the original triple and the
	 * de-serialized one will be similar but slightly
	 * different and this may generate sneaky bugs.
	 *
	 * If you need to use triples in objects intended to
	 * be serialized use the parent class Triple.
	 * To compare those classes you can write your own
	 * comparator or you can use the factory methods
	 * to convert them into ComparableTriples when needed.
	 */

	/**
	 * Constructor with parameters.
	 * <p>
	 * This constructor is intended to be used
	 * by extending classes only.
	 * <p>
	 * To create a new triple use the factory methods.
	 *
	 * @param left             the {@code left} element of the triple.
	 * @param leftComparator   the {@link Comparator} for the left element.
	 * @param middle           the {@code middle} element of the triple.
	 * @param middleComparator the {@link Comparator} for the middle element.
	 * @param right            the {@code right} element of the triple.
	 * @param rightComparator  the {@link Comparator} for the right element.
	 */
	protected ComparableTriple( L left,   Comparator<L> leftComparator,
								M middle, Comparator<M> middleComparator,
								R right,  Comparator<R> rightComparator )
	{

		super( left, middle, right );
		
		this.leftComparator   = Require.nonNull( leftComparator,   "The comparator for the left value is mandatory" );
		this.rightComparator  = Require.nonNull( rightComparator,  "The comparator for the right value is mandatory" );
		this.middleComparator = Require.nonNull( middleComparator, "The comparator for the middle value is mandatory" );

	}


	/* ***************** */
	/*  FACTORY METHODS  */
	/* ***************** */


	/**
	 * Returns the singleton instance of an empty {@link ComparableTriple}.
	 *
	 * @param <L> type of the element {@code left}.
	 * @param <M> type of the element {@code middle}.
	 * @param <R> type of the element {@code right}.
	 * @return singleton instance of an empty {@link ComparableTriple}.
	 */
	@SuppressWarnings("unchecked")
	public static <L,M,R> ComparableTriple<L,M,R> empty()
	{

		return (ComparableTriple<L,M,R>) EMPTY;

	}

	/**
	 * Creates a new {@link ComparableTriple} with
	 * the given {@link Comparable} values.
	 * <p>
	 * This implementation considers {@code null} to be
     * less than {@code non-null}. Using this method is
     * the same as invoking
     * {@code ComparableTriple.of(left,false,middle,false,right,false)}.
	 *
	 * @param <L> type of the {@code left}   element.
	 * @param <M> type of the {@code middle} element.
	 * @param <R> type of the {@code right}  element.
	 * @param left   the {@code left}   element of the triple.
	 * @param middle the {@code middle} element of the triple.
	 * @param right  the {@code right}  element of the triple.
	 * @return a new comparable triple with the given values.
	 */
	public static <L extends Comparable<L>,M extends Comparable<M>,R extends Comparable<R>>
	ComparableTriple<L,M,R> of( L left, M middle, R right	)
	{

		return of( left, false, middle, false, right, false );
		
	}

	/**
	 * Creates a new {@link ComparableTriple} with
	 * the given {@link Comparable} values.
	 * <p>
	 * This implementation considers {@code null} to be
	 * less than {@code non-null}. Using this method is
	 * the same as invoking
	 * {@code ComparableTriple.of(left,false,middle,false,right,false)}.
	 * <p>
	 * This factory method is {@code null-safe}, if the
	 * given triple is {@code null}, then this method will
	 * return {@code null}.
	 *
	 * @param <L> type of the {@code left} element.
	 * @param <M> type of the {@code middle} element.
	 * @param <R> type of the {@code right} element.
	 * @param triple the source triple to make {@link Comparable}.
	 * @return a new comparable triple with the given values.
	 */
	public static <L extends Comparable<L>,M extends Comparable<M>,R extends Comparable<R>>
	ComparableTriple<L,M,R> of( Triple<L,M,R> triple	)
	{

		return triple != null
		? of( triple.left, false, triple.middle, false, triple.right, false )
		: null;

	}
	
	/**
	 * Creates a new {@link ComparableTriple} with
	 * the given values.
	 *
	 * @param <L> type of the {@code left}   element.
	 * @param <M> type of the {@code middle} element.
	 * @param <R> type of the {@code right}  element.
	 * @param left             the {@code left} element of the triple.
	 * @param leftNullLast    tells to consider {@code null} to be greater than {@code non-null}.
	 * @param middle           the {@code middle} element of the triple.
	 * @param middleNullLast  tells to consider {@code null} to be greater than {@code non-null}.
	 * @param right            the {@code right} element of the triple.
	 * @param rightNullLast   tells to consider {@code null} to be greater than {@code non-null}.
	 * @return a new comparable triple with the given values.
	 */
	public static <L extends Comparable<L>,M extends Comparable<M>,R extends Comparable<R>>
	ComparableTriple<L,M,R> of( L left,   boolean leftNullLast,
								M middle, boolean middleNullLast,
							    R right,  boolean rightNullLast )
	{
		
		return of(
			left,   CU.nullSafeNaturalOrderComparator(leftNullLast),
			middle, CU.nullSafeNaturalOrderComparator(middleNullLast),
			right,  CU.nullSafeNaturalOrderComparator(rightNullLast)
		);
		
	}


	/**
	 * Creates a new {@link ComparableTriple} starting
	 * from the given {@link Triple} of {@link Comparable}
	 * values and applies the given behavior for {@code null}
	 * values.
	 * <p>
	 * This factory method is {@code null-safe}, if the
	 * given triple is {@code null}, then this method will
	 * return {@code null}.
	 *
	 * @param <L> type of the {@code left}   element.
	 * @param <M> type of the {@code middle} element.
	 * @param <R> type of the {@code right}  element.
	 * @param triple the source triple to make {@link Comparable}.
	 * @param leftNullLast   tells to consider {@code null} to be greater than {@code non-null}.
	 * @param middleNullLast tells to consider {@code null} to be greater than {@code non-null}.
	 * @param rightNullLast  tells to consider {@code null} to be greater than {@code non-null}.
	 * @return a new comparable triple with the given values.
	 */
	public static <L extends Comparable<L>,M extends Comparable<M>,R extends Comparable<R>>
	ComparableTriple<L,M,R> of( Triple<L,M,R> triple, boolean leftNullLast,
								boolean middleNullLast, boolean rightNullLast )
	{

		return triple != null
		? of( triple.left,   CU.nullSafeNaturalOrderComparator(leftNullLast),
			  triple.middle, CU.nullSafeNaturalOrderComparator(middleNullLast),
			  triple.right,  CU.nullSafeNaturalOrderComparator(rightNullLast) )
		: null;

	}
	
	/**
	 * Creates a new {@link ComparableTriple} with
	 * the given values.
	 *
	 * @param <L> type of the {@code left}   element.
	 * @param <M> type of the {@code middle} element.
	 * @param <R> type of the {@code right}  element.
	 * @param left             the {@code left} element of the triple.
	 * @param leftComparator   comparator for values of type {@code <L>}.
	 * @param middle           the {@code middle} element of the triple.
	 * @param middleComparator comparator for values of type {@code <M>}.
	 * @param right            the {@code right} element of the triple.
	 * @param rightComparator  comparator for values of type {@code <R>}.
	 * @return a new comparable triple with the given values.
	 */
	public static <L,M,R>	ComparableTriple<L,M,R> of( L left,   Comparator<L> leftComparator,
														M middle, Comparator<M> middleComparator,
														R right, Comparator<R> rightComparator )
	{
		
		return left == null && middle == null &&right == null
		? empty()
		: new ComparableTriple<>(
			left,   leftComparator,
			middle, middleComparator,
			right,  rightComparator
		);
				
	}

	/**
	 * Creates a new {@link ComparableTriple} starting
	 * from the given {@link Triple} of {@link Comparable}
	 * values and applies the given value comparators.
	 * <p>
	 * This factory method is {@code null-safe}, if the
	 * given triple is {@code null}, then this method will
	 * return {@code null}.
	 *
	 * @param <L> type of the {@code left}   element.
	 * @param <M> type of the {@code middle} element.
	 * @param <R> type of the {@code right}  element.
	 * @param triple the source triple to make {@link Comparable}.
	 * @param leftComparator   comparator for values of type {@code <L>}.
	 * @param middleComparator comparator for values of type {@code <M>}.
	 * @param rightComparator  comparator for values of type {@code <R>}.
	 * @return a new comparable triple with the given values.
	 */
	public static <L,M,R> ComparableTriple<L,M,R> of( Triple<L,M,R> triple,
													  Comparator<L> leftComparator,
  													  Comparator<M> middleComparator,
													  Comparator<R> rightComparator )
	{

		return triple != null
		? new ComparableTriple<>( triple.left, leftComparator,
				  			 	  triple.middle, middleComparator,
								  triple.right,  rightComparator )
		: null;

	}


	/**
	 * Creates a new {@link ComparableTriple.MiddleBuilder} with
	 * the given {@code left} value.
	 * <p>
	 * This implementation considers {@code null} to be
	 * less than {@code non-null}. Using this method is
	 * the same as invoking
	 * {@code ComparableTriple.left(value,false)}.
	 *
	 * @param <L> type of the {@code left}   element.
	 * @param value the {@code left} element of the triple.
	 * @return a new {@link ComparableTriple.MiddleBuilder}
	 */
	public static <L extends Comparable<L>> ComparableTriple.MiddleBuilder<L> withLeft( L value	)
	{

		return withLeft( value, false );

	}

	/**
	 * Creates a new {@link ComparableTriple.MiddleBuilder} with
	 * the given {@code left} values.
	 *
	 * @param <L> type of the {@code left}   element.
	 * @param value     the {@code left} element of the triple.
	 * @param nullLast tells to consider {@code null} to be greater than {@code non-null}.
	 * @return a new {@link ComparableTriple.MiddleBuilder}
	 */
	public static <L extends Comparable<L>> ComparableTriple.MiddleBuilder<L> withLeft( L value, boolean nullLast )
	{

		return withLeft( value, CU.nullSafeNaturalOrderComparator(nullLast) );

	}

	/**
	 * Creates a new {@link ComparableTriple.MiddleBuilder} with
	 * the given {@code left} values.
	 *
	 * @param <L> type of the {@code left}   element.
	 * @param value      the {@code left} element of the triple.
	 * @param comparator comparator for values of type {@code <L>}.
	 * @return a new {@link ComparableTriple.MiddleBuilder}
	 */
	public static <L>	ComparableTriple.MiddleBuilder<L> withLeft( L value, Comparator<L> comparator )
	{

		return new MiddleBuilder<>( value, comparator );

	}


	/* ********************* */
	/*   INTERFACE METHODS   */
	/* ********************* */


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo( ComparableTriple<L,M,R> other )
	{

		final int left = leftComparator.compare( this.getLeft(), other.getLeft() );
		if( left != 0 ) return left;
		
		final int middle = middleComparator.compare( this.getMiddle(), other.getMiddle() );
		if( middle != 0 ) return middle;
		
		return rightComparator.compare( this.getRight(), other.getRight() );

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
	 * Returns the comparator for values of type {@code <M>}.
	 *
	 * @return comparator for values of type {@code <M>}.
	 */
	public Comparator<M> getMiddleComparator()
	{
		return middleComparator;
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
	 * to create {@link ComparableTriple}s with custom combination of
	 * {@code comparable} and {@code non comparable} values.
	 *
	 * <p>
	 * This builder implements the intermediate step of setting the
	 * {@code middle} value.
	 *
	 * @param <L> type of the {@code left} element.
	 *
	 * @author Massimo Coluzzi
	 * @since 2.0.0
	 */
	public static class MiddleBuilder<L>
	{

		/** The {@code left} value of the triple. */
		private L left;

		/** Comparator for the {@code left} value of the triple. */
		private Comparator<L> leftComparator;


		/**
		 * Constructor with parameters.
		 * <p>
		 * This constructor is intended to be used
		 * internaly of by extending classes.
		 *
		 * @param left            the {@code left} element of the triple.
		 * @param leftComparator  the {@link Comparator} for the left element.
		 */
		protected MiddleBuilder( L left, Comparator<L> leftComparator )
		{

			super();

			this.left = left;
			this.leftComparator = leftComparator;

		}


		/* **************** */
		/*  PUBLIC METHODS  */
		/* **************** */


		/**
		 * Sets the {@code middle} value of a {@link ComparableTriple}
		 * and returns a {@link ComparableTriple.FinalBuilder}.
		 * <p>
		 * This implementation considers {@code null} to be
		 * less than {@code non-null}. Using this method is
		 * the same as invoking
		 * {@code MiddleBuilder.middle(value,false)}.
		 *
		 * @param <M> type of the {@code middle} element.
		 * @param value the {@code middle} element of the triple.
		 */
		<M extends Comparable<M>> FinalBuilder<L,M> withMiddle( M value	)
		{

			return withMiddle( value, false );

		}

		/**
		 * Sets the {@code middle} value of a {@link ComparableTriple}
		 * and returns a {@link ComparableTriple.FinalBuilder}.
		 *
		 * @param <M> type of the {@code middle} element.
		 * @param value     the {@code right} element of the triple.
		 * @param nullLast tells to consider {@code null} to be greater than {@code non-null}.
		 */
		<M extends Comparable<M>> FinalBuilder<L,M> withMiddle( M value, boolean nullLast )
		{

			return withMiddle( value, CU.nullSafeNaturalOrderComparator(nullLast) );

		}

		/**
		 * Sets the {@code middle} value of a {@link ComparableTriple}
		 * and returns a {@link ComparableTriple.FinalBuilder}.
		 *
		 * @param <M> type of the {@code middle} element.
		 * @param value      the {@code right} element of the triple.
		 * @param comparator comparator for values of type {@code <R>}.
		 */
		<M> FinalBuilder<L,M> withMiddle( M value, Comparator<M> comparator )
		{

			return new FinalBuilder<>( left, leftComparator, value, comparator );

		}

	}


	/**
	 * Implementation of the {@code pattern builder} intended to be used
	 * to create {@link ComparableTriple}s with custom combination of
	 * {@code comparable} and {@code non comparable} values.
	 *
	 * <p>
	 * This builder implements the final step of setting the
	 * {@code right} value and creating the {@code ComparableTriple}.
	 *
	 * @param <L> type of the {@code left} element.
	 * @param <M> type of the {@code middle} element.
	 *
	 * @author Massimo Coluzzi
	 * @since 2.0.0
	 */
	public static class FinalBuilder<L,M>
	{

		/** The {@code left} value of the triple. */
		private L left;

		/** Comparator for the {@code left} value of the triple. */
		private Comparator<L> leftComparator;

		/** The {@code middle} value of the triple. */
		private M middle;

		/** Comparator for the {@code middle} value of the triple. */
		private Comparator<M> middleComparator;


		/**
		 * Constructor with parameters.
		 * <p>
		 * This constructor is intended to be used
		 * internaly of by extending classes.
		 *
		 * @param left             the {@code left} element of the triple.
		 * @param leftComparator   the {@link Comparator} for the left element.
		 * @param middle           the {@code middle} element of the triple.
		 * @param middleComparator the {@link Comparator} for the middle element.
		 */
		protected FinalBuilder( L left, Comparator<L> leftComparator, M middle, Comparator<M> middleComparator )
		{

			super();

			this.left = left;
			this.middle = middle;

			this.leftComparator = leftComparator;
			this.middleComparator = middleComparator;

		}


		/* **************** */
		/*  PUBLIC METHODS  */
		/* **************** */


		/**
		 * Creates a new {@link ComparableTriple} with the given {@code right} value.
		 * <p>
		 * This implementation considers {@code null} to be
		 * less than {@code non-null}. Using this method is
		 * the same as invoking
		 * {@code FinalBuilder.right(value,false)}.
		 *
		 * @param <R> type of the {@code right} element.
		 * @param value the {@code right} element of the triple.
		 */
		<R extends Comparable<R>> ComparableTriple<L,M,R> andRight( R value	)
		{

			return andRight( value, false );

		}

		/**
		 * Creates a new {@link ComparableTriple} with
		 * the given {@code right} values.
		 *
		 * @param <R>       type of the {@code right} element.
		 * @param value     the {@code right} element of the triple.
		 * @param nullLast tells to consider {@code null} to be greater than {@code non-null}.
		 */
		<R extends Comparable<R>> ComparableTriple<L,M,R> andRight( R value, boolean nullLast )
		{

			return andRight( value, CU.nullSafeNaturalOrderComparator(nullLast) );

		}


		/**
		 * Creates a new {@link ComparablePair} with
		 * the given {@code right} values.
		 *
		 * @param <R> type of the {@code right} element.
		 * @param value      the {@code right} element of the triple.
		 * @param comparator comparator for values of type {@code <R>}.
		 */
		<R> ComparableTriple<L,M,R> andRight( R value, Comparator<R> comparator )
		{

			return of( left, leftComparator, middle, middleComparator, value, comparator );

		}

	}

}
