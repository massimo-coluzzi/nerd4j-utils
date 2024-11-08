# Package _lang_

In this package you will find utility classes to simplify language related topics.

### Classes _Is_ and _IsNot_

This classes are intended to simplify checks on conditional statements.\
I collected into this classes all the most common checks I need to do in everydays coding.

In my experience, the most common checks are:

1. checking if some object is (or is not) _null_;
1. checking if some collection is (or is not) empty;
1. checking whether (or not) some string contains some significant text.

The methods of this two classes can be used in conditional statements like:
```java
  if( Is.empty(collection) )
    // load data
```

But can also be used as predicates in functional programming like:
```java
  Stream<String> stream =
  ...
  stream.filter( IsNot::blank ).forEach( /* do something */ );
```

### Class _Require_

Some of the most annoying code lines are those related to checking the consistency
of methods arguments.
For example you are writing a constructor with parameters and you
need to check that the provided arguments are properly valued.\
Usually you will write something like this:

```java
  public SomeType( String string, Collection<?> collection, Integer integer )
  {
    if( StringUtils.isBlank(string) )
      throw new IllegalArgumentException( "The string argument cannot be blank" );

    if( collection == null || collection.isEmpty() )
      throw new IllegalArgumentException( "The collection argument cannot be empty" );

    if( integer == null || integer <= 0 )
      throw new IllegalArgumentException( "The integer argument must be positive" );

    this.string = string;
    this.integer = integer;
    this.collection = collection;
  }
```

When I saw the method _Objects.requireNonNull(Object,String)_ for the first time,
I found it a great idea!\
Unfortunately the class _java.util.Objects_ contains only this single check.\
Therefore I decided to create the class _Require_ where I collected the most common
requirements I need to check on methods arguments.\
Using the utility _Require_ the previous constructor becomes:

```java
  public SomeType( String string, Collection<?> collection, Integer integer )
  {
    this.string = Require.nonBlank( string, "The string argument cannot be blank" );
    this.integer = Require.trueFor( integer, () -> integer > 0, "The integer argument must be positive" );
    this.collection = Require.nonEmpty( collection, "The collection argument cannot be empty" );
  }
```

As you can see this way checking the arguments consistency becomes quicker to write,
easier to understand and more elegant.

In my experience, the most common requirements for methods arguments are:

1. the argument is not null;
1. the collection is not empty;
1. the string contains some significant text.

In addition there are two general purpose methods:

1. _Require.trueFor_ that checks if the given argument is not null and if the value of the argument satisfies the given assertion.
1. _Require.toHold_ that checks if the given condition holds in the current state of the application.

All the methods of the class _Require_ throw a _RequirementFailure_ exception.


### Interface _Emptily_

This interface is intended to be used with the utility classes
_Is_, _IsNot_ and _Require_ described before.\
The classes _Collection_, _Map_ and _String_ share the same method _isEmpty()_.
Unfortunately the java libraries do not implement a common interface to emphasize
that instances of those classes may be empty.

The aim of the _Emptily_ interface is to highlight that instances of the implementing
class may have a content of may be empty.\
A common use case is when you write a class to contain the response of a service.
If the call fails, instead of returning _null_ you may want to implement
the EmptyObject pattern. Using _Emptily_ you can write your data model like:
```java
public class ServiceResponse implements Emptily
```
and you can check if the response is empty using
```java
ServiceResponse response = service.call();
if( IsNot.empty(response) )
  // Do something
```
or you may want to require the response to be not empty using
```java
ServiceResponse response = Require.nonEmpty(
  service.call(), "The service response cannot be empty"
);
```


### Classes _Hashcode_, _Equals_ and _ToString_

This classes are intended to provide a quick, concise and elegant way
to implement the methods _Object.hashCode()_, _Object.equals(Object)_ and _Object.toString()_.

Most of the IDEs provide tools to generate this methods in an automated way
but this approach has some drawbacks.
For _equals_ and _hashCode_ the generated code is weird and usually hard to understand.
By using this utility classes it will become much more concise and of immediate understanding.

