package org.tues.tudy.utils

fun <T> sortByTudiesThenAlphabetical(
    list: List<T>,
    getName: (T) -> String,
    getTudies: (T) -> Int
): List<T> {
    val withTudies = list.filter { getTudies(it) > 0 }
        .sortedWith(compareByDescending<T> { getTudies(it) }.thenBy { getName(it) })

    val withoutTudies = list.filter { getTudies(it) == 0 }
        .sortedBy { getName(it) }

    return withTudies + withoutTudies
}
