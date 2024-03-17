package dev.ikeze.kotlinnullsafety

class NullableScope {
    fun<T> T?.bind(): T {
        return  this ?: throw NullableException()
    }
}

/**
 * This class gets thrown but only in [NullableScope.bind]
 * It is used to make sure [nullable] block doesn't stop
 * throwing of other exceptions
 */
private class NullableException: Exception()


/**
 * This provides a block where nullable values can be used if they are non-null
 * by using bind, or short-circuits and return null
 * @sample dev.ikeze.kotlinnullsafety.NullableTest.nullableScopeStopsAfterFirstBindOnNull
 * @sample dev.ikeze.kotlinnullsafety.NullableTest.nullableScopeEvaluatesToValueIfNoNullValueWasBound
 */
inline fun<T> nullable(block: NullableScope.() -> T): T? {
    return with(NullableScope()) {
        try {
            block()
        } catch (e: NullableException) {
            null
        }
    }
}

/**
 * Wraps [this] and [other] in Pair if both non-null
 * or returns null if any is null
 * @sample dev.ikeze.kotlinnullsafety.NullableTest.zipTestAndSample
 */
fun<T, U> T?.zip(other: U?): Pair<T, U>?  = nullable {
    Pair(this@zip.bind(), other.bind())
}