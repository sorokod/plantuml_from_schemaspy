package soroko.gesrep

fun pumlTemplate(tableData: String, sequenceData: String): String {
    return """
@startuml
    'uncomment the line below for retina display
    'skinparam dpi 300
    
    skinparam defaultFontName Courier
    skinparam legendBackgroundColor Snow
    
    !define Table(name)    class name << (T,Gold) >>
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
}
