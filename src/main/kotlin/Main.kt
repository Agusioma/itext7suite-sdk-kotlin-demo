import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest


fun main(args: Array<String>) {
    val fileName = "/home/incognito/Downloads/Resume.pdf"
    val path: Path = Paths.get(fileName)
    val sizeInBytes = Files.size(path)

    val md = MessageDigest.getInstance("MD5")


    val hex = checksum(fileName, md)
    println(hex)
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    //println("Program arguments: ${args.joinToString()}")
}


private fun checksum(filepath: String, md: MessageDigest): String? {

    // DigestInputStream is better, but you also can hash file like this.
    FileInputStream(filepath).use { fis ->
        val buffer = ByteArray(1024)
        var nread: Int
        while (fis.read(buffer).also { nread = it } != -1) {
            md.update(buffer, 0, nread)
        }
    }

    // bytes to hex
    val result = StringBuilder()
    for (b in md.digest()) {
        result.append(String.format("%02x", b))
    }
    return result.toString()
}