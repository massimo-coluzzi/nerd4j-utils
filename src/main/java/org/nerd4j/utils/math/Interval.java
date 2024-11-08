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

import java.util.NoSuchElementException;

import static org.nerd4j.utils.math.CU.*;

/**
 * In mathematics, given a set {@code S} and a total order relation {@code <=}
 * over the elements of {@code S}, we define an interval {@code I} as a
 * subset of {@code S} such that: given {@code x,y in I such that x <= y} we have
 * that {@code for each s in S} if {@code x <= s <= y} then {@code s belongs to I}.
 *
 * <p>
 * An interval can be empty, it can have a lower bound value called
 * {@code inf}, it can have an upper bound value called {@code sup},
 * and it can also be unbounded. The upper and lower bounds can be included
 * in the interval, in this case we say that the interval is closed, or they may
 * be excluded from the interval, in this case we say that the interval is open.
 *
 * <p>
 * This definition can be applied to numbers but it can also be applied to
 * dates, strings, pairs and any other comparable value.
 *
 * <p>
 * This class provides a Java representation of an interval as described above.
 * It allows some operations on intervals like: union, intersection, subtraction and
 * checks like if two intervals overlaps, if are disjoint, if one includes another, if
 * a given value belongs to the interval, etc.
 *
 * <p>
 * An interval is immutable. Operations on intervals will create new intervals.
 * If two intervals are disjoint the method {@link #unify(Interval)} will throw
 * a {@link NoSuchIntervalException}. For unbounded intervals the methods
 * {@link #getInf()} and {@link #getSup()} may throw a {@link NoSuchElementException}.
 *
 *
 * @param <T> type of comparable elements of the interval.
 *
 * @author Massimo Coluzzi
 * @since 2.0.0
 */
