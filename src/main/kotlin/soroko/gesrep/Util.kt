package soroko.gesrep

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.io.File.separator

/**
 * Sometimes Jackson converts XML to JSON in ways that are not completely obvious.
 * This can help to figure out what is going on.
 */
fun xml2json(xmlFile: File, mapper: ObjectMapper): String =
    mapper.readTree(xmlFile).toPrettyString()


fun <T> Iterable<T>.joinAsRows(transform: ((T) -> CharSequence)? = null): String =
    this.joinToString(separator = "\n", prefix = "\n", postfix = "\n", transform = transform)

