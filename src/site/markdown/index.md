# Welcome to nerd4j-utils

This is a collection of utility classes developed during the years to avoid
the effort to rewrite always the same [boilerplate code](https://en.wikipedia.org/wiki/Boilerplate_code).

How many times did you see something like this?

```java
if( someObject != null && ! someObject.empty() )
  // do something
```

It would be much better to write:

```java
if( IsNot.empty(someObject) )
   // do something
```

Perhaps you found yourself writing again and again code like:

```java
public SomeClass( List<SomeType> argument )
{
    if( argument == null || argument.isEmpty() )
        throw new IllegalArgumentException( "The provided argument cannot be null or empty" );

    this.argument = argument;
}
```

I did it a lot of times and finally I decided to write an utility class to allow
me to write statements like:

```java
public SomeClass( List<SomeType> argument )
{
    this.argument = Require.nonEmpty( argument, "The provided argument cannot be null or empty" );
}
```

This kind of code can be found spread in multiple libraries (e.g. Objects.requireNonNull or StringUtils.isBlank)
but I don't like to waste time searching for the right library (or worst, to rewrite every time the same code),
so I decided to put all the most common checks in one utility class.

Based on the same principle there are utility classes to create hash codes, to perform equality between objects, to serialize into String, etc.
Those classes are collected into different packages, in the following sections I will give a brief description of each package
providing some suggestions about how the classes are intended to be used.

### Packages:


Package | Description
--------|-------------
[org.nerd4j.utils.lang](package/lang.html)   | This package contains classes to help simplifying language related operations.
[org.nerd4j.utils.tuple](package/tuple.html) | This package contains classes to define tuples of objects.
[org.nerd4j.utils.math](package/math.html)   | This package contains classes to help simplifying mathematical related operations.
[org.nerd4j.utils.cache](package/cache.html) | This package contains classes to help simplifying caching related operations.
