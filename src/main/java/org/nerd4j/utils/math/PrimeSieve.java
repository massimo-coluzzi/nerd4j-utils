/*
 * #%L
 * Nerd4j Utils
 * %%
 * Copyright (C) 2011 - 2014 Nerd4j
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

import org.nerd4j.utils.lang.*;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;


/**
 * This class permits calculations about prime numbers.
 *
 * <p>
 * This class uses an implementation of the Sieve of Eratosthenes
 * to get all the prime numbers less or equal than a given number.
 *
 * <p>
 * This implementation handles numbers up to 3*2<sup>37</sup>-577
 * i.e. ({@link Integer#MAX_VALUE} - 2) * {@link Long#SIZE} * 3 -1
 * = 412316859839.
 *
 * <p>
 * This class keeps the bit-array used to compute the Sieve of Eratosthenes so,
 * if the requested prime number is very big, this class needs and keeps
 * allocated a lot of memory. To tell if the required N is prime it needs
 * up to 2*N/3 bits (therefore up to N/12 bytes) of memory. For example,
 * to tell if 1000000181 is prime (by the way, it is), you need about
 * 80MB of memory.
 *
 * <p>
 * The limit of 3*2<sup>37</sup>-577 is based on the fact that the JVM does
 * not allow to allocate an array with a size greater than {@link Integer#MAX_VALUE} - 2,
 * but the platform-specific limits can be more restrictive, for example
 * the VM may not have enough memory.
 *
 * <p>
 * If the required N is too big, you may run into a {@link java.lang.OutOfMemoryError}.
 * For example finding all primes less than 3*2<sup>37</sup>-576, besides being
 * relatively time consuming, it requires {@code 16Gb} of memory.
 * You can use the method {@link PrimeSieve#getMemoryConsumptionInBytes(long)} to know
 * the memory occupation given the upper limit and you can use
 * the method {@link PrimeSieve#getGreatestComputableValue(long)}
 * to know the greatest value that can be computed using the given amount of memory.
 *
 * @author Massimo Coluzzi
 * @since 2.0.0
 */
public class PrimeSieve
{


	/** The size of a data block. */
	private static final int BLOCK_SIZE = Long.SIZE;

	/**
	 * The number of bits representing the block size.
	 * For example if the block size is 64, this value
	 * is 6 = log<sub>2</sub> 64.
	 */
	private static final int BLOCK_SIZE_SHIFT = 6;

	/**
	 * The limit of blocks that can be allocated is given by
	 * the current limits of array size that is
	 * {@link Integer#MAX_VALUE } - 2 i.e. 2<sup>31</sup>-3.
	 */
	private static final int BLOCK_LIMIT = Integer.MAX_VALUE - 2;

	/** The minimum size for the {@link #primePool}. */
	private static final int MIN_POOL_SIZE = 16;

	/**
	 * Number of bytes of memory occupied by this object
	 * assuming the {@link #primePool} array to be empty.
	 */
	private static final int CURRENT_OBJECT_SIZE_IN_BYTES = 68;

	/**
	 * The greatest value theoretically handled by this class is 3*2<sup>37</sup>-577
	 * i.e. BLOCK_LIMIT * Long.SIZE * 3 - 1 = (2<sup>31</sup>-3)*64*3-1.
	 * This is an upper limit but the platform-specific limits can be more restrictive.
	 */
	public static final long MAX_VALUE = ((long) BLOCK_LIMIT) * ((long) BLOCK_SIZE) * 3L - 1;

	/** The minimum amount of memory allocated by this object when new created. */
	public static final int MIN_MEMORY_CONSUMPTION_IN_BYTES
	= (MIN_POOL_SIZE << BLOCK_SIZE_SHIFT >> 3) + CURRENT_OBJECT_SIZE_IN_BYTES;


	/**
	 * Contains data for the Sieve of Eratosthenes.
	 * <p>
	 * For each bit in this bit-field:
	 * <ul>
	 *  <li>0 means that the related position <b>is prime</b>,</li>
	 *  <li>1 means that the related position is <b>not prime</b>.</li>
	 * </ul>
	 */
	private long[] primePool;

	/** The limit of blocks that this instance is allowed to allocate. */
	private int maxPoolSize;

	/** The greatest value that this instance is able to compute. */
	private long maxValue;