For example some code like this:
```java
public boolean equals( Object obj )
{
    if( obj == this )
        return true;
    if( getClass() != obj.getClass() )
        return false;

    ThisClass other = (ThisClass) obj;
    if( field == null )
    {
        if( other.field != null )
            return false;
    } else if( ! field.equals(other.field) )
        return false;

    if( array == null )
        return other.array == null;

    else if( other.array == null )
        return false;

    if( array.length != other.array.length )
        return false;

    for( int i=0; i&lt;array.length; i++ )
    {
        Object o1 = array[i];
        Object o2 = other.array[i];
        boolean e = o1==null ? o2==null : o1.equals( o2 );
        if( ! e )
            return false;
    }

    return true;
}

public boolean hashCode()
{
    int hashCode = 31
    hashCode *= field1 & 0xFFFFF800
    hashCode = hashCode ^ (field2 << 11);
    hashCode += field3 << 6;
    hashCode += field4 == null 0 : field4.hashCode();

    return hashCode ^ (hashCode >>> 32);
}
```

will become:
```java
public boolean equals( Object other )
{
    return Equals.ifSameClass(
        this, other,
        o -> o.field,
        o -> o.array
    );
}

public boolean hashCode()
{
    return Hashcode.of( field1, field2, field3, field4 );
}
```

Much better!

To compute hash codes there is already _Objects.hash(Object...)_ why should I use your _Hashcode.of(Object...)_?\
Actually you don't need to use the _Hashcode_ class, unless you want to store the hash code outside the JVM
expecting that a subsequent run of the JVM will generate the same code. This is not true for _Objects.hash(Object...)_.
If you generate hash codes (especially for enums) and store them you may notice that the same object can generate
different hash codes in different runs of the JVM. The _Hashcode_ utility has been developed to avoid this misbehaviour
and generate always the same hashcode.

For _toString_, if you use the code generated by your favorite IDE,
all the objects will have the same string format. If you want to customize
the output you need to rewrite the _toString_ method manually.

Using the _ToString_ utility you have 4 builtin layouts and, in a few steps,
you can implement your own preferred layout.

For example using IntelliJ you may generate something like this:
```java
public String toString()
{
    return "SomeType{" +
        "id=" + id +
        ", string='" + string + '\'' +
        ", intArray=" + Arrays.toString(intArray) +
        ", stringMatrix=" + Arrays.toString(stringMatrix) +
        '}';
}

```
Using the _ToString_ utility you can get the same result by writing:
```java
public String toString()
{
    return ToString.of( this )
        .print( "id", id )
        .print( "string", string )
        .print( "intArray", intArray )
        .print( "stringMatrix", stringMatrix )
        .likeIntellij();
}
```
But, if you want to change the output in a function-like format, you just need
to change the last method from _likeIntellij()_ to _likeFunction()_.
Finally you can implement you own _ToString.Printer_ and get your favorite layout.


### Interface _Comparative_

Everyone that worked with comparable objects can tell that dealing
with the _java.lang.Comparable_ interface is such a pain!\
Java libraries have tried to work around the problem by introducing
the _java.util.Comparator_ interface, but sometimes you still need
to use plain old comparable objects (e.g. if you work with BigDecimals).

In those cases you may want to perform some comparisons like:
 ```sql
( a < b and b != c ) or ( b == c and c >= d ) 
```

and to do so you need to write some almost unreadable code
```java
( a.compareTo(b) < 0 && b.compareTo(c) != 0 ) || ( b.compareTo(c) == 0 && c.compareTo(d) >= 0)

```
It would be nice if the _java.lang.Comparable_ interface would be improved with some
convenience (default) method like
```java
default boolean lt( T o )
{
    return compareTo( o ) < 0;
}
```
This way the previous check will become something like
```java
( a.lt(b) && b.ne(c) ) || ( b.eq(c) && c.ge(d) )
```
The last statement is much similar to the mathematical notation and therefor
it is of immediate understanding.

I suggested this improvement to the java community and I hope that it will be implemented,
but in the meantime I have created the _Comparative_ interface for this purpose.

By implementing the _Comparative_ interface you will have objects that implement
_Comparable_ and can be compared using the convenience methods described above.