public class Interval<T extends Comparable<T>>
       implements Emptily
{

    /** Singleton instance of the "empty set" interval. */
    private static final Interval<?> EMPTY = new Interval<>();

    /** Singleton instance of an unbounded interval. */
    @SuppressWarnings({"unchecked","rawtypes"})
    private static final Interval UNBOUNDED
    = new Interval( IntervalLimit.unboundedInf(), IntervalLimit.unboundedSup() );


    /** The inferior limit of the interval. */
    private final IntervalLimit<T> inf;

    /** The superior limit of the interval. */
    private final IntervalLimit<T> sup;


    /**
     * Default constructor.
     * <p>
     * This constructor is intended to be used
     * by reflection during de-serialization.
     * <p>
     * To create a new interval
     * use factory methods.
     */
    protected Interval()
    {

        super();

        this.inf = null;
        this.sup = null;

    }

    /**
     * Constructor with parameters.
     * <p>
     * This constructor is intended to be used
     * by extending classes only.
     * <p>
     * To create a new interval use
     * the factory methods.
     *
     * @param inf the inferior limit of the interval.
     * @param sup the superior limit of the interval.
     */
    protected Interval( IntervalLimit<T> inf, IntervalLimit<T> sup )
    {

        super();

        this.inf = Require.nonNull( inf, "The inferior limit is mandatory" );
        this.sup = Require.nonNull( sup, "The superior limit is mandatory" );

        Require.toHold( inf.isInf(), "The provided 'inf' param is not an inferior limit" );
        Require.toHold( sup.isSup(), "The provided 'sup' param is not a  superior limit" );

        if( ! inf.isUnbounded() && ! sup.isUnbounded() )
            Require.toHold( le(inf.value,sup.value), "The inferior limit cannot be greater than the superior limit" );

    }


    /* ***************** */
    /*  FACTORY METHODS  */
    /* ***************** */


    /**
     * Returns the singleton instance of the "empty set" {@link Interval}.
     * <p>
     * The empty set is considered to be an empty interval with undefined
     * superior and inferior limits.
     * <p>
     * Invoking {@link #getInf()} or {@link #getSup()} on this instance will
     * throw a {@link NoSuchElementException}.
     *
     * @param <T> type of the elements in the interval.
     * @return singleton instance of an empty {@link Interval}.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> Interval<T> empty()
    {

        return (Interval<T>) EMPTY;

    }

    /**
     * Returns the singleton instance of an unbounded {@link Interval}.
     * <p>
     * Superior and inferior limits are not defined for an unbounded
     * interval so, invoking {@link #getInf()} or {@link #getSup()}
     * on this instance, will throw a {@link NoSuchElementException}.
     *
     * @param <T> type of the elements in the interval.
     * @return singleton instance of an unbounded {@link Interval}.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> Interval<T> unbounded()
    {

        return (Interval<T>) UNBOUNDED;

    }

    /**
     * Creates a new {@link Interval} with a closed inferior
     * limit and an unbounded superior limit.
     * <p>
     * Invoking {@link #getSup()} on this instance
     * will throw a {@link NoSuchElementException}.
     *
     * @param <T> type of the elements in the interval.
     * @param value the inferior limit.
     * @return new instance of {@link Interval}.
     * @throws RequirementFailure if limits are inconsistent.
     */
    public static <T extends Comparable<T>> Interval<T> closedInf( T value )
    {

        return new Interval<>(
            IntervalLimit.closedInf( value ),
            IntervalLimit.unboundedSup()
        );

    }

    /**
     * Creates a new {@link Interval} with a open inferior
     * limit and an unbounded superior limit.
     * <p>
     * Invoking {@link #getSup()} on this instance
     * will throw a {@link NoSuchElementException}.
     *
     * @param <T> type of the elements in the interval.
     * @param value the inferior limit.
     * @return new instance of {@link Interval}.
     * @throws RequirementFailure if limits are inconsistent.
     */
    public static <T extends Comparable<T>> Interval<T> openInf( T value )
    {

        return new Interval<>(
            IntervalLimit.openInf( value ),
            IntervalLimit.unboundedSup()
        );

    }

    /**
     * Creates a new {@link Interval} with a closed superior
     * limit and an unbounded inferior limit.
     * <p>
     * Invoking {@link #getInf()} on this instance
     * will throw a {@link NoSuchElementException}.
     *
     * @param <T> type of the elements in the interval.
     * @param value the superior limit.
     * @return new instance of {@link Interval}.
     * @throws RequirementFailure if limits are inconsistent.
     */
    public static <T extends Comparable<T>> Interval<T> closedSup( T value )
    {

        return new Interval<>(
            IntervalLimit.unboundedInf(),
            IntervalLimit.closedSup( value )
        );

    }

    /**
     * Creates a new {@link Interval} with a open superior
     * limit and an unbounded inferior limit.
     * <p>
     * Invoking {@link #getInf()} on this instance
     * will throw a {@link NoSuchElementException}.
     *
     * @param <T> type of the elements in the interval.
     * @param value the superior limit.
     * @return new instance of {@link Interval}.
     * @throws RequirementFailure if limits are inconsistent.
     */
    public static <T extends Comparable<T>> Interval<T> openSup( T value )
    {

        return new Interval<>(
            IntervalLimit.unboundedInf(),
            IntervalLimit.openSup( value )
        );

    }

    /**
     * Creates a new closed {@link Interval} with the given limits.
     *
     * @param <T> type of the elements in the interval.
     * @param inf the inferior limit.
     * @param sup the superior limit.
     * @return new instance of {@link Interval}.
     * @throws RequirementFailure if limits are inconsistent.
     */
    public static <T extends Comparable<T>> Interval<T> closed( T inf, T sup )
    {

        return new Interval<>(
                IntervalLimit.closedInf( inf ),
                IntervalLimit.closedSup( sup )
        );

    }

    /**
     * Creates a new open {@link Interval} with the given limits.
     *
     * @param <T> type of the elements in the interval.
     * @param inf the inferior limit.
     * @param sup the superior limit.
     * @return new instance of an open {@link Interval}.
     * @throws RequirementFailure if limits are inconsistent.
     */
    public static <T extends Comparable<T>> Interval<T> open( T inf, T sup )
    {

        return new Interval<>(
            IntervalLimit.openInf( inf ),
            IntervalLimit.openSup( sup )
        );

    }

    /**
     * Creates a new {@link Interval} with a closed inferior limit
     * and an open superior limit.
     *
     * @param <T> type of the elements in the interval.
     * @param inf the inferior limit.
     * @param sup the superior limit.
     * @return new instance of {@link Interval}.
     * @throws RequirementFailure if limits are inconsistent.
     */
    public static <T extends Comparable<T>> Interval<T> closedOpen( T inf, T sup )
    {

        return new Interval<>(
                IntervalLimit.closedInf( inf ),
                IntervalLimit.openSup( sup )
        );

    }

    /**
     * Creates a new {@link Interval} with an open inferior limit
     * and a closed superior limit.
     *
     * @param <T> type of the elements in the interval.
     * @param inf the inferior limit.
     * @param sup the superior limit.
     * @return new instance of {@link Interval}.
     * @throws RequirementFailure if limits are inconsistent.
     */
    public static <T extends Comparable<T>> Interval<T> openClosed( T inf, T sup )
    {

        return new Interval<>(
                IntervalLimit.openInf( inf ),
                IntervalLimit.closedSup( sup )
        );

    }


    /* ******************* */
    /*  INTERFACE METHODS  */
    /* ******************* */


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty()
    {

        /*
         * An interval is considered to be empty
         * if it is the empty set or if it is a
         * single point open interval in the form
         * (x,x), (x,x] or [x,x).
         */
        if( isEmptySet() )
            return true;

        if( eq(inf.value,sup.value) )
             return inf.isOpen() || sup.isOpen();

        return false;

    }


    /* **************** */
    /*  PUBLIC METHODS  */
    /* **************** */


    /**
     * An interval is considered to be empty
     * if it is the empty set or if it is a
     * single point open interval in the form
     * {@code (x,x)}.
     * <p>
     * The method {@link #isEmpty()} returns
     * {@code true} in both cases. This method
     * returns {@code true} only if this interval
     * is the empty set.
     *
     * @return {@code true} if this interval is the empty set.
     */
    public boolean isEmptySet()
    {

        /*
         * The empty interval is the only one
         * with undefined inferior and superior
         * limits.
         */
        return inf == null && sup == null;

    }

    /**
     * Tells if both the inferior limit and
     * the superior limit are unbounded.
     *
     * @return {@code true} if both limit are unbounded.
     */
    public boolean isUnbounded()
    {

        return isUnboundedInf() && isUnboundedSup();

    }

    /**
     * Tells if the inferior limit is unbounded.
     *
     * @return {@code true} if the inferior limit is unbounded.
     */
    public boolean isUnboundedInf()
    {

        return inf != null && inf.isUnbounded();

    }

    /**
     * Tells if the superior limit is unbounded.
     *
     * @return {@code true} if the superior limit is unbounded.
     */
    public boolean isUnboundedSup()
    {

        return sup != null && sup.isUnbounded();

    }

    /**
     * Tells if both the inferior limit and
     * the superior limit are closed.
     *
     * @return {@code true} if both limit are closed.
     */
    public boolean isClosed()
    {

        return isClosedInf() && isClosedSup();

    }

    /**
     * Tells if the inferior limit is closed.
     *
     * @return {@code true} if the inferior limit is closed.
     */
    public boolean isClosedInf()
    {

        return inf != null && inf.isClosed();

    }

    /**
     * Tells if the superior limit is closed.
     *
     * @return {@code true} if the superior limit is closed.
     */
    public boolean isClosedSup()
    {

        return sup != null && sup.isClosed();

    }

    /**
     * Tells if both the inferior limit and
     * the superior limit are open.
     *
     * @return {@code true} if both limit are open.
     */
    public boolean isOpen()
    {

        return isOpenInf() && isOpenSup();

    }

    /**
     * Tells if the inferior limit is open.
     *
     * @return {@code true} if the inferior limit is open.
     */
    public boolean isOpenInf()
    {

        return inf != null && inf.isOpen();

    }

    /**
     * Tells if the superior limit is open.
     *
     * @return {@code true} if the superior limit is open.
     */
    public boolean isOpenSup()
    {

        return sup != null && sup.isOpen();

    }

    /**
     * Returns the value of the inferior limit
     * if it is defined. If the inferior limit
     * is unbounded or if this interval is the
     * empty set a {@link NoSuchElementException}
     * will be thrown.
     *
     * @return the inferior limit if defined.
     * @throws NoSuchElementException if the inferior
     * limit is unbounded or this is the empty set.
     */
    public T getInf()
    {

        if( inf == null || inf.isUnbounded() )
            throw new NoSuchElementException( "The inferior limit is undefined for " + this );

        return inf.value;

    }


    /**
     * Returns the value of the superior limit
     * if it is defined. If the superior limit
     * is unbounded or if this interval is the
     * empty set a {@link NoSuchElementException}
     * will be thrown.
     *
     * @return the superior limit if defined.
     * @throws NoSuchElementException if the superior
     * limit is unbounded or this is the empty set.
     */
    public T getSup()
    {

        if( sup == null || sup.isUnbounded() )
            throw new NoSuchElementException( "The superior limit is undefined for " + this );

        return sup.value;

    }

    /**
     * Tells if this interval contains the given value.
     *
     * @param value the value to check.
     * @return {@code true} it the value belongs to the interval.
     * @throws RequirementFailure if the provided value is {@code null}.
     */
    public boolean contains( T value )
    {

        Require.nonNull( value, "The value to check is mandatory" );
        return inf != null && sup != null &&
               inf.contains( value ) &&
               sup.contains( value );

    }

    /**
     * Tells if this interval includes the given one.
     * <p>
     * An interval A is included into another interval B
     * if any element of A belongs also to B.
     * <p>
     * The empty set is included into any other interval.
     *
     * @param other the interval to check.
     * @return {@code true} if the other interval is included in this one.
     * @throws RequirementFailure if the other interval is {@code null}.
     */
    public boolean includes( Interval<T> other )
    {

        Require.nonNull( other, "The interval to check is mandatory" );

        /*
         * Every interval includes the empty interval,
         * even the empty interval itself.
         */
        if( other.isEmptySet() )
            return true;

        /*
         * If the other interval is not the empty set
         * we check this interval to be not the empty
         * set and we check the limits of the other
         * interval to lay within the limits of this one.
         */
        return !  this.isEmptySet()
               && le( this.inf, other.inf )
               && ge( this.sup, other.sup );

    }

    /**
     * Tells if this interval overlaps the given one.
     * <p>
     * Two intervals overlap if they have at least one
     * element in common and if they are not including
     * each other. In mathematical notation: two intervals
     * {@code A} and {@code B} overlap if {@code A \u2229 B \u2260 \u2205}
     * and {@code A \u2288 B} and {@code B \u2288 A}.
     * Since there must exist at least one element in common
     * an empty interval cannot overlap with any other interval.
     *
     * @param other the interval to check.
     * @return {@code true} if this interval overlaps the other one.
     * @throws RequirementFailure if the other interval is {@code null}.
     */
    public boolean overlaps( Interval<T> other )
    {

        Require.nonNull( other, "The interval to check is mandatory" );

        /*
         * If the one interval includes the other
         * they are not considered to be overlapping.
         * If one of the of the two intervals is empty
         * is included into the other so the emptiness
         * check is already made by the method includes.
         */
        if( this.includes(other) || other.includes(this) )
            return false;

        /*
         * Neither of the two intervals is empty
         * and neither of the two intervals includes
         * the other so, for both of them, inf and sup
         * are not null and not both equal.
         * We need to check if either inf or sup of one
         * interval belong to the other.
         */
        return ( le(this.inf,other.inf) && ge(this.sup,other.inf) )
            || ( le(this.inf,other.sup) && ge(this.sup,other.sup) );

    }

    /**
     * Tells if this interval is disjoint frp, the given one.
     * <p>
     * Two intervals are said to be disjoint if they have
     * no elements in common. In mathematical notation:
     * two intervals {@code A} and {@code B} are disjoint
     * if {@code A \u2229 B = \u2205}.
     * <p>
     * Since two intervals are disjoint if they have no elements
     * in common then any empty interval is disjoint from any other
     * interval including itself.
     *
     * @param other the interval to check.
     * @return {@code true} if this interval overlaps the other one.
     * @throws RequirementFailure if the other interval is {@code null}.
     */
    public boolean isDisjointFrom( Interval<T> other )
    {

        Require.nonNull( other, "The interval to check is mandatory" );

        /*
         * The empty set is disjoint from any other interval.
         */
        if( this.isEmpty() || other.isEmpty() )
            return true;

        /*
         * If the intervals are unbounded cannot be disjoint.
         * Otherwise we need to check that they do not overlap.
         */
        if( ! this.sup.isUnbounded() &&
            ! other.inf.isUnbounded() &&
            lt(this.sup, other.inf) )
            return true;

        return ! other.sup.isUnbounded() &&
               ! this.inf.isUnbounded() &&
               lt( other.sup, this.inf );

    }

    /**
     * Tells if this interval is consecutive to the given one.
     * <p>
     * Two intervals are considered to be consecutive if they
     * share one limit and at least for one of the intervals
     * the given limit is closed. {@code (x,y),[y,z]}, {@code [x,y],(y,z]}
     * and {@code (x,y],[y,z)} are all consecutive intervals
     * while {@code [x,y),(y,z]} are not consecutive.
     * <p>
     * The empty set is consecutive to any other interval.
     *
     * @param other the interval to check.
     * @return {@code} if this is consecutive to other.
     * @throws RequirementFailure if the other interval is {@code null}.
     */
    public boolean isConsecutiveTo( Interval<T> other )
    {

        Require.nonNull( other, "The interval to check is mandatory" );

        /*
         * The empty set is consecutive to any
         * other interval.
         */
        if( this.isEmptySet() || other.isEmptySet() )
            return true;
        /*
         * We check if this.sup coincides
         * with other.inf.
         */
        if( ! this.sup.isUnbounded() && ! other.inf.isUnbounded()
            && eq( this.sup.value, other.inf.value ) )
            return this.sup.isClosed() || other.inf.isClosed();

        /*
         * We check if this.inf coincides
         * with other.sup.
         */
        if( ! this.inf.isUnbounded() && ! other.sup.isUnbounded()
            && eq( this.inf.value, other.sup.value ) )
            return this.inf.isClosed() || other.sup.isClosed();

        /* Otherwise they are not consecutive. */
        return false;

    }


    /**
     * This is a strict version of the {@link #isConsecutiveTo(Interval)}
     * method where a further check is made to ensure that the two
     * intervals are not overlapping.
     * <p>
     * In this case {@code (x,y),[y,z]} and {@code [x,y],(y,z]}
     * are still considered to be consecutive while
     * {@code (x,y],[y,z)} are not.
     * <p>
     * The empty set is strictly consecutive to any other interval.
     *
     * @param other the interval to check.
     * @return {@code} if this is consecutive to other.
     * @throws RequirementFailure if the other interval is {@code null}.
     */
    public boolean isStrictlyConsecutiveTo( Interval<T> other )
    {

        Require.nonNull( other, "The interval to check is mandatory" );

        /*
         * The empty set is consecutive to any
         * other interval.
         */
        if( this.isEmptySet() || other.isEmptySet() )
            return true;

        /*
         * We check if this.sup coincides
         * with other.inf.
         */
        if( ! this.sup.isUnbounded() && ! other.inf.isUnbounded()
            && eq( this.sup.value, other.inf.value ) )
            return ( this.sup.isClosed() && other.inf.isOpen() ) ||
                   ( this.sup.isOpen() && other.inf.isClosed() );

        /*
         * We check if this.inf coincides
         * with other.sup.
         */
        if( ! this.inf.isUnbounded() && ! other.sup.isUnbounded()
            && eq( this.inf.value, other.sup.value ) )
            return ( this.inf.isClosed() && other.sup.isOpen() ) ||
                   ( this.inf.isOpen() && other.sup.isClosed() );

        /* Otherwise they are not consecutive. */
        return false;

    }

    /**
     * Tells if this interval can be unified with the given one.
     * <p>
     * When we work with sets, the operation of union is always
     * possible. If we unify two sets, the result is still a set.
     * When we work with intervals this is not true.
     * <p>
     * By definition, a totally ordered set {@code I} is an interval
     * if: for each {@code x} such that {@code inf(I) <= x <= sup(I)}
     * we have that {@code x} belongs to {@code I}.
     * <p>
     * If we unify {@code [0,5]} and {@code [15,20]} the result is
     * not an interval because, for example the value {@code 10}, does
     * not belong to {@code [0,5]\u222a[15,20]}
     * <p>
     * Two intervals can be unified if:
     * <ol>
     *  <li>they are equal</li>
     *  <li>they overlap</li>
     *  <li>they are consecutive</li>
     * </ol>
     * <p>
     * A special case is given by the empty intervals. While the empty
     * set can be unified to any other interval, an empty interval can
     * be unified with another interval only if its limits lay within
     * the limits of the other interval. For example {@code [0,10]} and
     * {@code (5,5)} can be unified while {@code [5,10]} and {@code (0,0)}
     * cannot.
     * <p>
     * If this method returns {@code true} the method {@link #unify(Interval)}
     * will return a new {@link Interval} that represents the union of this
     * interval with the given one. Otherwise, if this method returns {@code false}
     * the method {@link #unify(Interval)} will throw a {@link NoSuchIntervalException}.
     *
     * @param other the interval to check.
     * @return {@code true} if this interval can be unified with the other one.
     * @throws RequirementFailure if the other interval is {@code null}.
     */
    public boolean canUnify( Interval<T> other )
    {

        Require.nonNull( other, "The interval to check is mandatory" );


        /*
         * If an interval includes the other they can be unified.
         * This check includes also the cases where the intervals
         * are empty end the intervals are equal.
         */
        if( this.includes(other) || other.includes(this) )
            return true;

        /*
         * This check catches three cases:
         * 1. If two non empty intervals are equal.
         * 2. If two non empty intervals overlap.
         * 3. If the limits of an empty interval
         *    lay within the limits of the other one.
         */
        if( ( le(this.inf,other.inf) && ge(this.sup,other.inf) ) ||
            ( le(this.inf,other.sup) && ge(this.sup,other.sup) ) )
            return true;

        /*
         * At this point the only case left is that
         * the two intervals are consecutive.
         */
        return this.isConsecutiveTo( other );

    }

    /**
     * Tells if the given interval can be subtracted from this one.
     * <p>
     * When we work with sets, the operation of subtraction is always
     * possible. If we subtract two sets, the result is still a set.
     * When we work with intervals this is not true.
     * <p>
     * By definition, a totally ordered set {@code I} is an interval
     * if: for each {@code x} such that {@code inf(I) <= x <= sup(I)}
     * we have that {@code x} belongs to {@code I}.
     * <p>
     * If we subtract {@code (5,15)} from {@code [0,20]} the result is
     * not an interval because, for example the value {@code 10}, does
     * not belong to {@code [0,5]\u222a[15,20]}
     * <p>
     * We can subtract an interval A from another interval B only of
     * B is not entirely included into A. For example, from the interval
     * {@code [0,20]}, we can subtract  @code [10,20]} but cannot subtract
     * {@code [10,15]} or {@code [10,20)}.
     * <p>
     * A special case is given by the empty intervals. Se can always
     * subtract an empty interval even if it is entirely included
     * into the other one. For example, from the interval {@code [0,20]},
     * we can subtract {@code [10,10)}, {@code (10,10]} and {@code (10,10)}.
     * <p>
     * If this method returns {@code true} the method {@link #subtract(Interval)}
     * will return a new {@link Interval} that represents this interval minus
     * the given one. Otherwise, if this method returns {@code false}
     * the method {@link #subtract(Interval)} will throw a {@link NoSuchIntervalException}.
     *
     * @param other the interval to check.
     * @return {@code true} if this interval can subtract the other one.
     * @throws RequirementFailure if the other interval is {@code null}.
     */
    public boolean canSubtract( Interval<T> other )
    {

        Require.nonNull( other, "The interval to check is mandatory" );

        /* Empty intervals can always be subtracted. */
        if( this.isEmpty() || other.isEmpty() )
            return true;

        /*
         * A non empty interval A can subtract
         * a non empty interval B if A do not
         * entirely include B. This means that
         * not both limits of B should lay
         * inside the limits of A.
         */
        return le(other.inf,this.inf) || ge(other.sup,this.sup);

    }

    /**
     * Returns an interval containing only those
     * elements that belongs to both this interval
     * and the given one.
     * <p>
     * If one of the intervals is the empty set or
     * if the two intervals are disjoint then the
     * empty set will be returned.
     *
     * @param other the interval to intersect.
     * @return the intersection of the two intervals.
     * @throws RequirementFailure if the other interval is {@code null}.
     */
    public Interval<T> intersect( Interval<T> other )
    {

        Require.nonNull( other, "The interval to intersect is mandatory" );

        /*
         * If this includes other the intersection is other.
         * This check includes also the case of empty intervals.
         * For example if this = [0,10] and other = [5,5) then
         * [5,5) will be returned.
         */
        if( this.includes(other) )
            return other;

        /*
         * If other includes this the intersection is this.
         * This check includes also the case of empty intervals.
         * For example if this = (5,5] and other = [0,10] then
         * (5,5] will be returned.
         */
        if( other.includes(this) )
            return this;

        /*
         * At this point the two intervals can be disjoint
         * or they are both non empty and overlap.
         */
        if( this.isDisjointFrom(other) )
            return empty();

        /*
         * Since the two intervals overlap, the intersection area
         * is the one between the greatest inferior limit and the
         * smallest superior limit.
         */
        final IntervalLimit<T> newInf = max( this.inf, other.inf );
        final IntervalLimit<T> newSup = min( this.sup, other.sup );

        return new Interval<>( newInf, newSup );

    }

    /**
     * Returns an interval containing those elements
     * that belongs to at least one between this interval
     * and the given one.
     * <p>
     * If one of the intervals is the empty the other one
     * will be returned.
     * <p>
     * Two intervals can be unified if either they overlap
     * or they are consecutive. If none of this condition
     * holds then a {@link NoSuchIntervalException} will
     * be thrown.
     * <p>
     *  If the methog {@link #canUnify(Interval)} returns {@code false}
     *  this method will throw a {@link NoSuchIntervalException}.
     *
     * @param other the interval to intersect.
     * @return the intersection of the two intervals.
     * @throws RequirementFailure if the other interval is {@code null}.
     * @throws NoSuchIntervalException it the two intervals cannot be unified.
     */
    public Interval<T> unify( Interval<T> other )
    {

        /*
         * If this interval cannot be unified with the other
         * then a NoSuchIntervalException will be thrown.
         */
        if( ! canUnify(other) )
            throw new NoSuchIntervalException( this, other, " \u222a " );

        /* The empty set is the neutral element for union. */
        if( this.isEmptySet() )
            return other;

        /* The empty set is the neutral element for union. */
        if( other.isEmptySet() )
            return this;

        /*
         * Otherwise this interval and the other either
         * overlap or they are consecutive. In both cases
         * the union will have the smallest inferior limit
         * and the greatest superior limit.
         */
        final IntervalLimit<T> newInf = min( this.inf, other.inf );
        final IntervalLimit<T> newSup = max( this.sup, other.sup );

        return new Interval<>( newInf, newSup );

    }

    /**
     * Returns an interval containing only those
     * elements that belongs to this interval
     * but not to the given one.
     * <p>
     * If one of the intervals is the empty set or
     * if the two intervals are disjoint this interval
     * will be returned.
     *
     * @param other the interval to subtract.
     * @return the subtraction of the other interval from this one.
     * @throws RequirementFailure if the other interval is {@code null}.
     */
    public Interval<T> subtract( Interval<T> other )
    {

        Require.nonNull( other, "The interval to subtract is mandatory" );

        /*
         * If this interval cannot subtract the other
         * then a NoSuchIntervalException will be thrown.
         * This check makes sure that we cannot be in the
         * situation where: this.includes(other) && ne(this,other).
         */
        if( ! canSubtract(other) )
            throw new NoSuchIntervalException( this, other, " \\ " );

        /*
         * If this interval is entirely included in the one to remove,
         * for example [1,2] \ [0,3], then the empty set will be returned.
         * This case include also the case in which the two intervals are equal.
         */
        if( other.includes(this) )
            return empty();

        /*
         * If the intervals are disjoint then subtracting
         * the other interval will not change this.
         * This case includes also the case where the
         * interval to remove is empty.
         */
        if( this.isDisjointFrom(other) )
            return this;


        /*
         * At this point we have two intervals that are: not empty,
         * not equal, not disjoint and are not including each other.
         * So they must overlap, this means that we need to remove
         * the overlapping portion from this interval.
         * We need to check the unbounded cases separately.
         */

        /*
         * Case 1: the interval to remove has an unbounded inferior limit.
         * Can be in the form (-inf,x) or (-inf,x]. In both cases we need
         * to shift upwards the inferior limit of this interval.
         * The interval to remove cannot be larger than this one otherwise
         * we would have exit in the second check.
         */
        if( other.inf.isUnbounded() )
        {
            final IntervalLimit<T> newInf
            = other.sup.isClosed()
            ? IntervalLimit.openInf( other.sup.value )
            : IntervalLimit.closedInf( other.sup.value );

            return new Interval<>( newInf, this.sup );

        }

        /*
         * Case 2: the interval to remove has an unbounded superior limit.
         * Can be in the form (x,+inf) or [x,+inf). In both cases we need
         * to shift downwards the superior limit of this interval.
         * The interval to remove cannot be larger than this one otherwise
         * we would have exit in the second check.
         */
        if( other.sup.isUnbounded() )
        {
            final IntervalLimit<T> newSup
            = other.inf.isClosed()
            ? IntervalLimit.openSup( other.inf.value )
            : IntervalLimit.closedSup( other.inf.value );

            return new Interval<>( this.inf, newSup );

        }


        /*
         * Case 3: the interval to remove is bounded on both sides.
         * Can be in the form (x,y) or [x,y), (x,y] or [x,x] with x < y.
         * Since the other interval cannot be entirely included into
         * this one it must be that one limit of other lays inside
         * this interval and the other limit lays outside.
         */
        if( lt(this.inf,other.inf) && ge(this.sup,other.inf) )
        {

            final IntervalLimit<T> newSup
                    = other.inf.isClosed()
                    ? IntervalLimit.openSup( other.inf.value )
                    : IntervalLimit.closedSup( other.inf.value );

            return new Interval<>( this.inf, newSup );

        }
        else
        {

            final IntervalLimit<T> newInf
                    = other.sup.isClosed()
                    ? IntervalLimit.openInf( other.sup.value )
                    : IntervalLimit.closedInf( other.sup.value );

            return new Interval<>( newInf, this.sup );

        }

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

        return Hashcode.of( inf, sup );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object other )
    {

        return Equals.ifSameClass(
            this, other,
            o -> o.inf,
            o -> o.sup
        );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {

        return isEmptySet() ? "\u2205" : inf + "," + sup;

    }

}
