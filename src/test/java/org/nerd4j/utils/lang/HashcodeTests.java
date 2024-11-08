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
package org.nerd4j.utils.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.nerd4j.utils.lang.TestClasses.SomeType;

/**
 * Test suite for the utility class {@link Hashcode}.
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing utility class: Hashcode")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class HashcodeTests
{


    /* ************************ */
    /*  TEST METHOD of(Object)  */
    /* ************************ */

    /**
     * Performs unit test on the method {@link Hashcode#of(Object)}.
     */
    @Nested
    @DisplayName("on invoking method: of(Object)")
    public class WithOneArgument
    {

        @Test
        public void the_hashcode_of_null_is_0()
        {

            assertEquals(Hashcode.of(null), 0);

        }

        @Test
        public void the_same_object_must_have_the_same_hashcode()
        {

            final Object object = new Object();

            final int first  = Hashcode.of( object );
            final int second = Hashcode.of( object );

            assertEquals(first, second);

        }

        @Test
        public void if_two_objects_are_equal_must_have_the_same_hashcode()
        {

            final Long a = Long.valueOf( 10000 );
            final Long b = Long.valueOf( 10000 );

            assertNotSame( a, b );
            assertEquals( a, b );

            final int acode = Hashcode.of( a );
            final int bcode = Hashcode.of( b );

            assertEquals( acode, bcode );

        }

    }


    /* ********************************** */
    /*  TEST METHOD of(Object,Object...)  */
    /* ********************************** */

    /**
     * Performs unit test on the method {@link Hashcode#of(Object,Object...)}.
     */
    @Nested
    @DisplayName("on invoking method: of(Object,Object...)")
    public class WithMoreArguments
    {

        @Test
        public void the_hashcode_of_nulls_is_0()
        {

            assertEquals(Hashcode.of(null, null, null), 0);

        }

        @Test
        public void the_same_objects_must_have_the_same_hashcode()
        {

            final Object a = new Object();
            final Object b = new Object();
            final Object c = new Object();

            final int first = Hashcode.of(a, b, c);
            final int second = Hashcode.of(a, b, c);

            assertEquals(first, second);

        }

        @Test
        public void if_two_objects_are_equal_must_have_the_same_hashcode()
        {

            final Short sa = Short.valueOf( (short) 10000 );
            final Short sb = Short.valueOf( (short) 10000 );

            final Long la = Long.valueOf( 10000 );
            final Long lb = Long.valueOf( 10000 );

            assertNotSame( sa, sb );
            assertNotSame( la, lb );
            assertEquals(  sa, sb );
            assertEquals(  la, lb );

            final int acode = Hashcode.of( sa, la );
            final int bcode = Hashcode.of( sb, lb );

            assertEquals(acode, bcode);

        }

        @ParameterizedTest
        @MethodSource("org.nerd4j.utils.lang.TestClasses#parametrizedTestCases")
        public void if_two_objects_have_different_fields_must_have_different_hashcodes( SomeType other )
        {


            final SomeType type = new SomeType();
            assertNotEquals(type.hashCode(), other.hashCode());

        }

    }


    /* ************* */
    /*  TEST ARRAYS  */
    /* ************* */


    @Nested
    @DisplayName("using an array as argument")
    public class TestArrays
    {

        @Test
        public void test_expected_values_for_arrays()
        {

            final Object[] objects = new Object[] { "1", "2", "3" };
            assertEquals( 79 * Arrays.deepHashCode(objects), Hashcode.of(objects) );
            
            final byte[] bytes = new byte[] { 1, 2, 3 };
            assertEquals( 79 * Arrays.hashCode(bytes), Hashcode.of(bytes) );

            final short[] shorts = new short[] { 1, 2, 3 };
            assertEquals( 79 * Arrays.hashCode(shorts), Hashcode.of(shorts) );

            final int[] ints = new int[] { 1, 2, 3 };
            assertEquals( 79 * Arrays.hashCode(ints), Hashcode.of(ints) );

            final long[] longs = new long[] { 1, 2, 3 };
            assertEquals( 79 * Arrays.hashCode(longs), Hashcode.of(longs) );

            final float[] floats = new float[] { 1, 2, 3 };
            assertEquals( 79 * Arrays.hashCode(floats), Hashcode.of(floats) );

            final double[] doubles = new double[] { 1, 2, 3 };
            assertEquals( 79 * Arrays.hashCode(doubles), Hashcode.of(doubles) );

            final char[] chars = new char[] { '1', '2', '3' };
            assertEquals( 79 * Arrays.hashCode(chars), Hashcode.of(chars) );

            final boolean[] booleans = new boolean[] { true, false };
            assertEquals( 79 * Arrays.hashCode(booleans), Hashcode.of(booleans) );

        }

    }


    /* ****************** */
    /*  TEST CONSISTENCY  */
    /* ****************** */


    /**
     * Performs consistency checks between {@link Hashcode}
     * and {@link Equals}.
     */
    @Nested
    @DisplayName("about consistency between Hashcode and Equals")
    public class AboutConsistency
    {

        @Test
        public void if_two_objects_are_equals_must_have_the_same_hashcode()
        {

            final SomeType a = new SomeType();
            final SomeType b = new SomeType();

            assertEquals( a, b );
            assertEquals( a.hashCode(), b.hashCode() );

        }

        @Test
        public void if_two_objects_have_different_hashcodes_must_NOT_equals()
        {

            final SomeType a = new SomeType();
            final SomeType b = new SomeType();
            b.intMatrix[1][1] = -1;

            assertNotEquals( a.hashCode(), b.hashCode() );
            assertNotEquals( a, b );

        }

    }

}
