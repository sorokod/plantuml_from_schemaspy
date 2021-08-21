package soroko.gesrep

fun pumlTemplate(data: String): String {
    return """
@startuml
    'uncomment the line below for retina display
    'skinparam dpi 300
    !define Table(name)    class name << (T,Gold) >>
    !define Sequence(name) class name << (S,SandyBrown) >>
    ' ##################################
    !define uniqueIndex(x) -{method}<b>x
    !define index(x) #{method}<b>x
    !define nullable(x) x ∅ 
    !define fk(x, y, z) x➜y.z
    !define fkArrow(x, y) x --> y
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
    endlegend

    $data

@enduml
""".trimIndent()
}
