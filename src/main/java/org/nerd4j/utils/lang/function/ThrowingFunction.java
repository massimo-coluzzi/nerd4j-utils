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

import java.util.function.Function;

import org.nerd4j.utils.lang.Require;
import org.nerd4j.utils.lang.RequirementFailure;

/**
 * Extension of {@link java.util.function.Function}
 * designed to be able to handle {@code checked exceptions}.
 * 
 * <p>
 * The method {@link #applyThrowing(Object)} allows its implementations
 * to throw any subclass of {@link java.lang.Exception}.
 * 
 * <p>
 * To allow composition, the type of exception thrown by the method
 * {@link #applyThrowing(Object)} cannot be defined, so this method
 * will declare a generic {@link java.lang.Exception}.
 *  
 * @param <I> the type of the input to the function.
 * @param <O> the type of the output of the function.
 *
 * @author Massimo Coluzzi
 * @since 2.1.0
 */
@FunctionalInterface
public interface ThrowingFunction<I,O> extends Function<I,O>
{

    /**
     * Applies this function to the given argument.
     * <p>
     * This method works even if the implementation
     * throws a checked exception.
     * 
     * @param input the function input argument.
     * @return the function output.
     * @throws Exception if the implementation breaks.
     */
    O applyThrowing( I input ) throws Exception;

    /**
     * Applies this function to the given argument.
     * <p>
     * This method implements {@link java.util.function.Function#apply(Object)}
     * replacing any checked exception with a {@code java.lang.RuntimeException}.
     *
     * @param input the function input argument.
     * @return the function output.
     */
    default O apply( I input )
    {

        try{

            return applyThrowing( input );

        }catch( RuntimeException ex )
        {

            throw ex;

        }catch( Exception ex )
        {

            throw new RuntimeException( ex );

        }

    }

    /**
     * Returns a composed {@link ThrowingFunction} that first applies the {@code before}
     * {@link ThrowingFunction} to its input, and then applies this {@link ThrowingFunction}
     * to the result. If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <S> the type of input to the {@code before} function, and to the
     *            composed function
     * @param before the function to apply before this function is applied
     * @return a composed {@link ThrowingFunction} that first applies the {@code before}
     *         {@link ThrowingFunction} and then applies this {@link ThrowingFunction}
     * @throws RequirementFailure if before is {@code null}
     * @see #andThen(ThrowingFunction)
     */
    default <S> ThrowingFunction<S,O> compose(
        ThrowingFunction<? super S, ? extends I> before
    )
    {

        Require.nonNull( before, "The function to apply first cannot be null" );
        return (S source) -> applyThrowing(
            before.applyThrowing( source )
        );

    }

    /**
     * Returns a composed {@link ThrowingFunction} that first applies the {@code before}
     * {@link Function} to its input, and then applies this {@link ThrowingFunction}
     * to the result. If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <S> the type of input to the {@code before} function, and to the
     *            composed function
     * @param before the function to apply before this function is applied
     * @return a composed {@link ThrowingFunction} that first applies the {@code before}
     *         {@link Function} and then applies this {@link ThrowingFunction}
     * @throws RequirementFailure if before is {@code null}
     * @see #andThen(Function)
     */
    @Override
    default <S> ThrowingFunction<S,O> compose(
        Function<? super S, ? extends I> before
    )
    {

        Require.nonNull( before, "The function to apply first cannot be null" );
        return (S source) -> applyThrowing(
            before.apply( source )
        );

    }

    /**
     * Returns a composed {@link ThrowingFunction} that first applies
     * this {@link ThrowingFunction} to its input, and then applies
     * the {@code after} {@link ThrowingFunction} to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <T> the type of output of the {@code after} function, and of the
     *           composed function
     * @param after the function to apply after this function is applied.
     * @return a composed function that first applies this {@link ThrowingFunction}
     *         and then applies the {@code after} {@link ThrowingFunction}.
     * @throws RequirementFailure if after is {@code null}
     * @see #compose(ThrowingFunction)
     */
    default <T> ThrowingFunction<I,T> andThen(
        ThrowingFunction<? super O, ? extends T> after
    )
    {
        
        Require.nonNull( after, "The function to apply after cannot be null" );
        return (I input) -> after.applyThrowing(
            applyThrowing( input )
        );

    }

    /**
     * Returns a composed {@link ThrowingFunction} that first applies
     * this {@link ThrowingFunction} to its input, and then applies
     * the {@code after} {@link Function} to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <T> the type of output of the {@code after} function, and of the
     *           composed function
     * @param after the function to apply after this function is applied.
     * @return a composed function that first applies this {@link ThrowingFunction}
     *         and then applies the {@code after} {@link Function}.
     * @throws RequirementFailure if after is {@code null}
     * @see #compose(Function)
     */
    default <T> ThrowingFunction<I,T> andThen(
        Function<? super O, ? extends T> after
    )
    {
        
        Require.nonNull( after, "The function to apply after cannot be null" );
        return (I input) -> after.apply(
            applyThrowing( input )
        );

    }

}
