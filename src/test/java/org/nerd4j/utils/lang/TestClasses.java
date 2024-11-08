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

import java.util.stream.Stream;

/**
 * This class contains the definition of test classes used
 * in tests of the {@link org.nerd4j.utils.lang} package.
 *
 * @author Massimo Coluzzi
 */
public class TestClasses
{

    /**
     * Represents a generic interface with some meaningful methods.
     */
    public interface SomeInterface
    {

        int getId();

        String getString();

    }

    /**
     * Represents a generic type with some meaningful fields
     * that implements {@link SomeInterface}.
     *
     */
    public static class SomeType implements SomeInterface
    {

        public int id;

        public String string;

        public int[] intArray;

        public String[] stringArray;

        public int[][] intMatrix;

        public String[][] stringMatrix;


        public SomeType()
        {

            super();

            this.id = 1;
            this.string = "string";
            this.intArray = new int[]{1, 2, 3};
            this.intMatrix = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
            this.stringArray = new String[]{"1", "2", "3"};
            this.stringMatrix = new String[][]{{"1", "2", "3"}, {"4", "5", "6"}, {"7", "8", "9"}};

        }

        @Override
        public int getId()
        {
            return id;
        }

        @Override
        public String getString()
        {
            return string;
        }

        @Override
        public int hashCode()
        {

            return Hashcode.of(
                    this.id,
                    this.string,
                    this.intArray,
                    this.intMatrix,
                    this.stringArray,
                    this.stringMatrix
            );

        }

        @Override
        public boolean equals(Object other)
        {

            return Equals.ifSameClass(
                    this, other,
                    o -> o.id,
                    o -> o.string,
                    o -> o.intArray,
                    o -> o.intMatrix,
                    o -> o.stringArray,
                    o -> o.stringMatrix
            );
        }

    }

    /**
     * Some other type that implements the same {@link SomeInterface}.
     *
     */
    public static class SomeOtherType implements SomeInterface
    {

        public int id;

        public String string;

        public SomeOtherType()
        {

            super();

            this.id = 1;
            this.string = "string";

        }

        @Override
        public int getId() { return id; }

        @Override
        public String getString() { return string; }

    }


    /**
     * Represents a class that extends {@link SomeType}.
     *
     */
    public static class ExtendsSomeType extends SomeType
    {}


    
    /**
     * A class that implements {@link Emptily}
     *
     */
    public static class EmptilyType implements Emptily
    {

        private boolean isEmpty;

        public EmptilyType(boolean isEmpty )
        {

            super();

            this.isEmpty = isEmpty;

        }

        @Override
        public boolean isEmpty()
        {

            return isEmpty;

        }

    }

    /**
     * A class that implements {@link Comparative}
     *
     */
    public static class ComparativeType implements Comparative<ComparativeType>
    {
    	
    	private int value;
    	
    	public ComparativeType( int value )
    	{
    		
    		super();
    		
    		this.value = value;
    		
    	}
    	
    	@Override
    	public int compareTo( ComparativeType other )
    	{
    		
    		return value - other.value;
    		
    	}
    	
    }
    

    /* **************** */
    /*  HELPER METHODS  */
    /* **************** */


    /**
     * Test cases for parametrized tests.
     *
     * @return stream of test cases.
     */
    public static Stream<SomeType> parametrizedTestCases()
    {

        final SomeType a = new SomeType();
        a.id = 2;

        final SomeType b = new SomeType();
        b.string = "another string";

        final SomeType c = new SomeType();
        c.intArray = new int[]{1, 0, 3};

        final SomeType d = new SomeType();
        d.stringArray = new String[]{"1", "0", "3"};

        final SomeType e = new SomeType();
        e.intMatrix = new int[][]{{1, 2, 3}, {4, 0, 6}, {7, 8, 9}};

        final SomeType f = new SomeType();
        f.stringMatrix = new String[][]{{"1", "2", "3"}, {"4", "0", "6"}, {"7", "8", "9"}};

        return Stream.of(a, b, c, d, e, f);

    }

}
