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
package org.nerd4j.utils.math;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for the utility class {@link CU}.
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing utility class: CU")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CUTests
{


    /**
     * Performs unit test on all methods of {@link CU}
     * invoking the default version of each method.
     */
    @Nested
    @DisplayName("default version (null is less than non-null)")
    public class DefaultVersion
    {

    	@CsvSource(value={
        	"null,1000,true",
			"null,null,false",
			"1000,null,false",
			"1000,2000,true",
			"1500,1500,false",
			"2000,1000,false"
        },nullValues="null")
        @ParameterizedTest(name="CU.lt({0},{1}) == {2}")
        public void method_lt_should_return_the_expected_value(
        	Integer a, Integer b, boolean expected
        )
        {

    		assertEquals( expected, CU.lt(a,b) );

        }

		@CsvSource(value={
			"null,1000,true",
			"null,null,true",
			"1000,null,false",
			"1000,2000,true",
			"1500,1500,true",
			"2000,1000,false"
		},nullValues="null")
		@ParameterizedTest(name="CU.le({0},{1}) == {2}")
		public void method_le_should_return_the_expected_value(
			Integer a, Integer b, boolean expected
		)
		{

			assertEquals( expected, CU.le(a,b) );

		}

		@CsvSource(value={
			"null,1000,false",
			"null,null,true",
			"1000,null,false",
			"1000,2000,false",
			"1500,1500,true",
			"2000,1000,false"
		},nullValues="null")
		@ParameterizedTest(name="CU.eq({0},{1}) == {2}")
		public void method_eq_should_return_the_expected_value(
			Integer a, Integer b, boolean expected
		)
		{

			assertEquals( expected, CU.eq(a,b) );

		}

		@CsvSource(value={
			"null,1000,true",
			"null,null,false",
			"1000,null,true",
			"1000,2000,true",
			"1500,1500,false",
			"2000,1000,true"
		},nullValues="null")
		@ParameterizedTest(name="CU.ne({0},{1}) == {2}")
		public void method_ne_should_return_the_expected_value(
			Integer a, Integer b, boolean expected
		)
		{

			assertEquals( expected, CU.ne(a,b) );

		}

		@CsvSource(value={
			"null,1000,false",
			"null,null,true",
			"1000,null,true",
			"1000,2000,false",
			"1500,1500,true",
			"2000,1000,true"
		},nullValues="null")
		@ParameterizedTest(name="CU.ge({0},{1}) == {2}")
		public void method_ge_should_return_the_expected_value(
			Integer a, Integer b, boolean expected
		)
		{

			assertEquals( expected, CU.ge(a,b) );

		}

		@CsvSource(value={
			"null,1000,false",
			"null,null,false",
			"1000,null,true",
			"1000,2000,false",
			"1500,1500,false",
			"2000,1000,true"
		},nullValues="null")
		@ParameterizedTest(name="CU.gt({0},{1}) == {2}")
		public void method_gt_should_return_the_expected_value(
			Integer a, Integer b, boolean expected
		)
		{

			assertEquals( expected, CU.gt(a,b) );

		}

		@CsvSource(value={
			"null,1000,null",
			"null,null,null",
			"1000,null,null",
			"1000,2000,1000",
			"1500,1500,1500",
			"2000,1000,1000"
		},nullValues="null")
		@ParameterizedTest(name="CU.min({0},{1}) == {2}")
		public void method_min_should_return_the_expected_value(
			Integer a, Integer b, Integer expected
		)
		{

			assertEquals( expected, CU.min(a,b) );

		}

		@CsvSource(value={
			"null,1000,1000",
			"null,null,null",
			"1000,null,1000",
			"1000,2000,2000",
			"1500,1500,1500",
			"2000,1000,2000"
		},nullValues="null")
		@ParameterizedTest(name="CU.max({0},{1}) == {2}")
		public void method_max_should_return_the_expected_value(
			Integer a, Integer b, Integer expected
		)
		{

			assertEquals( expected, CU.max(a,b) );

		}

    }

    /**
     * Performs unit test on all methods of {@link CU}
     * assuming {@code nullFirst} to be {@code true}.
     */
    @Nested
    @DisplayName("null is less than non-null")
    public class NullIsLessThanNonNull
    {

    	@CsvSource(value={
        	"null,1000,true",
			"null,null,false",
			"1000,null,false",
			"1000,2000,true",
			"1500,1500,false",
			"2000,1000,false"
        },nullValues="null")
        @ParameterizedTest(name="CU.lt({0},{1},true) == {2}")
        public void method_lt_should_return_the_expected_value(
        	Integer a, Integer b, boolean expected
        )
        {

    		assertEquals( expected, CU.lt(a,b,false) );

        }

		@CsvSource(value={
			"null,1000,true",
			"null,null,true",
			"1000,null,false",
			"1000,2000,true",
			"1500,1500,true",
			"2000,1000,false"
		},nullValues="null")
		@ParameterizedTest(name="CU.le({0},{1},true) == {2}")
		public void method_le_should_return_the_expected_value(
			Integer a, Integer b, boolean expected
		)
		{

			assertEquals( expected, CU.le(a,b,false) );

		}

		@CsvSource(value={
			"null,1000,false",
			"null,null,true",
			"1000,null,false",
			"1000,2000,false",
			"1500,1500,true",
			"2000,1000,false"
		},nullValues="null")
		@ParameterizedTest(name="CU.eq({0},{1},true) == {2}")
		public void method_eq_should_return_the_expected_value(
			Integer a, Integer b, boolean expected
		)
		{

			assertEquals( expected, CU.eq(a,b,false) );

		}

		@CsvSource(value={
			"null,1000,true",
			"null,null,false",
			"1000,null,true",
			"1000,2000,true",
			"1500,1500,false",
			"2000,1000,true"
		},nullValues="null")
		@ParameterizedTest(name="CU.ne({0},{1},true) == {2}")
		public void method_ne_should_return_the_expected_value(
			Integer a, Integer b, boolean expected
		)
		{

			assertEquals( expected, CU.ne(a,b,false) );

		}

		@CsvSource(value={
			"null,1000,false",
			"null,null,true",
			"1000,null,true",
			"1000,2000,false",
			"1500,1500,true",
			"2000,1000,true"
		},nullValues="null")
		@ParameterizedTest(name="CU.ge({0},{1},true) == {2}")
		public void method_ge_should_return_the_expected_value(
			Integer a, Integer b, boolean expected
		)
		{

			assertEquals( expected, CU.ge(a,b,false) );

		}

		@CsvSource(value={
			"null,1000,false",
			"null,null,false",
			"1000,null,true",
			"1000,2000,false",
			"1500,1500,false",
			"2000,1000,true"
		},nullValues="null")
		@ParameterizedTest(name="CU.gt({0},{1},true) == {2}")
		public void method_gt_should_return_the_expected_value(
			Integer a, Integer b, boolean expected
		)
		{

			assertEquals( expected, CU.gt(a,b,false) );

		}

		@CsvSource(value={
			"null,1000,null",
			"null,null,null",
			"1000,null,null",
			"1000,2000,1000",
			"1500,1500,1500",
			"2000,1000,1000"
		},nullValues="null")
		@ParameterizedTest(name="CU.min({0},{1},true) == {2}")
		public void method_min_should_return_the_expected_value(
			Integer a, Integer b, Integer expected
		)
		{

			assertEquals( expected, CU.min(a,b,false) );

		}

		@CsvSource(value={
			"null,1000,1000",
			"null,null,null",
			"1000,null,1000",
			"1000,2000,2000",
			"1500,1500,1500",
			"2000,1000,2000"
		},nullValues="null")
		@ParameterizedTest(name="CU.max({0},{1},true) == {2}")
		public void method_max_should_return_the_expected_value(
			Integer a, Integer b, Integer expected
		)
		{

			assertEquals( expected, CU.max(a,b,false) );

		}

    }

    /**
     * Performs unit test on all methods of {@link CU}
     * assuming {@code nullFirst} to be {@code false}.
     */
    @Nested
    @DisplayName("null is greater than non-null")
    public class NullIsGreaterThanNonNull
    {

    	@CsvSource(value={
        	"null,1000,false",
			"null,null,false",
			"1000,null,true",
			"1000,2000,true",
			"1500,1500,false",
			"2000,1000,false"
        },nullValues="null")
        @ParameterizedTest(name="CU.lt({0},{1},false) == {2}")
        public void method_lt_should_return_the_expected_value(
        	Integer a, Integer b, boolean expected
        )
        {

    		assertEquals( expected, CU.lt(a,b,true) );

        }

		@CsvSource(value={
			"null,1000,false",
			"null,null,true",
			"1000,null,true",
			"1000,2000,true",
			"1500,1500,true",
			"2000,1000,false"
		},nullValues="null")
		@ParameterizedTest(name="CU.le({0},{1},false) == {2}")
		public void method_le_should_return_the_expected_value(
			Integer a, Integer b, boolean expected
		)
		{

			assertEquals( expected, CU.le(a,b,true) );

		}

		@CsvSource(value={
			"null,1000,false",
			"null,null,true",
			"1000,null,false",
			"1000,2000,false",
			"1500,1500,true",
			"2000,1000,false"
		},nullValues="null")
		@ParameterizedTest(name="CU.eq({0},{1},false) == {2}")
		public void method_eq_should_return_the_expected_value(
			Integer a, Integer b, boolean expected
		)
		{

			assertEquals( expected, CU.eq(a,b,true) );

		}

		@CsvSource(value={
			"null,1000,true",
			"null,null,false",
			"1000,null,true",
			"1000,2000,true",
			"1500,1500,false",
			"2000,1000,true"
		},nullValues="null")
		@ParameterizedTest(name="CU.ne({0},{1},false) == {2}")
		public void method_ne_should_return_the_expected_value(
			Integer a, Integer b, boolean expected
		)
		{

			assertEquals( expected, CU.ne(a,b,true) );

		}

		@CsvSource(value={
			"null,1000,true",
			"null,null,true",
			"1000,null,false",
			"1000,2000,false",
			"1500,1500,true",
			"2000,1000,true"
		},nullValues="null")
		@ParameterizedTest(name="CU.ge({0},{1},false) == {2}")
		public void method_ge_should_return_the_expected_value(
			Integer a, Integer b, boolean expected
		)
		{

			assertEquals( expected, CU.ge(a,b,true) );

		}

		@CsvSource(value={
			"null,1000,true",
			"null,null,false",
			"1000,null,false",
			"1000,2000,false",
			"1500,1500,false",
			"2000,1000,true"
		},nullValues="null")
		@ParameterizedTest(name="CU.gt({0},{1},false) == {2}")
		public void method_gt_should_return_the_expected_value(
			Integer a, Integer b, boolean expected
		)
		{

			assertEquals( expected, CU.gt(a,b,true) );

		}

		@CsvSource(value={
			"null,1000,1000",
			"null,null,null",
			"1000,null,1000",
			"1000,2000,1000",
			"1500,1500,1500",
			"2000,1000,1000"
		},nullValues="null")
		@ParameterizedTest(name="CU.min({0},{1},false) == {2}")
		public void method_min_should_return_the_expected_value(
			Integer a, Integer b, Integer expected
		)
		{

			assertEquals( expected, CU.min(a,b,true) );

		}

		@CsvSource(value={
			"null,1000,null",
			"null,null,null",
			"1000,null,null",
			"1000,2000,2000",
			"1500,1500,1500",
			"2000,1000,2000"
		},nullValues="null")
		@ParameterizedTest(name="CU.max({0},{1},false) == {2}")
		public void method_max_should_return_the_expected_value(
			Integer a, Integer b, Integer expected
		)
		{

			assertEquals( expected, CU.max(a,b,true) );

		}

    }

}
