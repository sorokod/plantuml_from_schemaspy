package soroko.gesrep


class RenderData(
    val dbName: String,
    var tableData: String = "",
    var viewData: String = "",
    var sequenceData: String = "",
    var fkData: String = ""
)


fun render(database: Database): String {
    val fkAccumulator: MutableList<Pair<String, String>> = mutableListOf()
    val tableList = database.tables.tables
    val sequenceList = database.sequences?.sequence ?: emptyList()

    val renderDataAccumulator = RenderData(database.name)

    with(renderDataAccumulator) {
        tableData = renderAllTablesAndViews(tableList, "TABLE", fkAccumulator)
        viewData = renderAllTablesAndViews(tableList, "VIEW", fkAccumulator)
        fkData = fkAccumulator.map { "fkArrow$it" }.joinAsRows("")
        sequenceData = renderAllSequences(sequenceList)
    }

    return pumlTemplate(renderDataAccumulator)
}

fun renderAllTablesAndViews(
    tables: List<Table>,
    typeFilter: String,
    fkAccumulator: MutableList<Pair<String, String>>
): String {

    fun renderOne(table: Table, fkAccumulator: MutableList<Pair<String, String>>): String {
        val columnData = renderAllColumns(table.columns)
        val indexData = renderAllIndices(table.indices)
        val fkData = renderAllFKs(table.columns, table.name, fkAccumulator)

        return if (table.type == "VIEW") {
            viewTemplate(table.name.uppercase(), columnData, indexData, fkData)
        } else {
            tableTemplate(table.name.uppercase(), columnData, indexData, fkData)
        }
    }
    return tables.filter { it.type == typeFilter }.joinAsRows("    ") { table -> renderOne(table, fkAccumulator) }
}

fun renderAllSequences(sequences: List<Sequence>): String {
    fun renderOne(seq: Sequence): String =
        sequenceTemplate(seq.name.uppercase(), seq.startValue, seq.increment)

    return sequences.map { seq -> renderOne(seq) }.joinAsRows("    ")
}

fun renderAllColumns(columns: List<Column>): String {
    fun renderOne(column: Column): String =
        columnTemplate(column.name, column.nullable, column.type)

    return columns.joinAsRows("    ") { col -> renderOne(col) }
}

/**
 * We render FK within a table as well as accumulating a global list in fkAccumulator to support drawing arrows between
 * dependent tables.
 */
fun renderAllFKs(columns: List<Column>, tableName: String, fkAccumulator: MutableList<Pair<String, String>>): String {
    val fkColumns = columns.filter { it.parent != null }

    val fkData = fkColumns.map { column ->
        val parent = column.parent!!
        fkAccumulator.add(Pair(tableName.uppercase(), parent.table.uppercase()))
        "fk(${column.name}, ${parent.table.uppercase()}, ${parent.column})"
    }
    return when (fkData.isNotEmpty()) {
        true -> "==FK==\n    " + fkData.joinAsRows("    ")
        false -> ""
    }
}

fun renderAllIndices(indices: List<Index>): String {

    fun renderOne(index: Index): String {
        val data = index.columns.joinToString(separator = " + ") { it.name }
        return if (index.unique) {
            "uniqueIndex($data)"
        } else {
            "index($data)"
        }
    }

    if (indices.isEmpty()) return ""
    // we want unique indices to precede the non-unique and all indices to be sorted
    val (uniqueIdx, nonUniqueIdx) = indices.partition { it.unique }
    val orderedIndices = uniqueIdx.sortedBy { it.name } + nonUniqueIdx.sortedBy { it.name }

    val data = orderedIndices.map { idx -> renderOne(idx) }.joinAsRows("    ")
    return "==Indices==\n    $data"
}

