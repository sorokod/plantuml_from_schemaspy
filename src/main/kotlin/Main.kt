import soroko.gesrep.Database
import soroko.gesrep.Renderer
import soroko.gesrep.process
import java.io.File

fun main(args: Array<String>) {
    val inFile = File(args[0])
    val outFile = File(args[1])

    val db: Database = process(inFile)

    val x = Renderer().render(db)

    outFile.writeText(x)

    println("DONE.")
}
