package dev.ikeze.kotlinnullsafety

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class EasyTest {
    private fun easy1(str: String?): String {
        return str?.let {
            return "$str is so easy"
        } ?: "Not so easy after all"
    }

    // We can easily call this function with a null or String value; like so
    @Test
    fun easy1Test() {
        assertEquals(easy1(null), "Not so easy after all")
        assertEquals(easy1("Life"), "Life is so easy")
    }

    private fun easy2(str: String): String {
        return "$str is so easy"
    }

    @Test
    fun easy2Test() {
        // compile error: Null can not be a value of a non-null type String
        // assertEquals(easy2(null), "Not so easy after all")
        assertEquals(easy2("Life"), "Life is so easy")
    }
}