	/**
	 * Constructor with parameters.
	 *
	 * @param maxValue the greatest computable value.
	 */
	protected PrimeSieve( long maxValue )
	{

		super();

		Require.toHold( maxValue >= 0, "The greatest computable value must be positive" );
		Require.toHold( maxValue <= MAX_VALUE, () -> "The greatest computable value cannot be greater than " + MAX_VALUE );

		this.maxValue = maxValue;

		double poolSize = maxValue;
		poolSize = poolSize / BLOCK_SIZE / 3;
		this.maxPoolSize = (int) Math.ceil( poolSize );

		this.init();

	}


	/* ***************** */
	/*  FACTORY METHODS  */
	/* ***************** */


	/**
	 * Returns a new {@link PrimeSieve} with the default limits:
	 * <ul>
	 *  <li>Greatest computable value: {@code 412316859839}</li>
	 *  <li>Memory occupation upper bound: {@code 16GB}</li>
	 * </ul>
	 *
	 * @return a new instance of {@link PrimeSieve}
	 */
	public static PrimeSieve get()
	{

		return new PrimeSieve( MAX_VALUE );

	}

	/**
	 * Returns a new {@link PrimeSieve} that will compute prime
	 * numbers up to the given value.
	 * <p>
	 * The provided value cannot be greater than 3*2<sup>37</sup>-577,
	 * otherwise an exception will be thrown.
	 * <p>
	 * If the method {@link #isPrime(long)} is invoked with a
	 * value greater than this limit an exception will be thrown.
	 *
	 * @param upperLimit the greatest computable value.
	 * @return a new instance of {@link PrimeSieve}
	 * @throws RequirementFailure if the upper limit is greater than 3*2<sup>37</sup>-577
	 */
	public static PrimeSieve withUpperLimit( long upperLimit )
	{

		return new PrimeSieve( upperLimit );

	}

	/**
	 * Returns a new {@link PrimeSieve} that will use up to the
	 * given amount of memory.
	 * <p>
	 * The minimum amount of memory used by an instance of this class
	 * is {@link #MIN_MEMORY_CONSUMPTION_IN_BYTES}. So, if the given
	 * memory limit is less than {@link #MIN_MEMORY_CONSUMPTION_IN_BYTES},
	 * it will be ignored.
	 * <p>
	 * If the method {@link #isPrime(long)} is invoked with a
	 * value that requires, for being computed, more memory than the
	 * given limit, an exception will be thrown.
	 *
	 * @param memoryLimitInBytes amount of memory in bytes that
	 *                           this instance is allowed to use.
	 * @return a new instance of {@link PrimeSieve}
	 */
	public static PrimeSieve withMemoryLimit( long memoryLimitInBytes )
	{

		final long memoryLimit = Require.trueFor( memoryLimitInBytes,
			memoryLimitInBytes >= MIN_MEMORY_CONSUMPTION_IN_BYTES,
			"The memory limit must be greater than the minimum amount of required memory of "
			+ MIN_MEMORY_CONSUMPTION_IN_BYTES + " bytes"
		);

		final long upperLimit = getGreatestComputableValue( memoryLimit );
		return new PrimeSieve( upperLimit );

	}


	/* **************** */
	/*  STATIC METHODS  */
	/* **************** */


	/**
	 * Returns the maximum amount of memory (in bytes) needed
	 * to compute all primes less or equal than the given upper limit.
	 * <p>
	 * In any case the given value must not be greater than 3*2<sup>37</sup>-577.
	 *
	 * @param upperLimit the upper limit of the values to compute.
	 * @return the needed amount of memory expressed in bytes.
	 * @throws RequirementFailure if the upper limit is greater than 3*2<sup>37</sup>-577
	 */
	public static long getMemoryConsumptionInBytes( long upperLimit )
	{

		/* Checks if the limit belongs to the interval [0,412316859839]. */
		Require.toHold(
			upperLimit >= 0 && upperLimit <= MAX_VALUE,
			() -> upperLimit + " is not in the interval [0,412316859839]."
		);

		/*
		 * We use long words to store the bit-array,
		 * so the memory occupation is a multiple of
		 * 64 bit (8 byte). Each
		 */
		long blocks = upperLimit / ( BLOCK_SIZE * 3 );

		/* We always use at least 16 blocks of 64 bits. */
		blocks = blocks < MIN_POOL_SIZE ? MIN_POOL_SIZE : blocks;

		/*
		 * The size of an object is 16 bytes regardless
		 * of its content. So we need to count 16 byes
		 * for the current object + 32 bytes for the
		 * array and other 20 for the references in
		 * the current object.
		 * So the memory used is 16 + 32 + 20 + 8 * blocks.
		 */
		return ( blocks << 3 ) + CURRENT_OBJECT_SIZE_IN_BYTES;

	}


