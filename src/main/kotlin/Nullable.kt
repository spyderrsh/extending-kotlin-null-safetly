package dev.ikeze.kotlinnullsafety

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

class NullableScope {
    fun<T> T?.bind(): T {
        return  this ?: throw NullableException()
    }

    @OptIn(ExperimentalContracts::class)
    fun<T1> bind(p1: T1?) {
        contract {
            returns() implies (p1 != null)
        }
        p1.bind()
    }

    @OptIn(ExperimentalContracts::class)
    fun<T1, T2> bind(p1: T1?, p2: T2?) {
        contract {
            returns() implies (p1 != null && p2 != null)
        }
        p1.bind()
        p2.bind()
    }

    @OptIn(ExperimentalContracts::class)
    fun<T1, T2, T3> bind(p1: T1?, p2: T2?, p3: T3?) {
        contract {
            returns() implies (p1 != null && p2 != null && p3 != null)
        }
        p1.bind()
        p2.bind()
        p3.bind()
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