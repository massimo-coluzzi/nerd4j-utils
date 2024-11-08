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
package org.nerd4j.utils.math;

import org.nerd4j.utils.lang.IsNot;
import org.nerd4j.utils.lang.Require;

import java.math.BigInteger;
import java.util.function.BinaryOperator;

/**
 * This utility class provides some convenience methods
 * to deal with {@link BigInteger}s.
 *
 * <p>
 * The name of this class is an abbreviation of
 * BigInteger. We have intentionally used
 * a short name for readability because this class
 * is intended to be used in calculations and algebraic
 * statements, for example:
 * <pre>
 * long a = ...;
 * long b = ...;
 * BigInteger c = ...;
 * if( BI.lt(a,BI.sum(b,c) )
 *   // Do something...
 * </pre>
 * instead of
 * <pre>
 * long a = ...;
 * long b = ...;
 * BigInteger c = ...;
 * if( BigInteger.valueOf(a).compareTo(BigInteger.valueOf(b).add(c)) &lt; 0 )
 *   // Do something...
 * </pre>
 *
 * <p>
 * The aim of this utility class is to facilitate operations with {@link BigInteger}s.
 *
 * @author Massimo Coluzzi
 * @since 2.0.0
 */
public class BI extends CU
{

    /** Performs the sum of two {@code non-null} {@link BigInteger} values. */
    public static BinaryOperator<BigInteger> SUM = ( a, b ) -> a.add( b );

    /** Performs the subtraction of two {@code non-null} {@link BigInteger} values. */
    public static BinaryOperator<BigInteger> SUB = ( a, b ) -> a.subtract( b );

    /** Performs the multiplication of two {@code non-null} {@link BigInteger} values. */
    public static BinaryOperator<BigInteger> MUL = ( a, b ) -> a.multiply( b );

    /** Performs the division of two {@code non-null} {@link BigInteger} values. */
    public static BinaryOperator<BigInteger> DIV = ( a, b ) -> a.divide( b );

    /** Performs the reminder of the division of two {@code non-null} {@link BigInteger} values. */
    public static BinaryOperator<BigInteger> REM = ( a, b ) -> a.remainder( b );

    /**
     * Performs the module of two {@code non-null} {@link BigInteger} values.
     * This operation differs form {@link BI#REM} because a reminder can be
     * negative while the module is always {@code >= 0}.
     */
    public static BinaryOperator<BigInteger> MOD = ( a, b ) -> a.mod( b );

    /** Performs the binary and of of two {@code non-null} {@link BigInteger} values. */
    public static BinaryOperator<BigInteger> AND = ( a, b ) -> a.and( b );

    /** Performs the binary or of of two {@code non-null} {@link BigInteger} values. */
    public static BinaryOperator<BigInteger>  OR = ( a, b ) -> a.or( b );

    /** Performs the binary xor of of two {@code non-null} {@link BigInteger} values. */
    public static BinaryOperator<BigInteger> XOR = ( a, b ) -> a.xor( b );

    /** Finds the minimum of two {@code non-null} {@link BigInteger} values. */
    public static BinaryOperator<BigInteger> MIN = ( a, b ) -> a.min( b );

    /** Finds the maximum of two {@code non-null} {@link BigInteger} values. */
    public static BinaryOperator<BigInteger> MAX = ( a, b ) -> a.max( b );


    /**
     * This class is intended to be static
     * so there is no public constructor.
     */
    private BI() {}


    /* ***************** */
    /*  FACTORY METHODS  */
    /* ***************** */


    /**
     * Wrapper for the method {@link BigInteger#valueOf(long)}.
     *
     * @param  value value of the BigInteger to return.
     * @return a BigInteger with the specified value.
     */
    public static BigInteger of( long value )
    {

        return BigInteger.valueOf( value );

    }