	/**
	 * Returns the the greatest value that can be computed
	 * with the given amount of memory expressed in bytes.
	 *
	 * @param memoryLimitInBytes number of bytes of available memory.
	 * @return the upper limit of the interval computable using the given amount of memory.
	 * @throws RequirementFailure if the amount of available memory is not positive.
	 */
	public static long getGreatestComputableValue( long memoryLimitInBytes )
	{

		/* The available memory must be strict positive. */
		Require.toHold(
			memoryLimitInBytes >= MIN_MEMORY_CONSUMPTION_IN_BYTES,
			"The amount of available memory must be at least " + MIN_MEMORY_CONSUMPTION_IN_BYTES + " bytes"
		);

		/*
		 * First of all we need to remove the amount of memory
		 * used by the object itself.
		 * Than we compute the amount of blocks that we are
		 * able to allocate with the remaining memory.
		 */
		final long memoryLimitInBits = (memoryLimitInBytes - CURRENT_OBJECT_SIZE_IN_BYTES) << 3;
		final long blocks = memoryLimitInBits >> BLOCK_SIZE_SHIFT;

		/*
		 * The greatest value is given by the amount of values
		 * representable by the number of blocks.
		 * In any case it cannot be greater than MAX_VALUE.
		 */
		return Math.min( blocks * BLOCK_SIZE * 3, MAX_VALUE );

	}


	/* **************** */
	/*  PUBLIC METHODS  */
	/* **************** */


	/**
	 * Tells if the given value is a prime number.
	 * <p>
	 * The given value must belong to the interval [0,{@link #getMaxValue()}].
	 * <p>
	 * The computation for value N takes up to N/12 bytes of memory.
	 * <p>
	 * If the prime values in the interval {@code [0,value]} have been computed
	 * this method takes {@code O(1)}, otherwise it takes {@code O(N)}
	 *
	 * @param value the value to check.
	 * @return {@code true} if it is a prime number, {@code false} otherwise.
	 * @throws OutOfMemoryError if the required memory exceeds the available one.
	 * @throws RequirementFailure if the value is greater than {@link #getMaxValue()}.
	 */
	public boolean isPrime( long value )
	{

		/* Checks if the limit belongs to the interval [0,412316859839]. */
		Require.toHold(
			value >= 0 && value <= maxValue,
			() -> value + " is not in the interval [0," + maxValue + "]."
		);

		/*
		 * A multiple of 2 or 3 can't be prime.
		 * This check is necessary because the internal
		 * structure is optimized to handle only odd values
		 * that are not multiple of 3.
		 */
		if( value < 2 ) return false;
		if( value == 2 || value == 3 ) return true;
		if( value % 2 == 0 || value % 3 == 0 ) return false;

		ensureBounds( value );

		return isPrime( this.primePool, value );

	}

