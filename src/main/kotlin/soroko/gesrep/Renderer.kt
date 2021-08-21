package soroko.gesrep

class Renderer() {

    /**
     * Wraps data with PlantUML boilerplate
     */
    fun boilerplateWrap(data: String): String {
        return """
@startuml
    ' uncomment the line below for retina display
    'skinparam dpi 300
    !define Table(name) class name << (T,#FFAAAA) >>
    ' ##################################
    !define uniqueIndex(x) <color:red><b>》x《
    !define index(x) <color:red>⟦x⟧</color>   
    !define nullable(x) x ∅ 
    !define fk(x, y, z) x➜y.z
    !define fkArrow(x, y) x --> y
    ' other tags available:
    ' <i></i>
    ' <back:COLOR></color>, where color is a color name or html color code
    ' (#FFAACC)
    ' see: http://plantuml.com/classes.html#More
    hide methods
    hide stereotypes

    legend top left
     》《  Unique Index
     ⟦ ⟧   Index
      ∅  Nullable
      ➜  Foreign Key
      ∅
      ⇨
      ✱
      ACCOUNT --> ACCOUNT_BACKLOG  
    endlegend

    $data

@enduml
""".trimIndent()
    }

    fun render(database: Database): String {
        val fkList: MutableList<Pair<String, String>> = mutableListOf()
        val tableList = database.tables.tables

        var data = tableList.joinToString(separator = "\n", prefix = "\n", postfix = "\n") { render(it, fkList) }

        if (fkList.isNotEmpty()) {
            val fkArrows = fkList.map { "fkArrow$it" }
            data += fkArrows.joinToString(separator = "\n    ", prefix = "\n", postfix = "\n")
        }

        return boilerplateWrap(data)
    }

    fun render(table: Table, fkMap: MutableList<Pair<String, String>>): String {
        val columnData = renderAllColumns(table.columns)
        val indexData = renderAllIndices(table.indices)
        val fkData = renderAllFKs(table.columns, table.name, fkMap)

        return "Table(${table.name.uppercase()}) { " + columnData + indexData + fkData + "}\n"
    }
}


fun renderAllColumns(columns: List<Column>): String {

    fun renderOne(column: Column): String = column.run {
        renderType {
            renderNullable {
                renderName()
            }
        }
    }

    return columns.joinToString(separator = "\n    ", prefix = "\n    ", postfix = "\n") { renderOne(it) }
}


fun Column.renderType(target: Column.() -> String) = "${this.target()} :: ${this.type.uppercase()}"

fun Column.renderNullable(target: Column.() -> String): String =
    when (this.nullable) {
        true -> "nullable(${this.target()})"
        false -> this.target()
    }

fun Column.renderName() = this.name

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
            val data = fkData.joinToString(separator = "\n    ", prefix = "\n    ", postfix = "\n")
            "    ==FK==$data"
        }
    }
}

fun renderAllIndices(indices: List<Index>): String =
    when (indices.isEmpty()) {
        true -> ""
        false -> {
            val indexData =
                indices.map { renderOne(it) }.joinToString(separator = "\n    ", prefix = "\n    ", postfix = "\n")
            "    ==Indices==$indexData"
        }
    }


fun renderOne(index: Index): String {
    val data = index.columns.joinToString(separator = " | ") { it.name }
    return if (index.unique) {
        "uniqueIndex($data)"
    } else {
        "index($data)"
    }
}
