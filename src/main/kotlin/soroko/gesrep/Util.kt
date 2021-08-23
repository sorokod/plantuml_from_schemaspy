package soroko.gesrep

//
//fun <T> Iterable<T>.joinAsRows(transform: ((T) -> CharSequence)? = null): String =
//    this.joinToString(separator = "\n", prefix = "\n", transform = transform)

fun <T> Iterable<T>.joinAsRows(indent: String, transform: ((T) -> CharSequence)? = null): String =
    this.joinToString(separator = "\n$indent", transform = transform)