	/**
	 * Returns the smallest prime number greater or equal than the provided value.
	 * <p>
	 * If there are no prime numbers grater or equal than the given threshold
	 * within the internal bounds the value {@code -1} will be returned.
	 * <p>
	 * If the prime values in the interval {@code [0,value]} have been computed
	 * this method takes {@code O(1)}, otherwise it takes {@code O(N)}
	 *
	 * @param  value the threshold value.
	 * @return the prime found if any, {@code -1} otherwise.
	 * @throws OutOfMemoryError if the required memory exceeds the available one.
	 * @throws RequirementFailure if the value is greater than {@link #getMaxValue()}.
	 */
	public long getSmallestPrimeGreaterEqual( long value )
	{

		/* Checks if the limit belongs to the interval [0,412316859839]. */
		Require.toHold(
			value >= 0 && value <= maxValue,
			() -> value + " is not in the interval [0," + maxValue + "]."
		);

		/*
		 * The values represented in the primePool starts from 5
		 * so we need to handle as special cases the values less than 5.
		 */
		if( value <=2 ) return 2;
		if( value == 3 ) return 3;
		if( value <= 5 ) return 5;

		/*
		 * The prime pool represents only odd values that are not multiples of 3.
		 * So, to be able to properly search into the pool, we need to ensure that
		 * the requested value is representable. Since we want to find the smallest
		 * prime greater or equal to the given value we can add some quantity to
		 * fix the requirements.
		 */
		final boolean isOdd = value % 2 == 1;
		final long oddValue = isOdd ? value : value + 1;
		final long representableValue = oddValue % 3 == 0 ? oddValue + 2 : oddValue;

		/* We need to ensure the pool capacity for the requested value. */
		ensureBounds( representableValue );

		/*
		 * If the value to search for is greater than the greatest value represented
		 * in the pool (even after the enlargement) than we are not able to find the
		 * value we are searching for.
		 */
		if( representableValue > getGreatestComputedValue() )
			return -1;

		/* We get the index related to the given value. */
		final long index = representableValue / 3;

		/* We get the position of the block containing the given index. */
		int blockPos = (int)(index >> BLOCK_SIZE_SHIFT);

		/* We get the block containing the given index. */
		long block = primePool[blockPos];

		/*
		 * Now we check if the searched prime is in the current block.
		 * To do so we need to mask all the bits smaller than the
		 * current value.
		 */
		long mask = -1L << index;

		/* If the value is greater than 0 we found the prime. */
		long prime = getSmallestPrimeInBlock( blockPos, block | ~mask );
		if( prime > 0 ) return prime;

		/*
		 * If the searched prime is not in the current block
		 * we look in the others.
		 */

		while( ++blockPos < maxPoolSize )
		{

			if( blockPos >= primePool.length )
				enlargePool( blockPos << BLOCK_SIZE_SHIFT );

			block =  primePool[blockPos];
			if( (prime = getSmallestPrimeInBlock(blockPos, block)) > 0 )
				return prime;

		}

		return -1;

	}


	/**
	 * Returns the greatest prime number less or equal than the provided value.
	 * <p>
	 * If there are no prime numbers less or equal than the given threshold
	 * within the internal bounds the value {@code -1} will be returned.
	 * <p>
	 * If the prime values in the interval {@code [0,value]} have been computed
	 * this method takes {@code O(1)}, otherwise it takes {@code O(N)}
	 *
	 * @param  value the threshold value.
	 * @return the prime found if any, {@code -1} otherwise.
	 * @throws OutOfMemoryError if the required memory exceeds the available one.
	 * @throws RequirementFailure if the value is greater than {@link #getMaxValue()}.
	 */
	public long getGreatestPrimeLessEqual( long value )
	{

		/* Checks if the limit belongs to the interval [0,412316859839]. */
		Require.toHold(
			value >= 0 && value <= maxValue,
			() -> value + " is not in the interval [0," + maxValue + "]."
		);

		/*
		 * The values represented in the primePool starts from 5
		 * so we need to handle as special cases the values less than 5.
		 */
		if( value < 2 ) return -1;
		if( value == 2 ) return 2;
		if( value < 5 ) return 3; // values less than 3 are already been checked.

		/*
		 * The prime pool represents only odd values that are not multiples of 3.
		 * So, to be able to properly search into the pool, we need to ensure that
		 * the requested value is representable. Since we want to find the greatest
		 * prime less or equal to the given value we can remove some quantity to
		 * fix the requirements.
		 */
		final boolean isOdd = value % 2 == 1;
		final long oddValue = isOdd ? value : value - 1;
		final long representableValue = oddValue % 3 == 0 ? oddValue - 2 : oddValue;

		/* We need to ensure the pool capacity for the requested value. */
		ensureBounds( representableValue );

		/* We get the index related to the given value. */
		final long index = representableValue / 3;

		/* We get the position of the block containing the given index. */
		int blockPos = (int)(index >> BLOCK_SIZE_SHIFT);

		/* We get the block containing the given value. */
		long block = primePool[blockPos];

		/*
		 * Now we check if the searched prime is in this block.
		 * To do so we need to mask all the bits greater than
		 * the current value.
		 */
		long mask = -1L >>> -index-1;

		/* If the value is greater than 0 we found the prime. */
		long prime = getGreatestPrimeInBlock( blockPos, block | ~mask );
		if( prime > 0 ) return prime;

		/*
		 * If the searched prime is not in the current block
		 * we look in the others.
		 */
		while( --blockPos >= 0 )
		{

			block =  primePool[blockPos];

			if( (prime = getGreatestPrimeInBlock(blockPos, block)) > 0 )
				return prime;

		}

		return -1;

	}



