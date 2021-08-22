package soroko.gesrep


class Renderer {

    fun render(database: Database): String {
        val fkList: MutableList<Pair<String, String>> = mutableListOf()
        val tableList = database.tables.tables
        val sequenceList = database.sequences?.sequence ?: emptyList()

        var tableData = tableList.joinAsRows { renderOne(it, fkList) }

        if (fkList.isNotEmpty()) {
            val fkArrows = fkList.map { "fkArrow$it" }
            tableData += fkArrows.joinAsRows()
        }

        var sequenceData = ""
        if (sequenceList.isNotEmpty()) {
            sequenceData = renderAllSequences(sequenceList)
        }

        return pumlTemplate(tableData, sequenceData)
    }

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
}

fun renderAllSequences(sequences: List<Sequence>): String {
    fun renderOne(seq: Sequence): String =
        sequenceTemplate(seq.name.uppercase(), seq.startValue, seq.increment)

    return sequences.map { renderOne(it) }.joinAsRows()
}

fun renderAllColumns(columns: List<Column>): String {

    fun Column.renderName() = this.name
    fun Column.renderType(target: Column.() -> String) = "${this.target()} :: ${this.type.uppercase()}"
    fun Column.renderNullable(target: Column.() -> String): String =
        when (this.nullable) {
            true -> "nullable(${this.target()})"
            false -> this.target()
        }

    fun renderOne(column: Column): String = column.run {
        renderType {
            renderNullable {
                renderName()
            }
        }
    }
    return columns.joinAsRows { renderOne(it) }
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
            val data = fkData.joinToString(separator = "\n", prefix = "\n", postfix = "\n")
            "==FK==$data"
        }
    }
}

fun renderAllIndices(indices: List<Index>): String {
    if (indices.isEmpty()) return ""
    // we want unique indices to precede the non-unique and all indices to be sorted
    val (uniqueIdx, nonUniqueIdx) = indices.partition { it.unique }
    val orderedIndices = uniqueIdx.sortedBy { it.name } + nonUniqueIdx.sortedBy { it.name }

    val indexData =
        orderedIndices.map { renderOne(it) }.joinToString(separator = "\n", prefix = "\n", postfix = "\n")

    return "==Indices==$indexData"
}

fun renderOne(index: Index): String {
    val data = index.columns.joinToString(separator = " | ") { it.name }
    return if (index.unique) {
        "uniqueIndex($data)"
    } else {
        "index($data)"
    }
}
