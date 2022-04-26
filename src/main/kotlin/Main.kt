
import khttp.extensions.fileLike
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody.Builder
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest


fun main(args: Array<String>) {
    val fileName = "/home/incognito/Downloads/Resume.pdf"
    val path: Path = Paths.get(fileName)
    val sizeInBytes = Files.size(path)
    val baseUrl = "http://localhost:8080/api/job"
    // val files = listOf(File("/home/incognito/Downloads/Resume.pdf").fileLike())
    val md = MessageDigest.getInstance("MD5")
    val checksum = fileCheckSum(fileName, md)

    val postPayload = jsonPayload(checksum, "Resume.pdf", sizeInBytes)
    //val request = post("http://localhost:8080/api/job", files = files, data = postPayload)
    // val e = post(baseUrl,json = postPayload, files = files)


  //  val loadee = postPayload.toString()
    val loadee = mapOf("job" to postPayload)
    print(loadee)
  //  val client = HttpClient.newBuilder().build();
  /*  val request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/api/job"))
        .POST(HttpRequest.BodyPublishers.ofString(loadee))
        .header("Content-Type", "application/x-www-form-urlencoded")
        .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString());*/
  //  println(loadee)
    val files = listOf(File("/home/incognito/Downloads/Resume.pdf").fileLike())

   /* val client: OkHttpClient = OkHttpClient().newBuilder()
        .build()
    val mediaType = "text/plain".toMediaTypeOrNull()

    val OCTET: MediaType? = "application/octet-stream".toMediaTypeOrNull()
    var filee = File(fileName)
   // val fileRequestBody: RequestBody = filee.toRequestBody(OCTET)
    val fileRequestBody: RequestBody = filee.asRequestBody(OCTET)

    val JSON: MediaType? = "application/json".toMediaTypeOrNull()
    val requestBody: RequestBody = loadee.toRequestBody(JSON)
    val body: RequestBody = Builder().setType(MultipartBody.FORM)
        .addFormDataPart("job", null, requestBody)
        .addFormDataPart("file","Resume.pdf", fileRequestBody)
        .build()

    var request:Request = Request.Builder()
        .url("http://localhost:8080/api/job")
        .method("POST", body)
        .build();
    val response =
        client.newCall(request).execute()
    println(response.code)*/
    //request.newBuilder()
    //val r = post(url, files = files)
val boon = khttp.post("http://localhost:8080/api/job", files = files, data=loadee)
    print(boon.text)

   /*

    val loadee2 = mapOf("job" to postPayload).toList()
    print(loadee2)
    val (request, response, result) = Fuel.post("http://localhost:8080/api/job")

        //.jsonBody(loadee)
        .upload().add { FileDataPart(File("/home/incognito/Downloads/Resume.pdf"), filename = "Resume.pdf") }
        .response()

    println(response)*/

}


public fun fileCheckSum(filepath: String, md: MessageDigest): String {

    FileInputStream(filepath).use { fis ->
        val buffer = ByteArray(1024)
        var nread: Int
        while (fis.read(buffer).also { nread = it } != -1) {
            md.update(buffer, 0, nread)
        }
    }

    val result = StringBuilder()
    for (b in md.digest()) {
        result.append(String.format("%02x", b))
    }
    return result.toString()
}

public fun jsonPayload(checkSum: String, fileName: String, fileSize: Long): JSONObject {
    val reqSpec = JSONObject()
    val reqSpecItems = JSONObject()
    val srcFilesItems = JSONObject()
    val srcFiles = JSONObject()
    val srcFilesArray = JSONArray()

    reqSpecItems.put("type", "PDF_TO_IMAGES")
    reqSpecItems.put("imageType", "JPEG")
    reqSpecItems.put("pageNumbers", "1")
    reqSpecItems.put("defaultPageScaling", 1)
    reqSpec.put("request", reqSpecItems)

    //val srcFilesItems = mapOf("checksum" to checkSum, "fileType" to "application/pdf", "fileSize" to fileSize, "fileName" to fileName)
    srcFilesItems.put("checksum", checkSum)
    srcFilesItems.put("fileType", "application/pdf")
    srcFilesItems.put("fileSize", fileSize)
    srcFilesItems.put("fileName", fileName)

    srcFilesArray.put(srcFilesItems)
    reqSpec.put("sourceFiles", srcFilesArray)
    return reqSpec

}