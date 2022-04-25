import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


fun main(args: Array<String>) {
    val fileName = "/home/incognito/Downloads/Resume.pdf"
    val path: Path = Paths.get(fileName)
    val sizeInBytes = Files.size(path)
    println(sizeInBytes)
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    //println("Program arguments: ${args.joinToString()}")
}