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
    ' we use bold for primary key
    ' green color for unique
    ' and underscore for not_null
    !define pk(x) <b>《x》</b>
    !define unique(x) <color:green>x</color>
    !define nullable(x) ∅ x
    !define fk(x, y, z) x➜y.z
    ' other tags available:
    ' <i></i>
    ' <back:COLOR></color>, where color is a color name or html color code
    ' (#FFAACC)
    ' see: http://plantuml.com/classes.html#More
    hide methods
    hide stereotypes

    legend top left
     《 》Primary Key
      ◯  Nullable
      ➜  Foreign Key
      ∅
      ⇨
      ✱
    endlegend

    $data

    @enduml
""".trimIndent()
    }

    fun render(database: Database): String {
        val data =
            database.tables.tables.map { render(it) }
                .joinToString(separator = "\n    ", prefix = "    \n", postfix = "\n")
        return boilerplateWrap(data)
    }

    fun render(table: Table): String {
        return "Table(${table.name.uppercase()})"
    }
}