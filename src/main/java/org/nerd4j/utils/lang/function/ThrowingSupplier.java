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

import java.util.function.Supplier;

/**
 * Extension of {@link java.util.function.Supplier}
 * designed to be able to handle {@code checked exceptions}.
 * 
 * <p>
 * The method {@link #getThrowing()} allows its implementations
 * to throw any subclass of {@link java.lang.Exception}.
 *  
 * @param <O> the type of results supplied by this supplier.
 *
 * @author Massimo Coluzzi
 * @since 2.1.0
 */
@FunctionalInterface
public interface ThrowingSupplier<O> extends Supplier<O>
{

    /**
     * Gets a result.
     * <p>
     * This method works even if the implementation
     * throws a checked exception.
     * 
     * @return a result.
     * @throws Exception if the implementation breaks.
     */
    O getThrowing() throws Exception;

    /**
     * Gets a result.
     * <p>
     * This method implements {@link java.util.function.Supplier#get()}
     * replacing any checked exception with a {@code java.lang.RuntimeException}.
     *
     * @return a result.
     */
    default O get()
    {

        try{

            return getThrowing();

        }catch( RuntimeException ex )
        {

            throw ex;

        }catch( Exception ex )
        {

            throw new RuntimeException( ex );

        }

    }
    
}
