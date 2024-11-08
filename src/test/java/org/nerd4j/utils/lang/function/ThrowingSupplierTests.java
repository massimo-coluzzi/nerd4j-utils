/*-
 * #%L
 * Nerd4j Utils
 * %%
 * Copyright (C) 2011 - 2019 Nerd4j
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;


/**
 * Test suite for the functional interface {@link ThrowingSupplier}.
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing functional interface: ThrowingSupplier")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ThrowingSupplierTests
{


    private static final Random random = new Random();


    /**
     * Returns a new ReflectiveOperationException
     * with a random generated message.
     * 
     * @return new exception.
     */
    private Exception getRandomException()
    {

        final String message = "Error_" + random.nextInt();
        return new ReflectiveOperationException( message );

    }

    /**
     * Returns a new ReflectiveOperationException
     * with a random generated message.
     * 
     * @return new exception.
     */
    private RuntimeException getRandomRuntimeException()
    {

        final String message = "Error_" + random.nextInt();
        return new RejectedExecutionException( message );

    }


    /* ************** */
    /*  TEST METHODS  */
    /* ************** */


    @Test
    public void a_ThrowingSupplier_is_also_a_Supplier()
    {

        final ThrowingSupplier<?> supplier = () -> "Message";
        assertTrue( supplier instanceof Supplier );
        
    }


    @Test
    public void getThrowing_should_throw_a_checked_Exception()
    {

        final Exception exception = getRandomException();
        final ThrowingSupplier<?> supplier = () -> { throw exception; };

        final Exception thrown = assertThrows(
            ReflectiveOperationException.class,
            () -> supplier.getThrowing()
        );

        assertFalse( thrown instanceof RuntimeException );
        assertSame( exception, thrown );
        
    }

    @Test
    public void if_the_original_error_is_a_RuntimeException_then_method_run_should_forward_the_original_exception()
    {

        final RuntimeException exception = getRandomRuntimeException();
        final ThrowingSupplier<?> supplier = () -> { throw exception; };

        final Exception thrown = assertThrows(
            exception.getClass(),
            () -> supplier.get()
        );

        assertSame( exception, thrown );
        
    }

    @Test
    public void if_the_original_error_is_a_checked_Exception_then_method_run_should_throw_a_new_RuntimeException()
    {

        final Exception exception = getRandomException();
        final ThrowingSupplier<?> supplier = () -> { throw exception; };

        final Exception thrown = assertThrows(
            RuntimeException.class,
            () -> supplier.get()
        );

        assertSame( exception, thrown.getCause() );
        
    }

}
