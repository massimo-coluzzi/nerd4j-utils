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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


/**
 * Test suite for the functional interface {@link ThrowingFunction}.
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing functional interface: ThrowingFunction")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ThrowingFunctionTests
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
    public void a_ThrowingFunction_is_also_a_Function()
    {

        final ThrowingFunction<?,?> Function = input -> new Object();
        assertTrue( Function instanceof Function );
        
    }


    @Test
    public void applyThrowing_should_throw_a_checked_Exception()
    {

        final Exception exception = getRandomException();
        final ThrowingFunction<String,?> Function = input -> { throw exception; };

        final Exception thrown = assertThrows(
            exception.getClass(),
            () -> Function.applyThrowing( "input" )
        );

        assertFalse( thrown instanceof RuntimeException );
        assertSame( exception, thrown );
        
    }

    @Test
    public void if_the_original_error_is_a_RuntimeException_then_method_apply_should_forward_the_original_exception()
    {

        final RuntimeException exception = getRandomRuntimeException();
        final ThrowingFunction<String,?> function = input -> { throw exception; };

        final Exception thrown = assertThrows(
            exception.getClass(),
            () -> function.apply( "input" )
        );

        assertSame( exception, thrown );
        
    }

    @Test
    public void if_the_original_error_is_a_checked_Exception_then_method_apply_should_throw_a_new_RuntimeException()
    {

        final Exception exception = getRandomException();
        final ThrowingFunction<String,?> function = input -> { throw exception; };

        final Exception thrown = assertThrows(
            RuntimeException.class,
            () -> function.apply( "input" )
        );

        assertSame( exception, thrown.getCause() );
        
    }


    @Nested
    @DisplayName("on method compose()")
    public class Compose
    {

        @Nested
        @DisplayName("with ThrowingFunction")
        public class WithThrowingFunction
        {

            @Test
            public void method_compose_should_return_a_ThrowingFunction()
            {

                final ThrowingFunction<String,Short> internal = input -> { return 5; };
                final ThrowingFunction<Short,Object> external  = input -> { return new Object(); };

                final Object composed = external.compose( internal );

                assertNotNull( composed );
                assertTrue( composed instanceof ThrowingFunction );

            }

            @CsvSource({
                "2,3,17",
                "3,2,13"
            })        
            @ParameterizedTest(name="5 * {0} + {1} = {2}")
            public void the_composed_Function_should_return_as_expected(
                int extVal, int intVal, int expectedResult
            )
            {

                final ThrowingFunction<Integer,Integer> external = input -> input + extVal;
                final ThrowingFunction<Integer,Integer> internal = input -> input * intVal;

                final ThrowingFunction<Integer,Integer> composed = external.compose( internal );

                assertEquals( expectedResult, composed.apply(5) );

            }

            @Test
            public void if_internal_throws_Exception_should_be_propagated()
            {

                final Exception exception = getRandomException();
                final AtomicBoolean externalInvoked = new AtomicBoolean();
                final ThrowingFunction<Object,String> internal = input -> { throw exception; };
                final ThrowingFunction<String,Object> external = input ->
                { 
                    externalInvoked.set( true );
                    return true;
                };

                final ThrowingFunction<String,?> composed = external.compose( internal );
                final Exception thrown = assertThrows(
                    exception.getClass(), 
                    () -> composed.applyThrowing( "input" )
                );

                assertFalse( externalInvoked.get() );
                assertFalse( thrown instanceof RuntimeException );
                assertSame( exception, thrown );

            }

            @Test
            public void if_external_throws_Exception_should_be_propagated()
            {

                final Exception exception = getRandomException();
                final AtomicBoolean internalInvoked = new AtomicBoolean();
                final ThrowingFunction<Object,String> external = input -> { throw exception; };
                final ThrowingFunction<String,Object> internal = input ->
                { 
                    internalInvoked.set( true );
                    return true;
                };

                final ThrowingFunction<String,?> composed = external.compose( internal );
                final Exception thrown = assertThrows(
                    exception.getClass(), 
                    () -> composed.applyThrowing( "input" )
                );

                assertTrue( internalInvoked.get() );
                assertFalse( thrown instanceof RuntimeException );
                assertSame( exception, thrown );

            }
        }

        @Nested
        @DisplayName("with Function")
        public class WithFunction
        {

            @Test
            public void method_compose_should_return_a_ThrowingFunction()
            {

                final Function<String,Short> internal = input -> { return 5; };
                final ThrowingFunction<Short,Object> external  = input -> { return new Object(); };

                final Object composed = external.compose( internal );

                assertNotNull( composed );
                assertTrue( composed instanceof ThrowingFunction );

            }

            @CsvSource({
                "2,3,17",
                "3,2,13"
            })        
            @ParameterizedTest(name="5 * {0} + {1} = {2}")
            public void the_composed_Function_should_return_as_expected(
                int extVal, int intVal, int expectedResult
            )
            {

                final Function<Integer,Integer> internal = input -> input * intVal;
                final ThrowingFunction<Integer,Integer> external = input -> input + extVal;

                final ThrowingFunction<Integer,Integer> composed = external.compose( internal );

                assertEquals( expectedResult, composed.apply(5) );

            }

            @Test
            public void if_internal_throws_Exception_should_be_propagated()
            {

                final RuntimeException exception = new RuntimeException();
                final AtomicBoolean externalInvoked = new AtomicBoolean();
                final Function<Object,String> internal = input -> { throw exception; };
                final ThrowingFunction<String,Object> external = input ->
                { 
                    externalInvoked.set( true );
                    return true;
                };

                final ThrowingFunction<String,?> composed = external.compose( internal );
                final Exception thrown = assertThrows(
                    exception.getClass(), 
                    () -> composed.applyThrowing( "input" )
                );

                assertFalse( externalInvoked.get() );
                assertTrue( thrown instanceof RuntimeException );
                assertSame( exception, thrown );

            }

            @Test
            public void if_external_throws_Exception_should_be_propagated()
            {

                final Exception exception = getRandomException();
                final AtomicBoolean internalInvoked = new AtomicBoolean();
                final ThrowingFunction<Object,String> external = input -> { throw exception; };
                final ThrowingFunction<String,Object> internal = input ->
                { 
                    internalInvoked.set( true );
                    return true;
                };

                final ThrowingFunction<String,?> composed = external.compose( internal );
                final Exception thrown = assertThrows(
                    exception.getClass(), 
                    () -> composed.applyThrowing( "input" )
                );

                assertTrue( internalInvoked.get() );
                assertFalse( thrown instanceof RuntimeException );
                assertSame( exception, thrown );

            }
        }

    }


    @Nested
    @DisplayName("on method andThen()")
    public class AndThen
    {

        @Nested
        @DisplayName("with ThrowingFunction")
        public class WithThrowingFunction
        {

            @Test
            public void method_andThen_should_return_a_ThrowingFunction()
            {

                final ThrowingFunction<String,Short> internal = input -> { return 5; };
                final ThrowingFunction<Short,Object> external  = input -> { return new Object(); };

                final Object composed = internal.andThen( external );

                assertNotNull( composed );
                assertTrue( composed instanceof ThrowingFunction );

            }

            @CsvSource({
                "2,3,17",
                "3,2,13"
            })        
            @ParameterizedTest(name="5 * {0} + {1} = {2}")
            public void the_composed_Function_should_return_as_expected(
                int extVal, int intVal, int expectedResult
            )
            {

                final ThrowingFunction<Integer,Integer> external = input -> input + extVal;
                final ThrowingFunction<Integer,Integer> internal = input -> input * intVal;

                final ThrowingFunction<Integer,Integer> composed = internal.andThen( external );

                assertEquals( expectedResult, composed.apply(5) );

            }

            @Test
            public void if_internal_throws_Exception_should_be_propagated()
            {

                final Exception exception = getRandomException();
                final AtomicBoolean externalInvoked = new AtomicBoolean();
                final ThrowingFunction<String,Object> internal = input -> { throw exception; };
                final ThrowingFunction<Object,String> external = input ->
                { 
                    externalInvoked.set( true );
                    return "output";
                };

                final ThrowingFunction<String,?> composed = internal.andThen( external );
                final Exception thrown = assertThrows(
                    exception.getClass(), 
                    () -> composed.applyThrowing( "input" )
                );

                assertFalse( externalInvoked.get() );
                assertFalse( thrown instanceof RuntimeException );
                assertSame( exception, thrown );

            }

            @Test
            public void if_external_throws_Exception_should_be_propagated()
            {

                final Exception exception = getRandomException();
                final AtomicBoolean internalInvoked = new AtomicBoolean();
                final ThrowingFunction<Object,String> external = input -> { throw exception; };
                final ThrowingFunction<String,Object> internal = input ->
                { 
                    internalInvoked.set( true );
                    return true;
                };

                final ThrowingFunction<String,?> composed = internal.andThen( external );
                final Exception thrown = assertThrows(
                    exception.getClass(), 
                    () -> composed.applyThrowing( "input" )
                );

                assertTrue( internalInvoked.get() );
                assertFalse( thrown instanceof RuntimeException );
                assertSame( exception, thrown );

            }
        }

        @Nested
        @DisplayName("with Function")
        public class WithFunction
        {

            @Test
            public void method_compose_should_return_a_ThrowingFunction()
            {

                final ThrowingFunction<String,Short> internal = input -> { return 5; };
                final Function<Short,Object> external = input -> { return new Object(); };

                final Object composed = internal.andThen( external );

                assertNotNull( composed );
                assertTrue( composed instanceof ThrowingFunction );

            }

            @CsvSource({
                "2,3,17",
                "3,2,13"
            })        
            @ParameterizedTest(name="5 * {0} + {1} = {2}")
            public void the_composed_Function_should_return_as_expected(
                int extVal, int intVal, int expectedResult
            )
            {

                final ThrowingFunction<Integer,Integer> internal = input -> input * intVal;
                final Function<Integer,Integer> external = input -> input + extVal;

                final ThrowingFunction<Integer,Integer> composed = internal.andThen( external );

                assertEquals( expectedResult, composed.apply(5) );

            }

            @Test
            public void if_internal_throws_Exception_should_be_propagated()
            {

                final Exception exception = getRandomException();
                final AtomicBoolean externalInvoked = new AtomicBoolean();
                final ThrowingFunction<Object,String> internal = input -> { throw exception; };
                final Function<String,Object> external = input ->
                { 
                    externalInvoked.set( true );
                    return true;
                };

                final ThrowingFunction<Object,?> composed = internal.andThen( external );
                final Exception thrown = assertThrows(
                    exception.getClass(), 
                    () -> composed.applyThrowing( "input" )
                );

                assertFalse( externalInvoked.get() );
                assertFalse( thrown instanceof RuntimeException );
                assertSame( exception, thrown );

            }

            @Test
            public void if_external_throws_Exception_should_be_propagated()
            {

                final Exception exception = getRandomException();
                final AtomicBoolean internalInvoked = new AtomicBoolean();
                final ThrowingFunction<Object,String> external = input -> { throw exception; };
                final Function<String,Object> internal = input ->
                { 
                    internalInvoked.set( true );
                    return true;
                };

                final ThrowingFunction<String,?> composed = external.compose( internal );
                final Exception thrown = assertThrows(
                    exception.getClass(), 
                    () -> composed.applyThrowing( "input" )
                );

                assertTrue( internalInvoked.get() );
                assertFalse( thrown instanceof RuntimeException );
                assertSame( exception, thrown );

            }
        }

    }

}
