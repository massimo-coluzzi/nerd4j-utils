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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


/**
 * Test suite for the functional interface {@link ThrowingConsumer}.
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing functional interface: ThrowingConsumer")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ThrowingConsumerTests
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
    public void a_ThrowingConsumer_is_also_a_Consumer()
    {

        final ThrowingConsumer<?> consumer = input -> System.out.println( input );
        assertTrue( consumer instanceof Consumer );
        
    }


    @Test
    public void acceptThrowing_should_throw_a_checked_Exception()
    {

        final Exception exception = getRandomException();
        final ThrowingConsumer<String> consumer = input -> { throw exception; };

        final Exception thrown = assertThrows(
            exception.getClass(),
            () -> consumer.acceptThrowing( "input" )
        );

        assertFalse( thrown instanceof RuntimeException );
        assertSame( exception, thrown );
        
    }

    @Test
    public void if_the_original_error_is_a_RuntimeException_then_method_accept_should_forward_the_original_exception()
    {

        final RuntimeException exception = getRandomRuntimeException();
        final ThrowingConsumer<String> consumer = input -> { throw exception; };

        final Exception thrown = assertThrows(
            exception.getClass(),
            () -> consumer.accept( "input" )
        );

        assertSame( exception, thrown );
        
    }

    @Test
    public void if_the_original_error_is_a_checked_Exception_then_method_accept_should_throw_a_new_RuntimeException()
    {

        final Exception exception = getRandomException();
        final ThrowingConsumer<String> consumer = input -> { throw exception; };

        final Exception thrown = assertThrows(
            RuntimeException.class,
            () -> consumer.accept( "input" )
        );

        assertSame( exception, thrown.getCause() );
        
    }


    @Nested
    @DisplayName("on method andThen(ThrowingConsumer)")
    public class AndThenWithThrowingConsumer
    {

        @Test
        public void method_andThen_should_return_a_ThrowingConsumer()
        {

            final ThrowingConsumer<String> before = input -> { return; };
            final ThrowingConsumer<String> after  = input -> { return; };

            final Object composed = before.andThen( after );
            assertNotNull( composed );
            assertTrue( composed instanceof ThrowingConsumer );

        }

        @Test
        public void both_childs_should_consume_the_input()
        {

            final AtomicBoolean beforeConsumed = new AtomicBoolean();
            final AtomicBoolean  afterConsumed = new AtomicBoolean();

            final ThrowingConsumer<String> before = input -> beforeConsumed.set( true );
            final ThrowingConsumer<String> after  = input ->  afterConsumed.set( true );

            final Consumer<String> composed = before.andThen( after );

            assertFalse( beforeConsumed.get() );
            assertFalse(  afterConsumed.get() );

            composed.accept( "input" );

            assertTrue( beforeConsumed.get() );
            assertTrue(  afterConsumed.get() );

        }

        @Test
        public void if_before_throws_Exception_should_be_propagated()
        {

            final Exception exception = getRandomException();
            final AtomicBoolean beforeConsumed = new AtomicBoolean();
            final AtomicBoolean  afterConsumed = new AtomicBoolean();

            final ThrowingConsumer<String> before = input -> { throw exception; };
            final ThrowingConsumer<String> after  = input ->  afterConsumed.set( true );

            final ThrowingConsumer<String> composed = before.andThen( after );

            assertFalse( beforeConsumed.get() );
            assertFalse(  afterConsumed.get() );

            final Exception thrown = assertThrows(
                exception.getClass(), 
                () -> composed.acceptThrowing( "input" )
            );

            assertFalse( thrown instanceof RuntimeException );
            assertSame( exception, thrown );

            assertFalse( beforeConsumed.get() );
            assertFalse(  afterConsumed.get() );

        }

        @Test
        public void if_after_throws_Exception_should_be_propagated()
        {

            final Exception exception = getRandomException();
            final AtomicBoolean beforeConsumed = new AtomicBoolean();
            final AtomicBoolean  afterConsumed = new AtomicBoolean();

            final ThrowingConsumer<String> before = input ->  beforeConsumed.set( true );
            final ThrowingConsumer<String> after  = input -> { throw exception; };

            final ThrowingConsumer<String> composed = before.andThen( after );

            assertFalse( beforeConsumed.get() );
            assertFalse(  afterConsumed.get() );

            final Exception thrown = assertThrows(
                exception.getClass(), 
                () -> composed.acceptThrowing( "input" )
            );

            assertFalse( thrown instanceof RuntimeException );
            assertSame( exception, thrown );

            assertTrue( beforeConsumed.get() );
            assertFalse( afterConsumed.get() );

        }

    }

    @Nested
    @DisplayName("on method andThen(Consumer)")
    public class AndThenWithConsumer
    {

        @Test
        public void method_andThen_should_return_a_ThrowingConsumer()
        {

            final ThrowingConsumer<String> before = input -> { return; };
            final Consumer<String> after = input -> { return; };

            final Object composed = before.andThen( after );
            assertNotNull( composed );
            assertTrue( composed instanceof ThrowingConsumer );

        }

        @Test
        public void both_childs_should_consume_the_input()
        {

            final AtomicBoolean beforeConsumed = new AtomicBoolean();
            final AtomicBoolean  afterConsumed = new AtomicBoolean();

            final ThrowingConsumer<String> before = input -> beforeConsumed.set( true );
            final Consumer<String> after = input -> afterConsumed.set( true );

            final Consumer<String> composed = before.andThen( after );

            assertFalse( beforeConsumed.get() );
            assertFalse(  afterConsumed.get() );

            composed.accept( "input" );

            assertTrue( beforeConsumed.get() );
            assertTrue(  afterConsumed.get() );

        }

        @Test
        public void if_before_throws_Exception_should_be_propagated()
        {

            final Exception exception = getRandomException();
            final AtomicBoolean beforeConsumed = new AtomicBoolean();
            final AtomicBoolean  afterConsumed = new AtomicBoolean();

            final ThrowingConsumer<String> before = input -> { throw exception; };
            final Consumer<String> after = input -> afterConsumed.set( true );

            final ThrowingConsumer<String> composed = before.andThen( after );

            assertFalse( beforeConsumed.get() );
            assertFalse(  afterConsumed.get() );

            final Exception thrown = assertThrows(
                exception.getClass(), 
                () -> composed.acceptThrowing( "input" )
            );

            assertFalse( thrown instanceof RuntimeException );
            assertSame( exception, thrown );

            assertFalse( beforeConsumed.get() );
            assertFalse(  afterConsumed.get() );

        }

        @Test
        public void if_after_throws_Exception_should_be_propagated()
        {

            final RuntimeException exception = new RuntimeException( "Error_" + random.nextInt() );
            final AtomicBoolean beforeConsumed = new AtomicBoolean();
            final AtomicBoolean  afterConsumed = new AtomicBoolean();

            final ThrowingConsumer<String> before = input ->  beforeConsumed.set( true );
            final Consumer<String> after = input -> { throw exception; };

            final ThrowingConsumer<String> composed = before.andThen( after );

            assertFalse( beforeConsumed.get() );
            assertFalse(  afterConsumed.get() );

            final Exception thrown = assertThrows(
                exception.getClass(), 
                () -> composed.acceptThrowing( "input" )
            );

            assertTrue( thrown instanceof RuntimeException );
            assertSame( exception, thrown );

            assertTrue( beforeConsumed.get() );
            assertFalse( afterConsumed.get() );

        }

    }

}
