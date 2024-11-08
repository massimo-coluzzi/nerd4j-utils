# Package _math_

Every developer sooner or later has to deal with mathematical tasks.
In my experience I worked with finance, gambling and other environments that require
mathematical skills and computational precision.


### Class _PrimeSieve_

For a period I worked with modular arithmetic especially with the ring of integers
modulo n and in this branch of mathematics prime numbers play a crucial role.

I needed to know which numbers of a given interval was prime, the smallest prime
greater than a given number or the greatest prime less than a given number and I
had to do it quickly, possibly in _O(1)_.

For this purpose I developed the _PrimeSieve_. It is an implementation of the
algorithm known as [Sieve Of Eratosthenes](https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes).
Numbers are represented using bits. The implementation is optimized so that only odd
numbers that are not multiples of 3 are represented. This allows the _PrimeSieve_ to find and
keep track of all primes between _0_ and _412316859839_. This limit is determined by the greatest
size of an array in Java. According to the Java documentation this size is _Integer.MAX_VALUE_
but, by testing this class, I found out that it is actually _Integer.MAX_VALUE - 2_.

Of course this limit can be bypassed using multiple arrays, but it is out of the purpose of this class.
Besides, allocating such a big array, will need 16Gb of memory and, even if this class takes advantage
of the Java Fork/Join framework to operate in parallel, sieving all those primes will take a remarkable
amount of time. I have tested this class on a _AMD FX(tm)-8350 Eight-Core Processor_ and it took an average of _166446ms_
to find all primes less or equal to _412316859839_ i.e.  it was able to sieve about 25 billion numbers per second. 


### Classes _CU_, _BI_ and _BD_

First of all I had to deal with `java.lang.BigDecimal` and `java.lang.Comparable` and it's such a pain!
The lack of support for comparing and performing mathematical operations with objects in Java is really frustrating.\
A statement like:
```java
if( a + b < c && c < d + e )
  // do something...
```
assuming to consider _null_ to be less than _non-null_, it becomes:
```java
BigDecimal sumAB;
if( a == null )
    sumAB = b;
else if( b == null )
    sumAB = a;
else 
    sumAB = a.sum(b);

BigDecimal sumDE;
if( d == null )
    sumDE = e;
else if( e == null )
    sumDE = d;
else 
    sumDE = d.sum(e);

boolean sumABltC;
if( sumAB == null )
    sumABltC = c != null;
else if( c == null )
    sumABltC = false;
else
    sumABltC = sumAB.compareTo(c) < 0;

boolean cLTsumDE;
if( c == null )
    cLTsumDE = sumDE != null;
else if( sumDE == null )
    cLTsumDE = false;
else
    cLTsumDE = c.compareTo(sumDE) < 0;

if( summABltC && cLTsumDE )
  // do something...
```
that, from my point of view, is really awful!

Therefore I have developed this three utility classes that provide a collection of
static **null-safe** methods to facilitate working with certain type of classes:

* **CU** stands for Comparable Utils and provides a list of convenience methods that
  allow to perform all the most common comparison checks: _<, <=, ==, !=, >=_ and _>_.
* **BI** stands for BigInteger Utils and provides a list of convenience methods that
  allow to perform all the most common operations with _BigInteger_ objects.
* **BD** stands for BigDecimal Utils and provides a list of convenience methods that
  allow to perform all the most common operations with _BigDecimal_ objects.
  
The names of this classes are short on purpose because are intended to be used
inside comparison and algebraic statements and need to be easy to read.

Using this classes the previous example becomes:
```java
if( BD.lt(BD.sum(a,b),c) && BD.lt(c,BD.sum(d,e)) )
  // do something...
```
By using static imports it becomes even more readable:
```java
if( lt(sum(a,b),c) && lt(c,sum(d,e)) )
  // do something...
```

The most important thing to notice in the last two examples is that we don't need
to check for the BigDecimal instances to be _not null_ because the utility methods
are **null-safe**.


### Class Interval

In mathematics, given a set _S_ and a total order relation _≼_ over the elements of _S_,
we define an interval _I_ as a subset of _S_ such that: given _x,y ∊ I | x ≼ y_ we have
that _∀ s ∊ S_ if _x ≼ s ≼ y_ then _s ∊ I_.

An interval can be empty, it can have a lower bound value called
_inf_, it can have an upper bound value called _sup_, and it can also be unbounded.
The upper and lower bounds can be included in the interval, in this case we say that the interval is closed,
or they may be excluded from the interval, in this case we say that the interval is open.

The mathematical definition of interval can be applied to any set of comparable objects,
it is not restricted to numbers. It could be applied to strings, dates, bit arrays or any other comparable object.

I wasn't able to find a rigorous implementation of the concept of interval, able to handle any type
of comparable object and able to handle unbounded intervals, so I decided to write it by myself. 