	/* ***************** */
	/*  PRIVATE METHODS  */
	/* ***************** */


	/**
	 * Ensures that the internal {@link #primePool} is big enough
	 * to contain the given value.
	 * <p>
	 * The given value is granted to be in the interval [0,{@link #maxValue}).
	 *
	 * @param value the value to check.
	 */
	private void ensureBounds( long value )
	{

		final long index = value / 3;
		final long poolSize = primePool.length;
		final long upperBound = poolSize << BLOCK_SIZE_SHIFT;
		if( index >= upperBound )
			enlargePool( index );

	}


	/**
	 * Returns {@code true} if the bit in the given position
	 * is set to {@code 0}, {@code false} otherwise.
	 *
	 * @param data     the {@code int} bit-word containing the bit to evaluate.
	 * @param position the position of the bit in range [0,{@link #BLOCK_SIZE}).
	 *
	 * @return {@code true} if the bit value is {@code 0};<br />
	 *         {@code false} otherwise.
	 */
	private boolean booleanAt( long data, int position )
	{

		/*
		 * We assume the bit position to be in range [0,BLOCK_SIZE).
		 * This method return true if the bit in the given
		 * position is 0 and false if the bit is 1.
		 */
		return ( (data >> position) & 1L ) == 0;

	}


	/**
	 * Returns {@code true} if the given value is prime.
	 *
	 * @param pool  pool of primes to check.
	 * @param value value to check.
	 * @return {@code true} if is prime, {@code false} otherwise.
	 */
	private boolean isPrime( long[] pool, long value )
	{

		/*
		 * We always ensure the requested value to be odd
		 * and to be not a multiple of 3.
		 * The bits in the bit-array represents only odd
		 * values not multiple of 3, so to get the right
		 * position in the bit-array we need to divide
		 * the value by 3.
		 */
		final long index = value / 3;
		final int block = (int)(index >> BLOCK_SIZE_SHIFT);
		final int position = (int)(index % BLOCK_SIZE);

		return booleanAt( pool[block], position );

	}


	/**
	 * Initialize the {@link #primePool} with the
	 * default values.
	 *
	 */
	private void init()
	{

		/* We start with a pool size of 16. */
		final long[] pool = new long[16];

		/*
		 * The number 1 is not considered to be prime,
		 * so we start setting 1 as not prime.
		 */
		pool[0] = 1L;

		/* We apply the sieve starting from the first block. */
		performSieve( pool, 0 );

		this.primePool = pool;

	}


	/**
	 * Enlarges the {@link #primePool} size to enclose the given value.
	 * <p>
	 * This is the only method that modifies the pool. This method is
	 * {@code thread-safe} even without synchronization however, if the
	 * requested values are big, the size of the pool can be remarkable
	 * so we better avoid multiple threads trying to enlarge the pool
	 * at the same time. Therefore this method is synchronized.
	 *
	 * @param index the index to be enclosed by the {@link #primePool}.
	 */
	private synchronized void enlargePool( long index )
	{

		final long currentPoolSize = primePool.length;

		/*
		 * We double the size of the pool until
		 * the requested value belongs to the pool.
		 */
		long newPoolSize = primePool.length;
		while( index >= (newPoolSize << BLOCK_SIZE_SHIFT) )
			newPoolSize = newPoolSize << 1; /* poolSize = poolSize * 2 */

		if( newPoolSize > maxPoolSize )
			newPoolSize = maxPoolSize;

		/* If another thread had already enlarged the pool nothing needs to be done. */
		if( newPoolSize == currentPoolSize ) return;

		/*
		 * Otherwise we create a new bit-array with the needed size,
		 * we copy the old bit-array into the new one, and apply the
		 * sieve to the new bits.
		 */
		final long[] newPool = Arrays.copyOf( this.primePool, (int) newPoolSize );

		/* We apply the sieve to the new blocks. */
		performSieve( newPool, currentPoolSize );

		/* Finally we update the internal primePool. */
		this.primePool = newPool;

	}

	/**
	 * This method divides the data block into smaller parts to take
	 * advantage of the Java Fork/Join framework.
	 * <p>
	 * This method can be called with a partially computed bit-array
	 * therefore it requires the index of the block to start with.
	 *
	 * @param data       the data to sift.
	 * @param startBlock the block to start with.
	 */
	private void performSieve( long[] data, long startBlock )
	{

		final PerformSieve performSieve = new PerformSieve( data, startBlock, data.length );
		final ForkJoinPool pool = ForkJoinPool.commonPool();

		pool.invoke( performSieve );

	}


