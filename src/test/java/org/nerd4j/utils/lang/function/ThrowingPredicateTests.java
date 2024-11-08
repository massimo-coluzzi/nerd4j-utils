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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Predicate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


/**
 * Test suite for the functional interface {@link ThrowingPredicate}.
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing functional interface: ThrowingPredicate")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ThrowingPredicateTests
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
    public void a_ThrowingPredicate_is_also_a_Predicate()
    {

        final ThrowingPredicate<?> predicate = input -> true;
        assertTrue( predicate instanceof Predicate );
        
    }


    @Test
    public void testThrowing_should_throw_a_checked_Exception()
    {

        final Exception exception = getRandomException();
        final ThrowingPredicate<String> predicate = input -> { throw exception; };

        final Exception thrown = assertThrows(
            exception.getClass(),
            () -> predicate.testThrowing( "input" )
        );

        assertFalse( thrown instanceof RuntimeException );
        assertSame( exception, thrown );
        
    }

    @Test
    public void if_the_original_error_is_a_RuntimeException_then_method_test_should_forward_the_original_exception()
    {

        final RuntimeException exception = getRandomRuntimeException();
        final ThrowingPredicate<String> predicate = input -> { throw exception; };

        final Exception thrown = assertThrows(
            exception.getClass(),
            () -> predicate.test( "input" )
        );

        assertSame( exception, thrown );
        
    }

    @Test
    public void if_the_original_error_is_a_checked_Exception_then_method_test_should_throw_a_new_RuntimeException()
    {

        final Exception exception = getRandomException();
        final ThrowingPredicate<String> predicate = input -> { throw exception; };

        final Exception thrown = assertThrows(
            RuntimeException.class,
            () -> predicate.test( "input" )
        );

        assertSame( exception, thrown.getCause() );
        
    }


    @Nested
    @DisplayName("on method and()")
    public class And
    {

        @Nested
        @DisplayName("with ThrowingPredicate")
        public class WithThrowingPredicate
        {

            @Test
            public void method_AND_should_return_a_ThrowingPredicate()
            {

                final ThrowingPredicate<String> before = input -> { return true; };
                final ThrowingPredicate<String> after  = input -> { return true; };

                final Object composed = before.and( after );
                assertNotNull( composed );
                assertTrue( composed instanceof ThrowingPredicate );

            }

            @CsvSource({
                "false,false,false",
                "false, true,false",
                " true,false,false",
                " true, true, true",
            })        
            @ParameterizedTest(name="{0} AND {1} = {2}")
            public void the_AND_predicate_should_return_as_expected(
                boolean beforeResult, boolean afterResult, boolean expectedResult
            )
            {

                final ThrowingPredicate<String> before = input -> beforeResult;
                final ThrowingPredicate<String> after  = input ->  afterResult;

                final Predicate<String> composed = before.and( after );

                assertEquals( expectedResult, composed.test("input") );

            }

            @Test
            public void if_before_throws_Exception_should_be_propagated()
            {

                final Exception exception = getRandomException();
                final ThrowingPredicate<String> before = input -> { throw exception; };
                final ThrowingPredicate<String> after  = input ->  true;

                final ThrowingPredicate<String> composed = before.and( after );

                final Exception thrown = assertThrows(
                    exception.getClass(), 
                    () -> composed.testThrowing( "input" )
                );

                assertFalse( thrown instanceof RuntimeException );
                assertSame( exception, thrown );

            }

            @Nested
            @DisplayName("on after throwing Exception")
            public class OnAfterThrowingException
            {

                @Test
                public void if_before_is_False_then_after_should_not_be_evaluated()
                {

                    final Exception exception = getRandomException();

                    final ThrowingPredicate<String> before = input -> false;
                    final ThrowingPredicate<String> after  = input -> { throw exception; };

                    final ThrowingPredicate<String> composed = before.and( after );
                    final boolean result = assertDoesNotThrow( 
                        () -> composed.testThrowing( "input" )
                    );

                    assertFalse( result );

                }

                @Test
                public void if_before_is_True_then_after_should_be_evaluated()
                {

                    final Exception exception = getRandomException();

                    final ThrowingPredicate<String> before = input ->  true;
                    final ThrowingPredicate<String> after  = input -> { throw exception; };

                    final ThrowingPredicate<String> composed = before.and( after );

                    final Exception thrown = assertThrows(
                        exception.getClass(), 
                        () -> composed.testThrowing( "input" )
                    );

                    assertFalse( thrown instanceof RuntimeException );
                    assertSame( exception, thrown );

                }
            }
        }

        @Nested
        @DisplayName("with Predicate")
        public class WithPredicate
        {

            @Test
            public void method_AND_should_return_a_ThrowingPredicate()
            {

                final ThrowingPredicate<String> before = input -> true;
                final Predicate<String> after = input -> true;

                final Object composed = before.and( after );
                assertNotNull( composed );
                assertTrue( composed instanceof ThrowingPredicate );

            }

            @CsvSource({
                "false,false,false",
                "false, true,false",
                " true,false,false",
                " true, true, true",
            })        
            @ParameterizedTest(name="{0} AND {1} = {2}")
            public void the_AND_predicate_should_return_as_expected(
                boolean beforeResult, boolean afterResult, boolean expectedResult
            )
            {

                final ThrowingPredicate<String> before = input -> beforeResult;
                final Predicate<String> after  = input ->  afterResult;

                final ThrowingPredicate<String> composed = before.and( after );
                final boolean result = assertDoesNotThrow( () -> composed.test("input") );
                assertEquals( expectedResult, result );

            }

            @Test
            public void if_before_throws_Exception_should_be_propagated()
            {

                final Exception exception = getRandomException();

                final ThrowingPredicate<String> before = input -> { throw exception; };
                final Predicate<String> after = input -> true;

                final ThrowingPredicate<String> composed = before.and( after );

                final Exception thrown = assertThrows(
                    exception.getClass(), 
                    () -> composed.testThrowing( "input" )
                );

                assertFalse( thrown instanceof RuntimeException );
                assertSame( exception, thrown );

            }

            @Nested
            @DisplayName("on after throwing Exception")
            public class OnAfterThrowingException
            {

                @Test
                public void if_before_is_False_then_after_should_not_be_evaluated()
                {

                    final RuntimeException exception = new RuntimeException();
                    final ThrowingPredicate<String> before = input -> false;
                    final Predicate<String> after  = input -> { throw exception; };

                    final ThrowingPredicate<String> composed = before.and( after );
                    final boolean result = assertDoesNotThrow( 
                        () -> composed.testThrowing( "input" )
                    );
                    assertFalse( result );

                }

                @Test
                public void if_before_is_True_then_after_should_be_evaluated()
                {

                    final RuntimeException exception = new RuntimeException();
                    final ThrowingPredicate<String> before = input ->  true;
                    final Predicate<String> after = input -> { throw exception; };

                    final ThrowingPredicate<String> composed = before.and( after );

                    final Exception thrown = assertThrows(
                        RuntimeException.class, 
                        () -> composed.testThrowing( "input" )
                    );

                    assertSame( exception, thrown );

                }

            }
        }

    }


    @Nested
    @DisplayName("on method or()")
    public class Or
    {

        @Nested
        @DisplayName("with ThrowingPredicate")
        public class WithThrowingPredicate
        {

            @Test
            public void method_OR_should_return_a_ThrowingPredicate()
            {

                final ThrowingPredicate<String> before = input -> { return true; };
                final ThrowingPredicate<String> after  = input -> { return true; };

                final Object composed = before.or( after );
                assertNotNull( composed );
                assertTrue( composed instanceof ThrowingPredicate );

            }

            @CsvSource({
                "false,false,false",
                "false, true, true",
                " true,false, true",
                " true, true, true",
            })        
            @ParameterizedTest(name="{0} OR {1} = {2}")
            public void the_OR_predicate_should_return_as_expected(
                boolean beforeResult, boolean afterResult, boolean expectedResult
            )
            {

                final ThrowingPredicate<String> before = input -> beforeResult;
                final ThrowingPredicate<String> after  = input ->  afterResult;

                final Predicate<String> composed = before.or( after );

                assertEquals( expectedResult, composed.test("input") );

            }

            @Test
            public void if_before_throws_Exception_should_be_propagated()
            {

                final Exception exception = getRandomException();
                final ThrowingPredicate<String> before = input -> { throw exception; };
                final ThrowingPredicate<String> after  = input ->  true;

                final ThrowingPredicate<String> composed = before.or( after );

                final Exception thrown = assertThrows(
                    exception.getClass(), 
                    () -> composed.testThrowing( "input" )
                );

                assertFalse( thrown instanceof RuntimeException );
                assertSame( exception, thrown );

            }

            @Nested
            @DisplayName("on after throwing Exception")
            public class OnAfterThrowingException
            {

                @Test
                public void if_before_is_True_then_after_should_not_be_evaluated()
                {

                    final Exception exception = getRandomException();

                    final ThrowingPredicate<String> before = input -> true;
                    final ThrowingPredicate<String> after  = input -> { throw exception; };

                    final ThrowingPredicate<String> composed = before.or( after );
                    final boolean result = assertDoesNotThrow( 
                        () -> composed.testThrowing( "input" )
                    );

                    assertTrue( result );

                }

                @Test
                public void if_before_is_False_then_after_should_be_evaluated()
                {

                    final Exception exception = getRandomException();

                    final ThrowingPredicate<String> before = input -> false;
                    final ThrowingPredicate<String> after  = input -> { throw exception; };

                    final ThrowingPredicate<String> composed = before.or( after );

                    final Exception thrown = assertThrows(
                        exception.getClass(), 
                        () -> composed.testThrowing( "input" )
                    );

                    assertFalse( thrown instanceof RuntimeException );
                    assertSame( exception, thrown );

                }
            }
        }

        @Nested
        @DisplayName("with Predicate")
        public class WithPredicate
        {

            @Test
            public void method_OR_should_return_a_ThrowingPredicate()
            {

                final ThrowingPredicate<String> before = input -> true;
                final Predicate<String> after = input -> true;

                final Object composed = before.or( after );
                assertNotNull( composed );
                assertTrue( composed instanceof ThrowingPredicate );

            }

            @CsvSource({
                "false,false,false",
                "false, true, true",
                " true,false, true",
                " true, true, true",
            })        
            @ParameterizedTest(name="{0} OR {1} = {2}")
            public void the_OR_predicate_should_return_as_expected(
                boolean beforeResult, boolean afterResult, boolean expectedResult
            )
            {

                final ThrowingPredicate<String> before = input -> beforeResult;
                final Predicate<String> after  = input ->  afterResult;

                final ThrowingPredicate<String> composed = before.or( after );
                final boolean result = assertDoesNotThrow( () -> composed.test("input") );
                assertEquals( expectedResult, result );

            }

            @Test
            public void if_before_throws_Exception_should_be_propagated()
            {

                final Exception exception = getRandomException();

                final ThrowingPredicate<String> before = input -> { throw exception; };
                final Predicate<String> after = input -> true;

                final ThrowingPredicate<String> composed = before.or( after );

                final Exception thrown = assertThrows(
                    exception.getClass(), 
                    () -> composed.testThrowing( "input" )
                );

                assertFalse( thrown instanceof RuntimeException );
                assertSame( exception, thrown );

            }

            @Nested
            @DisplayName("on after throwing Exception")
            public class OnAfterThrowingException
            {

                @Test
                public void if_before_is_True_then_after_should_not_be_evaluated()
                {

                    final RuntimeException exception = new RuntimeException();
                    final ThrowingPredicate<String> before = input -> true;
                    final Predicate<String> after  = input -> { throw exception; };

                    final ThrowingPredicate<String> composed = before.or( after );
                    final boolean result = assertDoesNotThrow( 
                        () -> composed.testThrowing( "input" )
                    );

                    assertTrue( result );

                }

                @Test
                public void if_before_is_False_then_after_should_be_evaluated()
                {

                    final RuntimeException exception = new RuntimeException();
                    final ThrowingPredicate<String> before = input -> false;
                    final Predicate<String> after = input -> { throw exception; };

                    final ThrowingPredicate<String> composed = before.or( after );

                    final Exception thrown = assertThrows(
                        RuntimeException.class, 
                        () -> composed.testThrowing( "input" )
                    );

                    assertSame( exception, thrown );

                }

            }
        
        }

    }

    @Nested
    @DisplayName("on method negate()")
    public class Negate
    {

        @Test
        public void method_NEGATE_should_return_a_ThrowingPredicate()
        {

            final ThrowingPredicate<String> source = input -> { return true; };
            final Object target = source.negate();

            assertNotNull( target );
            assertTrue( target instanceof ThrowingPredicate );

        }

        @CsvSource({
            "false, true",
            " true,false",
        })        
        @ParameterizedTest(name="NEGATE {0} = {1}")
        public void the_NEGATE_predicate_should_return_as_expected(
            boolean sourceResult, boolean expectedResult
        )
        {

            final ThrowingPredicate<String> source = input -> sourceResult;
            final Predicate<String> target = source.negate();

            assertEquals( expectedResult, target.test("input") );

        }

        @Test
        public void if_source_throws_Exception_so_does_its_negation()
        {

            final Exception exception = getRandomException();
            final ThrowingPredicate<String> source = input -> { throw exception; };
            final ThrowingPredicate<String> target = source.negate();

            final Exception thrown = assertThrows(
                exception.getClass(), 
                () -> target.testThrowing( "input" )
            );

                assertFalse( thrown instanceof RuntimeException );
                assertSame( exception, thrown );

        }
    }
}
