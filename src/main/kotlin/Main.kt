import soroko.gesrep.Database
import soroko.gesrep.Mapper
import soroko.gesrep.Renderer
import java.io.File

fun main(args: Array<String>) {
    val file = File(args[0])
    val mapper = Mapper().mapper

    val db: Database = mapper.readValue(file, Database::class.java)

    println(db.tables.tables.size)

    val x = Renderer().render(db)

    File("/Users/xor/work/kotlin/good_enough_schema_report/src/test/resources/out.puml").writeText(x)

    println("DONE")
}