	/**
	 * This method actually performs the Sieve of Eratosthenes algorithm.
	 * <p>
	 * This method can be called with a partially computed bit-array
	 * therefore it requires the index of the block to start with
	 * and the index of the block to end with.
	 *
	 * @param data       the data to sift.
	 * @param startBlock the block to start with.
	 * @param endBlock   the block to end with.
	 */
	private void performSieve( long[] data, long startBlock, long endBlock )
	{

		/* This is the superior limit of the data to be sifted. */
		final long endValue = 3L * (endBlock << BLOCK_SIZE_SHIFT);

		/* The number represented by the first bit of the start block. */
		final long startBlockValue = 3L * (startBlock << BLOCK_SIZE_SHIFT);

		/*
		 * We need only to remove the multiples of the primes
		 * in the interval [5, sqrt(endValue)] because the other
		 * non primes have already been sifted.
		 */
		final long lastPrime = Math.round( Math.sqrt(endValue) );

		/*
		 * The bit-array doesn't contain multiples of 2 or 3
		 * so removing them is not necessary, but also they
		 * are not represented so must be skipped.
		 * The boost value is needed to skip multiples of 3
		 * that are not also multiples of 2 i.e. the odd
		 * multiples of 3.
		 * Starting from the list of all numbers:
		 * 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 ...
		 * we remove all the even numbers getting:
		 * 1 3 5 7 9 11 13 15 17 19 21 23 25 27 29 31 33 ...
		 * n n y y n  y  y  n  y  y  n  y  y  n  y  y  n ...
		 * The numbers represented in the bit-array are all
		 * those marked with 'y'. So in the following loop
		 * we need to skip one position each 2, for this reason
		 * we use the boost.
		 */
		int boost = 1;
		long startValue;
		for( long i = 5; i <= lastPrime; i += 2 << boost )
		{

			/*
			 * boost is initialized to 1 so during this first
			 * loop boost becomes 0 and the next value of i
			 * is 7. Then boost becomes 1 so 9 will be skipped
			 * and the following value of i will be 11 and so on.
			 */
			boost = 1 - boost;

			if( isPrime(data, i) )
			{

				/*
				 * We don't need to start from the beginning
				 * because all the non-primes in the interval
				 * [3, i*i) have already been sifted. Actually
				 * we can start from the greatest value between
				 * startBlockValue and i * i.
				 */
				startValue = i * i;

				/*
				 * We need the start value to be an odd multiple
				 * of i that is not divisible by 3. If the greatest
				 * value is i * i we already have a valid value,
				 * otherwise we have a power of 2, so we need to
				 * adjust it to get the needed value.
				 */
				if( startValue <= startBlockValue )
					startValue = adjustValue( startBlockValue, i );

				/* The start index is the startValue / 3. */

				/* We remove all the multiples of i. */
				siftMultiples( data, startValue, endValue, i );

			}
		}

	}


	/**
	 * Takes the given value and the given prime and returns
	 * the greatest odd multiple of prime smaller than value.
	 *
	 * @param value value to adjust.
	 * @param prime prime value to adjust for.
	 * @return the adjusted value.
	 */
	private long adjustValue( long value, long prime )
	{

		/*
		 * We need the value to be an odd multiple of prime.
		 * As first step we bring it to be a multiple of prime
		 * removing the difference between value and the
		 * greatest multiple of prime smaller then value.
		 * Ex. value = 64, prime = 7 => 64 - (64 % 7) = 64 - 1 = 63;
		 */
		value = value - (value % prime);

		/*
		 * If value is even, we remove prime to get the
		 * greatest odd multiple of prime smaller than
		 * value. The arguments given to this method
		 * ensures that the returned value will never
		 * be smaller then 5;
		 * Ex. 63 is odd, the value remains unchanged.
		 */
		value = value % 2 == 0 ? value - prime : value;

		/*
		 * At this point we need to ensure that the value
		 * is not divisible by 3, otherwise we add 2 * prime
		 * getting the smallest multiple of prime not divisible
		 * by 3 and not sifted yet.
		 * Ex. 63 is a multiple of 3 => 63 + (7 << 1) = 63 + 14 = 77.
		 */
		return value % 3 == 0 ? value + (prime << 1) : value;

	}


