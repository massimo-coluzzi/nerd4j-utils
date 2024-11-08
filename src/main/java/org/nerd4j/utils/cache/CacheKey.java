/*
 * #%L
 * Nerd4j Utils
 * %%
 * Copyright (C) 2011 - 2016 Nerd4j
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
package org.nerd4j.utils.cache;

import org.nerd4j.utils.lang.Equals;
import org.nerd4j.utils.lang.Hashcode;
import org.nerd4j.utils.lang.Require;
import org.nerd4j.utils.lang.ToString;

import java.util.Arrays;
import java.util.stream.Stream;


/**
 * Represents the search key used by the {@link SelfLoadingCache}
 * facade and the related {@link CacheProvider}.
 *
 * <p>
 * Each key is intended to identify a specific type of objects,
 * therefore to be created a {@link CacheKey} requires:
 * <ol>
 *  <li>The type of the data model to store.</li>
 *  <li>The version of the data model identified by the key.</li>
 *  <li>The key attributes needed to uniquely identify a data model.</li>
 * </ol>
 *
 * <p>
 * The data model version can be very useful when working with an external
 * cache server. If you have multiple application servers that access the same
 * cache server and you need to release a new version of the application
 * that changes the cached data model, during the release the application
 * servers with the old code should still retrieve the old models from the
 * cache while the application servers with the new code should populate the
 * cache with the new models. This is automatically achieved by using versions.
 *
 * <p>
 * You may want to cache two different objects of the same type, with the same attributes.
 * For example you want to cache two sets of {@code UserStats} for the same {@code User},
 * so the data model type will be {@code UserStats} and the attribute will be the user ID
 * but you need to cache two different values in the same cache. Also in this case
 * you can use the version to distinguish the two cache keys. You can create two different
 * cache keys like:
 * <pre>
 *     CacheKey dailyCacheKey = CacheKey.of( UserStats.class, "daily", 1 );
 *     CacheKey monthlyCacheKey = CacheKey.of( UserStats.class, "monthly", 1 );
 * </pre>
 *
 * <p>
 * To make it easier to build keys from data this class provides
 * a {@link Prototype} that can be defined {@code static} and configured
 * with type and version. The {@link Prototype} is {@code thread-safe}
 * and can be defined as a static singleton object.
 * The common use case for this prototype object is the following:
 * <pre>
 *   public class SomeManager
 *   {
 *
 *     The static prototype to use where needed.
 *     private static final CacheKey.Prototype myDataModelCacheKey = CacheKey.prototype( MyDataModel.class, "1.0.0" );
 *
 *     ...
 *
 *     public void someMethod()
 *     {
 *
 *       CacheKey actualCacheKey = myDataModelCacheKey.of( list of attributes );
 *       ...
 *
 *     }
 *   }
 * </pre>
 *
 * @author Massimo Coluzzi
 * @since 2.1.0
 */
public class CacheKey
{

	/**
	 * Value to use if the user do not provide a custom version.
	 * By default every data model is supposed to have the latest version.
	 */
	protected static final String DEFAULT_VERSION = "LATEST";


	/** The type of the data model pointed by this cache key. */
	private final Class<?> dataModelType;

	/**
	 * This value is intended to distinguish different versions
	 * of the data model related to the same key.
	 */
	private final String version;

	/** Key attributes used to define the unique identifier. */
	private final Object[] attributes;

	/**
	 * Since this is an immutable object the output of the method
	 * {@link #toString()} will be saved in this field to avoid
	 * the invocations of the method to recreate the same object
	 * multiple times.
	 */
	private transient String toStringOutcome;


	/**
	 * Default constructor.
	 * <p>
	 * This constructor is intended to be used
	 * by reflection during de-serialization.
	 * <p>
	 * To create a new cache key
	 * use the factory methods.
	 */
	protected CacheKey()
	{

		super();

		this.version  = null;
		this.attributes = null;
		this.dataModelType = null;
		this.toStringOutcome = null;

	}

	/**
	 * Constructor with parameters.
	 * <p>
	 * This constructor is intended to be used
	 * by extending classes only.
	 * <p>
	 * To create a new cache key
	 * use the factory methods.
	 *
	 * @param dataModelType  the type of the data model pointed by this cache key.
	 * @param version        the version of the data model pointed by this cache key.
	 * @param attributes     the key attributes used to define the unique identifier.
	 */
	protected CacheKey( Class<?> dataModelType, String version, Object[] attributes )
	{

		super();

		this.version = Require.nonBlank( version, "The data model version is mandatory" );
		this.attributes = Require.nonNull( attributes, "The cache key attributes are mandatory" );
		this.dataModelType = Require.nonNull( dataModelType, "The data model type is mandatory" );
		this.toStringOutcome = null;

	}


	/* ***************** */
	/*  FACTORY METHODS  */
	/* ***************** */


	/**
	 * Creates a new cache key with the given values.
	 *
	 * @param dataModelType  the type of the data model pointed by this cache key.
	 * @param attributes     the key attributes used to define the unique identifier.
	 * @return a new cache key with the given values.
	 */
	public static CacheKey of( Class<?> dataModelType, Object... attributes )
	{

		return new CacheKey( dataModelType, DEFAULT_VERSION, attributes );

	}

