package soroko.gesrep

fun pumlTemplate(tableData: String, sequenceData: String): String =
    """
@startuml
    'Uncomment for retina display
    'skinparam dpi 300
    
    skinparam defaultFontName Courier
    skinparam legendBackgroundColor Snow
    
    !define Table(name)    class name << (T,Gold) >>
    !define View(name)     class name << (V,Silver) >>
    !define Sequence(name) class name << (S,Lime) >>
    ' ##################################
    !define uniqueIndex(x) -{method}<b>x
    !define index(x) #{method}<b>x
    !define nullable(x) x ∅ 
    !define fk(x, y, z) x➜y.z
    !define fkArrow(x, y) x --> y
    
    hide methods
    hide stereotypes

    legend top left
      T  Table
      V  View
      S  Sequence 
      ■  Unique Index
      ♢  Non-unique Index
      ∅  Nullable Column
      ➜  Foreign Key
    endlegend

    together {
    $tableData
    }

    together {
    $sequenceData
    }

@enduml
""".trimIndent()


fun sequenceTemplate(name: String, startValue: Int, increment: Int): String = """
Sequence($name) {
    startValue :: $startValue
    increment :: $increment
}    
"""

fun tableTemplate(name: String, columnData: String, indexData: String, fkData: String): String = """
Table($name) {
    $columnData
    $indexData
    $fkData
}    
"""

fun viewTemplate(name: String, columnData: String, indexData: String, fkData: String): String = """
View($name) {
    $columnData
    $indexData
    $fkData
}    
"""
