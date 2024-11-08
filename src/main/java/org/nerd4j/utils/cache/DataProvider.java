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


/**
 * Represents a provider able to retrieve the values
 * to cache in relation with the given key.
 *
 * <p>
 * The assumption behind this interface is that the
 * given key is able to uniquely identify the related
 * data and therefore contains all the information
 * needed to retrieve it.
 *
 * <p>
 * This is a functional interface and therefore is
 * intended to be implemented using {@code Java 8 Lambdas}.
 * The {@code throws Exception} in the {@link #retrieve(CacheKey)}
 * methods allows the implementations to throw any type of exception.
 *
 * @param <V> type of the data to retrieve.
 *
 * @author Massimo Coluzzi
 * @since 2.1.0
 */
@FunctionalInterface
public interface DataProvider<V>
{

	/**
	 * Returns the data to cache in relation with
	 * the given key.
     *
	 * @param key key to cache.
	 * @return the data to cache.
     * @throws Exception depends on the implementation.
	 */
	V retrieve( CacheKey key ) throws Exception;

}