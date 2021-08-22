import soroko.gesrep.Database
import soroko.gesrep.process
import soroko.gesrep.render
import java.io.File

fun main(args: Array<String>) {
    val inFile = File(args[0])
    val outFile = File(args[1])

    val db: Database = process(inFile)
    val data = render(db)

    outFile.writeText(data)

    println("DONE.")
}
