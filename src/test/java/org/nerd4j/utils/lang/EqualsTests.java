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

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.nerd4j.utils.lang.TestClasses.*;

import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the utility class {@link Equals}.
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing utility class: Equals")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class EqualsTests
{


    /* ************************* */
    /*  TEST METHOD ifSameClass  */
    /* ************************* */


    /**
     * Performs unit test on the method {@link Equals#ifSameClass(Object, Object, Function[])}.
     */
    @Nested
    @DisplayName("on invoking method: ifSameClass(Object, Object, Function[])")
    public class IfSameClass
    {

        /**
         * Performs unit test on the method {@link Equals#ifSameClass(Object, Object, Function[])}
         * where there are not check to be performed on fields.
         */
        @Nested
        public class with_NO_checks_on_fields
        {

            @Test
            public void null_instances_should_always_equals()
            {

                assertTrue( Equals.ifSameClass(null, null) );

            }

            @Test
            public void if_only_one_is_null_always_fails()
            {

                final SomeType instance = new SomeType();

                assertAll(
                    () -> assertFalse( Equals.ifSameClass(null, instance) ),
                    () -> assertFalse( Equals.ifSameClass(instance, null) )
                );

            }

            @Test
            public void same_instance_should_always_equals()
            {

                final SomeType instance = new SomeType();
                assertTrue( Equals.ifSameClass(instance, instance) );

            }

            @Test
            public void instances_with_the_same_class_should_equals()
            {

                final SomeType a = new SomeType();
                final SomeType b = new SomeType();

                assertTrue( Equals.ifSameClass(a, b) );

            }

            @Test
            public void instances_with_different_classes_always_fails()
            {

                final Object o   = new Object();
                final SomeType a = new SomeType();

                assertFalse( Equals.ifSameClass(a, o) );

            }

            @Test
            public void instances_with_different_classes_in_the_same_hierarchy_also_fails()
            {

                final SomeType a = new SomeType();
                final SomeType b = new ExtendsSomeType();

                assertFalse( Equals.ifSameClass(a, b) );

            }

        }


        /**
         * Performs unit test on the method {@link Equals#ifSameClass(Object, Object, Function[])}
         * where there are check to be performed on fields.
         */
        @Nested
        public class with_checks_on_fields
        {

            @Test
            public void null_instances_should_always_equals()
            {

                assertTrue( Equals.ifSameClass(null, null, Object::toString) );

            }

            @Test
            public void if_only_one_is_null_always_fails()
            {

                final SomeType instance = new SomeType();

                assertAll(
                    () -> assertFalse( Equals.ifSameClass(null, instance, checkAllFields) ),
                    () -> assertFalse( Equals.ifSameClass(instance, null, checkAllFields) )
                );

            }

            @Test
            public void same_instance_should_always_equals()
            {

                final SomeType instance = new SomeType();
                assertTrue( Equals.ifSameClass(instance, instance, checkAllFields) );

            }

            @Test
            public void instances_with_the_same_class_should_equal_if_selected_fields_are_equal()
            {

                final SomeType a = new SomeType();
                final SomeType b = new SomeType();

                assertTrue( Equals.ifSameClass(a, b, checkAllFields) );

            }

            @ParameterizedTest
            @MethodSource("org.nerd4j.utils.lang.TestClasses#parametrizedTestCases")
            public void if_selected_fields_differ_should_not_equal( SomeType other )
            {

                final SomeType value = new SomeType();
                assertFalse( Equals.ifSameClass( value, other, checkAllFields) );

            }

            @Test
            public void instances_with_different_classes_always_fails() {

                final SomeType a = new SomeType();
                final SomeOtherType b = new SomeOtherType();

                assertFalse(Equals.ifSameClass(a, b, checkAllFields));

            }

            @Test
            public void instances_with_different_classes_in_the_same_hierarchy_also_fails() {

                final SomeType a = new SomeType();
                final SomeType b = new ExtendsSomeType();

                assertFalse(Equals.ifSameClass(a, b, checkAllFields));

            }

        }

        @Nested
        @DisplayName("using an array as argument")
        public class TestArrays
        {

            private class Model
            {
                public Object value;

                public Model( Object value )
                {
                    super();
                    this.value = value;
                }
            }

            @Test
            public void test_expected_values_for_arrays()
            {

                final Model objects1 = new Model( new Object[] {"1","2","3"} );
                assertTrue( Equals.ifSameClass(objects1, objects1, m -> m.value) );
                final Model objects2 = new Model( new Object[] {"3","2","1"} );
                assertFalse( Equals.ifSameClass(objects1, objects2, m -> m.value) );
                
                final Model bytes1 = new Model( new byte[] {1,2,3} );
                assertTrue( Equals.ifSameClass(bytes1, bytes1, m -> m.value) );
                final Model bytes2 = new Model( new byte[] {3,2,1} );
                assertFalse( Equals.ifSameClass(bytes1, bytes2, m -> m.value) );

                final Model shorts1 = new Model( new short[] {1,2,3} );
                assertTrue( Equals.ifSameClass(shorts1, shorts1, m -> m.value) );
                final Model shorts2 = new Model( new short[] {3,2,1} );
                assertFalse( Equals.ifSameClass(shorts1, shorts2, m -> m.value) );

                final Model ints1 = new Model( new int[] {1,2,3} );
                assertTrue( Equals.ifSameClass(ints1, ints1, m -> m.value) );
                final Model ints2 = new Model( new int[] {3,2,1} );
                assertFalse( Equals.ifSameClass(ints1, ints2, m -> m.value) );

                final Model longs1 = new Model( new long[] {1,2,3} );
                assertTrue( Equals.ifSameClass(longs1, longs1, m -> m.value) );
                final Model longs2 = new Model( new long[] {3,2,1} );
                assertFalse( Equals.ifSameClass(longs1, longs2, m -> m.value) );

                final Model floats1 = new Model( new float[] {1,2,3} );
                assertTrue( Equals.ifSameClass(floats1, floats1, m -> m.value) );
                final Model floats2 = new Model( new float[] {3,2,1} );
                assertFalse( Equals.ifSameClass(floats1, floats2, m -> m.value) );

                final Model doubles1 = new Model( new double[] {1,2,3} );
                assertTrue( Equals.ifSameClass(doubles1, doubles1, m -> m.value) );
                final Model doubles2 = new Model( new double[] {3,2,1} );
                assertFalse( Equals.ifSameClass(doubles1, doubles2, m -> m.value) );

                final Model chars1 = new Model( new char[] {'1','2','3'} );
                assertTrue( Equals.ifSameClass(chars1, chars1, m -> m.value) );
                final Model chars2 = new Model( new char[] {'3','2','1'} );
                assertFalse( Equals.ifSameClass(chars1, chars2, m -> m.value) );

                final Model booleans1 = new Model( new boolean[] {true,false} );
                assertTrue( Equals.ifSameClass(booleans1, booleans1, m -> m.value) );
                final Model booleans2 = new Model( new boolean[] {false,true} );
                assertFalse( Equals.ifSameClass(booleans1, booleans2, m -> m.value) );

            }

            @Test
            public void arrays_with_different_sizes_should_not_be_equal()
            {

                final Model array1 = new Model( new Object[] {"1","2"} );
                final Model array2 = new Model( new Object[] {"1","2","3"} );
                assertFalse( Equals.ifSameClass(array1, array2, m -> m.value) );

            }

        }

    }


    /* *************************** */
    /*  TEST METHOD ifInstancesOf  */
    /* *************************** */


    /**
     * Performs unit test on the method {@link Equals#ifInstancesOf(Class,Object,Object,BiFunction)}.
     */
    @Nested
    @DisplayName("on invoking method: ifInstancesOf(Class,Object,Object,BiFunction)")
    public class IfInstancesOf
    {

        /**
         * Performs unit test on the method {@link Equals#ifInstancesOf(Class,Object,Object,BiFunction)}
         * where there are not check to be performed on fields.
         */
        @Nested
        public class with_NO_checks_on_fields
        {

            @Test
            public void null_instances_should_always_equals()
            {

                assertTrue( Equals.ifInstancesOf( Object.class, null, null,null ) );

            }

            @Test
            public void if_only_one_is_null_always_fails()
            {

                final SomeType instance = new SomeType();

                assertAll(
                        () -> assertFalse( Equals.ifInstancesOf( SomeType.class, null, instance, null ) ),
                        () -> assertFalse( Equals.ifInstancesOf( SomeType.class, instance, null, null ) )
                );

            }

            @Test
            public void same_instance_implementing_the_requested_type_should_equals()
            {

                final SomeType instance = new SomeType();
                assertTrue( Equals.ifInstancesOf( SomeType.class, instance, instance, null ) );

            }

            @Test
            public void same_instance_not_implementing_the_requested_type_should_fail()
            {

                final SomeType instance = new SomeType();
                assertFalse( Equals.ifInstancesOf(ExtendsSomeType.class, instance, instance, null ) );

            }

            @Test
            public void instances_with_the_same_requested_class_should_equals()
            {

                final SomeType a = new SomeType();
                final SomeType b = new SomeType();

                assertTrue( Equals.ifInstancesOf( SomeType.class, a, b, null ) );

            }

            @Test
            public void instances_with_the_same_not_requested_class_should_fail()
            {

                final SomeType a = new SomeType();
                final SomeType b = new SomeType();

                assertFalse( Equals.ifInstancesOf( ExtendsSomeType.class, a, b, null ) );

            }

            @Test
            public void instances_not_in_the_same_hierarchy_always_fails()
            {

                final Object o = new Object();
                final SomeType a = new SomeType();

                assertFalse( Equals.ifInstancesOf(SomeType.class, a, o, null) );

            }

            @Test
            public void instances_with_different_classes_in_the_same_requested_hierarchy_should_equals()
            {

                final SomeType a = new SomeType();
                final ExtendsSomeType b = new ExtendsSomeType();

                assertTrue( Equals.ifInstancesOf(SomeType.class, a, b, null) );

            }

            @Test
            public void instances_with_different_classes_in_the_same_not_requested_hierarchy_should_fail()
            {

                final SomeType a = new SomeType();
                final ExtendsSomeType b = new ExtendsSomeType();

                assertFalse( Equals.ifInstancesOf(ExtendsSomeType.class, a, b, null) );

            }

        }


        /**
         * Performs unit test on the method {@link Equals#ifInstancesOf(Class,Object,Object,BiFunction)}
         * where there are check to be performed on fields.
         */
        @Nested
        public class with_checks_on_fields
        {

            @Test
            public void null_instances_should_always_equals()
            {

                assertTrue(
                    Equals.ifInstancesOf( Object.class, null, null,
                        (a, b) -> a.toString().equals( b.toString() )
                    )
                );

            }

            @Test
            public void if_only_one_is_null_always_fails()
            {

                final SomeType instance = new SomeType();

                assertAll(
                    () -> assertFalse(
                        Equals.ifInstancesOf( SomeType.class, null, instance,
                            (a, b) -> a.toString().equals( b.toString() )
                        )
                    ),
                    () -> assertFalse(
                        Equals.ifInstancesOf( SomeType.class, instance, null,
                            (a, b) -> a.toString().equals( b.toString() )
                        )
                    )
                );

            }

            @Test
            public void same_instance_implementing_the_requested_type_should_equals()
            {

                final SomeType instance = new SomeType();
                assertTrue(
                    Equals.ifInstancesOf(
                        SomeType.class, instance, instance, (a, b) -> a.toString().equals( b.toString() )
                    )
                );

            }

            @Test
            public void same_instance_not_implementing_the_requested_type_should_fail()
            {

                final SomeType instance = new SomeType();
                assertFalse(
                    Equals.ifInstancesOf(
                        ExtendsSomeType.class, instance, instance, (a, b) -> a.toString().equals( b.toString() )
                    )
                );

            }

            @Test
            public void instances_with_the_same_requested_class_should_equals()
            {

                final SomeType a = new SomeType();
                final SomeOtherType b = new SomeOtherType();
                b.id = a.id;
                b.string = a.string;

                assertTrue(
                    Equals.ifInstancesOf(
                        SomeInterface.class, a, b, defaultInstanceCheck()
                    )
                );

            }

            @Test
            public void instances_with_the_same_not_requested_class_should_fail()
            {

                final SomeInterface a = new SomeType();
                final SomeInterface b = new SomeType();

                assertFalse(
                    Equals.ifInstancesOf(
                        ExtendsSomeType.class, a, b, defaultInstanceCheck()
                    )
                );

            }

            @Test
            public void instances_not_in_the_same_hierarchy_always_fails()
            {

                final Object o = new Object();
                final SomeType a = new SomeType();

                assertFalse( Equals.ifInstancesOf(SomeType.class, a, o, defaultInstanceCheck()) );

            }

            @Test
            public void instances_with_different_classes_in_the_same_requested_hierarchy_should_equals()
            {

                final SomeType a = new SomeType();
                final ExtendsSomeType b = new ExtendsSomeType();

                assertTrue( Equals.ifInstancesOf(SomeInterface.class, a, b, defaultInstanceCheck()) );

            }

            @Test
            public void instances_with_different_classes_in_the_same_not_requested_hierarchy_should_fail()
            {

                final SomeType a = new SomeType();
                final ExtendsSomeType b = new ExtendsSomeType();

                assertFalse( Equals.ifInstancesOf(ExtendsSomeType.class, a, b, defaultInstanceCheck()) );

            }

        }

    }


    /* **************** */
    /*  HELPER METHODS  */
    /* **************** */


    /**
     * Array of functions used to extract any field of the {@link SomeType} class.
     */
    private static final Function<SomeType,Object>[] checkAllFields = buildCheckAllFields();
	private static Function<SomeType,Object>[] buildCheckAllFields()
    {

		@SuppressWarnings("unchecked")
        final Function<SomeType,Object>[] array = new Function[6];
        array[0] = o -> o.id;
        array[1] = o -> o.string;
        array[2] = o -> o.intArray;
        array[3] = o -> o.intMatrix;
        array[4] = o -> o.stringArray;
        array[5] = o -> o.stringMatrix;

        return  array;

    }

    /**
     * Default function to check equality against {@link SomeInterface}.
     */
    private static <T extends SomeInterface> BiFunction<T,T,Boolean> defaultInstanceCheck()
    {

        return (a,b) -> a.getId() == b.getId() && a.getString().equals( b.getString() );

    }

}
