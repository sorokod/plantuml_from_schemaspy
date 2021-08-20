import soroko.gesrep.Database
import soroko.gesrep.Mapper
import soroko.gesrep.Renderer
import java.io.File

fun main(args: Array<String>) {
    val inFile = File(args[0])
    val outFile = File(args[1])

    val mapper = Mapper().mapper

    val db: Database = mapper.readValue(inFile, Database::class.java)

    println(db.tables.tables.size)

    val x = Renderer().render(db)

    outFile.writeText(x)

    println("DONE")
}
