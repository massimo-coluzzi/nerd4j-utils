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

import java.util.function.Consumer;

import org.nerd4j.utils.lang.Require;
import org.nerd4j.utils.lang.RequirementFailure;

/**
 * Extension of {@link java.util.function.Consumer}
 * designed to be able to handle {@code checked exceptions}.
 * 
 * <p>
 * The method {@link #acceptThrowing(Object)} allows its implementations
 * to throw any subclass of {@link java.lang.Exception}.
 * 
 * <p>
 * To allow composition, the type of exception thrown by the method
 * {@link #acceptThrowing(Object)} cannot be defined, so this method
 * will declare a generic {@link java.lang.Exception}.
 *  
 * @param <I> the type of the input to the operation.
 *
 * @author Massimo Coluzzi
 * @since 2.1.0
 */
@FunctionalInterface
public interface ThrowingConsumer<I> extends Consumer<I>
{

    /**
     * Performs this operation on the given argument.
     * <p>
     * This method works even if the implementation
     * throws a checked exception.
     * 
     * @param input the input argument.
     * @throws Exception if the implementation breaks.
     */
    void acceptThrowing( I input ) throws Exception;

    /**
     * Performs this operation on the given argument.
     * <p>
     * This method implements {@link java.util.function.Consumer#accept(Object)}
     * replacing any checked exception with a {@code java.lang.RuntimeException}.
     *
     * @param input the input argument.
     */
    default void accept( I input )
    {

        try{

            acceptThrowing( input );

        }catch( RuntimeException ex )
        {

            throw ex;

        }catch( Exception ex )
        {

            throw new RuntimeException( ex );

        }

    }

    /**
     * Returns a composed {@link ThrowingConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@link ThrowingConsumer} that performs in sequence
     *         this  operation followed by the {@code after} operation
     * @throws RequirementFailure if after is {@code null}
     */
    default ThrowingConsumer<I> andThen( ThrowingConsumer<? super I> after )
    {
        
        Require.nonNull( after, "The operation to apply after cannot be null" );
        return (I input) ->
        {
            acceptThrowing( input );
            after.acceptThrowing( input );
        };

    }

    /**
     * Returns a composed {@link ThrowingConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@link ThrowingConsumer} that performs in sequence
     *         this  operation followed by the {@code after} operation
     * @throws RequirementFailure if after is {@code null}
     */
    @Override
    default ThrowingConsumer<I> andThen( Consumer<? super I> after )
    {
        
        Require.nonNull( after, "The operation to apply after cannot be null" );
        return (I input) ->
        {
            acceptThrowing( input );
            after.accept( input );
        };

    }

}