	/**
	 * Creates a new cache key with the given values.
	 *
	 * @param dataModelType  the type of the data model pointed by this cache key.
	 * @param version        the version of the data model pointed by this cache key.
	 * @param attributes     the key attributes used to define the unique identifier.
	 * @return a new cache key with the given values.
	 */
	public static CacheKey of( Class<?> dataModelType, String version, Object... attributes )
	{

		return new CacheKey( dataModelType, version, attributes );

	}

	/**
	 * Creates a new cache key prototype with the given data model type.
	 *
	 * @param dataModelType  the type of the data model pointed by this cache key.
	 * @return a new cache key prototype with the given values.
	 */
	public static Prototype prototype( Class<?> dataModelType )
	{

		return new Prototype( dataModelType, DEFAULT_VERSION );

	}

	/**
	 * Creates a new cache key prototype with the given values.
	 *
	 * @param dataModelType  the type of the data model pointed by this cache key.
	 * @param version        the version of the data model pointed by this cache key.
	 * @return a new cache key prototype with the given values.
	 */
	public static Prototype prototype( Class<?> dataModelType, String version )
	{

		return new Prototype( dataModelType, version );

	}


	/* *********************** */
	/*  CONFIGURATION METHODS  */
	/* *********************** */


	/**
	 * Returns the type of the data model identified by this cache key.
	 *
	 * @return the type of the data model identified by this cache key.
	 */
	public Class<?> dataModelType()
	{

		return dataModelType;

	}

	/**
	 * Returns the version of the data model identified by this cache key.
	 *
	 * @return the version of the data model identified by this cache key.
	 */
	public String version()
	{

		return version;

	}

	/**
	 * Returns the attributes defining this cache key.
	 *
	 * @return the attributes defining this cache key.
	 */
	public Stream<Object> attributes()
	{

		return Arrays.stream( attributes );

	}


	/* ****************** */
	/*  OBJECT OVERRIDES  */
	/* ****************** */


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{

		return Hashcode.of( dataModelType, version, attributes );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals( Object other )
	{

		return Equals.ifSameClass(
			this, other,
			o -> o.dataModelType,
			o -> o.version,
			o -> o.attributes
		);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{

		if( toStringOutcome == null )
			toStringOutcome = ToString.of( this )
				.print( dataModelType.getSimpleName(), version, attributes )
				.using( "<", "-", ">" );

		return toStringOutcome;

	}


	/* *************** */
	/*  INNER CLASSES  */
	/* *************** */


	/**
	 * Prototype class populated with the data model type and version.
	 *
	 * <p>
	 * The aim of this class is to provide a clean way to create
	 * the cache keys, avoiding to insert every time the model type and version.
	 *
	 * @author Massimo Coluzzi
	 * @since 2.1.0
	 */
	public static class Prototype
	{

		/** The type of the data model pointed by this cache key. */
		private final Class<?> dataModelType;

		/**
		 * This value is intended to distinguish different versions
		 * of the data model related to the same key.
		 */
		private final String version;

		/**
		 * Since this is an immutable object the output of the method
		 * {@link #toString()} will be saved in this field to avoid
		 * the invocations of the method to recreate the same object
		 * multiple times.
		 */
		private transient String toStringOutcome;


		/**
		 * Default constructor.
		 * <p>
		 * This constructor is intended to be used
		 * by reflection during de-serialization.
		 * <p>
		 * To create a new cache key
		 * use the factory methods.
		 */
		protected Prototype()
		{

			super();

			this.version  = null;
			this.dataModelType = null;
			this.toStringOutcome = null;

		}

		/**
		 * Constructor with parameters.
		 * <p>
		 * This constructor is intended to be used
		 * by extending classes only.
		 * <p>
		 * To create a new cache key
		 * use the factory methods.
		 *
		 * @param dataModelType  the type of the data model pointed by this cache key.
		 * @param version        the version of the data model pointed by this cache key.
		 */
		protected Prototype( Class<?> dataModelType, String version )
		{

			super();

			this.version = Require.nonBlank( version, "The data model version is mandatory" );
			this.dataModelType = Require.nonNull( dataModelType, "The data model type is mandatory" );
			this.toStringOutcome = null;

		}


		/* **************** */
		/*  PUBLIC METHODS  */
		/* **************** */


		/**
		 * Creates a new {@link CacheKey} with the given values.
		 *
		 * @param attributes  the key attributes used to define the unique identifier.
		 * @return a new {@link CacheKey}
		 */
		public CacheKey of( Object... attributes )
		{

			return new CacheKey( dataModelType, version, attributes );

		}


		/* ****************** */
		/*  OBJECT OVERRIDES  */
		/* ****************** */


		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode()
		{

			return Hashcode.of( dataModelType, version );

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals( Object other )
		{

			return Equals.ifSameClass(
				this, other,
				o -> o.dataModelType,
				o -> o.version
			);

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString()
		{

			if( toStringOutcome == null )
				toStringOutcome = ToString.of( this )
					.withCustomClassName( "CacheKeyPrototype" )
					.print( dataModelType.getSimpleName() )
					.print( version )
					.using( "<", "-", ">" );

			return toStringOutcome;

		}

	}

}