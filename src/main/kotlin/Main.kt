import khttp.extensions.fileLike
import khttp.responses.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest


fun main(args: Array<String>) {
    val fileName = "<file-path>"

    //getting the size in bytes
    val path: Path = Paths.get(fileName)
    val sizeInBytes = Files.size(path)

    //getting the file checksum
    val msgDigest = MessageDigest.getInstance("MD5")
    val checksum = fileCheckSum(fileName, msgDigest)

    //getting the JSON payload
    val postPayload = jsonPayload(checksum, "<filename>", sizeInBytes)
    val jobData = mapOf("job" to postPayload)
    
    //getting the file
    val files = listOf(File("<filepath>").fileLike())
    
    //executing the POST request
    val response = khttp.post("<address>/api/job", files = files, data = jobData)
    print(response.text)

}

fun fileCheckSum(filepath: String, msgDigest: MessageDigest): String {

    FileInputStream(filepath).use { fis ->
        val buffer = ByteArray(1024)
        var nread: Int
        while (fis.read(buffer).also { nread = it } != -1) {
            msgDigest.update(buffer, 0, nread)
        }
    }

    val result = StringBuilder()
    for (byte in msgDigest.digest()) {
        result.append(String.format("%02x", byte))
    }
    return result.toString()
}

fun jsonPayload(checkSum: String, fileName: String, fileSize: Long): JSONObject {
    val reqSpec = JSONObject()
    val reqSpecItems = JSONObject()
    val srcFilesItems = JSONObject()
    val srcFilesArray = JSONArray()

    reqSpecItems.put("type", "HTML_TO_PDF")
    reqSpec.put("request", reqSpecItems)

    srcFilesItems.put("checksum", checkSum)
    srcFilesItems.put("fileType", "text/html")
    srcFilesItems.put("fileSize", fileSize)
    srcFilesItems.put("fileName", fileName)

    srcFilesArray.put(srcFilesItems)
    reqSpec.put("sourceFiles", srcFilesArray)
    return reqSpec

}

fun getStatus(jobID: String){
    val response : Response = khttp.get("<address>/api/"+jobID)
    //write your processing logic here
    println(response)
}

fun downloadResult(jobID: String){
    val response : Response = khttp.get("<address>/api/"+jobID+"/file/result")
    val jpegData : ByteArray = response.content
    //write your saving logic here
}

