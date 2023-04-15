package util

interface ToJson {
    fun toJson(): String
}

fun <T: ToJson> List<T>.toJson(): String {
    return joinToString(", ", "[", "]") { it.toJson() }
}