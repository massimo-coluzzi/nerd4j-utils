# Package _tuple_

Defining relations between objects is a quite common task for developers,
therefore there are a lot of libraries implementing their version of _Pair_ and _Triple_,
the most famous of those libraries are the _Apache Commons Lang_.\
So, why writing my own version of those two classes?
As explained in the introduction this library collects the code I have to use more frequently
in my everyday coding, so yes, this is just another implementation of the most common classes
ever!

How my implementation differs from the one in the _Apache Common Lang_ library?\
I always try to keep things as simple as possible.
The [org.apache.commons.lang3.tuple.Pair](https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/tuple/Pair.html)
implements `java.lang.Comparable` and `java.util.Map.Entry`, I cannot understand the reason
for this choice, it makes no sense.

1. Implementing `java.util.Map.Entry` adds an useless complexity forcing the Pair class to have
a `setValue(V)` and therefore forcing instances to be mutable.
1. Imlementing `java.lang.Comparable` is even worst because it induces a total order over
the paris that has completely no sense.

For example I may have a class ServiceResponse that do not implement _Comparable_ because it
makes no sense to define an order over service responses. For some reason I need to define
a relation between two responses so I create a _Pair_ of _ServiceResponse_, the responses are
not comparable so why do I need to force an order on a pair of them?

I created my own version of _Pair_ and _Triple_ because I wanted to create a simple, reasonable
and easy to use way to define relations between objects.


### Classes _Pair_ and _Triple_

The implementation of this two classes is trivial, it is about _POJOs_ decorated with some
useful factory methods to make them easy to use.\
Those classes are immutable because the aim of a pair is to define a relation between two
objects, and modifying such relation makes no sense.\
To change the values in the pair equals to define a new relation so it makes more sense
to create a new pair instead. 

This classes are safe against serialization. You can serialize and de-serialize instances
of _Pair_ and _Triple_ in any way.


### Classes _ComparablePair_ and _ComparableTriple_

There can be some cases where you may want to define a comparable relation between two objects.
For example you create pairs of numeric values and want to sort those pairs. For this purpose
I created the comparable versions of _Pair_ and _Triple_.\
For obvious reasons this classes are more complex and unfortunately cannot be safe for serialization.
You may be able to serialize a _ComparablePair_ but when you try to deserialize it you may get
a exception.

To create a _ComparablePair_ or a _ComparableTriple_ the values need to be comparable or you need 
to provide a suitable _Comparator_.
There are some factory methods to cover the most common use cases and, if you need to define
a very specific combination of comparable and non-comparable values, you can use the builders.

For example if you need to define a triple `<a,b,c>` where class `A` is comparable,
class `B` is not comparable but you want to define an ascending order and class `C`
is not comparable and you want to define a descending order.\
You can create a _ComparableTriple_ like this:
```java
Comparator<B> compB = ...;
Comparator<C> compC = ...;
ComparableTriple triple = ComparableTriple
  .left( a )
  .middle( b, compB )
  .right( c, compC );
```
 