    /**
     * Transforms the string representation of a number
     * into a {@link BigInteger}.
     * <p>
     * If the argument is {@code null} or empty
     * this method returns {@code null}.
     * <p>
     * If the argument is not empty and does not represent
     * a number this method throws an exception.
     *
     * @param value decimal String representation of BigInteger.
     * @return a BigInteger with the specified value.
     * @throws NumberFormatException if {@code value} is not a valid
     *         representation of a BigInteger.
     */
    public static BigInteger of( String value )
    {

        return IsNot.empty( value )
        ? new BigInteger( value )
        : null;

    }


    /* ************ */
    /*  METHOD ADD  */
    /* ************ */


    /**
     * This is a {@code null-safe} method that, given two {@link BigInteger}
     * values {@code a} and {@code b}, returns {@code a + b}.
     * <p>
     * If one of the values is {@code null}, the other will be returned.
     * If both values are {@code null}, then {@code null} will be returned.
     *
     * @param a value to add.
     * @param b value to add.
     * @return a {@link BigInteger} representing {@code a + b}.
     */
    public static BigInteger sum( BigInteger a, BigInteger b )
    {


        return apply( SUM, a, b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns {@code a + b}.
     * <p>
     * If the {@link BigInteger} values is {@code null}, the other will be returned.
     *
     * @param a value to add.
     * @param b value to add.
     * @return a {@link BigInteger} representing {@code a + b}.
     */
    public static BigInteger sum( BigInteger a, long b )
    {


        final BigInteger bigB = of( b );
        return a == null ? bigB : a.add( bigB );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long} value
     * {@code a} and a {@link BigInteger} value {@code b}, returns {@code a + b}.
     * <p>
     * If the {@link BigInteger} values is {@code null}, the other will be returned.
     *
     * @param a value to add.
     * @param b value to add.
     * @return a {@link BigInteger} representing {@code a + b}.
     */
    public static BigInteger sum( long a, BigInteger b )
    {

        final BigInteger bigA = of( a );
        return b == null ? bigA : bigA.add( b );

    }

    /**
     * Given two {@code long} values {@code a} and {@code b},
     * returns a {@link BigInteger} that represents the value
     * {@code a + b}.
     *
     * @param a value to add.
     * @param b value to add.
     * @return a {@link BigInteger} representing {@code a + b}.
     */
    public static BigInteger sum( long a, long b )
    {

        final BigInteger bigA = of( a );
        final BigInteger bigB = of( b );
        return bigA.add( bigB );

    }


    /* ************ */
    /*  METHOD SUB  */
    /* ************ */


    /**
     * This is a {@code null-safe} method that, given two {@link BigInteger}
     * values {@code a} and {@code b}, returns {@code a - b}.
     * <p>
     * If one of the values is {@code null}, the other will be returned.
     * If both values are {@code null}, then {@code null} will be returned.
     *
     * @param a value to subtract from.
     * @param b value to be subtracted.
     * @return a {@link BigInteger} representing {@code a - b}.
     */
    public static BigInteger sub( BigInteger a, BigInteger b )
    {


        return apply( SUB, a, b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns {@code a - b}.
     * <p>
     * If the {@link BigInteger} values is {@code null}, the other will be returned.
     *
     * @param a value to subtract from.
     * @param b value to be subtracted.
     * @return a {@link BigInteger} representing {@code a - b}.
     */
    public static BigInteger sub( BigInteger a, long b )
    {

        final BigInteger bigB = of( b );
        return a == null ? bigB : a.subtract( bigB );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long} value
     * {@code a} and a {@link BigInteger} value {@code b}, returns {@code a - b}.
     * <p>
     * If the {@link BigInteger} values is {@code null}, the other will be returned.
     *
     * @param a value to subtract from.
     * @param b value to be subtracted.
     * @return a {@link BigInteger} representing {@code a - b}.
     */
    public static BigInteger sub( long a, BigInteger b )
    {


        final BigInteger bigA = of( a );
        return b == null ? bigA : bigA.subtract( b );

    }

    /**
     * Given two {@code long} values {@code a} and {@code b},
     * returns a {@link BigInteger} that represents the value
     * {@code a - b}.
     *
     * @param a value to subtract from.
     * @param b value to be subtracted.
     * @return a {@link BigInteger} representing {@code a - b}.
     */
    public static BigInteger sub( long a, long b )
    {

        final BigInteger bigA = of( a );
        final BigInteger bigB = of( b );
        return bigA.subtract( bigB );

    }


    /* ************ */
    /*  METHOD MUL  */
    /* ************ */


    /**
     * This is a {@code null-safe} method that, given two {@link BigInteger}
     * values {@code a} and {@code b}, returns {@code a * b}.
     * <p>
     * If one of the values is {@code null}, the other will be returned.
     * If both values are {@code null}, then {@code null} will be returned.
     *
     * @param a value to multiply.
     * @param b value to multiply.
     * @return a {@link BigInteger} representing {@code a * b}.
     */
    public static BigInteger mul( BigInteger a, BigInteger b )
    {

        return apply( MUL, a, b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns {@code a * b}.
     * <p>
     * If the {@link BigInteger} values is {@code null}, the other will be returned.
     *
     * @param a value to multiply.
     * @param b value to multiply.
     * @return a {@link BigInteger} representing {@code a * b}.
     */
    public static BigInteger mul( BigInteger a, long b )
    {

        final BigInteger bigB = of( b );
        return a == null ? bigB : a.multiply( bigB );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long} value
     * {@code a} and a {@link BigInteger} value {@code b}, returns {@code a * b}.
     * <p>
     * If the {@link BigInteger} values is {@code null}, the other will be returned.
     *
     * @param a value to multiply.
     * @param b value to multiply.
     * @return a {@link BigInteger} representing {@code a * b}.
     */
    public static BigInteger mul( long a, BigInteger b )
    {

        final BigInteger bigA = of( a );
        return b == null ? bigA : bigA.multiply( b );

    }

    /**
     * Given two {@code long} values {@code a} and {@code b},
     * returns a {@link BigInteger} that represents the value
     * {@code a * b}.
     *
     * @param a value to multiply.
     * @param b value to multiply.
     * @return a {@link BigInteger} representing {@code a * b}.
     */
    public static BigInteger mul( long a, long b )
    {

        final BigInteger bigA = of( a );
        final BigInteger bigB = of( b );
        return bigA.multiply( bigB );

    }


    /* ************ */
    /*  METHOD DIV  */
    /* ************ */


    /**
     * This is a {@code null-safe} method that, given two {@link BigInteger}
     * values {@code a} and {@code b}, returns {@code a / b}.
     * <p>
     * If one of the values is {@code null}, the other will be returned.
     * If both values are {@code null}, then {@code null} will be returned.
     *
     * @param a value to be divided.
     * @param b the divider value.
     * @return a {@link BigInteger} representing {@code a / b}.
     */
    public static BigInteger div( BigInteger a, BigInteger b )
    {


        return apply( DIV, a, b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns {@code a / b}.
     * <p>
     * If the {@link BigInteger} values is {@code null}, the other will be returned.
     *
     * @param a value to be divided.
     * @param b the divider value.
     * @return a {@link BigInteger} representing {@code a / b}.
     */
    public static BigInteger div( BigInteger a, long b )
    {

        final BigInteger bigB = of( b );
        return a == null ? bigB : a.divide( bigB );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long} value
     * {@code a} and a {@link BigInteger} value {@code b}, returns {@code a / b}.
     * <p>
     * If the {@link BigInteger} values is {@code null}, the other will be returned.
     *
     * @param a value to be divided.
     * @param b the divider value.
     * @return a {@link BigInteger} representing {@code a / b}.
     */
    public static BigInteger div( long a, BigInteger b )
    {

        final BigInteger bigA = of( a );
        return b == null ? bigA : bigA.divide( b );

    }

    /**
     * Given two {@code long} values {@code a} and {@code b},
     * returns a {@link BigInteger} that represents the value
     * {@code a / b}.
     *
     * @param a value to be divided.
     * @param b the divider value.
     * @return a {@link BigInteger} representing {@code a / b}.
     */
    public static BigInteger div( long a, long b )
    {

        return of( a / b );

    }


    /* ************ */
    /*  METHOD REM  */
    /* ************ */


    /**
     * This is a {@code null-safe} method that, given two {@link BigInteger}
     * values {@code a} and {@code b}, returns {@code a % b}.
     * <p>
     * If one of the values is {@code null}, the other will be returned.
     * If both values are {@code null}, then {@code null} will be returned.
     *
     * @param a value to be divided.
     * @param b the divider value.
     * @return a {@link BigInteger} representing {@code a % b}.
     */
    public static BigInteger rem( BigInteger a, BigInteger b )
    {

        return apply( REM, a, b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns {@code a % b}.
     * <p>
     * If the {@link BigInteger} values is {@code null}, the other will be returned.
     *
     * @param a value to be divided.
     * @param b the divider value.
     * @return a {@link BigInteger} representing {@code a % b}.
     */
    public static BigInteger rem( BigInteger a, long b )
    {

        final BigInteger bigB = of( b );
        return a == null ? bigB : a.remainder( bigB );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long} value
     * {@code a} and a {@link BigInteger} value {@code b}, returns {@code a % b}.
     * <p>
     * If the {@link BigInteger} values is {@code null}, the other will be returned.
     *
     * @param a value to be divided.
     * @param b the divider value.
     * @return a {@link BigInteger} representing {@code a % b}.
     */
    public static BigInteger rem( long a, BigInteger b )
    {

        final BigInteger bigA = of( a );
        return b == null ? bigA : bigA.remainder( b );

    }

    /**
     * Given two {@code long} values {@code a} and {@code b},
     * returns a {@link BigInteger} that represents the value
     * {@code a % b}.
     *
     * @param a value to be divided.
     * @param b the divider value.
     * @return a {@link BigInteger} representing {@code a % b}.
     */
    public static BigInteger rem( long a, long b )
    {

        final BigInteger bigA = of( a );
        final BigInteger bigB = of( b );
        return bigA.remainder( bigB );

    }


    /* ************ */
    /*  METHOD MOD  */
    /* ************ */


    /**
     * This is a {@code null-safe} method that, given two {@link BigInteger}
     * values {@code a} and {@code b}, returns {@code a mod b}.
     * <p>
     * If one of the values is {@code null}, the other will be returned.
     * If both values are {@code null}, then {@code null} will be returned.
     * <p>
     * This operation differs form {@link BI#rem(BigInteger,BigInteger)}
     * because a reminder can be negative while the module is always {@code >= 0}.
     *
     * @param a value to be divided.
     * @param b value of the module.
     * @return a {@link BigInteger} representing {@code a mod b}.
     */
    public static BigInteger mod( BigInteger a, BigInteger b )
    {


        return apply( MOD, a, b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns {@code a mod b}.
     * <p>
     * If the {@link BigInteger} values is {@code null}, the other will be returned.
     * <p>
     * This operation differs form {@link BI#rem(BigInteger,long)}
     * because a reminder can be negative while the module is always {@code >= 0}.
     *
     * @param a value to be divided.
     * @param b value of the module.
     * @return a {@link BigInteger} representing {@code a mod b}.
     */
    public static BigInteger mod( BigInteger a, long b )
    {

        final BigInteger bigB = of( b );
        return a == null ? bigB : a.mod( bigB );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long} value
     * {@code a} and a {@link BigInteger} value {@code b}, returns {@code a mod b}.
     * <p>
     * If the {@link BigInteger} values is {@code null}, the other will be returned.
     * <p>
     * This operation differs form {@link BI#rem(long,BigInteger)}
     * because a reminder can be negative while the module is always {@code >= 0}.
     *
     * @param a value to be divided.
     * @param b value of the module.
     * @return a {@link BigInteger} representing {@code a mod b}.
     */
    public static BigInteger mod( long a, BigInteger b )
    {

        final BigInteger bigA = of( a );
        return b == null ? bigA : bigA.mod( b );

    }

    /**
     * Given two {@code long} values {@code a} and {@code b},
     * returns a {@link BigInteger} that represents the value
     * {@code a mod b}.
     * <p>
     * This operation differs form {@link BI#rem(long,long)}
     * because a reminder can be negative while the module is always {@code >= 0}.
     *
     * @param a value to be divided.
     * @param b value of the module.
     * @return a {@link BigInteger} representing {@code a mod b}.
     */
    public static BigInteger mod( long a, long b )
    {

        final BigInteger bigA = of( a );
        final BigInteger bigB = of( b );
        return bigA.mod( bigB );

    }


    /* ************ */
    /*  METHOD AND  */
    /* ************ */


    /**
     * This is a {@code null-safe} method that, given two {@link BigInteger}
     * values {@code a} and {@code b}, returns the binary operation {@code a & b}.
     * <p>
     * If one of the values is {@code null}, the other will be returned.
     * If both values are {@code null}, then {@code null} will be returned.
     *
     * @param a an operand.
     * @param b an operand.
     * @return a {@link BigInteger} representing {@code a & b}.
     */
    public static BigInteger and( BigInteger a, BigInteger b )
    {


        return apply( AND, a, b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b},
     * returns the binary operation {@code a & b}.
     * <p>
     * If the {@link BigInteger} values is {@code null}, the other will be returned.
     *
     * @param a an operand.
     * @param b an operand.
     * @return a {@link BigInteger} representing {@code a & b}.
     */
    public static BigInteger and( BigInteger a, long b )
    {

        final BigInteger bigB = of( b );
        return a == null ? bigB : a.and( bigB );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long} value
     * {@code a} and a {@link BigInteger} value {@code b},
     * returns the binary operation {@code a & b}.
     * <p>
     * If the {@link BigInteger} values is {@code null}, the other will be returned.
     *
     * @param a an operand.
     * @param b an operand.
     * @return a {@link BigInteger} representing {@code a & b}.
     */
    public static BigInteger and( long a, BigInteger b )
    {

        final BigInteger bigA = of( a );
        return b == null ? bigA : bigA.and( b );

    }

    /**
     * Given two {@code long} values {@code a} and {@code b},
     * returns a {@link BigInteger} that represents the binary
     * operation {@code a & b}.
     *
     * @param a an operand.
     * @param b an operand.
     * @return a {@link BigInteger} representing {@code a & b}.
     */
    public static BigInteger and( long a, long b )
    {

        return of( a & b );

    }


    /* *********** */
    /*  METHOD OR  */
    /* *********** */


    /**
     * This is a {@code null-safe} method that, given two {@link BigInteger}
     * values {@code a} and {@code b}, returns the binary operation {@code a | b}.
     * <p>
     * If one of the values is {@code null}, the other will be returned.
     * If both values are {@code null}, then {@code null} will be returned.
     *
     * @param a an operand.
     * @param b an operand.
     * @return a {@link BigInteger} representing {@code a | b}.
     */
    public static BigInteger or( BigInteger a, BigInteger b )
    {


        return apply( OR, a, b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b},
     * returns the binary operation {@code a | b}.
     * <p>
     * If the {@link BigInteger} values is {@code null}, the other will be returned.
     *
     * @param a an operand.
     * @param b an operand.
     * @return a {@link BigInteger} representing {@code a | b}.
     */
    public static BigInteger or( BigInteger a, long b )
    {

        final BigInteger bigB = of( b );
        return a == null ? bigB : a.or( bigB );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long} value
     * {@code a} and a {@link BigInteger} value {@code b},
     * returns the binary operation {@code a | b}.
     * <p>
     * If the {@link BigInteger} values is {@code null}, the other will be returned.
     *
     * @param a an operand.
     * @param b an operand.
     * @return a {@link BigInteger} representing {@code a | b}.
     */
    public static BigInteger or( long a, BigInteger b )
    {

        final BigInteger bigA = of( a );
        return b == null ? bigA : bigA.or( b );

    }

    /**
     * Given two {@code long} values {@code a} and {@code b},
     * returns a {@link BigInteger} that represents the binary
     * operation {@code a | b}.
     *
     * @param a an operand.
     * @param b an operand.
     * @return a {@link BigInteger} representing {@code a | b}.
     */
    public static BigInteger or( long a, long b )
    {

        return of( a | b );

    }


    /* ************ */
    /*  METHOD XOR  */
    /* ************ */


    /**
     * This is a {@code null-safe} method that, given two {@link BigInteger}
     * values {@code a} and {@code b}, returns the binary operation {@code a ^ b}.
     * <p>
     * If one of the values is {@code null}, the other will be returned.
     * If both values are {@code null}, then {@code null} will be returned.
     *
     * @param a an operand.
     * @param b an operand.
     * @return a {@link BigInteger} representing {@code a ^ b}.
     */
    public static BigInteger xor( BigInteger a, BigInteger b )
    {


        return apply( XOR, a, b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b},
     * returns the binary operation {@code a ^ b}.
     * <p>
     * If the {@link BigInteger} values is {@code null}, the other will be returned.
     *
     * @param a an operand.
     * @param b an operand.
     * @return a {@link BigInteger} representing {@code a ^ b}.
     */
    public static BigInteger xor( BigInteger a, long b )
    {

        final BigInteger bigB = of( b );
        return a == null ? bigB : a.xor( bigB );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long} value
     * {@code a} and a {@link BigInteger} value {@code b},
     * returns the binary operation {@code a ^ b}.
     * <p>
     * If the {@link BigInteger} values is {@code null}, the other will be returned.
     *
     * @param a an operand.
     * @param b an operand.
     * @return a {@link BigInteger} representing {@code a ^ b}.
     */
    public static BigInteger xor( long a, BigInteger b )
    {

        final BigInteger bigA = of( a );
        return b == null ? bigA : bigA.xor( b );

    }

    /**
     * Given two {@code long} values {@code a} and {@code b},
     * returns a {@link BigInteger} that represents the binary
     * operation {@code a ^ b}.
     *
     * @param a an operand.
     * @param b an operand.
     * @return a {@link BigInteger} representing {@code a ^ b}.
     */
    public static BigInteger xor( long a, long b )
    {

        return of( a ^ b );

    }


    /* ************ */
    /*  METHOD MIN  */
    /* ************ */


    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns the
     * minimum value between them. If the values are equal returns {@code a}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return the minimum between {@code a} and {@code b}.
     */
    public static BigInteger min( BigInteger a, long b )
    {

        return min( a, of(b) );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns the
     * minimum value between them. If the values are equal returns {@code a}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return the minimum between {@code a} and {@code b}.
     */
    public static BigInteger min( BigInteger a, long b, boolean nullLast )
    {

        return min( a, of(b), nullLast );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long}
     * value {@code a} and a {@link BigInteger} value {@code b}, returns the
     * minimum value between them. If the values are equal returns {@code a}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return the minimum between {@code a} and {@code b}.
     */
    public static BigInteger min( long a, BigInteger b )
    {

        return min( of(a), b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long}
     * value {@code a} and a {@link BigInteger} value {@code b}, returns the
     * minimum value between them. If the values are equal returns {@code a}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return the minimum between {@code a} and {@code b}.
     */
    public static BigInteger min( long a, BigInteger b, boolean nullLast )
    {

        return min( of(a), b, nullLast );

    }

    /**
     * Given two {@code long} values {@code a} and {@code b},
     * returns a {@link BigInteger} that represents the minimum
     * of them.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return the minimum between {@code a} and {@code b}.
     */
    public static BigInteger min( long a, long b )
    {

        return a <= b ? of( a ) : of( b );

    }


    /* ************ */
    /*  METHOD MAX  */
    /* ************ */


    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns the
     * maximum value between them. If the values are equal returns {@code a}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return the maximum between {@code a} and {@code b}.
     */
    public static BigInteger max( BigInteger a, long b )
    {

        return max( a, of(b) );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns the
     * maximum value between them. If the values are equal returns {@code a}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return the maximum between {@code a} and {@code b}.
     */
    public static BigInteger max( BigInteger a, long b, boolean nullLast )
    {

        return max( a, of(b), nullLast );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long}
     * value {@code a} and a {@link BigInteger} value {@code b}, returns the
     * maximum value between them. If the values are equal returns {@code a}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return the maximum between {@code a} and {@code b}.
     */
    public static BigInteger max( long a, BigInteger b )
    {

        return max( of(a), b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long}
     * value {@code a} and a {@link BigInteger} value {@code b}, returns the
     * maximum value between them. If the values are equal returns {@code a}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return the maximum between {@code a} and {@code b}.
     */
    public static BigInteger max( long a, BigInteger b, boolean nullLast )
    {

        return max( of(a), b, nullLast );

    }

    /**
     * Given two {@code long} values {@code a} and {@code b},
     * returns a {@link BigInteger} that represents the maximum
     * of them.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return the maximum between {@code a} and {@code b}.
     */
    public static BigInteger max( long a, long b )
    {

        return a >= b ? of( a ) : of( b );

    }


    /* *********** */
    /*  METHOD LT  */
    /* *********** */

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns {@code true}
     * if {@code a < b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a < b}.
     */
    public static boolean lt( BigInteger a, long b )
    {

        return lt( a, of(b) );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns {@code true}
     * if {@code a < b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a < b}.
     */
    public static boolean lt( BigInteger a, long b, boolean nullLast )
    {

        return lt( a, of(b), nullLast );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long}
     * value {@code a} and a {@link BigInteger} value {@code b},
     * returns {@code true} if {@code a < b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a < b}.
     */
    public static boolean lt( long a, BigInteger b )
    {

        return lt( of(a), b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long}
     * value {@code a} and a {@link BigInteger} value {@code b},
     * returns {@code true} if {@code a < b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a < b}.
     */
    public static boolean lt( long a, BigInteger b, boolean nullLast )
    {

        return lt( of(a), b, nullLast );

    }


    /* *********** */
    /*  METHOD LE  */
    /* *********** */

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns {@code true}
     * if {@code a <= b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a <= b}.
     */
    public static boolean le( BigInteger a, long b )
    {

        return le( a, of(b) );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns {@code true}
     * if {@code a <= b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a <= b}.
     */
    public static boolean le( BigInteger a, long b, boolean nullLast )
    {

        return le( a, of(b), nullLast );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long}
     * value {@code a} and a {@link BigInteger} value {@code b},
     * returns {@code true} if {@code a <= b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a <= b}.
     */
    public static boolean le( long a, BigInteger b )
    {

        return le( of(a), b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long}
     * value {@code a} and a {@link BigInteger} value {@code b},
     * returns {@code true} if {@code a <= b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a <= b}.
     */
    public static boolean le( long a, BigInteger b, boolean nullLast )
    {

        return le( of(a), b, nullLast );

    }


    /* *********** */
    /*  METHOD EQ  */
    /* *********** */

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns {@code true}
     * if {@code a == b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a == b}.
     */
    public static boolean eq( BigInteger a, long b )
    {

        return eq( a, of(b) );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns {@code true}
     * if {@code a == b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a == b}.
     */
    public static boolean eq( BigInteger a, long b, boolean nullLast )
    {

        return eq( a, of(b), nullLast );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long}
     * value {@code a} and a {@link BigInteger} value {@code b},
     * returns {@code true} if {@code a == b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a == b}.
     */
    public static boolean eq( long a, BigInteger b )
    {

        return eq( of(a), b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long}
     * value {@code a} and a {@link BigInteger} value {@code b},
     * returns {@code true} if {@code a == b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a == b}.
     */
    public static boolean eq( long a, BigInteger b, boolean nullLast )
    {

        return eq( of(a), b, nullLast );

    }


    /* *********** */
    /*  METHOD NE  */
    /* *********** */

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns {@code true}
     * if {@code a != b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a != b}.
     */
    public static boolean ne( BigInteger a, long b )
    {

        return ne( a, of(b) );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns {@code true}
     * if {@code a != b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a != b}.
     */
    public static boolean ne( BigInteger a, long b, boolean nullLast )
    {

        return ne( a, of(b), nullLast );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long}
     * value {@code a} and a {@link BigInteger} value {@code b},
     * returns {@code true} if {@code a != b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a != b}.
     */
    public static boolean ne( long a, BigInteger b )
    {

        return ne( of(a), b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long}
     * value {@code a} and a {@link BigInteger} value {@code b},
     * returns {@code true} if {@code a != b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a != b}.
     */
    public static boolean ne( long a, BigInteger b, boolean nullLast )
    {

        return ne( of(a), b, nullLast );

    }


    /* *********** */
    /*  METHOD GE  */
    /* *********** */

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns {@code true}
     * if {@code a >= b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a >= b}.
     */
    public static boolean ge( BigInteger a, long b )
    {

        return ge( a, of(b) );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns {@code true}
     * if {@code a >= b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a >= b}.
     */
    public static boolean ge( BigInteger a, long b, boolean nullLast )
    {

        return ge( a, of(b), nullLast );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long}
     * value {@code a} and a {@link BigInteger} value {@code b},
     * returns {@code true} if {@code a >= b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a >= b}.
     */
    public static boolean ge( long a, BigInteger b )
    {

        return ge( of(a), b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long}
     * value {@code a} and a {@link BigInteger} value {@code b},
     * returns {@code true} if {@code a >= b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a >= b}.
     */
    public static boolean ge( long a, BigInteger b, boolean nullLast )
    {

        return ge( of(a), b, nullLast );

    }


    /* *********** */
    /*  METHOD GT  */
    /* *********** */

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns {@code true}
     * if {@code a > b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a > b}.
     */
    public static boolean gt( BigInteger a, long b )
    {

        return gt( a, of(b) );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigInteger}
     * value {@code a} and a {@code long} value {@code b}, returns {@code true}
     * if {@code a > b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a > b}.
     */
    public static boolean gt( BigInteger a, long b, boolean nullLast )
    {

        return gt( a, of(b), nullLast );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long}
     * value {@code a} and a {@link BigInteger} value {@code b},
     * returns {@code true} if {@code a > b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a > b}.
     */
    public static boolean gt( long a, BigInteger b )
    {

        return gt( of(a), b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code long}
     * value {@code a} and a {@link BigInteger} value {@code b},
     * returns {@code true} if {@code a > b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a > b}.
     */
    public static boolean gt( long a, BigInteger b, boolean nullLast )
    {

        return gt( of(a), b, nullLast );

    }


    /* ************** */
    /*  METHOD APPLY  */
    /* ************** */

    /**
     * This is a {@code null-safe} method that, given two {@link BigInteger}
     * values {@code a} and {@code b}, returns the result of the given operator.
     * <p>
     * If one of the values is {@code null}, the other will be returned.
     * If both values are {@code null}, then {@code null} will be returned.
     *
     * @param operator the operator to be applied.
     * @param a an operand.
     * @param b an operand.
     * @return the result of the operation.
     */
    public static BigInteger apply( BinaryOperator< BigInteger> operator,
                                    BigInteger a, BigInteger b )
    {

        if( a == null ) return b;
        if( b == null ) return a;

        return Require.nonNull(
            operator, "The operator to apply is required"
        ).apply( a, b );

    }

}
