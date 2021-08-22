package soroko.gesrep


fun <T> Iterable<T>.joinAsRows(transform: ((T) -> CharSequence)? = null): String =
    this.joinToString(separator = "\n", prefix = "\n", transform = transform)

