package soroko.gesrep

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

/**
 * Sometimes Jackson converts XML to JSON in ways that are not completley obvious.
 * This can help to figure out what is going on.
 */
fun xml2json(xmlFile: File, mapper: ObjectMapper): String =
    mapper.readTree(xmlFile).toPrettyString()
