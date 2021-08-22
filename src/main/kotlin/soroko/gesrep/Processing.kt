package soroko.gesrep

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File
import java.util.*


private object Mapper {
    val mapper: ObjectMapper

    init {
        mapper = XmlMapper(JacksonXmlModule().apply { setDefaultUseWrapper(false) })
        mapper.registerKotlinModule()
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
}

fun process(schemaFile: File): Database {
    val db: Database = Mapper.mapper.readValue(schemaFile, Database::class.java)

    // we want columns to be sorted by name in-place - hence we use java's Collections.sort
    db.tables.tables.forEach { table ->
        Collections.sort(table.columns) { c1: Column, c2: Column -> c1.name.compareTo(c2.name) }
    }

    return db.also { report(it) }
}

private fun report(db: Database) =
    println("Parsed tables: ${db.tables.tables.size}")


/**
 * Sometimes Jackson converts XML to JSON in ways that are not completely obvious.
 * This can help to figure out what is going on.
 */
fun xml2json(xmlFile: File): String =
    Mapper.mapper.readTree(xmlFile).toPrettyString()


