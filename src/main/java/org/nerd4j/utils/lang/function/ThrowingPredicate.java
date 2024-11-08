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
package org.nerd4j.utils.lang.function;

import java.util.function.Predicate;

import org.nerd4j.utils.lang.Require;
import org.nerd4j.utils.lang.RequirementFailure;

/**
 * Extension of {@link java.util.function.Predicate}
 * designed to be able to handle {@code checked exceptions}.
 * 
 * <p>
 * The method {@link #testThrowing(Object)} allows its implementations
 * to throw any subclass of {@link java.lang.Exception}.
 * 
 * <p>
 * To allow composition, the type of exception thrown by the method
 * {@link #testThrowing(Object)} cannot be defined, so this method
 * will declare a generic {@link java.lang.Exception}.
 *  
 * @param <I> the type of the input to the predicate.
 *
 * @author Massimo Coluzzi
 * @since 2.1.0
 */
@FunctionalInterface
public interface ThrowingPredicate<I> extends Predicate<I>
{

    /**
     * Evaluates this predicate on the given argument.
     * <p>
     * This method works even if the implementation
     * throws a checked exception.
     * 
     * @param input the input argument
     * @return {@code true} if the input argument
     *         matches the predicate, {@code false}
     *         otherwise.
     * @throws Exception if the implementation breaks.
     */
    boolean testThrowing( I input ) throws Exception;

    /**
     * Evaluates this predicate on the given argument.
     * <p>
     * This method implements {@link java.util.function.Predicate#test(Object)}
     * replacing any checked exception with a {@code java.lang.RuntimeException}.
     *
     * @param input the input argument.
     * @return {@code true} if the input argument
     *         matches the predicate, {@code false}
     *         otherwise.
     */
    default boolean test( I input )
    {

        try{

            return testThrowing( input );

        }catch( RuntimeException ex )
        {

            throw ex;
            
        }catch( Exception ex )
        {

            throw new RuntimeException( ex );

        }

    }

    /**
     * Returns a composed {@link ThrowingPredicate} that represents
     * a short-circuiting logical {@code AND} of this {@link ThrowingPredicate}
     * and another. When evaluating the composed predicate, if this predicate
     * is {@code false}, then the {@code other} predicate is not evaluated.
     * <p>
     * Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this {@link ThrowingPredicate} throws an exception,
     * the {@code other} predicate will not be evaluated.
     *
     * @param other a {@link ThrowingPredicate} that will be logically-ANDed with this one.
     * @return a composed {@link ThrowingPredicate} that represents the short-circuiting logical
     *         {@code AND} of this predicate and the {@code other} predicate
     * @throws RequirementFailure if other is {@code null}
     */
    default ThrowingPredicate<I> and( ThrowingPredicate<? super I> other )
    {

        Require.nonNull( other, "The predicate to put in AND is mandatory" );
        return (I input) ->
            testThrowing( input ) && other.testThrowing( input );

    }

        /**
     * Returns a composed {@link ThrowingPredicate} that represents
     * a short-circuiting logical {@code AND} of this {@link ThrowingPredicate}
     * and a {@link Predicate}. When evaluating the composed predicate, if this predicate
     * is {@code false}, then the {@code other} predicate is not evaluated.
     * <p>
     * Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this {@link ThrowingPredicate} throws an exception,
     * the {@code other} predicate will not be evaluated.
     *
     * @param other a {@link Predicate} that will be logically-ANDed with this one.
     * @return a composed {@link ThrowingPredicate} that represents the short-circuiting logical
     *         {@code AND} of this predicate and the {@code other} predicate
     * @throws RequirementFailure if other is {@code null}
     */
    @Override
    default ThrowingPredicate<I> and( Predicate<? super I> other )
    {

        Require.nonNull( other, "The predicate to put in AND is mandatory" );
        return (I input) ->
            testThrowing( input ) && other.test( input );

    }

    /**
     * Returns a composed {@link ThrowingPredicate} that represents
     * a short-circuiting logical {@code OR} of this {@link ThrowingPredicate}
     * and another. When evaluating the composed predicate, if this predicate
     * is {@code true}, then the {@code other} predicate is not evaluated.
     * <p>
     * Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this {@link ThrowingPredicate} throws
     * an exception, the {@code other} predicate will not be evaluated.
     *
     * @param other a {@link ThrowingPredicate} that will be logically-ORed
     *              with this one.
     * @return a composed {@link ThrowingPredicate} that represents the
     *         short-circuiting logical {@code OR} of this predicate and
     *         the {@code other} predicate
     * @throws RequirementFailure if other is {@code null}
     */
    default ThrowingPredicate<I> or( ThrowingPredicate<? super I> other )
    {

        Require.nonNull( other, "The predicate to put in OR is mandatory" );
        return (I input) ->
            testThrowing( input ) || other.testThrowing( input );

    }

    /**
     * Returns a composed {@link ThrowingPredicate} that represents
     * a short-circuiting logical {@code OR} of this {@link ThrowingPredicate}
     * and a {@link Predicate}. When evaluating the composed predicate, if this predicate
     * is {@code true}, then the {@code other} predicate is not evaluated.
     * <p>
     * Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this {@link ThrowingPredicate} throws
     * an exception, the {@code other} predicate will not be evaluated.
     *
     * @param other a {@link Predicate} that will be logically-ORed
     *              with this one.
     * @return a composed {@link ThrowingPredicate} that represents the
     *         short-circuiting logical {@code OR} of this predicate and
     *         the {@code other} predicate
     * @throws RequirementFailure if other is {@code null}
     */
    @Override
    default ThrowingPredicate<I> or( Predicate<? super I> other )
    {

        Require.nonNull( other, "The predicate to put in OR is mandatory" );
        return (I input) ->
            testThrowing( input ) || other.test( input );

    }

    /**
     * Returns a {@link ThrowingPredicate} that represents
     * the logical negation of this one.
     *
     * @return a {@link ThrowingPredicate} that represents
     *         the logical negation of this one.
     */
    default ThrowingPredicate<I> negate()
    {

        return (I input) -> ! testThrowing( input );

    }

}
