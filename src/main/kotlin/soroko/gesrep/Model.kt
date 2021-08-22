package soroko.gesrep

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Unfortunately we need to rebuild the in-memory model that Schemaspy serialized to XML.
 * We use only the bits that interest us.
 */

data class Database(
    val name: String,
    val type: String,
    val sequences: Sequences?,
    val tables: Tables
)

data class Sequences(
    val sequence: List<Sequence>
)

data class Sequence(
    val name: String,
    val increment: Int,
    val startValue: Int
)

data class Tables(
    @JsonProperty("table")
    val tables: List<Table>
)

data class Table(
    val name: String,
    val type: String,

    @JsonProperty("column")
    val columns: List<Column>,

    @JsonProperty("index")
    val indices: List<Index> = emptyList()
)

data class Column(
    val name: String,
    val nullable: Boolean,
    val type: String,
    val parent: Parent?
)

/**
 * AKA foreign key
 */
data class Parent(
    val table: String,
    val column: String,
    val foreignKey: String,
    val onDeleteCascade: Boolean
)

data class Index(
    val name: String,
    val unique: Boolean,
    @JsonProperty("column")
    val columns: List<IndexColumn>
)

data class IndexColumn(val name: String)



fun Table.getColumn(columnName: String): Column =
    this.columns.firstOrNull { it.name == columnName }!!



