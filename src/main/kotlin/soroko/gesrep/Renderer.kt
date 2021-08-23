package soroko.gesrep


fun render(database: Database): String {
    val fkList: MutableList<Pair<String, String>> = mutableListOf()
    val tableList = database.tables.tables
    val sequenceList = database.sequences?.sequence ?: emptyList()

    var tableData = renderAllTables(tableList, fkList)

    if (fkList.isNotEmpty()) {
        val fkArrows = fkList.map { "fkArrow$it" }
        tableData += fkArrows.joinAsRows("")
    }

    var sequenceData = ""
    if (sequenceList.isNotEmpty()) {
        sequenceData = renderAllSequences(sequenceList)
    }

    return pumlTemplate(tableData, sequenceData)
}

fun renderAllTables(tables: List<Table>, fkList: MutableList<Pair<String, String>>): String {
    fun renderOne(table: Table, fkMap: MutableList<Pair<String, String>>): String {
        val columnData = renderAllColumns(table.columns)
        val indexData = renderAllIndices(table.indices)
        val fkData = renderAllFKs(table.columns, table.name, fkMap)

        return if (table.type == "VIEW") {
            viewTemplate(table.name.uppercase(), columnData, indexData, fkData)
        } else {
            tableTemplate(table.name.uppercase(), columnData, indexData, fkData)
        }
    }
    return tables.joinAsRows("    ") { table -> renderOne(table, fkList) }
}

fun renderAllSequences(sequences: List<Sequence>): String {
    fun renderOne(seq: Sequence): String =
        sequenceTemplate(seq.name.uppercase(), seq.startValue, seq.increment)

//    return sequences.map { seq -> renderOne(seq) }.joinAsRows()
    return sequences.map { seq -> renderOne(seq) }.joinAsRows("    ")
}

fun renderAllColumns(columns: List<Column>): String {
    fun renderOne(column: Column): String =
        columnTemplate(column.name, column.nullable, column.type)

    return columns.joinAsRows("    ") { col -> renderOne(col) }
}


fun renderAllFKs(columns: List<Column>, tableName: String, fkList: MutableList<Pair<String, String>>): String {
    val fkColumns = columns.filter { it.parent != null }
    return when (fkColumns.isEmpty()) {
        true -> ""
        false -> {
            val fkData = fkColumns.map { column ->
                val parent = column.parent!!
                fkList.add(Pair(tableName.uppercase(), parent.table.uppercase()))
                "fk(${column.name}, ${parent.table.uppercase()}, ${parent.column})"
            }
            val data = fkData.joinAsRows("    ")
            "==FK==\n    $data"
        }
    }
}

fun renderAllIndices(indices: List<Index>): String {

    fun renderOne(index: Index): String {
        val data = index.columns.joinToString(separator = " | ") { it.name }
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