	/**
	 * This method sifts the multiples of the given prime
	 * according to the Sieve of Eratosthenes algorithm.
	 *
	 * @param data       the data to sift.
	 * @param startValue the value to start with.
	 * @param endValue   the value that represents the end of the interval.
	 * @param prime      the prime which multiples have to be sifted out.
	 */
	private void siftMultiples( long[] data, long startValue, long endValue, long prime )
	{

		/*
		 * The principle of skipping the odd multiples of 3 exposed in the method
		 * "performSieve" holds also in this method. Here we need to remove all
		 * the multiples of prime that are not divisible by 2 or 3 i.e. we need to
		 * skip all odd values x such that x % 3 = 0.
		 * Assuming prime % 3 = 1 we have:
		 * prime = 3x + 1 => 2 * prime = 6x + 2 => (2 * prime) % 3 = 2.
		 * So we need to skip all the positions p in the form p * i + 2.
		 * Otherwise if prime % 3 = 2 we have:
		 * prime = 3x + 2 => 2 * prime = 6x + 4 => 2 * prime = 3 * (2x + 1) + 1
		 * => 2 * prime % 3 = 1.
		 * So we need to skip all the positions p in the form p * i + 1.
		 * For that reason we initialize the boost as prime % 3.
		 */
		int boost = (int)(prime % 3);

		/*
		 * The startValue is granted not to be a multiple of 3
		 * so startValue % 3 can be 1 or 2. In the following loop
		 * we start by startValue and we step forward by 2 * prime
		 * or 4 * prime depending on the boost. So we need to
		 * adjust the boost so that we not catch a multiple of 3.
		 * To do that we need to properly combine the values of
		 * startValue % 3 and prime % 3.
		 * The following operation is given by the table:
		 *
		 * startValue % 3	prime % 3	result
		 * 		1				1			2
		 * 		1				2			1
		 * 		2				1			1
		 * 		2 	 			2			2
		 *
		 * But considering the fact that the boost will be
		 * inverted during the loop we need to invert the
		 * operation to get the right starting value.
		 */
		boost = startValue % 3 == 2 ? 3 - boost : boost;


		/*
		 * NOTES REGARDING THE FOLLOWING BINARY OPERATIONS:
		 *
		 * Setting the bit-word to 1 means that all the
		 * bits in the bit-word are 0 rather than the last one.
		 * 1 = 00000000-00000000-00000000-00000001 (32 bit-word)
		 *
		 * This operation moves all the bits to the left by the given
		 * number of positions and fills the rest of the word with 0.
		 * For example if N = 20 we obtain the following mask:
		 * mask = 00000000-00010000-00000000-00000000
		 * The positions are 0-based so shifting position 20 means
		 * that the bit 1 is in the 21th position.
		 *
		 * This operation is modular that means that shifting 33 positions
		 * is the same as shifting 1 position. We need a mask with all 0 and
		 * only one 1 in the requested position.
		 */
		int block;
		long mask, index;
		for( long value = startValue; value < endValue; value += prime << boost )
		{

			/*
			 * For the same reason exposed in the method "performSieve"
			 * the boost value must be alternated between 1 and 2.
			 * We ensure boost to be 1 or 2, and with this operation
			 * we set boost = 2 if it was 1 and vice versa.
			 */
			boost = 3 - boost;

			/* The bit-array index is given by the value / 3. */
			index = value / 3;

			/* We get the current block. */
			block = (int)( index >> BLOCK_SIZE_SHIFT );

			/* We create the bit-mask for the given value. */
			mask = 1L << index;

			/* We use the bit mask to set the bit to 1. */
			data[ block ] |= mask;

		}

	}


	/**
	 * Returns the value of the smallest prime in the given block if any.
	 *
	 * @param blockPos the position of the block
	 * @param block    the block to analyze.
	 * @return the smallest prime if any, {@code -1} otherwise.
	 */
	private long getSmallestPrimeInBlock( long blockPos, long block )
	{

		/*
		 * For this operation we need to invert the logic
		 * considering 1 to be prime and 0 to be non prime.
		 * Therefore we ne to complement the bits in the block.
		 */
		final long complement = ~block;

		/* Returns the number of zero-bit preceding the first 1-bit. */
		int zeros = Long.numberOfTrailingZeros( complement );

		/*
		 * If the number of zeros equals the block size it means that
		 * there is no 1-bits in the complemented block, otherwise we
		 * use the zeros to retrieve the position of the searched prime.
		 */
		if( zeros == BLOCK_SIZE )
			return -1;

		final long index = (blockPos << BLOCK_SIZE_SHIFT) + zeros;

		final long value = index * 3;
		final long boost = (value % 2) + 1;

		final long prime = value + boost;
		return prime;

	}


