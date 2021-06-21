import org.junit.Assert
import org.junit.Test
import java.lang.reflect.ParameterizedType

class A

class SomeClass {
    val providerA: Provider<A> = Provider { A() }
    val providerB: Provider<A> = object : Provider<A> {
        override fun get(): A = A()
    }
}

/**
 * object : SingleMethodInterface<T> ===//=== SingleMethodInterface<T> { }
 *
 */
class SingleAbstractMethodTest {
    /**
     * On kotlin 1.5 invalid behavior
     * output :
     * interface javax.inject.Provider
     * javax.inject.Provider<A>
     *
     */
    @Test(expected = ClassCastException::class)
    fun that_passes_on_kotlin_1_5() {
        val t = SomeClass()
        println("${t.providerA.javaClass.genericInterfaces[0]}")
        println("${t.providerB.javaClass.genericInterfaces[0]}")
        val a = (t.providerA.javaClass.genericInterfaces[0] as ParameterizedType).actualTypeArguments[0]
        val b = (t.providerB.javaClass.genericInterfaces[0] as ParameterizedType).actualTypeArguments[0]

        Assert.fail("This should not be reached as genericInterfaces[0] contains Class object")
    }

    /**
     * On kotlin 1.3 - works correctly
     * output :
     *
     * javax.inject.Provider<A>
     * javax.inject.Provider<A>
     *
     */
    @Test()
    fun that_passes_on_kotlin_1_3() {
        val t = SomeClass()
        println("${t.providerA.javaClass.genericInterfaces[0]}")
        println("${t.providerB.javaClass.genericInterfaces[0]}")
        val a = (t.providerA.javaClass.genericInterfaces[0] as ParameterizedType).actualTypeArguments[0]
        val b = (t.providerB.javaClass.genericInterfaces[0] as ParameterizedType).actualTypeArguments[0]
        Assert.assertEquals(a, b)
    }
}