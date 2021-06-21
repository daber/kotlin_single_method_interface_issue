# Problem

On Kotlin 1.5 SAM { implementation} is not equivalent to object: SAM{ method = implementation} as it produces different
class objects. It introduced runtime failures on our code when upgrading kotlin 1.3 to kotlin 1.5

### How to repro:

Assuming following

Java code:

```java 
public interface Provider<T> {
    T get();
}
```

And running following code

```kotlin
class SomeClass() {
    val providerA: Provider<A> = Provider { A() }
    val providerB: Provider<A> = object : Provider<A> {
        override fun get(): A = A()
    }
}
```

this will provide different genericsInterfaces for javaClass for providerA and providerB on kotlin 1.5

```kotlin
//ON KOTLIN 1.5.10
val t = SomeClass()

println("${t.providerA.javaClass.genericInterfaces[0]}")
// interface Provider
println("${t.providerB.javaClass.genericInterfaces[0]}")
// Provider<A>
```
On kotlin 1.3.72 effect is following

```kotlin
//ON KOTLIN 1.3.72
val t = SomeClass()

println("${t.providerA.javaClass.genericInterfaces[0]}")
// Provider<A>
println("${t.providerB.javaClass.genericInterfaces[0]}")
// Provider<A>

```
Actual:

- on 1.5.10 fields of SomeClass have different types
- on 1.3.72 fields of SomeClass have same type.

Expected:

- on both fields should have same type
- type should not change between compiler versions
 
Tests in following repo can be used to replicate behavior:

https://github.com/daber/kotlin_single_method_interface_issue

Outcome of the test can be altered by changing kotlin version
from 1.5.10 <-> 1.3.72 in build.gradle file