	/**
	 * Returns the value of the greatest prime in the given block if any.
	 *
	 * @param blockPos the position of the block
	 * @param block    the block to analyze.
	 * @return the greatest prime if any, {@code -1} otherwise.
	 */
	private long getGreatestPrimeInBlock( long blockPos, long block )
	{

		/*
		 * For this operation we need to invert the logic
		 * considering 1 to be prime and 0 to be non prime.
		 * Therefore we ne to complement the bits in the block.
		 */
		final long complement = ~block;

		/* Returns the number of zero-bit following the last 1-bit. */
		int zeros = Long.numberOfLeadingZeros( complement );

		/*
		 * If the number of zeros equals the block size it means that
		 * there is no 1-bits in the complemented block, otherwise we
		 * use the zeros to retrieve the position of the searched prime.
		 */
		if( zeros == BLOCK_SIZE )
			return -1;

		final long index = (++blockPos << BLOCK_SIZE_SHIFT) - ++zeros;

		final long value = index * 3;
		final long boost = (value % 2) + 1;

		final long prime = value + boost;
		return prime;

	}


	/* *************** */
	/*  INNER CLASSES  */
	/* *************** */


	/**
	 * Implementation of the {@link ForkJoinTask}
	 * that divides the {@link #primePool} into
	 * blocks and performs the Sieve of Eratosthenes
	 * algorithm in parallel on each block.
	 *
	 * <p>
	 * This class follows the Fork/Join best practices
	 * to take the most advantage on the divide and conquer
	 * paradigm.
	 */
	private class PerformSieve extends RecursiveAction
	{

		/** Serial Version UID */
		private static final long serialVersionUID = 1L;

		/**
		 * If the number of blocks to compute is smaller than this threshold it is more
		 * performing to execute the sieve sequentially.
		 */
		private static final long THRESHOLD = 1 << 16;

		/** The bit array to sieve. */
		private final long[] data;

		/** The block to start with. */
		private final long start;

		/** The block to end with. */
		private final long end;


		/**
		 * Constructor with parameters.
		 *
		 * @param data  The bit array to sieve.
		 * @param start The block to start with.
		 * @param end   The block to end with.
		 */
		public PerformSieve(long[] data, long start, long end )
		{

			super();

			this.data  = Require.nonEmpty( data, "The bit array to sieve cannot be empty" );
			this.start = Require.trueFor( start, start >= 0, "The start block cannot be negative" );
			this.end   = Require.trueFor( end, end > start, "The end block must be greater than the start block" );

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void compute()
		{

			if( end - start > THRESHOLD )
			{

				final long middle = (start + end) >> 1;
				final PerformSieve left_half  = new PerformSieve( data, start, middle );
				final PerformSieve right_half = new PerformSieve( data, middle, end );

				ForkJoinTask.invokeAll( left_half, right_half );

			}
			else
				performSieve( data, start, end );

		}
	}



	/* *********************** */
	/*  CONFIGURATION METHODS  */
	/* *********************** */


	/**
	 * Returns the greatest value computable by this instance.
	 *
	 * @return the greatest value computable by this instance.
	 */
	public long getMaxValue()
	{

		return maxValue;

	}

	/**
	 * Returns the greatest value computed so far.
	 *
	 * @return the greatest value computed so far.
	 */
	public long getGreatestComputedValue()
	{

		final long poolSize = primePool.length;
		return ( poolSize << BLOCK_SIZE_SHIFT ) * 3L;

	}

	/**
	 * Returns the amount of memory, in bytes, currently used by this object.
	 *
	 * @return the amount of memory, in bytes, currently used by this object.
	 */
	public long getCurrentMemoryConsumptionInBytes()
	{

		return getMemoryConsumptionInBytes( getGreatestComputedValue() );

	}


	/* ***************** */
	/*  OBJECT OVERRIDES */
	/* ***************** */


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{

		return Hashcode.of( maxValue );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals( Object other )
	{

		return Equals.ifSameClass(
			this, other,
			o -> o.maxValue
		);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{

		return ToString.of( this ).print(  maxValue ).using( "( [0,", "", ") )" );

	}

}
