package dev.ikeze.kotlinnullsafety

import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class NullableTest {


    private val personService = object : PersonService {
        override fun getName(): String? {
            return if (Random.nextInt() > 100) null else "Jon"
        }

        override fun getAge(): Int? {
            return if (Random.nextInt() > 100) null else 34
        }

        override fun getEmail(): String? {
            return if (Random.nextInt() > 100) null else "jon@doe.com"
        }

        override fun createPerson(name: String, age: Int, email: String): Person {
            return Person(name, age, email)
        }

    }

    data class Person(val name: String, val age: Int, val email: String)

    interface PersonService {
        fun getName(): String?
        fun getAge(): Int?
        fun getEmail(): String?
        fun createPerson(name: String, age: Int, email: String): Person
    }

    @Test
    fun createPerson() {
        val person: Person? = personService.getName()?.let { name ->
            personService.getAge()?.let { age ->
                personService.getEmail()?.let { email ->
                    personService.createPerson(name, age, email)
                }
            }
        }
    }

    private fun getMe(a: String, b: Int): Int {
        return a.slice(0..b).length
    }

    @Test
    fun nullableScopeStopsAfterFirstBindOnNull() {
        val v1: String? = null
        val v2: Int? = null
        var count = 0

        val v5 = nullable {
            count++
            val v3 = v1.bind()
            count++
            val v4 = v2.bind()
            count++
            getMe(v3, v4)
        }

        assertEquals(count, 1)
        assertEquals(v5, null)
    }

    @Test
    fun `nullable scope stops after first bind on null 2`() {
        var v1: String? = null
        val v2: Int? = null
        var count = 0
        val v5 = nullable {
            v1 = "value"
            count++
            val v3 = v1.bind()
            count++
            val v4 = v2.bind()
            count++
            getMe(v3, v4)
        }

        assertEquals(count, 2)
        assertEquals(v5, null)
    }

    @Test
    fun nullableScopeEvaluatesToValueIfNoNullValueWasBound() {
        var v1: String? = "value"
        val v2: Int? = 2
        var count = 0
        val v5 = nullable {
            count++
            val v3 = v1.bind()
            count++
            val v4 = v2.bind()
            count++
            v3.slice(0..v4)
        }

        assertEquals(count, 3)
        assertEquals(v5, "val")
    }

    @Test
    fun `nullable scope does lets exception to be thrown`() {

        val v1: String? = null
        val v2: Int? = null
        var count = 0
        val v5 = {
            nullable {
                if (count == 0) {
                    throw Exception()
                }
                val v3 = v1.bind()
                count++
                val v4 = v2.bind()
                count++
                getMe(v3, v4)
            }

        }

        assertEquals(count, 0)
        assertThrows<Exception> { v5() }
    }

    @Test
    fun zipTestAndSample() {
        var v1: String? = null
        var v2: Int? = null
        assertEquals(v1.zip(v2), null)

        v1 = "Hi"
        assertEquals(v1.zip(v2), null)

        v1 = null
        v2 = 3
        assertEquals(v1.zip(v2), null)

        v1 = "Hi"
        assertEquals(v1.zip(v2), Pair("Hi", 3))
    }

    @Test
    fun zipTest() {
        var v1: String? = null
        var v2: Int? = null

        val res1 = v1.zip(v2)?.let { (a, b) -> "v1 is $a and v2 is $b" }
        assertEquals(res1, null)

        v1 = "Hi"
        v2 = 4
        val res2 = v1.zip(v2)?.let { (a, b) -> "v1 is $a and v2 is $b" }
        assertEquals(res2, "v1 is Hi and v2 is 4")

        v2 = null
        val res3 = v1.zip(v2)?.let { (a, b) -> "v1 is $a and v2 is $b" }
        assertEquals(res3, null)
    }

    @Test
    fun createPerson2() {
        val person: Person? =
            personService.getName()
                .zip(personService.getAge())
                .zip(personService.getEmail())
                ?.let { (nameAndAgePair, email) ->
                    val (name, age) = nameAndAgePair
                    personService.createPerson(name, age, email)
                }
    }


    @Test
    fun createPerson3() {
        val person: Person? = nullable {
            val name = personService.getName().bind()
            val age = personService.getAge().bind()
            val email = personService.getEmail().bind()

            Person(name, age, email)
        }
    }
}
